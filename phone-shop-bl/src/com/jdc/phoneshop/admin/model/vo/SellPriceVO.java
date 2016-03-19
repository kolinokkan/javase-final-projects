package com.jdc.phoneshop.admin.model.vo;

import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;

public class SellPriceVO {

	private Item item;
	private ItemPrice itemPrice;
	private PurchaseItem purchaseItem;

	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public ItemPrice getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(ItemPrice itemPrice) {
		this.itemPrice = itemPrice;
	}
	public PurchaseItem getPurchaseItem() {
		return purchaseItem;
	}
	public void setPurchaseItem(PurchaseItem purchaseItem) {
		this.purchaseItem = purchaseItem;
	}
	
}
