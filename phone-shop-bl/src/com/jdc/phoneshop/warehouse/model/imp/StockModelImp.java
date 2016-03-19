package com.jdc.phoneshop.warehouse.model.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.utils.StringUtils;
import com.jdc.phoneshop.warehouse.model.StockModel;
import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice.PriceStatus;
import com.jdc.phoneshop.warehouse.model.entity.Maker;
import com.jdc.phoneshop.warehouse.model.entity.Stock;
import com.jdc.phoneshop.warehouse.vo.StockVO;

public class StockModelImp implements StockModel {

	private Dao<Stock> stDao;
	private Dao<Item> itDao;
	private Dao<Category> catDao;
	private Dao<Maker> mkDao;
	private Dao<ItemPrice> priceDao;

	public StockModelImp() {
		stDao = DaoFactory.generate(Stock.class);
		itDao = DaoFactory.generate(Item.class);
		catDao = DaoFactory.generate(Category.class);
		mkDao = DaoFactory.generate(Maker.class);
		priceDao = DaoFactory.generate(ItemPrice.class);
	}

	@Override
	public Set<StockVO> getStock(Category cat, Maker mk, String kw, String count) {

		try {
			Set<StockVO> stocks = new LinkedHashSet<>();

			StringBuffer sb = new StringBuffer();
			List<Object> params = new ArrayList<>();

			// count
			if (!StringUtils.isEmptyString(count)) {

				int cnt = Integer.parseInt(count);

				List<Stock> list = stDao.find("count <= ?", Arrays.asList(cnt));
				for (int i = 0; i < list.size(); i++) {

					if (i == 0) {
						sb.append("id in (");
					} else {
						sb.append(", ");
					}

					sb.append("?");

					if (i == list.size() - 1) {
						sb.append(") ");
					}
					params.add(list.get(i).getItemId());
				}
				
				if(list.size() == 0) {
					return stocks;
				}
			}

			if (null != cat) {
				if (params.size() > 0) {
					sb.append("and ");
				}
				sb.append("category_id = ? ");
				params.add(cat.getId());
			}

			if (null != mk) {
				if (params.size() > 0) {
					sb.append("and ");
				}
				sb.append("maker_id = ? ");
				params.add(mk.getId());
			}

			if (!StringUtils.isEmptyString(kw)) {
				if (params.size() > 0) {
					sb.append("and ");
				}
				sb.append("(model like ? or specification like ?)");
				params.add(kw.concat("%"));
				params.add(kw.concat("%"));
			}

			// find items
			List<Item> items = itDao.find(sb.toString(), params);

			// find stocks
			for (Item item : items) {

				StockVO vo = new StockVO();
				vo.setItem(item);
				vo.setStock(getStock(item.getId()));
				vo.setItemPrice(getItemPrice(item.getId()));
				vo.setMaker(getMaker(item.getMaker()));
				vo.setCategory(getCategory(item.getCategory()));
				if(vo.getStock() != null)
					stocks.add(vo);
			}

			return stocks;
		} catch (NumberFormatException e) {
			throw new ApplicationException(ErrorType.Warning, "You must enter digit on Max Count!");
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	private Category getCategory(int category) throws InstantiationException, IllegalAccessException {
		List<Category> list = catDao.find("id = ?", Arrays.asList(category));
		for (Category cat : list) {
			return cat;
		}
		return null;
	}

	private Maker getMaker(int maker) throws InstantiationException, IllegalAccessException {
		List<Maker> list = mkDao.find("id = ?", Arrays.asList(maker));
		for (Maker mk : list) {
			return mk;
		}
		return null;
	}

	private ItemPrice getItemPrice(int id) throws InstantiationException, IllegalAccessException {
		List<ItemPrice> list = priceDao.find("item_id = ? and status = ? order by ref_date desc",
				Arrays.asList(id, PriceStatus.Confirmed));
		
		for (ItemPrice itemPrice : list) {
			return itemPrice;
		}
		return null;
	}

	private Stock getStock(int id) throws InstantiationException, IllegalAccessException {
		List<Stock> list = stDao.find("item_id = ?", Arrays.asList(id));
		for (Stock item : list) {
			return item;
		}
		return null;
	}

	@Override
	public Set<Item> getItemsByCount(int count) {
		try {

			List<Stock> stocks = stDao.find("count <= ?", Arrays.asList(count));
			if (stocks.size() > 0) {
				StringBuilder sb = new StringBuilder("id in (");
				List<Object> params = new ArrayList<>();
				for (int i = 0; i < stocks.size(); i++) {

					if (i > 0) {
						sb.append(", ");
					}

					sb.append("?");
					params.add(stocks.get(i).getItemId());
				}

				return new HashSet<>(itDao.find(sb.toString(), params));
			}
			return null;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

}