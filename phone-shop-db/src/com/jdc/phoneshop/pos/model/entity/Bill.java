package com.jdc.phoneshop.pos.model.entity;

import java.time.LocalDateTime;

import com.jdc.phoneshop.common.db.Column;
import com.jdc.phoneshop.common.db.GeneratedID;
import com.jdc.phoneshop.common.db.Table;

@Table(autoGenerate=true)
public class Bill {
	@GeneratedID
	private int id;
	@Column(name="customer_id")
	private int customerId;
	@Column(name="sub_total")
	private int subTotal;
	@Column(name="ref_date")
	private LocalDateTime referenceDate;
	private int tax;
	private int total;
	private LocalDateTime creation;
	private LocalDateTime modification;
	@Column(name="create_user")
	private int createUserId;
	@Column(name="update_user")
	private int modifyUserId;
	
	public Bill() {
		this.creation = LocalDateTime.now();
		this.modification = LocalDateTime.now();
		this.referenceDate = LocalDateTime.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(int subTotal) {
		this.subTotal = subTotal;
	}

	public LocalDateTime getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(LocalDateTime referenceDate) {
		this.referenceDate = referenceDate;
	}

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public LocalDateTime getCreation() {
		return creation;
	}

	public void setCreation(LocalDateTime creation) {
		this.creation = creation;
		this.modification = creation;
	}

	public LocalDateTime getModification() {
		return modification;
	}

	public void setModification(LocalDateTime modification) {
		this.modification = modification;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
		this.modifyUserId = createUserId;
	}

	public int getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(int modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

}