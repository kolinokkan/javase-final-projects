package com.jdc.phoneshop.pos.model.entity;

import com.jdc.phoneshop.common.db.GeneratedID;
import com.jdc.phoneshop.common.db.Table;

@Table(autoGenerate=true)
public class Customer {

	@GeneratedID
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

    
    
}