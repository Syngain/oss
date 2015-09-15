package com.guanhuodata.excel.service.impl;

import com.guanhuodata.excel.dao.IChartDAO;
import com.guanhuodata.excel.service.IChartService;

public class IChartServiceImpl implements IChartService{

	private IChartDAO chartDAO;
	@Override
	public void readChart(String path) {
		// TODO Auto-generated method stub
		chartDAO.readChart(path);
	}
	public IChartDAO getChartDAO() {
		return chartDAO;
	}
	public void setChartDAO(IChartDAO chartDAO) {
		this.chartDAO = chartDAO;
	}
	
}
