package com.jdc.phoneshop.pos.model.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.pos.model.CustomerModel;
import com.jdc.phoneshop.pos.model.entity.Customer;
import com.jdc.phoneshop.utils.ApplicationException;

public class CustomerModelImp implements CustomerModel {

	private Dao<Customer> dao;
	
    public CustomerModelImp() {
    	dao = DaoFactory.generate(Customer.class);
    }

	@Override
	public void crate(Customer c) {
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			dao.create(c, conn);
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

	@Override
	public Set<Customer> search(String name) {

		try {
			
			String where = "name like ?";
			List<Object> params = Arrays.asList(name.concat("%"));
			
			List<Customer> list = dao.find(where, params);
			return new LinkedHashSet<>(list);
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Customer search(int id) {
		try {
			
			String where = "id = ?";
			List<Object> params = Arrays.asList(id);
			
			List<Customer> list = dao.find(where, params);
			
			if(list.size() > 0) {
				return list.get(0);
			}
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return null;
	}

	@Override
	public void update(Customer c) {
		Connection conn = null;

		try {
			
			if(c != null) {
				String set = "name = ?, phone = ?, email = ?, address = ?";
				String where = "id = ?";
				List<Object> params = Arrays.asList(c.getName(), c.getPhone(), c.getEmail(), c.getAddress(), c.getId());

				conn = ConnectionManager.getConnection();
				conn.setAutoCommit(false);
				
				dao.update(set, where, params, conn);
				
				conn.commit();			
			}
			
		} catch (Exception e) {
			try {
				if(null != conn)
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

}