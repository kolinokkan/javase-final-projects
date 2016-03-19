package com.jdc.phoneshop.warehouse.vo;

import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice;
import com.jdc.phoneshop.warehouse.model.entity.Maker;
import com.jdc.phoneshop.warehouse.model.entity.Stock;

public class StockVO {
	
	private Stock stock;
	private Item item;
	private Category category;
	private Maker maker;
	private ItemPrice itemPrice;
	
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Maker getMaker() {
		return maker;
	}
	public void setMaker(Maker maker) {
		this.maker = maker;
	}
	public ItemPrice getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(ItemPrice itemPrice) {
		this.itemPrice = itemPrice;
	}
}
