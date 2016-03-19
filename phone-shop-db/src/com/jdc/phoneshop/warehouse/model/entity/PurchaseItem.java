package com.jdc.phoneshop.warehouse.model.entity;

import com.jdc.phoneshop.common.db.Column;
import com.jdc.phoneshop.common.db.Table;

@Table(name="purchase_item")
public class PurchaseItem {

	@Column(name="purchase_id")
    private int purchase;
	@Column(name="item_id")
    private int item;
    private int count;
    private int price;
    
	public int getPurchase() {
		return purchase;
	}
	public void setPurchase(int purchase) {
		this.purchase = purchase;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}


}