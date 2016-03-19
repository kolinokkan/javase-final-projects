package com.jdc.phoneshop.admin.model.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.admin.model.SellPriceAdminModel;
import com.jdc.phoneshop.admin.model.vo.SellPriceVO;
import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.pos.model.ItemModel;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.warehouse.model.PurchaseModel;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice.PriceStatus;
import com.jdc.phoneshop.warehouse.model.entity.Purchase;
import com.jdc.phoneshop.warehouse.model.entity.Purchase.PurchaseStatus;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;
import com.jdc.phoneshop.warehouse.model.entity.Stock;
import com.jdc.phoneshop.warehouse.model.entity.StockHistory;
import com.jdc.phoneshop.warehouse.model.entity.StockHistory.Operation;

public class SellPriceAdminModelImp implements SellPriceAdminModel {

	private Dao<ItemPrice> ipDao;
	private Dao<PurchaseItem> piDao;
	private Dao<Stock> stDao;
	private Dao<StockHistory> shDao;
	private Dao<Purchase> pcDao;
	
	
    public SellPriceAdminModelImp() {
    	ipDao = DaoFactory.generate(ItemPrice.class);
    	piDao = DaoFactory.generate(PurchaseItem.class);
    	stDao = DaoFactory.generate(Stock.class);
    	shDao = DaoFactory.generate(StockHistory.class);
    	pcDao = DaoFactory.generate(Purchase.class);
    }

    public Set<SellPriceVO> getSellPriceList(PriceStatus ... status) {

    	try {
    		Set<SellPriceVO> result = new HashSet<>();
    		String where = null;
    		List<Object> params = new ArrayList<>();
    		
    		if(null != status && status.length > 0) {
    			where = "status in (%s)";
    			StringBuilder sb = new StringBuilder();
    			for (int i = 0; i < status.length; i++) {
					if(i > 0) {
						sb.append(", ");
					}
					sb.append("?");
					params.add(status[i]);
				}
    			where = String.format(where, sb.toString());
    		}
    		
    		List<ItemPrice> list = ipDao.find(where, params);
    		for (ItemPrice itemPrice : list) {
    			
    			SellPriceVO vo = new SellPriceVO();
    			// item price
    			vo.setItemPrice(itemPrice);
    			
        		// item
    			int itemId = itemPrice.getItem();
    			Item item = ItemModel.getModel().search(itemId);
    			vo.setItem(item);
    			
        		// purchase item
    			List<PurchaseItem> purchaseItems = piDao.find("item_id = ? and purchase_id = ?", Arrays.asList(itemId, itemPrice.getPurchaseId()));
    			if(purchaseItems.size() > 0) {
    				vo.setPurchaseItem(purchaseItems.get(0));
    				result.add(vo);
    			}
			}
    		
    		return result;
    		
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
    	
    }

    public void confirm(Set<ItemPrice> price, int empId) {
    	this.updateStatus(price, PriceStatus.Confirmed, empId);
    }

    public void cancel(Set<ItemPrice> price) {
    	this.updateStatus(price, PriceStatus.Cancel, 0);
    }

    public void updatePrice(ItemPrice price, int newPrice) {
    	Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			ipDao.update("price = ?, status = ?", "item_id = ? and ref_date = ?", 
					Arrays.asList(newPrice, PriceStatus.Confirmed , price.getItem(), price.getRefDate()), conn);
			
			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();
				throw new ApplicationException(e);
			} catch (SQLException e1) {
				throw new ApplicationException(e1);
			}
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
		}

    }
    
    private void updateStatus(Set<ItemPrice> price, PriceStatus status, int empId) {
    	Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			for (ItemPrice itemPrice : price) {
				// update price status
				ipDao.update("status = ?, ref_date = ?", "item_id = ? and purchase_id = ?", 
						Arrays.asList(status, LocalDate.now().plusDays(1),itemPrice.getItem(), itemPrice.getPurchaseId()), conn);
				
				if(status.equals(PriceStatus.Confirmed)) {
					// stock
					PurchaseItem purItem = getPurchaseItem(itemPrice.getItem(), itemPrice.getPurchaseId());
					
					int prevCount = 0;
					// update stock
					// select stock
					Stock stock = getStock(itemPrice.getItem());
					
					// insert stock history
					StockHistory his = new StockHistory();
					his.setItem(itemPrice.getItem());
					his.setUser(empId);
					his.setCount(0);
					his.setCount(purItem.getCount());
					his.setOperation(Operation.Buy);
					
					// if no stock insert stock
					if(stock == null) {
						his.setPrevCount(prevCount);
						shDao.create(his, conn);

						stock = new Stock();
						stock.setCount(his.getPrevCount() + purItem.getCount()); 
						stock.setItemId(purItem.getItem());
						stock.setHistoryId(his.getId());
						stDao.create(stock, conn);
					} else {
						// else update stock
						his.setPrevCount(stock.getCount());
						shDao.create(his, conn);
						
						stock.setCount(his.getPrevCount() + purItem.getCount());
						stDao.update("count = ?, history_id = ?", "item_id = ?", 
								Arrays.asList(stock.getCount(), his.getId(), stock.getItemId()), conn);
					}
					
					// update purchase status
					updatePurchaseStatus(purItem.getPurchase(), conn);
				}
			}

			conn.commit();
			
			for (ItemPrice itemPrice : price) {
				itemPrice.setStatus(status);
			}

		} catch (Exception e) {
			try {
				conn.rollback();
				throw new ApplicationException(e);
			} catch (SQLException e1) {
				throw new ApplicationException(e1);
			}
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
		}
    }

	private void updatePurchaseStatus(int purchase, Connection conn) throws InstantiationException, IllegalAccessException, SQLException {
		Purchase p = new Purchase();
		p.setId(purchase);
		Set<PurchaseItem> piList = PurchaseModel.getModel().getPurchaseItems(p);
		List<ItemPrice> priceList = ipDao.find("purchase_id = ?", Arrays.asList(purchase));
		
		if(piList.size() == priceList.size()) {
			pcDao.update("status = ?", "id = ?", Arrays.asList(PurchaseStatus.Complete, purchase), conn);
		}
	}

	private PurchaseItem getPurchaseItem(int item, int purchaseId) throws InstantiationException, IllegalAccessException {
		List<PurchaseItem> list = piDao.find("item_id = ? and purchase_id = ?", Arrays.asList(item, purchaseId));
		if(list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private Stock getStock(int item) throws InstantiationException, IllegalAccessException {
		List<Stock> stocks = stDao.find("item_id = ?", Arrays.asList(item));
		if(stocks.size() > 0) {
			return stocks.get(0);
		}
		return null;
	}

}