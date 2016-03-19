package com.jdc.phoneshop.admin.model.vo;

import java.time.LocalDate;

import com.jdc.phoneshop.pos.model.ItemModel;
import com.jdc.phoneshop.pos.model.entity.BillItem;
import com.jdc.phoneshop.warehouse.model.MasterDataModel;
import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Maker;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;

public class ReportVO implements Comparable<ReportVO> {

	private ItemModel itemModel;
	
	private void initData() {
		this.maker = MasterDataModel.getModel(Maker.class).findByID(item.getMaker());
		this.category = MasterDataModel.getModel(Category.class).findByID(item.getCategory());
	}
	
    public ReportVO(PurchaseItem item, LocalDate refDate) {
    	itemModel = ItemModel.getModel();
    	type = Type.PURCHASE;
    	this.refDate = refDate;
    	this.item = itemModel.search(item.getItem());
    	this.price = item.getPrice();
    	this.count = item.getCount();
    	
    	initData();
    }
    
    public ReportVO(BillItem item, LocalDate refDate) {
    	itemModel = ItemModel.getModel();
    	type = Type.SELL;
    	this.refDate = refDate;
    	this.item = itemModel.search(item.getItemId());
    	this.price = item.getUnitPrice();
    	this.count = item.getCount();

    	initData();
    }

    public LocalDate refDate;

    private Item item;
    private Maker maker;
    private Category category;

    private int count;

    private int price;

    private Type type;

    public enum Type {
        SELL,
        PURCHASE
    }

	public LocalDate getRefDate() {
		return refDate;
	}

	public void setRefDate(LocalDate refDate) {
		this.refDate = refDate;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Maker getMaker() {
		return maker;
	}

	public void setMaker(Maker maker) {
		this.maker = maker;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public int compareTo(ReportVO o) {
		if(null != refDate && null != o.refDate)
			return refDate.compareTo(o.refDate);
		
		return 0;
	}
}