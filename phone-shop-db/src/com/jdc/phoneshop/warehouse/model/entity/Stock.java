package com.jdc.phoneshop.warehouse.model.entity;

import com.jdc.phoneshop.common.db.Column;

public class Stock {
	
	@Column(name="item_id")
    private int itemId;
    private int count;
	@Column(name="history_id")
    private int historyId;
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
	public int getHistoryId() {
		return historyId;
	}
	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}

}