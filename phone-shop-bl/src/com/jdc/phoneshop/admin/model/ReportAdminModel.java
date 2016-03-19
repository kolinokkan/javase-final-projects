package com.jdc.phoneshop.admin.model;

import java.time.LocalDate;
import java.util.Set;

import com.jdc.phoneshop.admin.model.imp.ReportAdminModelImp;
import com.jdc.phoneshop.admin.model.vo.ReportVO;
import com.jdc.phoneshop.admin.model.vo.ReportVO.Type;

public interface ReportAdminModel {

    Set<ReportVO> getReport(LocalDate dtFrom, LocalDate dtTo, Type type);
    
    public static ReportAdminModel getModel() {
    	return new ReportAdminModelImp();
    }

}