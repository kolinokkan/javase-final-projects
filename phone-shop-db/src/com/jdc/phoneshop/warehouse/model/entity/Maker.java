package com.jdc.phoneshop.warehouse.model.entity;

import com.jdc.phoneshop.common.db.GeneratedID;
import com.jdc.phoneshop.common.db.Table;

@Table(autoGenerate=true)
public class Maker {
	@GeneratedID
    private int id;
    private String maker;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMaker() {
		return maker;
	}
	public void setMaker(String maker) {
		this.maker = maker;
	}
	@Override
	public String toString() {
		return maker;
	}
}