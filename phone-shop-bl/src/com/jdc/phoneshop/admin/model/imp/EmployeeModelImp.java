package com.jdc.phoneshop.admin.model.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.admin.model.EmployeeModel;
import com.jdc.phoneshop.admin.model.SecurityModel;
import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.admin.model.entity.Employee.Role;
import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.utils.StringUtils;

public class EmployeeModelImp implements EmployeeModel {
	
	private Dao<Employee> dao;

    public EmployeeModelImp() {
    	dao = DaoFactory.generate(Employee.class);
    }

	@Override
	public void create(Employee emp) {
		
		Connection conn = null;
		
		try {
			
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			SecurityModel.checkEmployee(emp);

			dao.create(emp, conn);
			conn.commit();
			
		} catch (Exception e) {
			
			try {
				conn.rollback();
				throw new ApplicationException(e);
			} catch (SQLException e1) {
				throw new ApplicationException(e1);
			}
		} finally {
			
			if(null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
			
		}
		
	}

	@Override
	public Employee findById(int id) {
		
		try {
			List<Employee> list = dao.find("id = ?", Arrays.asList(id));
			
			if(list.size() > 0) {
				return list.get(0);
			}
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		}		
		return null;
	}

	@Override
	public Set<Employee> getAll() {
		
		try {
			List<Employee> list = dao.find(null, null);
			return new LinkedHashSet<>(list);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		
	}

	@Override
	public void unactivate(Employee emp) {
		
		if(null == emp) {
			throw new ApplicationException(ErrorType.Warning, "Please select an Employee.");
		}
		
		Connection conn = null;
		
		try {
			
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			dao.update("role = ?", "id = ?", Arrays.asList(Role.CANCEL, emp.getId()), conn);

			conn.commit();
			
			emp.setRole(Role.CANCEL);
			
		} catch (Exception e) {
			try {
				conn.rollback();
				throw new ApplicationException(e);
			} catch (SQLException e1) {
				throw new ApplicationException(e1);
			}
			
		} finally {
			
			if(null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
		}		
	}

	@Override
	public List<Employee> find(Role role, String name) {
		try {
			StringBuffer sb = new StringBuffer();
			List<Object> params = new ArrayList<>();
			
			if(null != role) {
				sb.append("role = ? ");
				params.add(role);
			}
			
			if(StringUtils.isEmptyString(name)) {
				if(params.size() > 0) {
					sb.append("and ");
				}
				sb.append("name like ?");
				params.add(name.concat("%"));
			}
			
			return dao.find(sb.toString(), params);
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}


}