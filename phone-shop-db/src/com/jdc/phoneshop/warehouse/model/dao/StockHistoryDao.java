package com.jdc.phoneshop.warehouse.model.dao;

import java.util.Arrays;
import java.util.List;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.warehouse.model.entity.StockHistory;

public class StockHistoryDao extends Dao<StockHistory> {

    public StockHistoryDao() {
    	super(StockHistory.class);
    }

	@Override
	protected String getInsertColumns() {
		return "item_id, prev_count, count, operation, employee_id, modification";
	}

	@Override
	protected String getInsertValues() {
		return "?, ?, ?, ?, ?, ?";
	}

	@Override
	protected List<Object> getInsertParams(StockHistory t) {
		return Arrays.asList(
				t.getItem(), 
				t.getPrevCount(), 
				t.getCount(), 
				t.getOperation(), 
				t.getUser(), 
				t.getModification());
	}

}