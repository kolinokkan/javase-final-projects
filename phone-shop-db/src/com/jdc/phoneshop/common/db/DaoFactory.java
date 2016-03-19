package com.jdc.phoneshop.common.db;

import com.jdc.phoneshop.admin.model.dao.EmployeeDao;
import com.jdc.phoneshop.pos.model.dao.BillDao;
import com.jdc.phoneshop.pos.model.dao.BillItemDao;
import com.jdc.phoneshop.pos.model.dao.CustomerDao;
import com.jdc.phoneshop.warehouse.model.dao.CategoryDao;
import com.jdc.phoneshop.warehouse.model.dao.ItemDao;
import com.jdc.phoneshop.warehouse.model.dao.ItemPriceDao;
import com.jdc.phoneshop.warehouse.model.dao.MakerDao;
import com.jdc.phoneshop.warehouse.model.dao.PurchaseDao;
import com.jdc.phoneshop.warehouse.model.dao.PurchaseItemDao;
import com.jdc.phoneshop.warehouse.model.dao.StockDao;
import com.jdc.phoneshop.warehouse.model.dao.StockHistoryDao;

public class DaoFactory {

	@SuppressWarnings("unchecked")
	public static<T> Dao<T> generate(Class<T> type) {
		Dao<?> dao = null;

		switch (type.getSimpleName()) {
		case "Employee":
			dao = new EmployeeDao();
			break;
		case "Bill":
			dao = new BillDao();
			break;
		case "BillItem":
			dao = new BillItemDao();
			break;
		case "Customer":
			dao = new CustomerDao();
			break;
		case "Category":
			dao = new CategoryDao();
			break;
		case "Item":
			dao = new ItemDao();
			break;
		case "ItemPrice":
			dao = new ItemPriceDao();
			break;
		case "Maker":
			dao = new MakerDao();
			break;
		case "Purchase":
			dao = new PurchaseDao();
			break;
		case "PurchaseItem":
			dao = new PurchaseItemDao();
			break;
		case "Stock":
			dao = new StockDao();
			break;
		case "StockHistory":
			dao = new StockHistoryDao();
			break;

		default:
			break;
		}

		return (Dao<T>) dao;
	}

}
