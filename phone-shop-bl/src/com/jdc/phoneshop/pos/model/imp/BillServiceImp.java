package com.jdc.phoneshop.pos.model.imp;

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
import com.jdc.phoneshop.pos.model.BillModel;
import com.jdc.phoneshop.pos.model.BillService;
import com.jdc.phoneshop.pos.model.entity.Bill;
import com.jdc.phoneshop.pos.model.entity.BillItem;
import com.jdc.phoneshop.pos.model.entity.Customer;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.warehouse.model.entity.Stock;
import com.jdc.phoneshop.warehouse.model.entity.StockHistory;
import com.jdc.phoneshop.warehouse.model.entity.StockHistory.Operation;

public class BillServiceImp implements BillService{

	private Customer customer;
    private Bill bill;
    private Employee employee;
    
    private Set<BillItem> items;
    
    private Dao<Bill> billDao;
    private Dao<BillItem> billItemDao;
    private Dao<Customer> custDao;
    
    private Dao<Stock> stockDao;
    private Dao<StockHistory> shDao;
    
    public BillServiceImp(Customer c, Employee emp) {
		this.customer = c;
		this.employee = emp;
		
		bill = new Bill();
		bill.setCreateUserId(emp.getId());
		
		items = new LinkedHashSet<>();
		
		billDao = DaoFactory.generate(Bill.class);
		billItemDao = DaoFactory.generate(BillItem.class);
		custDao = DaoFactory.generate(Customer.class);
		
		stockDao = DaoFactory.generate(Stock.class);
		shDao = DaoFactory.generate(StockHistory.class);
	}

    @Override
	public void addItem(Set<BillItem> item) {
    	items.addAll(item);
	}

	@Override
	public void create() {
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			// new customer
			if(customer.getId() == 0) {
				custDao.create(customer, conn);
			}
			
			bill.setCustomerId(customer.getId());
			
			int subTotal = 0;
			
			for (BillItem billItem : items) {
				subTotal += billItem.getCount() * billItem.getUnitPrice();
			}
			bill.setSubTotal(subTotal);
			
			// tax
			Double tax = subTotal * BillModel.getModel().getTaxRate();
			bill.setTax(tax.intValue());
			
			// total
			bill.setTotal(bill.getTax() + bill.getSubTotal());
			
			// create bill
			billDao.create(bill, conn);
			
			// create bill items
			for (BillItem billItem : items) {
				billItem.setBillId(bill.getId());
				billItemDao.create(billItem, conn);
				
				updateStock(billItem, conn);
			}

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

	private void updateStock(BillItem billItem, Connection conn) throws IllegalArgumentException, IllegalAccessException, SQLException, InstantiationException {
		// find stock
		Stock stock = findStockByID(billItem.getItemId());
		
		// add to stock history
		StockHistory sh = new StockHistory();
		sh.setItem(billItem.getItemId());
		sh.setOperation(Operation.Sell);
		sh.setCount(billItem.getCount());
		sh.setPrevCount(stock.getCount());
		sh.setUser(this.employee.getId());
		
		shDao.create(sh, conn);
		
		// update stock
		int lastCount = stock.getCount() - billItem.getCount();
		stockDao.update("count = ?, history_id = ?", "item_id = ?", Arrays.asList(lastCount, sh.getId(), billItem.getItemId()), conn);
	}
	
	private Stock findStockByID(int itemId) throws InstantiationException, IllegalAccessException {
		List<Stock> list = stockDao.find("item_id = ?", Arrays.asList(itemId));
		for (Stock stock : list) {
			return stock;
		}
		return null;
	}


}
