package com.jdc.phoneshop.pos.model.imp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jdc.phoneshop.common.db.ConnectionManager;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.pos.model.ItemModel;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.utils.ApplicationException.ErrorType;
import com.jdc.phoneshop.utils.StringUtils;
import com.jdc.phoneshop.warehouse.model.MasterDataModel;
import com.jdc.phoneshop.warehouse.model.dao.ItemDao;
import com.jdc.phoneshop.warehouse.model.entity.Category;
import com.jdc.phoneshop.warehouse.model.entity.Item;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice;
import com.jdc.phoneshop.warehouse.model.entity.ItemPrice.PriceStatus;
import com.jdc.phoneshop.warehouse.model.entity.Maker;

public class ItemModelImp implements ItemModel {

	private Dao<Item> dao;
	private Dao<ItemPrice> priceDao;

	public ItemModelImp() {
		dao = DaoFactory.generate(Item.class);
		priceDao = DaoFactory.generate(ItemPrice.class);
	}

	@Override
	public Set<Item> search(Category cat, Maker mk, String kw) {

		String where = null;
		List<Object> params = new ArrayList<>();

		StringBuilder sb = new StringBuilder();

		if (null != cat) {
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

		if (null != kw && !kw.isEmpty()) {
			if (params.size() > 0) {
				sb.append("and ");
			}

			sb.append("model like ? ");
			params.add(kw.concat("%"));
		}

		if (params.size() > 0) {
			where = sb.toString();
		}

		try {

			List<Item> list = dao.find(where, params);
			return new LinkedHashSet<>(list);

		} catch (Exception e) {
			throw new ApplicationException(e);
		}

	}

	@Override
	public Item search(int id) {

		try {
			List<Item> list = dao.find("id = ?", Arrays.asList(id));
			if (list.size() > 0) {
				return list.get(0);
			}
			return null;
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public int getSellPrice(Item item) {

		try {
			List<ItemPrice> list = priceDao.find("item_id = ? and status = ? order by ref_date desc",
					Arrays.asList(item.getId(), PriceStatus.Confirmed));
			if (list.size() > 0) {
				return list.get(0).getPrice();
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return 0;
	}

	@Override
	public void create(Item item) {
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);

			this.check(item);
			dao.create(item, conn);

			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();
				throw new ApplicationException(e);
			} catch (SQLException e1) {
				throw new ApplicationException(e1);
			}
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
		}

	}

	private void check(Item item) {
		if (item.getCategory() == 0) {
			throw new ApplicationException(ErrorType.Warning, "Please set category for item.");
		}

		if (item.getMaker() == 0) {
			throw new ApplicationException(ErrorType.Warning, "Please set maker for item.");
		}

		if (StringUtils.isEmptyString(item.getModel())) {
			throw new ApplicationException(ErrorType.Warning, "Please set model for item.");
		}
	}

	@Override
	public void create(List<Item> items) {
		Connection conn = null;

		try {

			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);

			for (Item item : items) {
				this.check(item);
				dao.create(item, conn);
			}

			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();
				throw new ApplicationException(e);
			} catch (SQLException e1) {
				throw new ApplicationException(e1);
			}
		} finally {
			if (null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new ApplicationException(e);
				}
			}
		}
	}

	@Override
	public int getSellPrice(Item item, int purchaseID) {
		try {

			if (null == item) {
				throw new ApplicationException(ErrorType.Error, "Item is required to find sell price!");
			}

			if (0 == purchaseID) {
				throw new ApplicationException(ErrorType.Error, "Reference Date is required to find sell price!");
			}

			String where = "item_id = ? and purchase_id = ?";
			List<Object> params = Arrays.asList(item.getId(), purchaseID);

			List<ItemPrice> result = priceDao.find(where, params);

			for (ItemPrice itemPrice : result) {
				return itemPrice.getPrice();
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		return 0;
	}

	@Override
	public Map<Maker, List<ItemVO>> findByCategory(int catId) {
		ItemDao itemDao = (ItemDao) this.dao;
		Map<Maker, List<ItemVO>> result = new HashMap<>();
		try {

			// maker
			Set<Maker> makers = MasterDataModel.getModel(Maker.class).getAll();

			for (Maker maker : makers) {

				List<ItemVO> list = new ArrayList<>();

				// stock by category
				List<Item> items = itemDao.search(catId, maker.getId());

				// price
				for (Item item : items) {
					ItemVO vo = new ItemVO();
					vo.setItem(item);
					vo.setPrice(getSellPrice(item));
					list.add(vo);
				}
				
				if(list.size() > 0) {
					result.put(maker, list);
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}

		return result;
	}

}