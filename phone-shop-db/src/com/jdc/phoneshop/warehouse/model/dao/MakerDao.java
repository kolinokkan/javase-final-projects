package com.jdc.phoneshop.warehouse.model.dao;

import java.util.Arrays;
import java.util.List;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.warehouse.model.entity.Maker;

public class MakerDao extends Dao<Maker> {

    public MakerDao() {
    	super(Maker.class);
    }

	@Override
	protected String getInsertColumns() {
		return "maker";
	}

	@Override
	protected String getInsertValues() {
		return "?";
	}

	@Override
	protected List<Object> getInsertParams(Maker t) {
		return Arrays.asList(t.getMaker());
	}

}