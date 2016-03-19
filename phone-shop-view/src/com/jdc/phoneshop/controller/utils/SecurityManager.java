package com.jdc.phoneshop.controller.utils;

import com.jdc.phoneshop.admin.model.entity.Employee;

public class SecurityManager {

	private static Employee employee;
	
	public static void setEmployee(Employee employee) {
		SecurityManager.employee = employee;
	}
	
	public static Employee getEmployee() {
		return employee;
	}
}
