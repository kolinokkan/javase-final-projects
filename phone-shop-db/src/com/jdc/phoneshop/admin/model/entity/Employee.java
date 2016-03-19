package com.jdc.phoneshop.admin.model.entity;

import com.jdc.phoneshop.common.db.Column;
import com.jdc.phoneshop.common.db.GeneratedID;
import com.jdc.phoneshop.common.db.Table;

@Table(autoGenerate=true)
public class Employee {

	public enum Role {
	    MANAGER,
	    BUYER,
	    STORE,
	    POS,
	    CANCEL
	}

	@GeneratedID
	private int id;
    private String name;
    @Column(name="login_id")
    private String loginId;
    private String password;
    private String phone;
    private Role role;
	
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
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return name;
	}
}