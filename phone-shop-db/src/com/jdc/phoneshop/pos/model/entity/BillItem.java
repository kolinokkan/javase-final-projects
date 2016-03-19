package com.jdc.phoneshop.pos.model.entity;

import com.jdc.phoneshop.common.db.Column;
import com.jdc.phoneshop.common.db.Table;

@Table(name="bill_item")
public class BillItem {
	@Column(name="bill_id")
	private int billId;
	@Column(name="item_id")
	private int itemId;
	private int count;
	@Column(name="unit_price")
	private int unitPrice;

	public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

}