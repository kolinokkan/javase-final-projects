package com.jdc.phoneshop.warehouse.model;

import java.util.Set;

import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.Maker;
import com.jdc.phoneshop.warehouse.model.imp.StockModelImp;
import com.jdc.phoneshop.warehouse.vo.StockVO;

public interface StockModel {

    Set<StockVO> getStock(Category cat, Maker mk, String kw, String count);

    Set<Item> getItemsByCount(int count);
    
    public static StockModel getModel() {
    	return new StockModelImp();
    }
    
}