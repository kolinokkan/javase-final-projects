package com.jdc.phoneshop.admin.model.imp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.jdc.phoneshop.admin.model.ReportAdminModel;
import com.jdc.phoneshop.admin.model.vo.ReportVO;
import com.jdc.phoneshop.admin.model.vo.ReportVO.Type;
import com.jdc.phoneshop.common.db.Dao;
import com.jdc.phoneshop.common.db.DaoFactory;
import com.jdc.phoneshop.pos.model.entity.Bill;
import com.jdc.phoneshop.pos.model.entity.BillItem;
import com.jdc.phoneshop.utils.ApplicationException;
import com.jdc.phoneshop.warehouse.model.entity.Purchase;
import com.jdc.phoneshop.warehouse.model.entity.PurchaseItem;

public class ReportAdminModelImp implements ReportAdminModel {
	
	private Dao<Purchase> pDao;
	private Dao<PurchaseItem> purDao;

	private Dao<Bill> bDao;
	private Dao<BillItem> bitDao;
	
    public ReportAdminModelImp() {
    	purDao = DaoFactory.generate(PurchaseItem.class);
    	bitDao = DaoFactory.generate(BillItem.class);
    	bDao = DaoFactory.generate(Bill.class);
    	pDao = DaoFactory.generate(Purchase.class);
    }

	@Override
	public Set<ReportVO> getReport(LocalDate dtFrom, LocalDate dtTo, Type type) {
		
		try {
			List<ReportVO> result = new ArrayList<>();
			
			if(null != dtFrom && null != dtTo) {
				if(null != type) {
					switch (type) {
					case SELL:
						result.addAll(getSellReport(dtFrom, dtTo));
						break;
					case PURCHASE:
						result.addAll(getBuyReport(dtFrom, dtTo));
						break;
					}
				} else {
					result.addAll(getSellReport(dtFrom, dtTo));
					result.addAll(getBuyReport(dtFrom, dtTo));
				}
			}
			
			Collections.sort(result);
			
			return new LinkedHashSet<>(result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(e);
		}
	}
	
	private List<ReportVO> getBuyReport(LocalDate dtFrom, LocalDate dtTo) throws Exception{
		
		List<ReportVO> result = new ArrayList<>();
		
		String where = "ref_date >= ? && ref_date <= ?";
		List<Object> params = Arrays.asList(dtFrom, dtTo);
		
		List<Purchase> list = pDao.find(where, params);
		
		for (Purchase purchase : list) {
			List<PurchaseItem> items = purDao.find("purchase_id = ?", Arrays.asList(purchase.getId()));

			for (PurchaseItem purchaseItem : items) {
				result.add(new ReportVO(purchaseItem, purchase.getRefDate()));
			}
		}
		
		return result;
	}

	private Set<ReportVO> getSellReport(LocalDate dtFrom, LocalDate dtTo) throws Exception{

		Set<ReportVO> result = new HashSet<>();
		
		String where = "creation >= ? && creation <= ?";
		List<Object> params = Arrays.asList(dtFrom.atStartOfDay(), dtTo.atTime(LocalTime.of(23, 59)));
		
		List<Bill> list = bDao.find(where, params);
		
		for (Bill bill : list) {
			List<BillItem> items = bitDao.find("bill_id = ?", Arrays.asList(bill.getId()));
			for (BillItem item : items) {
				result.add(new ReportVO(item, bill.getReferenceDate().toLocalDate()));
			}
		}

		return result;
	}
}