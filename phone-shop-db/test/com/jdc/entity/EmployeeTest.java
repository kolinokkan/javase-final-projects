package com.jdc.entity;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
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

public class EmployeeTest {
	
	private Dao<Employee> dao;
	
	@BeforeClass
	public static void setUpBefore() {
		ConnectionManager.truncateTables();
	}

	@Before
	public void setUp() throws Exception {
		dao = DaoFactory.generate(Employee.class);
	}

	@Test
	public void test1() {
		
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			Employee emp = new Employee();
			emp.setName("Name");
			emp.setPassword("Pass");
			emp.setLoginId("login");
			emp.setPhone("phone");
			emp.setRole(Role.BUYER);
			
			dao.create(emp, conn);
			
			conn.commit();
			
			assertEquals(1, emp.getId());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Test
	public void test2() {
		try {
			List<Employee> list = dao.find(null, null);
			assertEquals(1, list.size());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test3() {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			dao.update("name = ?", "id = ?", Arrays.asList("Test3", 1), conn);
			
			conn.commit();
			
			List<Employee> list = dao.find(null, null);
			
			assertEquals("Test3", list.get(0).getName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void test4() {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			dao.delete("id = ?", Arrays.asList(1), conn);
			
			conn.commit();
			
			List<Employee> list = dao.find(null, null);
			
			assertEquals(0, list.size());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
