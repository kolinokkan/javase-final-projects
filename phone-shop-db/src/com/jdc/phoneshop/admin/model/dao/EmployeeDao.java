package com.jdc.phoneshop.admin.model.dao;

import java.util.*;

import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.common.db.Dao;

public class EmployeeDao extends Dao<Employee> {

    public EmployeeDao() {
    	super(Employee.class);
    }

	@Override
	protected String getInsertColumns() {
		return "name, login_id, password, phone, role";
	}

	@Override
	protected String getInsertValues() {
		return "?, ?, ?, ?, ?";
	}

	@Override
	protected List<Object> getInsertParams(Employee t) {
		return Arrays.asList(t.getName(), t.getLoginId(), t.getPassword(), t.getPhone(), t.getRole());
	}

}