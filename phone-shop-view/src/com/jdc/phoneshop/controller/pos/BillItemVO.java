package com.jdc.phoneshop.controller.pos;

import com.jdc.phoneshop.pos.model.entity.BillItem;
import com.jdc.phoneshop.warehouse.model.entity.Item;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BillItemVO {

	private Item item;
	private BillItem billItem;
	
	private IntegerProperty totalProperty;
	private IntegerProperty countProperty;
	private IntegerProperty unitPriceProperty;
	
	public BillItemVO() {
		totalProperty = new SimpleIntegerProperty(0);
		countProperty = new SimpleIntegerProperty(0);
		unitPriceProperty = new SimpleIntegerProperty(0);
		totalProperty.bind(countProperty.multiply(unitPriceProperty));
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public BillItem getBillItem() {
		return billItem;
	}

	public void setBillItem(BillItem billItem) {
		this.billItem = billItem;
		countProperty.set(billItem.getCount());
		unitPriceProperty.set(billItem.getUnitPrice());
	}
	
	public IntegerProperty totalProperty() {
		return totalProperty;
	}

}
