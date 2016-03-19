package com.jdc.phoneshop.warehouse.vo;

import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;

public class PurchaseItemVO {
	
	private PurchaseItem item;
	private int sellPrice;
	private boolean isUpdated;
	
	public boolean isUpdated() {
		return isUpdated;
	}
	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
	public PurchaseItem getItem() {
		return item;
	}
	public void setItem(PurchaseItem item) {
		this.item = item;
	}
	public int getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}
	
}
