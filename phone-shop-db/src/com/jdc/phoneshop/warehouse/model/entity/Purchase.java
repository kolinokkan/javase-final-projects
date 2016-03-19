package com.jdc.phoneshop.warehouse.model.entity;

import java.time.LocalDate;

import com.jdc.phoneshop.common.db.Column;
import com.jdc.phoneshop.common.db.GeneratedID;
import com.jdc.phoneshop.common.db.Table;

@Table(autoGenerate=true)
public class Purchase {

	public enum PurchaseStatus {
	    Apply,
	    Confirmed,
	    Cancel,
	    Complete
	}
	@GeneratedID
	private int id;
	@Column(name="employee_id")
    private int user;
	@Column(name="ref_date")
    private LocalDate refDate;
    private PurchaseStatus status;
    
    public Purchase() {
    	status = PurchaseStatus.Apply;
    	refDate = LocalDate.now();
	}
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser() {
		return user;
	}
	public void setUser(int user) {
		this.user = user;
	}
	public LocalDate getRefDate() {
		return refDate;
	}
	public void setRefDate(LocalDate refDate) {
		this.refDate = refDate;
	}
	public PurchaseStatus getStatus() {
		return status;
	}
	public void setStatus(PurchaseStatus status) {
		this.status = status;
	}


}