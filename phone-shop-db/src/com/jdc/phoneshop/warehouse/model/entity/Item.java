package com.jdc.phoneshop.warehouse.model.entity;

import com.jdc.phoneshop.common.db.Column;
import com.jdc.phoneshop.common.db.GeneratedID;
import com.jdc.phoneshop.common.db.Table;

@Table(autoGenerate=true)
public class Item {
	@GeneratedID
	private int id;
	@Column(name="category_id")
	private int category;
	@Column(name="maker_id")
	private int maker;
	private String model;
	@Column(name="specification")
	private String specifications;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getMaker() {
		return maker;
	}

	public void setMaker(int maker) {
		this.maker = maker;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

}