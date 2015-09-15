package com.guanhuodata.photo.service.impl;

import java.io.File;
import com.guanhuodata.photo.dao.IMaterialDAO;
import com.guanhuodata.photo.service.IMaterialService;

public class IMaterialServiceImpl implements IMaterialService{

	private IMaterialDAO materialDAO;
	
	@Override
	public boolean readMaterialExcelInsertToDB(File file) {
		// TODO Auto-generated method stub
		return materialDAO.readMaterialExcelInsertToDB(file);
	}
	public IMaterialDAO getMaterialDAO() {
		return materialDAO;
	}
	public void setMaterialDAO(IMaterialDAO materialDAO) {
		this.materialDAO = materialDAO;
	}
	
}
