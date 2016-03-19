package com.jdc.phoneshop.warehouse.model;

import java.util.Set;

import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Maker;
import com.jdc.phoneshop.warehouse.model.imp.CategoryModelImp;
import com.jdc.phoneshop.warehouse.model.imp.MakerModelImp;

public interface MasterDataModel<T> {

    Set<T> getAll();

    void add(T t);

    void update(T t, String value);
    
    Set<T> find(String name);
    
    T findByID(int id);
    
    @SuppressWarnings("unchecked")
	public static<T> MasterDataModel<T> getModel(Class<T> type) {
    	
    	if(type.equals(Category.class)) {
    		return (MasterDataModel<T>) new CategoryModelImp();
    	} else if (type.equals(Maker.class)) {
    		return (MasterDataModel<T>) new MakerModelImp();
    	}
    	
    	return null;
    	
    }

}