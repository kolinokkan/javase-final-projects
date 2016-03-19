package com.jdc.phoneshop.pos.model;

import java.util.Set;

import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.pos.model.entity.BillItem;
import com.jdc.phoneshop.pos.model.entity.Customer;
import com.jdc.phoneshop.pos.model.imp.BillServiceImp;

public interface BillService {

    void addItem(Set<BillItem> item);

    void create();
    
    public static BillService getService(Customer c, Employee emp) {
    	return new BillServiceImp(c, emp);
    }
}
