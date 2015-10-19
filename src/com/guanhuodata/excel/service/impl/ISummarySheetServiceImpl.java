package com.guanhuodata.excel.service.impl;

import java.io.File;
import java.io.OutputStream;
import com.guanhuodata.excel.dao.ISummarySheetDAO;
import com.guanhuodata.excel.service.ISummarySheetService;

public class ISummarySheetServiceImpl implements ISummarySheetService{

	private ISummarySheetDAO summarySheetDAO;

	@Override
	public void summaryByCondition(String[] params, OutputStream os) {
		// TODO Auto-generated method stub
		summarySheetDAO.summaryByCondition(params,os);
	}
	
	@Override
	public boolean readAndInsertToDB(File filePath) {
		// TODO Auto-generated method stub
		return summarySheetDAO.readAndInsertToDB(filePath);
	}
	
	@Override
	public boolean isOriginalitySheetStyle(File filePath,String sheetType) {
		// TODO Auto-generated method stub
		return summarySheetDAO.isOriginalitySheetStyle(filePath,sheetType);
	}
	
	public ISummarySheetDAO getSummarySheetDAO() {
		return summarySheetDAO;
	}

	public void setSummarySheetDAO(ISummarySheetDAO summarySheetDAO) {
		this.summarySheetDAO = summarySheetDAO;
	}
	
}
