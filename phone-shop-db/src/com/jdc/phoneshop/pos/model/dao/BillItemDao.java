package com.jdc.phoneshop.pos.model.dao;

import java.util.*;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.pos.model.entity.BillItem;

public class BillItemDao extends Dao<BillItem> {

    public BillItemDao() {
    	super(BillItem.class);
    }

	@Override
	protected String getInsertColumns() {
		return "bill_id, item_id, count, unit_price";
	}

	@Override
	protected String getInsertValues() {
		return "?, ?, ?, ?";
	}

	@Override
	protected List<Object> getInsertParams(BillItem t) {
		return Arrays.asList(t.getBillId(), t.getItemId(), t.getCount(), t.getUnitPrice());
	}

}