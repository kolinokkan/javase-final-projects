package com.jdc.phoneshop.warehouse.model.dao;

import java.util.Arrays;
import java.util.List;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice;

public class ItemPriceDao extends Dao<ItemPrice> {

    public ItemPriceDao() {
    	super(ItemPrice.class);
    }

	@Override
	protected String getInsertColumns() {
		return "item_id, purchase_id, ref_date, price, status";
	}

	@Override
	protected String getInsertValues() {
		return "?, ?, ?, ?, ?";
	}

	@Override
	protected List<Object> getInsertParams(ItemPrice t) {
		return Arrays.asList(t.getItem(), t.getPurchaseId(), t.getRefDate(), t.getPrice(), t.getStatus());
	}

}