package com.jdc.phoneshop.pos.model;

import java.time.LocalDate;
import java.util.Set;

import com.jdc.phoneshop.pos.model.entity.Bill;
import com.jdc.phoneshop.pos.model.entity.BillItem;
import com.jdc.phoneshop.pos.model.entity.Customer;
import com.jdc.phoneshop.pos.model.imp.BillModelImp;

public interface BillModel {

    Bill search(int id);

    Set<Bill> search(LocalDate refDate, Customer cust);
    
    Set<BillItem> searchItems(Bill bill);
   
    public static BillModel getModel() {
    	return new BillModelImp();
    }
    
    double getTaxRate();

}