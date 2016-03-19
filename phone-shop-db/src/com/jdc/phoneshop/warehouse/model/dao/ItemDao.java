package com.jdc.phoneshop.warehouse.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.warehouse.model.entity.Item;

public class ItemDao extends Dao<Item> {

    public ItemDao() {
    	super(Item.class);
    }

	@Override
	protected String getInsertColumns() {
		return "model, specification, category_id, maker_id";
	}

	@Override
	protected String getInsertValues() {
		return "?, ?, ?, ?";
	}

	@Override
	protected List<Object> getInsertParams(Item t) {
		return Arrays.asList(t.getModel(), t.getSpecifications(), t.getCategory(), t.getMaker());
	}
	
	public List<Item> search(int catId, int makerId) throws SQLException, InstantiationException, IllegalAccessException {
		List<Item> list = new ArrayList<>();
		List<Integer> params = new ArrayList<>();
		params.add(makerId);
		
		String sql = "select * from item inner join stock on item.id = stock.item_id where item.maker_id = ?";
		if(catId > 0) {
			sql = sql.concat(" and category_id = ?");
			params.add(catId);
		}
		
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setInt(i + 1, params.get(i));
			}
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				list.add(getObject(rs));
			}
		}
		
		return list;
	}

}