package com.guanhuodata.excel.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author 定向报表
 *
 */
public class DirectSheetBean implements Serializable {

	private static final long serialVersionUID = -6501383600165498576L;

	private long id;	//记录ID
	private String directoryName;	//定向名称
	private String unitName;	//单元名称
	private	String spreadSchedule;	//所属推广计划源数据
	private String paltform;	//平台
	private String putInPurpose;	//投放目的
	private String crowdInfo;	//人群信息
	private String userOrigin;	//用户来源
	private String crowdLayer;	//人群分层
	private String crowdAttr;	//人群属性
	private String standInfo;	//展位信息
	private String allowdSpreadTheme;	//所属推广主题
	private String allowSpreadSchedule;	//所属推广计划
	private String timeInterval;	//时段
	private String isTest;	//是否测试
	private String allowCategory;	//所属类目
	private Date  dateTimes;	//时间
	private double reveal;	//展现
	private double click;	//点击
	private double CTR;	//点击率
	private double consume;	//消耗
	private double showCostOf1000;	//千次展现成本(元)
	private double unitPriceOfClick;	//点击单价(元)
	private double rateOfReturn_3;	//3天回报率
	private double rateOfReturn_7;	//7天回报率
	private double rateOfReturn_15;	//15天回报率
	private double outputOfClick_3;	//3天点击产出
	private double outputOfClick_7;	//7天点击产出
	private double outputOfClick_15;	//15天点击产出
	private long customerOrderNum_3;	//3天顾客订单数
	private long customerOrderNum_7;	//7天顾客订单数
	private long customerOrderNum_15;	//15天顾客订单数
	private int shopCollectNum;	//店铺收藏数
	private int goodsCollectNum;	//宝贝收藏数
	private long visitor;	//访客
	private double getUserCost;	//访客获取成本
	private long touchOfUser;	//触达用户
	private double touchOfFrequency;	//触达频次
	private long collectUser;	//收藏用户
	private long showVisitor_3;	//3天展示访客
	private long showVisitor_7;	//7天展示访客
	private long showVisitor_15;	//15天展示访客
	private double showRateOfReturn_3;	//3天展示回报率
	private double showRateOfReturn_7;	//7天展示回报率
	private double showRateOfReturn_15;	//15天展示回报率
	private double outputOfReveal_3;	//3天展示产出
	private double outputOfReveal_7;	//7天展示产出
	private double outputOfReveal_15;	//15天展示产出
	private double orderSum_7;	//7天订单金额
	private double orderSum_15;	//15天订单金额
	private double orderPercentConversion_15;	//15天下单转化率
	private Date timestamps;	//记录当前时间
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDirectoryName() {
		return directoryName;
	}
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getSpreadSchedule() {
		return spreadSchedule;
	}
	public void setSpreadSchedule(String spreadSchedule) {
		this.spreadSchedule = spreadSchedule;
	}
	public String getPaltform() {
		return paltform;
	}
	public void setPaltform(String paltform) {
		this.paltform = paltform;
	}
	public String getPutInPurpose() {
		return putInPurpose;
	}
	public void setPutInPurpose(String putInPurpose) {
		this.putInPurpose = putInPurpose;
	}
	public String getCrowdInfo() {
		return crowdInfo;
	}
	public void setCrowdInfo(String crowdInfo) {
		this.crowdInfo = crowdInfo;
	}
	public String getUserOrigin() {
		return userOrigin;
	}
	public void setUserOrigin(String userOrigin) {
		this.userOrigin = userOrigin;
	}
	public String getCrowdLayer() {
		return crowdLayer;
	}
	public void setCrowdLayer(String crowdLayer) {
		this.crowdLayer = crowdLayer;
	}
	public String getCrowdAttr() {
		return crowdAttr;
	}
	public void setCrowdAttr(String crowdAttr) {
		this.crowdAttr = crowdAttr;
	}
	public String getStandInfo() {
		return standInfo;
	}
	public void setStandInfo(String standInfo) {
		this.standInfo = standInfo;
	}
	public String getAllowdSpreadTheme() {
		return allowdSpreadTheme;
	}
	public void setAllowdSpreadTheme(String allowdSpreadTheme) {
		this.allowdSpreadTheme = allowdSpreadTheme;
	}
	public String getAllowSpreadSchedule() {
		return allowSpreadSchedule;
	}
	public void setAllowSpreadSchedule(String allowSpreadSchedule) {
		this.allowSpreadSchedule = allowSpreadSchedule;
	}
	public String getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}
	public String getIsTest() {
		return isTest;
	}
	public void setIsTest(String isTest) {
		this.isTest = isTest;
	}
	public String getAllowCategory() {
		return allowCategory;
	}
	public void setAllowCategory(String allowCategory) {
		this.allowCategory = allowCategory;
	}
	public Date getDateTimes() {
		return dateTimes;
	}
	public void setDateTimes(Date dateTimes) {
		this.dateTimes = dateTimes;
	}
	public double getReveal() {
		return reveal;
	}
	public void setReveal(double reveal) {
		this.reveal = reveal;
	}
	public double getClick() {
		return click;
	}
	public void setClick(double click) {
		this.click = click;
	}
	public double getCTR() {
		return CTR;
	}
	public void setCTR(double cTR) {
		CTR = cTR;
	}
	public double getConsume() {
		return consume;
	}
	public void setConsume(double consume) {
		this.consume = consume;
	}
	public double getShowCostOf1000() {
		return showCostOf1000;
	}
	public void setShowCostOf1000(double showCostOf1000) {
		this.showCostOf1000 = showCostOf1000;
	}
	public double getUnitPriceOfClick() {
		return unitPriceOfClick;
	}
	public void setUnitPriceOfClick(double unitPriceOfClick) {
		this.unitPriceOfClick = unitPriceOfClick;
	}
	public double getRateOfReturn_3() {
		return rateOfReturn_3;
	}
	public void setRateOfReturn_3(double rateOfReturn_3) {
		this.rateOfReturn_3 = rateOfReturn_3;
	}
	public double getRateOfReturn_7() {
		return rateOfReturn_7;
	}
	public void setRateOfReturn_7(double rateOfReturn_7) {
		this.rateOfReturn_7 = rateOfReturn_7;
	}
	public double getRateOfReturn_15() {
		return rateOfReturn_15;
	}
	public void setRateOfReturn_15(double rateOfReturn_15) {
		this.rateOfReturn_15 = rateOfReturn_15;
	}
	public double getOutputOfClick_3() {
		return outputOfClick_3;
	}
	public void setOutputOfClick_3(double outputOfClick_3) {
		this.outputOfClick_3 = outputOfClick_3;
	}
	public double getOutputOfClick_7() {
		return outputOfClick_7;
	}
	public void setOutputOfClick_7(double outputOfClick_7) {
		this.outputOfClick_7 = outputOfClick_7;
	}
	public double getOutputOfClick_15() {
		return outputOfClick_15;
	}
	public void setOutputOfClick_15(double outputOfClick_15) {
		this.outputOfClick_15 = outputOfClick_15;
	}
	public long getCustomerOrderNum_3() {
		return customerOrderNum_3;
	}
	public void setCustomerOrderNum_3(long customerOrderNum_3) {
		this.customerOrderNum_3 = customerOrderNum_3;
	}
	public long getCustomerOrderNum_7() {
		return customerOrderNum_7;
	}
	public void setCustomerOrderNum_7(long customerOrderNum_7) {
		this.customerOrderNum_7 = customerOrderNum_7;
	}
	public long getCustomerOrderNum_15() {
		return customerOrderNum_15;
	}
	public void setCustomerOrderNum_15(long customerOrderNum_15) {
		this.customerOrderNum_15 = customerOrderNum_15;
	}
	public int getShopCollectNum() {
		return shopCollectNum;
	}
	public void setShopCollectNum(int shopCollectNum) {
		this.shopCollectNum = shopCollectNum;
	}
	public int getGoodsCollectNum() {
		return goodsCollectNum;
	}
	public void setGoodsCollectNum(int goodsCollectNum) {
		this.goodsCollectNum = goodsCollectNum;
	}
	public long getVisitor() {
		return visitor;
	}
	public void setVisitor(long visitor) {
		this.visitor = visitor;
	}
	public double getGetUserCost() {
		return getUserCost;
	}
	public void setGetUserCost(double getUserCost) {
		this.getUserCost = getUserCost;
	}
	public long getTouchOfUser() {
		return touchOfUser;
	}
	public void setTouchOfUser(long touchOfUser) {
		this.touchOfUser = touchOfUser;
	}
	public double getTouchOfFrequency() {
		return touchOfFrequency;
	}
	public void setTouchOfFrequency(double touchOfFrequency) {
		this.touchOfFrequency = touchOfFrequency;
	}
	public long getCollectUser() {
		return collectUser;
	}
	public void setCollectUser(long collectUser) {
		this.collectUser = collectUser;
	}
	public long getShowVisitor_3() {
		return showVisitor_3;
	}
	public void setShowVisitor_3(long showVisitor_3) {
		this.showVisitor_3 = showVisitor_3;
	}
	public long getShowVisitor_7() {
		return showVisitor_7;
	}
	public void setShowVisitor_7(long showVisitor_7) {
		this.showVisitor_7 = showVisitor_7;
	}
	public long getShowVisitor_15() {
		return showVisitor_15;
	}
	public void setShowVisitor_15(long showVisitor_15) {
		this.showVisitor_15 = showVisitor_15;
	}
	public double getShowRateOfReturn_3() {
		return showRateOfReturn_3;
	}
	public void setShowRateOfReturn_3(double showRateOfReturn_3) {
		this.showRateOfReturn_3 = showRateOfReturn_3;
	}
	public double getShowRateOfReturn_7() {
		return showRateOfReturn_7;
	}
	public void setShowRateOfReturn_7(double showRateOfReturn_7) {
		this.showRateOfReturn_7 = showRateOfReturn_7;
	}
	public double getShowRateOfReturn_15() {
		return showRateOfReturn_15;
	}
	public void setShowRateOfReturn_15(double showRateOfReturn_15) {
		this.showRateOfReturn_15 = showRateOfReturn_15;
	}
	public double getOutputOfReveal_3() {
		return outputOfReveal_3;
	}
	public void setOutputOfReveal_3(double outputOfReveal_3) {
		this.outputOfReveal_3 = outputOfReveal_3;
	}
	public double getOutputOfReveal_7() {
		return outputOfReveal_7;
	}
	public void setOutputOfReveal_7(double outputOfReveal_7) {
		this.outputOfReveal_7 = outputOfReveal_7;
	}
	public double getOutputOfReveal_15() {
		return outputOfReveal_15;
	}
	public void setOutputOfReveal_15(double outputOfReveal_15) {
		this.outputOfReveal_15 = outputOfReveal_15;
	}
	public double getOrderSum_7() {
		return orderSum_7;
	}
	public void setOrderSum_7(double orderSum_7) {
		this.orderSum_7 = orderSum_7;
	}
	public double getOrderSum_15() {
		return orderSum_15;
	}
	public void setOrderSum_15(double orderSum_15) {
		this.orderSum_15 = orderSum_15;
	}
	public double getOrderPercentConversion_15() {
		return orderPercentConversion_15;
	}
	public void setOrderPercentConversion_15(double orderPercentConversion_15) {
		this.orderPercentConversion_15 = orderPercentConversion_15;
	}
	public Date getTimestamps() {
		return timestamps;
	}
	public void setTimestamps(Date timestamps) {
		this.timestamps = timestamps;
	}
}
