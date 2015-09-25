package com.guanhuodata.photo.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.guanhuodata.framework.util.JsonUtil;
import com.guanhuodata.photo.bean.FileBean;
import com.guanhuodata.photo.bean.MaterialChartSplitBean;

public class ActionUtils {

	private static final Logger LOG = Logger.getLogger(ActionUtils.class);
	
	public String getImgPathsByListBean(List<MaterialChartSplitBean> list){
		LOG.info("ActionUtils getImgPathsByListBean invoke.");
		List<FileBean> fileBeanList = new ArrayList<FileBean>();
		for(MaterialChartSplitBean bean : list){
			FileBean fileBean = new FileBean();
			fileBean.setId(bean.getId());
			fileBean.setFileName(bean.getOriginalityName());
			fileBeanList.add(fileBean);
		}
		String ret = JsonUtil.makeListJson(fileBeanList);
		return ret;
	}
}
