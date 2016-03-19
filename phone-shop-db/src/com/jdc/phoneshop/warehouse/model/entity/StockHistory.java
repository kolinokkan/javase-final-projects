package com.jdc.phoneshop.warehouse.model.entity;

import java.time.LocalDateTime;

import com.jdc.phoneshop.common.db.Column;
import com.jdc.phoneshop.common.db.GeneratedID;
import com.jdc.phoneshop.common.db.Table;

@Table(autoGenerate=true, name="stock_history")
public class StockHistory {

	public enum Operation {
	    Buy,
	    Sell
	}

	@GeneratedID
	private int id;
	@Column(name="item_id")
    private int item;
	@Column(name="prev_count")
    private int prevCount;
    private int count;
    private Operation operation;
	@Column(name="employee_id")
    private int user;
    private LocalDateTime modification;
    
    public StockHistory() {
    	modification = LocalDateTime.now();
	}
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public int getPrevCount() {
		return prevCount;
	}
	public void setPrevCount(int prevCount) {
		this.prevCount = prevCount;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public LocalDateTime getModification() {
		return modification;
	}
	public void setModification(LocalDateTime modification) {
		this.modification = modification;
	}

}