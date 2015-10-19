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
			fileBean.setFileName(bean.getOriginalityName());//创意名称
			fileBean.setMaterialTheme(bean.getMaterialTheme());//活动
			fileBean.setMaterialStandAbbreviation(MaterialChartUtil.regexpStandSize(bean.getMaterialStandAbbreviation()));//展位
			fileBean.setCTR(bean.getCTR());//点击率
			fileBean.setClick(bean.getClick());//点击数
			fileBean.setReveal(bean.getReveal());//展现
			fileBean.setShowRateOfReturn_15(bean.getShowRateOfReturn_15());//展示ROI
			fileBean.setConsume(bean.getConsume());//消耗
			//fileBean.setcj(bean.getcj);//成交
			fileBean.setDateTime(bean.getDateTimes());//投放时间
			fileBean.setMaterialCrowd(bean.getMaterialCrowd());//投放人群
			fileBean.setMaterialContinuePage(bean.getMaterialContinuePage());//承接页
			fileBean.setShopName(bean.getShopName());//店铺名称
			fileBeanList.add(fileBean);
		}
		String ret = JsonUtil.makeListJson(fileBeanList);
		return ret;
	}
}
