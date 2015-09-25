package com.guanhuodata.photo.bean;

import java.io.Serializable;
import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

/**
 * 
 * @author fudk
 * 拆分报表Bean，用于存储报表Bean与查询操作
 *	带material：素材命名
 *	带schedule：计划命名
 *	带unit：单元命名
 */
public class MaterialChartSplitBean implements Serializable{

	private static final long serialVersionUID = 7771667938089972398L;

	@JsonTypeSpec(JsonType.NUMBER)
	private long id;	//记录id
	
	@JsonTypeSpec(JsonType.STRING)
	private String originalityName;	//创意名称
	
	@JsonTypeSpec(JsonType.STRING)
	private String spreadUnitBasicInfo;	//推广单元基本信息
	
	@JsonTypeSpec(JsonType.STRING)
	private String allowSpreadSchedule;	//所属推广计划
	
	@JsonTypeSpec(JsonType.STRING)
	private String dateTime;		//时间
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long reveal;	//展现
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long click;	//点击
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double CTR;	//CTR(Click-through Rate)点击率%
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double consume;	//消耗
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double showCostOf1000;	//千次展现成本(元)
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double unitPriceOfClick;	//点击单价(元)
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double rateOfReturn_3;	//3天回报率
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double rateOfReturn_7;	//7天回报率
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double rateOfReturn_15;	//15天回报率
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long customerOrderNum_3;	//3天顾客订单数
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long customerOrderNum_7;	//7天顾客订单数
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long customerOrderNum_15;	//15天顾客订单数
	
	@JsonTypeSpec(JsonType.NUMBER)
	private int shopCollectNum;	//店铺收藏数
	
	@JsonTypeSpec(JsonType.NUMBER)
	private int goodsCollectNum;	//宝贝收藏数
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long visitor;	//访客
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long touchOfUser;	//触达用户
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double touchOfFrequency;	//触达频次
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long collectUser;	//收藏用户
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long showVisitor_3;	//3天展示访客
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long showVisitor_7;	//7天展示访客
	
	@JsonTypeSpec(JsonType.NUMBER)
	private long showVisitor_15;	//15天展示访客
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double showRateOfReturn_3;	//3天展示回报率
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double showRateOfReturn_7;	//7天展示回报率
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double showRateOfReturn_15;	//15天展示回报率
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double orderSum_7;	//7天订单金额
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double orderSum_15;	//15天订单金额
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double clickSum_15;	//15天点击金额
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double showSum_15;	//15天展示金额
	
	@JsonTypeSpec(JsonType.STRING)
	private String materialCrowd;	//素材人群
	
	@JsonTypeSpec(JsonType.STRING)
	private String materialTheme;	//主题
	
	@JsonTypeSpec(JsonType.STRING)
	private String materialContinuePage;	//承接页
	
	@JsonTypeSpec(JsonType.STRING)
	private String materialStandAbbreviation;	//展位简称
	
	@JsonTypeSpec(JsonType.STRING)
	private String materialDesignAbbreviationAndSerialNumber;	//设计简称
	
	@JsonTypeSpec(JsonType.STRING)
	private String scheduleActivityTheme;	//活动主题
	
	@JsonTypeSpec(JsonType.STRING)
	private String schedulePutInPurpose;	//投放目的
	
	@JsonTypeSpec(JsonType.STRING)
	private String scheduleCrowdLayer;	//人群分层
	
	@JsonTypeSpec(JsonType.STRING)
	private String scheduleStandCategory;	//展位类别
	
	@JsonTypeSpec(JsonType.STRING)
	private String unitCrowd;	//单元人群
	
	@JsonTypeSpec(JsonType.STRING)
	private String unitSpecificStand;	//具体展位
	
	@JsonTypeSpec(JsonType.STRING)
	private String shopName;	//店铺名称
	
	@JsonTypeSpec(JsonType.STRING)
	private String operateTime;	//操作时间
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOriginalityName() {
		return originalityName;
	}

	public void setOriginalityName(String originalityName) {
		this.originalityName = originalityName;
	}

	public String getSpreadUnitBasicInfo() {
		return spreadUnitBasicInfo;
	}

	public void setSpreadUnitBasicInfo(String spreadUnitBasicInfo) {
		this.spreadUnitBasicInfo = spreadUnitBasicInfo;
	}

	public String getAllowSpreadSchedule() {
		return allowSpreadSchedule;
	}

	public void setAllowSpreadSchedule(String allowSpreadSchedule) {
		this.allowSpreadSchedule = allowSpreadSchedule;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public long getReveal() {
		return reveal;
	}

	public void setReveal(long reveal) {
		this.reveal = reveal;
	}

	public long getClick() {
		return click;
	}

	public void setClick(long click) {
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

	public double getClickSum_15() {
		return clickSum_15;
	}

	public void setClickSum_15(double clickSum_15) {
		this.clickSum_15 = clickSum_15;
	}

	public double getShowSum_15() {
		return showSum_15;
	}

	public void setShowSum_15(double showSum_15) {
		this.showSum_15 = showSum_15;
	}

	public String getMaterialCrowd() {
		return materialCrowd;
	}

	public void setMaterialCrowd(String materialCrowd) {
		this.materialCrowd = materialCrowd;
	}

	public String getMaterialTheme() {
		return materialTheme;
	}

	public void setMaterialTheme(String materialTheme) {
		this.materialTheme = materialTheme;
	}

	public String getMaterialContinuePage() {
		return materialContinuePage;
	}

	public void setMaterialContinuePage(String materialContinuePage) {
		this.materialContinuePage = materialContinuePage;
	}

	public String getMaterialStandAbbreviation() {
		return materialStandAbbreviation;
	}

	public void setMaterialStandAbbreviation(String materialStandAbbreviation) {
		this.materialStandAbbreviation = materialStandAbbreviation;
	}

	public String getMaterialDesignAbbreviationAndSerialNumber() {
		return materialDesignAbbreviationAndSerialNumber;
	}

	public void setMaterialDesignAbbreviationAndSerialNumber(String materialDesignAbbreviationAndSerialNumber) {
		this.materialDesignAbbreviationAndSerialNumber = materialDesignAbbreviationAndSerialNumber;
	}

	public String getScheduleActivityTheme() {
		return scheduleActivityTheme;
	}

	public void setScheduleActivityTheme(String scheduleActivityTheme) {
		this.scheduleActivityTheme = scheduleActivityTheme;
	}

	public String getSchedulePutInPurpose() {
		return schedulePutInPurpose;
	}

	public void setSchedulePutInPurpose(String schedulePutInPurpose) {
		this.schedulePutInPurpose = schedulePutInPurpose;
	}

	public String getScheduleCrowdLayer() {
		return scheduleCrowdLayer;
	}

	public void setScheduleCrowdLayer(String scheduleCrowdLayer) {
		this.scheduleCrowdLayer = scheduleCrowdLayer;
	}

	public String getScheduleStandCategory() {
		return scheduleStandCategory;
	}

	public void setScheduleStandCategory(String scheduleStandCategory) {
		this.scheduleStandCategory = scheduleStandCategory;
	}

	public String getUnitCrowd() {
		return unitCrowd;
	}

	public void setUnitCrowd(String unitCrowd) {
		this.unitCrowd = unitCrowd;
	}

	public String getUnitSpecificStand() {
		return unitSpecificStand;
	}

	public void setUnitSpecificStand(String unitSpecificStand) {
		this.unitSpecificStand = unitSpecificStand;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	
}
