package com.jdc.phoneshop.pos.model.dao;

import java.util.*;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.pos.model.entity.Bill;

public class BillDao extends Dao<Bill> {

	public BillDao() {
		super(Bill.class);
	}

	@Override
	protected String getInsertColumns() {
		return "customer_id, sub_total, ref_date, tax, total, creation, modification, create_user, update_user";
	}

	@Override
	protected String getInsertValues() {
		return "?, ?, ?, ?, ?, ?, ?, ?, ?";
	}

	@Override
	protected List<Object> getInsertParams(Bill t) {
		return Arrays.asList(t.getCustomerId(), t.getSubTotal(), t.getReferenceDate(), t.getTax(), t.getTotal(),
				t.getCreation(), t.getModification(), t.getCreateUserId(), t.getModifyUserId());
	}

}