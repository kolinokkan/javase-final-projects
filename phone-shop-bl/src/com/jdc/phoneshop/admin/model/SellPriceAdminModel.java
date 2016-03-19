package com.jdc.phoneshop.admin.model;

import java.util.Set;

import com.jdc.phoneshop.admin.model.imp.SellPriceAdminModelImp;
import com.jdc.phoneshop.admin.model.vo.SellPriceVO;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice.PriceStatus;

public interface SellPriceAdminModel {

    Set<SellPriceVO> getSellPriceList(PriceStatus ... status);

    void confirm(Set<ItemPrice> price, int employeeID);

    void cancel(Set<ItemPrice> price);

    void updatePrice(ItemPrice price, int newPrice);
    
    public static SellPriceAdminModel getModel() {
    	return new SellPriceAdminModelImp();
    }

}