package com.jdc.phoneshop.admin.model.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.admin.model.PurchaseAdminModel;
import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.warehouse.model.PurchaseModel;
import com.jdc.phoneshop.warehouse.model.entity.Purchase;
import com.jdc.phoneshop.warehouse.model.entity.Purchase.PurchaseStatus;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;
import com.jdc.phoneshop.warehouse.vo.PurchaseItemVO;

public class PurchaseAdminModelImp implements PurchaseAdminModel {

	private Dao<Purchase> pcDao;
	
    public PurchaseAdminModelImp() {
    	pcDao = DaoFactory.generate(Purchase.class);
    }

	@Override
	public Set<Purchase> getPurchaseList(PurchaseStatus status) {
		return PurchaseModel.getModel().getPurchaseList(status);
	}

	@Override
	public Set<PurchaseItem> getPurchaseItems(Purchase purchase) {
		return PurchaseModel.getModel().getPurchaseItems(purchase);
	}

	@Override
	public void cancel(Purchase purchase) {
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			updateStatus(purchase, PurchaseStatus.Cancel, conn);

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
	public Set<Purchase> getPurchaseList(PurchaseStatus status, Employee employee) {
		try {
			
			String where = null;
			List<Object> params = new ArrayList<>();
			StringBuilder sb = new StringBuilder();
			
			if(null != employee) {
				sb.append("employee_id = ? ");
				params.add(employee.getId());
			}
			
			if(null != status) {
				
				if(params.size() > 0) {
					sb.append("and ");
				}
				
				sb.append("status = ?");
				params.add(status);
			}
			
			if(params.size() > 0) {
				where = sb.toString();
			}
			
			return new HashSet<>(pcDao.find(where, params));
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public void udpateAndConfirm(Set<PurchaseItemVO> items, Purchase p) {
		
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);


			// update
			if(items.size() > 0) {
				PurchaseModel.getModel().updatePurchaseItems(items, conn);
			}
			
			// confirm
			updateStatus(p, PurchaseStatus.Confirmed, conn);

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

	private void updateStatus(Purchase pc, PurchaseStatus status, Connection conn) throws SQLException {
		
		pcDao.update("status = ?", "id = ?", 
				Arrays.asList(status, pc.getId()), conn);
	}



}