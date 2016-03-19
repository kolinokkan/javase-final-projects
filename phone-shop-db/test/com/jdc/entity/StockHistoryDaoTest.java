package com.jdc.entity;

import static com.jdc.entity.StockDaoTest.create;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.admin.model.entity.Employee.Role;
import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Maker;
import com.jdc.phoneshop.warehouse.model.entity.StockHistory;
import com.jdc.phoneshop.warehouse.model.entity.StockHistory.Operation;

public class StockHistoryDaoTest {
	
	private Dao<StockHistory> dao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// truncate tables
		ConnectionManager.truncateTables();
		
		// create category
		Category cat = new Category();
		cat.setName("Cloths");
		create(cat, Category.class);
		
		// create maker
		Maker mk = new Maker();
		mk.setMaker("Egales");
		create(mk, Maker.class);
		
		// create item
		Item item = new Item();
		item.setCategory(1);
		item.setMaker(1);
		item.setModel("Some Model");
		item.setSpecifications("XXL");
		create(item, Item.class);
		
		// create employee
		Employee emp = new Employee();
		emp.setName("Name");
		emp.setPassword("Pass");
		emp.setLoginId("login");
		emp.setPhone("phone");
		emp.setRole(Role.BUYER);
		create(emp, Employee.class);
	}

	@Before
	public void setUp() throws Exception {
		dao = DaoFactory.generate(StockHistory.class);
	}

	@Test
	public void test1() {
		
		StockHistory h = new StockHistory();
		h.setItem(1);
		h.setCount(1000);
		h.setPrevCount(500);
		h.setOperation(Operation.Buy);
		h.setUser(1);
		
		try(Connection conn = ConnectionManager.getConnection()) {
			dao.create(h, conn);
			
			assertEquals(1, h.getId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		try(Connection conn = ConnectionManager.getConnection()) {
			List<StockHistory> list = dao.find("operation = ?", Arrays.asList(Operation.Buy));
			assertEquals(1, list.size());
			
			List<StockHistory> list2 = dao.find("operation = ?", Arrays.asList(Operation.Sell));
			assertEquals(0, list2.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3() {
		try(Connection conn = ConnectionManager.getConnection()) {
			
			dao.update("operation = ?", "id = ?", Arrays.asList(Operation.Sell, 1), conn);
			
			List<StockHistory> list = dao.find("operation = ?", Arrays.asList(Operation.Buy));
			assertEquals(0, list.size());
			
			List<StockHistory> list2 = dao.find("operation = ?", Arrays.asList(Operation.Sell));
			assertEquals(1, list2.size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test4() {
		try(Connection conn = ConnectionManager.getConnection()) {
			
			dao.delete("id = ?", Arrays.asList(1), conn);
			
			List<StockHistory> list = dao.find(null, null);
			assertEquals(0, list.size());
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
