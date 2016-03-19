package com.jdc.phoneshop.admin.model;

import java.util.Set;

import com.jdc.phoneshop.admin.model.entity.Employee;
import com.jdc.phoneshop.admin.model.imp.PurchaseAdminModelImp;
import com.jdc.phoneshop.warehouse.model.entity.Purchase;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;
import com.jdc.phoneshop.warehouse.model.entity.Purchase.PurchaseStatus;
import com.jdc.phoneshop.warehouse.vo.PurchaseItemVO;

public interface PurchaseAdminModel {

    Set<Purchase> getPurchaseList(PurchaseStatus status);
	Set<Purchase> getPurchaseList(PurchaseStatus status, Employee employee);

	Set<PurchaseItem> getPurchaseItems(Purchase purchase);

    void cancel(Purchase purchase);
    void udpateAndConfirm(Set<PurchaseItemVO> items, Purchase p);

    public static PurchaseAdminModel getModel() {
    	return new PurchaseAdminModelImp();
    }

}