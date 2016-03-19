package com.jdc.phoneshop.warehouse.model;

import java.sql.Connection;
import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Purchase;
import com.jdc.phoneshop.warehouse.model.entity.Purchase.PurchaseStatus;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;
import com.jdc.phoneshop.warehouse.model.imp.PurchaseModelImp;
import com.jdc.phoneshop.warehouse.vo.PurchaseItemVO;

public interface PurchaseModel {

    Set<Purchase> getPurchaseList(PurchaseStatus status);

    Set<PurchaseItem> getPurchaseItems(Purchase p);

    Purchase findById(int id);
    
    void updatePurchase(Set<PurchaseItemVO> items);

    void purchase(List<Item> items, int count, Employee emp);
    void updatePurchaseItems(Set<PurchaseItemVO> items, Connection conn) throws Exception;
    
    public static PurchaseModel getModel() {
    	return new PurchaseModelImp();
    }


}