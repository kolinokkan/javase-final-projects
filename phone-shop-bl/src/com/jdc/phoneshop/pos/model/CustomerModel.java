package com.jdc.phoneshop.pos.model;

import java.util.Set;

import com.jdc.phoneshop.pos.model.entity.Customer;
import com.jdc.phoneshop.pos.model.imp.CustomerModelImp;

public interface CustomerModel {

    void crate(Customer c);

    Set<Customer> search(String name);

    Customer search(int id);

    void update(Customer c);
    
    public static CustomerModel getModel() {
    	return new CustomerModelImp();
    }

}