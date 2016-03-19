package com.jdc.phoneshop.warehouse.model.imp;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.warehouse.model.WarehouseModel;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.StockHistory;
import com.jdc.phoneshop.warehouse.model.entity.StockHistory.Operation;

/**
 * Look Sale Status
 * 
 * @author minlwin
 *
 */
public class BuyerModel implements WarehouseModel {

	@Override
	public Map<Item, Map<LocalDate, Integer>> getCondition(int categoryId, LocalDate dtFrom, LocalDate dtTo) {
		Map<Item, Map<LocalDate, Integer>> result = new HashMap<>();
		
		try {
			// find item by category
			List<Item> items = DaoFactory.generate(Item.class).find("category_id = ?", Arrays.asList(categoryId));
			
			// find stock purchase history by item
			for (Item item : items) {
				Map<LocalDate, Integer> stockMap = new HashMap<>();
				result.put(item, stockMap);
				List<StockHistory> histories = DaoFactory.generate(StockHistory.class).find("item_id = ? and operation = ? and (modification >= ? and modification < ?)", 
						Arrays.asList(item.getId(), Operation.Sell, dtFrom.atStartOfDay(), dtTo.atStartOfDay().plusDays(1)));
				
				Collections.sort(histories, new Comparator<StockHistory>() {

					@Override
					public int compare(StockHistory o1, StockHistory o2) {
						return o1.getModification().compareTo(o2.getModification());
					}
				});
				
				for (StockHistory stockHistory : histories) {
					addToMap(stockMap, stockHistory);
				}
			}
			
			// collect by date
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return result;
	}

	private void addToMap(Map<LocalDate, Integer> result, StockHistory stockHistory) {
		Integer count = result.get(stockHistory.getModification().toLocalDate());
		if(null != count) {
			result.put(stockHistory.getModification().toLocalDate(), stockHistory.getCount() + count);
		} else {
			result.put(stockHistory.getModification().toLocalDate(), stockHistory.getCount());
		}
	}

}
