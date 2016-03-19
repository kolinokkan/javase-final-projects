package com.jdc.phoneshop.pos.model.imp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.pos.model.BillModel;
import com.jdc.phoneshop.pos.model.entity.Bill;
import com.jdc.phoneshop.pos.model.entity.BillItem;
import com.jdc.phoneshop.pos.model.entity.Customer;
import com.jdc.phoneshop.utils.ApplicationException;

public class BillModelImp implements BillModel {

    private Dao<Bill> billDao;
    private Dao<BillItem> billItemDao;

    public BillModelImp() {
		billDao = DaoFactory.generate(Bill.class);
		billItemDao = DaoFactory.generate(BillItem.class);
	}

	@Override
	public Bill search(int id) {
		
		try {
			List<Bill> list = billDao.find("id = ?", Arrays.asList(id));
			
			if(list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
		
		return null;
	}

	@Override
	public Set<Bill> search(LocalDate refDate, Customer cust) {
		try {
			
			StringBuilder sb = new StringBuilder();
			List<Object> params = new ArrayList<>();
			
			if(null != cust) {
				sb.append("customer_id = ? ");
				params.add(cust.getId());
			}
			
			if(null != refDate) {
				if(params.size() > 0) {
					sb.append("and ");
				}
				
				sb.append("ref_date = ?");
				params.add(refDate.atStartOfDay());
			}
			
			List<Bill> items = billDao.find(sb.toString(), params);
			return new HashSet<>(items);

		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public Set<BillItem> searchItems(Bill bill) {
		try {
			
			List<BillItem> items = billItemDao.find("bill_id = ?", Arrays.asList(bill.getId()));
			return new HashSet<>(items);
			
		} catch (Exception e) {
			throw new ApplicationException(e);
		}
	}

	@Override
	public double getTaxRate() {
		return 0.05;
	}
}