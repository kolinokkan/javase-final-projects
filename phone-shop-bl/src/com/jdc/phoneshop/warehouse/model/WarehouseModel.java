package com.jdc.phoneshop.warehouse.model;

import java.time.LocalDate;
import java.util.Map;

import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.imp.BuyerModel;
import com.jdc.phoneshop.warehouse.model.imp.StoreModel;

public interface WarehouseModel {
	
	Map<Item, Map<LocalDate, Integer>> getCondition(int categoryId, LocalDate dtFrom, LocalDate dtTo);
	
	public static WarehouseModel getBuyerModel() {
		return new BuyerModel();
	}
	
	public static WarehouseModel getStoreModel() {
		return new StoreModel();
	}

}
