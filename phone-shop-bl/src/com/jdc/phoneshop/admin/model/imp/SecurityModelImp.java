package com.jdc.phoneshop.admin.model.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.jdc.phoneshop.admin.model.SecurityModel;
import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.admin.model.entity.Employee.Role;
import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.StringUtils;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;

public class SecurityModelImp implements SecurityModel {
	
	private Dao<Employee> dao;
	
	public SecurityModelImp() {
		dao = DaoFactory.generate(Employee.class);
	}

	@Override
	public boolean needToSignUp() {
		
		try {
			return dao.find(null, null).size() == 0;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Employee login(String userID, String password) {
		try {
			
			if(StringUtils.isEmptyString(userID)) {
				throw new ApplicationException(ErrorType.Warning, "Please Enter Login ID!");
			}
			
			if(StringUtils.isEmptyString(password)) {
				throw new ApplicationException(ErrorType.Warning, "Please Enter Password!");
			}
			
			List<Employee> list = dao.find("login_id = ?", Arrays.asList(userID));
			if(list.size() == 0) {
				throw new ApplicationException(ErrorType.Warning, "Please check your Login ID!");
			}
			
			if(!list.get(0).getPassword().equals(password)) {
				throw new ApplicationException(ErrorType.Warning, "Please check your password!");
			}
			
			return list.get(0);
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Employee signUp(String name, String userId, String password, String password2) {
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			
			if(StringUtils.isEmptyString(password2)) {
				throw new ApplicationException(ErrorType.Warning, "Please Enter Confirm Password!");
			}

			if(!password.equals(password2)) {
				throw new ApplicationException(ErrorType.Warning, "Please enter the same password with confirm password!");
			}
			
			Employee emp = new Employee();
			emp.setName(name);
			emp.setLoginId(userId);
			emp.setRole(Role.MANAGER);
			emp.setPassword(password);
			
			SecurityModel.checkEmployee(emp);
			
			dao.create(emp, conn);

			conn.commit();
			return emp;

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


}
