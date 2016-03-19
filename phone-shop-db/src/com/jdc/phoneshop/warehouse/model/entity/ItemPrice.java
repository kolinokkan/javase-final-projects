package com.jdc.phoneshop.warehouse.model.entity;

import java.time.LocalDate;

import com.jdc.phoneshop.common.db.Column;
import com.jdc.phoneshop.common.db.Table;

@Table(name="item_price")
public class ItemPrice {
	
	public enum PriceStatus {
	    NotConfirm,
	    Confirmed,
	    Cancel
	}
	
	@Column(name="item_id")
    private int item;
    @Column(name="purchase_id")
	private int purchaseId;
	private int price;
	@Column(name="ref_date")
    private LocalDate refDate;
    private PriceStatus status;
    
    public ItemPrice() {
    	status = PriceStatus.NotConfirm;
    	refDate = LocalDate.now();
	}
    
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public LocalDate getRefDate() {
		return refDate;
	}
	public void setRefDate(LocalDate refDate) {
		this.refDate = refDate;
	}
	public PriceStatus getStatus() {
		return status;
	}
	public void setStatus(PriceStatus status) {
		this.status = status;
	}

	public int getPurchaseId() {
		return purchaseId;
	}
	
	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}
    
}