package com.guanhuodata.excel.bean;

import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

/**
 * 
 * @author fudk
 *	Excel报表Bean
 */
public class ExcelBean implements java.io.Serializable{

	private static final long serialVersionUID = 3637682993719338714L;
	
	@JsonTypeSpec(JsonType.STRING)
	private String directName;	//定向名称
	@JsonTypeSpec(JsonType.STRING)
	private String allowSpreadSchedule;	//所属推广计划
	@JsonTypeSpec(JsonType.STRING)
	private String userAttribute;	//用户属性
	@JsonTypeSpec(JsonType.STRING)
	private String crowdInfo;	//人群信息
	@JsonTypeSpec(JsonType.STRING)
	private String paltform;	//平台
	@JsonTypeSpec(JsonType.STRING)
	private String standInfo;	//展位信息
	@JsonTypeSpec(JsonType.STRING)
	private String allowSpreadTheme;
	@JsonTypeSpec(JsonType.NUMBER)
	private long show;	//展现
	@JsonTypeSpec(JsonType.NUMBER)
	private long click;	//点击
	@JsonTypeSpec(JsonType.NUMBER)
	private double consume;	//消耗
	@JsonTypeSpec(JsonType.NUMBER)
	private long clickVisitor;	//点击访客
	@JsonTypeSpec(JsonType.NUMBER)
	private long showVisitor_15;	//15天展示访客
	@JsonTypeSpec(JsonType.NUMBER)
	private double CTR;	//CTR(Click-through Rate)点击率%
	@JsonTypeSpec(JsonType.NUMBER)
	private double showCostOf1000;	//千次展现成本(元)
	@JsonTypeSpec(JsonType.NUMBER)
	private double unitPriceOfClick;	//点击单价(元)
	@JsonTypeSpec(JsonType.NUMBER)
	private double clickRateOfReturn_15;	//15天点击回报率
	@JsonTypeSpec(JsonType.NUMBER)
	private double showRateOfReturn_15;	//15天展示回报率
	@JsonTypeSpec(JsonType.NUMBER)
	private double clickOutput_15;	//15天点击产出
	@JsonTypeSpec(JsonType.NUMBER)
	private double showOutput_15;	//15天展示产出
	@JsonTypeSpec(JsonType.NUMBER)
	private int customerOrderNum_15;//15天顾客订单数
	@JsonTypeSpec(JsonType.NUMBER)
	private int shopCollectNum;	//店铺收藏数
	@JsonTypeSpec(JsonType.NUMBER)
	private int goodsCollectNum;	//宝贝收藏数
	@JsonTypeSpec(JsonType.NUMBER)
	private double orderSum_15;	//15天订单金额
	
	public String getDirectName() {
		return directName;
	}
	public void setDirectName(String directName) {
		this.directName = directName;
	}
	public String getAllowSpreadSchedule() {
		return allowSpreadSchedule;
	}
	public void setAllowSpreadSchedule(String allowSpreadSchedule) {
		this.allowSpreadSchedule = allowSpreadSchedule;
	}
	public String getUserAttribute() {
		return userAttribute;
	}
	public void setUserAttribute(String userAttribute) {
		this.userAttribute = userAttribute;
	}
	public String getCrowdInfo() {
		return crowdInfo;
	}
	public void setCrowdInfo(String crowdInfo) {
		this.crowdInfo = crowdInfo;
	}
	public String getPaltform() {
		return paltform;
	}
	public void setPaltform(String paltform) {
		this.paltform = paltform;
	}
	public String getStandInfo() {
		return standInfo;
	}
	public void setStandInfo(String standInfo) {
		this.standInfo = standInfo;
	}
	public String getAllowSpreadTheme() {
		return allowSpreadTheme;
	}
	public void setAllowSpreadTheme(String allowSpreadTheme) {
		this.allowSpreadTheme = allowSpreadTheme;
	}
	public long getShow() {
		return show;
	}
	public void setShow(long show) {
		this.show = show;
	}
	public long getClick() {
		return click;
	}
	public void setClick(long click) {
		this.click = click;
	}
	public double getConsume() {
		return consume;
	}
	public void setConsume(double consume) {
		this.consume = consume;
	}
	public long getClickVisitor() {
		return clickVisitor;
	}
	public void setClickVisitor(long clickVisitor) {
		this.clickVisitor = clickVisitor;
	}
	public long getShowVisitor_15() {
		return showVisitor_15;
	}
	public void setShowVisitor_15(long showVisitor_15) {
		this.showVisitor_15 = showVisitor_15;
	}
	public double getCTR() {
		return CTR;
	}
	public void setCTR(double cTR) {
		CTR = cTR;
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
	public double getClickRateOfReturn_15() {
		return clickRateOfReturn_15;
	}
	public void setClickRateOfReturn_15(double clickRateOfReturn_15) {
		this.clickRateOfReturn_15 = clickRateOfReturn_15;
	}
	public double getShowRateOfReturn_15() {
		return showRateOfReturn_15;
	}
	public void setShowRateOfReturn_15(double showRateOfReturn_15) {
		this.showRateOfReturn_15 = showRateOfReturn_15;
	}
	public double getClickOutput_15() {
		return clickOutput_15;
	}
	public void setClickOutput_15(double clickOutput_15) {
		this.clickOutput_15 = clickOutput_15;
	}
	public double getShowOutput_15() {
		return showOutput_15;
	}
	public void setShowOutput_15(double showOutput_15) {
		this.showOutput_15 = showOutput_15;
	}
	public int getCustomerOrderNum_15() {
		return customerOrderNum_15;
	}
	public void setCustomerOrderNum_15(int customerOrderNum_15) {
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
	public double getOrderSum_15() {
		return orderSum_15;
	}
	public void setOrderSum_15(double orderSum_15) {
		this.orderSum_15 = orderSum_15;
	}
	
}
