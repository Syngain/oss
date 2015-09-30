package com.guanhuodata.photo.service.impl;

import java.io.File;
import java.util.List;

import com.guanhuodata.photo.bean.InitConditions;
import com.guanhuodata.photo.bean.MaterialChartSplitBean;
import com.guanhuodata.photo.bean.QueryCondition;
import com.guanhuodata.photo.dao.IMaterialDAO;
import com.guanhuodata.photo.service.IMaterialService;
import com.guanhuodata.photo.util.Page;

public class IMaterialServiceImpl implements IMaterialService{

	private IMaterialDAO materialDAO;
	
	@Override
	public List<MaterialChartSplitBean> getListByCondition(QueryCondition queryCondition) {
		// TODO Auto-generated method stub
		return materialDAO.getListByCondition(queryCondition);
	}
	
	@Override
	public Page getPaginationInfoByName(Page page,String originalityName) {
		// TODO Auto-generated method stub
		return materialDAO.getPaginationInfoByName(page,originalityName);
	}
	
	@Override
	public List<MaterialChartSplitBean> findByName(Page page,String originalityName) {
		return materialDAO.findByName(page,originalityName);
	}
	
	@Override
	public InitConditions getInitConditions() {
		// TODO Auto-generated method stub
		return materialDAO.getInitConditions();
	}
	
	@Override
	public Page getPaginationInfoByCondition(Page page, QueryCondition queryCondition) {
		// TODO Auto-generated method stub
		return materialDAO.getPaginationInfoByCondition(page,queryCondition);
	}
	
	@Override
	public Page getPaginationInfo(Page page) {
		return materialDAO.getPaginationInfo(page);
	}
	
	@Override
	public String getOriginalityNameById(long imageId) {
		return materialDAO.getOriginalityNameById(imageId);
	}
	
	@Override
	public String getQImageInfoByQImageId(long qImgId) {
		return materialDAO.getQImageInfoByQImageId(qImgId);
	}
	
	@Override
	public List<MaterialChartSplitBean> getPagedMaterialInfos(Page page, QueryCondition queryCondition) {
		return materialDAO.getPagedMaterialInfos(page,queryCondition);
	}
	
	@Override
	public boolean readMaterialExcelInsertToDB(File file) {
		return materialDAO.readMaterialExcelInsertToDB(file);
	}
	
	public IMaterialDAO getMaterialDAO() {
		return materialDAO;
	}
	public void setMaterialDAO(IMaterialDAO materialDAO) {
		this.materialDAO = materialDAO;
	}

}
