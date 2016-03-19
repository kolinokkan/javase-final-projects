package com.jdc.phoneshop.pos.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jdc.phoneshop.pos.model.imp.ItemModelImp;
import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Maker;

public interface ItemModel {

	Item search(int id);

	Set<Item> search(Category cat, Maker mk, String kw);

	Map<Maker, List<ItemVO>> findByCategory(int catId);

	int getSellPrice(Item item);

	int getSellPrice(Item item, int purchaseID);

	void create(Item item);

	void create(List<Item> items);

	public static ItemModel getModel() {
		return new ItemModelImp();
	}
	
	public static class ItemVO {
		private Item item;
		private int price;
		
		public Item getItem() {
			return item;
		}
		public void setItem(Item item) {
			this.item = item;
		}
		public int getPrice() {
			return price;
		}
		public void setPrice(int price) {
			this.price = price;
		}
	}

}