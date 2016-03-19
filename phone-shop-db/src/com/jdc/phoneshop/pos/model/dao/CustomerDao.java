package com.jdc.phoneshop.pos.model.dao;

import java.util.*;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.pos.model.entity.Customer;

public class CustomerDao extends Dao<Customer> {

    public CustomerDao() {
    	super(Customer.class);
    }

	@Override
	protected String getInsertColumns() {
		return "name, phone, email, address";
	}

	@Override
	protected String getInsertValues() {
		return "?, ?, ?, ?";
	}

	@Override
	protected List<Object> getInsertParams(Customer t) {
		return Arrays.asList(t.getName(), t.getPhone(), t.getEmail(), t.getAddress());
	}

}