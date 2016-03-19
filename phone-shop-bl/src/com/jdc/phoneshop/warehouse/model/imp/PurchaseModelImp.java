package com.jdc.phoneshop.warehouse.model.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.warehouse.model.PurchaseModel;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice;
import com.jdc.phoneshop.warehouse.model.entity.Purchase;
import com.jdc.phoneshop.warehouse.model.entity.Purchase.PurchaseStatus;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;
import com.jdc.phoneshop.warehouse.vo.PurchaseItemVO;

public class PurchaseModelImp implements PurchaseModel {

	private Dao<Purchase> pcDao;
	private Dao<PurchaseItem> piDao;
	private Dao<ItemPrice> spDao;

	public PurchaseModelImp() {
		pcDao = DaoFactory.generate(Purchase.class);
		piDao = DaoFactory.generate(PurchaseItem.class);
		spDao = DaoFactory.generate(ItemPrice.class);
	}


	@Override
	public Set<Purchase> getPurchaseList(PurchaseStatus status) {
		try {
			return new LinkedHashSet<>(pcDao.find("status = ?", Arrays.asList(status)));
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Set<PurchaseItem> getPurchaseItems(Purchase p) {
		try {
			return new LinkedHashSet<>(piDao.find("purchase_id = ?", Arrays.asList(p.getId())));
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Purchase findById(int id) {
		try {
			List<Purchase> list = pcDao.find("id = ?", Arrays.asList(id));
			for (Purchase purchase : list) {
				return purchase;
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return null;
	}


	@Override
	public void purchase(List<Item> items, int count, Employee emp) {
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);

			// create purchase
			Purchase p = new Purchase();
			p.setUser(emp.getId());

			pcDao.create(p, conn);

			// create purchase items
			for (Item itm : items) {
				PurchaseItem item = new PurchaseItem();
				item.setCount(count);
				item.setPurchase(p.getId());
				item.setItem(itm.getId());
				piDao.create(item, conn);
			}

			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}


	@Override
	public void updatePurchase(Set<PurchaseItemVO> items) {
		if(items.size() == 0) {
			throw new ApplicationException(ErrorType.Warning, "There is no updated information in purchase item list.");
		}
		
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			// update purchase item
			updatePurchaseItems(items, conn);
			
			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}


    public void updatePurchaseItems(Set<PurchaseItemVO> items, Connection conn) throws Exception{
		for (PurchaseItemVO vo : items) {
			// update purchase item
			PurchaseItem item =vo.getItem();
			
			// update sell price
			ItemPrice itemPrice = findItemPrice(item.getItem(), item.getPurchase());
			if(null == itemPrice) {
				// create sell price
				itemPrice = new ItemPrice();
				itemPrice.setItem(item.getItem());
				itemPrice.setPurchaseId(item.getPurchase());
				itemPrice.setPrice(vo.getSellPrice());
				spDao.create(itemPrice, conn);
			} else {
				// update sell price
				spDao.update("price = ?", "item_id = ? and purchase_id = ?", 
						Arrays.asList(vo.getSellPrice(), vo.getItem().getItem(), 
								vo.getItem().getPurchase()), conn);
			}
			
			// update purchase items
			piDao.update("count = ?, price = ?", "item_id = ? and purchase_id = ?",
					Arrays.asList(vo.getItem().getCount(), vo.getItem().getPrice(), 
							vo.getItem().getItem(), vo.getItem().getPurchase()), conn);
		}
    	
    }

	private ItemPrice findItemPrice(int item, int purchase) throws InstantiationException, IllegalAccessException {
		List<ItemPrice> list = spDao.find("item_id = ? and purchase_id = ?", Arrays.asList(item, purchase));
		for (ItemPrice itemPrice : list) {
			return itemPrice;
		}
		
		return null;
	}


}