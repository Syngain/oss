package com.guanhuodata.photo.dao.impl;

import java.io.File;
import java.util.List;
import com.guanhuodata.photo.bean.MaterialChartBean;
import com.guanhuodata.photo.dao.IMaterialDAO;
import com.guanhuodata.photo.util.MaterialChartUtil;

/**
 * 
 * @author fudk
 * 素材数据访问层
 *
 */
public class IMaterialDAOImpl implements IMaterialDAO{

	@Override
	public boolean readMaterialExcelInsertToDB(File file) {
		
		boolean flag = false;
		List<MaterialChartBean> list = MaterialChartUtil.readMaterialExcel(file);
		if(list.size() > 0){
			flag = true;
		}
		return flag;
	}
 
}
