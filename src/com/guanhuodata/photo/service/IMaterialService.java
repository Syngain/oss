package com.guanhuodata.photo.service;

import java.io.File;
import java.util.List;

import com.guanhuodata.photo.bean.InitConditions;
import com.guanhuodata.photo.bean.MaterialChartSplitBean;
import com.guanhuodata.photo.bean.QueryCondition;
import com.guanhuodata.photo.util.Page;

/**
 * 
 * @author fudk
 * 素材业务层接口
 *
 */
public interface IMaterialService {

	public boolean readMaterialExcelInsertToDB(File file);
	
	public List<MaterialChartSplitBean> getPagedMaterialInfos(Page page,QueryCondition queryCondition);

	public String getOriginalityNameById(long imageId);

	public String getQImageInfoByQImageId(long qImgId);

	public Page getPaginationInfo(Page page);

	public Page getPaginationInfoByCondition(Page page, QueryCondition queryCondition);

	public InitConditions getInitConditions();

	public List<MaterialChartSplitBean> findByName(Page page,String originalityName);

	public Page getPaginationInfoByName(Page page,String originalityName);
	
}
