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
import com.jdc.phoneshop.warehouse.model.entity.Maker;

public class MakerModelImp implements MasterDataModel<Maker> {
	
	private Dao<Maker> dao;

    public MakerModelImp() {
    	dao = DaoFactory.generate(Maker.class);
    }

	@Override
	public Set<Maker> getAll() {
		try {
			return new LinkedHashSet<>(dao.find(null, null));
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void add(Maker t) {
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			if(StringUtils.isEmptyString(t.getMaker())) {
				throw new ApplicationException(ErrorType.Warning, "Please enter maker name!");
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
	public void update(Maker t, String value) {

		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);

			dao.update("maker = ?", "id = ?", Arrays.asList(value, t.getId()), conn);
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
	public Set<Maker> find(String name) {
		try {
			
			String where = null;
			List<Object> params = null;
			
			if(null != name && !name.isEmpty()) {
				where = "maker like ?";
				params = Arrays.asList(name.concat("%"));
			}
			
			return new LinkedHashSet<>(dao.find(where, params));
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Maker findByID(int id) {
		try {
			List<Maker> list = dao.find("id = ?", Arrays.asList(id));
			if(list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return null;
	}

}