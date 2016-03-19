package com.jdc.phoneshop.warehouse.model.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.utils.StringUtils;
import com.jdc.phoneshop.warehouse.model.MasterDataModel;
import com.jdc.phoneshop.warehouse.model.entity.Category;

public class CategoryModelImp implements MasterDataModel<Category> {

	private Dao<Category> dao;
	
    public CategoryModelImp() {
    	dao = DaoFactory.generate(Category.class);
    }

	@Override
	public Set<Category> getAll() {
		try {
			return new LinkedHashSet<>(dao.find(null, null));
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void add(Category t) {
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			if(StringUtils.isEmptyString(t.getName())) {
				throw new ApplicationException(ErrorType.Warning, "You should enter Category Name!");
			}
			
			dao.create(t, conn);
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
	public void update(Category t, String value) {
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			dao.update("name = ?", "id = ?", Arrays.asList(value, t.getId()), conn);

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
	public Set<Category> find(String name) {
		try {
			String where = null;
			List<Object> params = null;
			
			if(null != name && !name.isEmpty()) {
				where = "name like ?";
				params = Arrays.asList(name.concat("%"));
			}
			
			return new LinkedHashSet<>(dao.find(where, params));
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Category findByID(int id) {
		try {
			List<Category> list = dao.find("id = ?", Arrays.asList(id));
			if(list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return null;
	}


}