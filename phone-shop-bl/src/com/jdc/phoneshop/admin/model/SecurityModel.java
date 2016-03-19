package com.jdc.phoneshop.admin.model;

import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.admin.model.imp.SecurityModelImp;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.StringUtils;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;

public interface SecurityModel {
	
	boolean needToSignUp();
	Employee login(String userID, String password);
	Employee signUp(String name, String userId, String password, String password2);
	
	public static SecurityModel getModel() {
		return new SecurityModelImp();
	}

	public static void checkEmployee(Employee emp) {
		if(StringUtils.isEmptyString(emp.getName())) {
			throw new ApplicationException(ErrorType.Warning, "Please Enter Name!");
		}

		if(StringUtils.isEmptyString(emp.getLoginId())) {
			throw new ApplicationException(ErrorType.Warning, "Please Enter Login ID!");
		}
		
		if(StringUtils.isEmptyString(emp.getPassword())) {
			throw new ApplicationException(ErrorType.Warning, "Please Enter Password!");
		}
		
		if(null == emp.getRole()) {
			throw new ApplicationException(ErrorType.Warning, "Please Select A Role!");
		}
	}
}
