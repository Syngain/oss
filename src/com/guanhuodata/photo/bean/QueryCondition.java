package com.guanhuodata.photo.bean;

import java.io.Serializable;

/**
 * 
 * @author fudk 封装页面的查询条件
 *
 */
public class QueryCondition implements Serializable {

	private static final long serialVersionUID = 9097277198835408147L;

	// 店铺
	private String shopName;

	// 展位
	private String standSize;

	// 活动名称
	private String activityName;

	// 投放人群
	private String putInCrowd;

	// 投放时间
	private String putInDateTime;

	// 点击率
	private String CTR;
	
	//点击率排序方式
	private String CTROrder;
	
	// 展现
	private String reveal;
	
	// 展现排序方式
	private String revealOrder;
	
	// 消耗
	private String consume;
	
	// 消耗排序方式
	private String consumeOrder;
	
	// 展示ROI
	private String showROI;
	
	// 展示ROI排序方式
	private String showROIOrder;
	
	// 点出ROI
	private String clickOutROI;
	
	// 点出ROI
	private String clickOutROIOrder;
	
	// CPC
	private String CPC;
	
	// CPC
	private String CPCOrder;

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getStandSize() {
		return standSize;
	}

	public void setStandSize(String standSize) {
		this.standSize = standSize;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getPutInCrowd() {
		return putInCrowd;
	}

	public void setPutInCrowd(String putInCrowd) {
		this.putInCrowd = putInCrowd;
	}

	public String getPutInDateTime() {
		return putInDateTime;
	}

	public void setPutInDateTime(String putInDateTime) {
		this.putInDateTime = putInDateTime;
	}

	public String getCTR() {
		return CTR;
	}

	public void setCTR(String cTR) {
		CTR = cTR;
	}

	public String getCTROrder() {
		return CTROrder;
	}

	public void setCTROrder(String cTROrder) {
		CTROrder = cTROrder;
	}

	public String getReveal() {
		return reveal;
	}

	public void setReveal(String reveal) {
		this.reveal = reveal;
	}

	public String getRevealOrder() {
		return revealOrder;
	}

	public void setRevealOrder(String revealOrder) {
		this.revealOrder = revealOrder;
	}

	public String getConsume() {
		return consume;
	}

	public void setConsume(String consume) {
		this.consume = consume;
	}

	public String getConsumeOrder() {
		return consumeOrder;
	}

	public void setConsumeOrder(String consumeOrder) {
		this.consumeOrder = consumeOrder;
	}

	public String getShowROI() {
		return showROI;
	}

	public void setShowROI(String showROI) {
		this.showROI = showROI;
	}

	public String getShowROIOrder() {
		return showROIOrder;
	}

	public void setShowROIOrder(String showROIOrder) {
		this.showROIOrder = showROIOrder;
	}

	public String getClickOutROI() {
		return clickOutROI;
	}

	public void setClickOutROI(String clickOutROI) {
		this.clickOutROI = clickOutROI;
	}

	public String getClickOutROIOrder() {
		return clickOutROIOrder;
	}

	public void setClickOutROIOrder(String clickOutROIOrder) {
		this.clickOutROIOrder = clickOutROIOrder;
	}

	public String getCPC() {
		return CPC;
	}

	public void setCPC(String cPC) {
		CPC = cPC;
	}

	public String getCPCOrder() {
		return CPCOrder;
	}

	public void setCPCOrder(String cPCOrder) {
		CPCOrder = cPCOrder;
	}

}
