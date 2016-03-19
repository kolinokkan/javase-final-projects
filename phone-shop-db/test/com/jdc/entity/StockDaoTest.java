package com.jdc.entity;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Maker;
import com.jdc.phoneshop.warehouse.model.entity.Stock;

public class StockDaoTest {
	
	private Dao<Stock> dao;

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
		
	}
	
	static<T> void create(T t, Class<T> type) {
		
		try(Connection conn = ConnectionManager.getConnection()) {
			DaoFactory.generate(type).create(t, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws Exception {
		// initialize dao object
		dao = DaoFactory.generate(Stock.class);
	}

	@Test
	public void test1() {
		
		Stock stock = new Stock();
		stock.setCount(500);
		stock.setHistoryId(10);
		stock.setItemId(1);
		
		try(Connection conn = ConnectionManager.getConnection()) {
			dao.create(stock, conn);
			
			Stock result = dao.find("item_id = ?", Arrays.asList(1)).get(0);
			assertEquals(500, result.getCount());
			assertEquals(10, result.getHistoryId());
			assertEquals(1, result.getItemId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		
		try(Connection conn = ConnectionManager.getConnection()) {
			
			dao.update("count = ?", "item_id = ?", Arrays.asList(490, 1), conn);
			
			List<Stock> list = dao.find("item_id = ?", Arrays.asList(1));
			
			Stock result = list.get(0);
			assertEquals(490, result.getCount());
			assertEquals(10, result.getHistoryId());
			assertEquals(1, result.getItemId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test3() {
		try(Connection conn = ConnectionManager.getConnection()) {
			
			dao.delete("item_id = ?", Arrays.asList(1), conn);
			
			List<Stock> list = dao.find("item_id = ?", Arrays.asList(1));
			assertEquals(0, list.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
