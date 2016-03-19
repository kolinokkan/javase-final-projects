package com.jdc.phoneshop.warehouse.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;

public class PurchaseItemDao extends Dao<PurchaseItem> {

    public PurchaseItemDao() {
    	super(PurchaseItem.class);
    }

	@Override
	protected String getInsertColumns() {
		return "purchase_id, item_id, count, price";
	}

	@Override
	protected String getInsertValues() {
		return "?, ?, ?, ?";
	}

	@Override
	protected List<Object> getInsertParams(PurchaseItem t) {
		return Arrays.asList(t.getPurchase(), t.getItem(), t.getCount(), t.getPrice());
	}

	public PurchaseItem find(int itemId, LocalDate refDate) throws SQLException {
		PurchaseItem item = null;
		String sql = "select * from purchase_item pi left join purchase p on "
				+ "p.id = pi.purchase_id where pi.item_id = ? and p.ref_date = ?";
		
		try(Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, itemId);
			stmt.setDate(2, Date.valueOf(refDate));
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				if(item == null) {
					item = new PurchaseItem();
				}
				item.setPurchase(rs.getInt("purchase_id"));
				item.setItem(rs.getInt("item_id"));
				item.setCount(rs.getInt("count"));
				item.setPrice(rs.getInt("price"));
			}
		} 
		
		return item;
	}

}