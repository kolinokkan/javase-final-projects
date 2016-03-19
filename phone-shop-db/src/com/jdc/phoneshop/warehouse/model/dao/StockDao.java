package com.jdc.phoneshop.warehouse.model.dao;

import java.util.Arrays;
import java.util.List;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.warehouse.model.entity.Stock;

public class StockDao extends Dao<Stock> {

    public StockDao() {
    	super(Stock.class);
    }

	@Override
	protected String getInsertColumns() {
		return "item_id, count, history_id";
	}

	@Override
	protected String getInsertValues() {
		return "?, ?, ?";
	}

	@Override
	protected List<Object> getInsertParams(Stock t) {
		return Arrays.asList(t.getItemId(), t.getCount(), t.getHistoryId());
	}

}