package com.jdc.phoneshop.warehouse.model.dao;

import java.util.*;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.warehouse.model.entity.Category;

public class CategoryDao extends Dao<Category> {

    public CategoryDao() {
    	super(Category.class);
    }

	@Override
	protected String getInsertColumns() {
		return "name";
	}

	@Override
	protected String getInsertValues() {
		return "?";
	}

	@Override
	protected List<Object> getInsertParams(Category t) {
		return Arrays.asList(t.getName());
	}

}