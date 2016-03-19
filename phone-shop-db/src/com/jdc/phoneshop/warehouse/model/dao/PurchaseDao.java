package com.jdc.phoneshop.warehouse.model.dao;

import java.util.Arrays;
import java.util.List;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.warehouse.model.entity.Purchase;

public class PurchaseDao extends Dao<Purchase> {

    public PurchaseDao() {
    	super(Purchase.class);
    }

	@Override
	protected String getInsertColumns() {
		return "employee_id, ref_date, status";
	}

	@Override
	protected String getInsertValues() {
		return "?, ?, ?";
	}

	@Override
	protected List<Object> getInsertParams(Purchase t) {
		return Arrays.asList(t.getUser(), t.getRefDate(), t.getStatus());
	}

}