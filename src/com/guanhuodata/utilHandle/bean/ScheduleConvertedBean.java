package com.guanhuodata.utilHandle.bean;

import java.io.Serializable;

public class ScheduleConvertedBean implements Serializable{

	private static final long serialVersionUID = 5453563001858151699L;

	private String scheduleName;	//计划名称
	private String unitName;	//单元名称
	private String directoryWay;	//定向方式
	private String directoryName;	//定向名称
	private String putInResource;	//投放资源位
	private String budget;	//预算(元)
	private String premium;	//溢价(%)
	private String originalityName;	//创意名称
	private String putInDate;	//投放日期
	private String putInWay;	//投放方式
	private String putInRegion;	//投放地域
	private String putInTimeInterval;	//投放时段
	
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getDirectoryWay() {
		return directoryWay;
	}
	public void setDirectoryWay(String directoryWay) {
		this.directoryWay = directoryWay;
	}
	public String getDirectoryName() {
		return directoryName;
	}
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	public String getPutInResource() {
		return putInResource;
	}
	public void setPutInResource(String putInResource) {
		this.putInResource = putInResource;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getPremium() {
		return premium;
	}
	public void setPremium(String premium) {
		this.premium = premium;
	}
	public String getOriginalityName() {
		return originalityName;
	}
	public void setOriginalityName(String originalityName) {
		this.originalityName = originalityName;
	}
	public String getPutInDate() {
		return putInDate;
	}
	public void setPutInDate(String putInDate) {
		this.putInDate = putInDate;
	}
	public String getPutInWay() {
		return putInWay;
	}
	public void setPutInWay(String putInWay) {
		this.putInWay = putInWay;
	}
	public String getPutInRegion() {
		return putInRegion;
	}
	public void setPutInRegion(String putInRegion) {
		this.putInRegion = putInRegion;
	}
	public String getPutInTimeInterval() {
		return putInTimeInterval;
	}
	public void setPutInTimeInterval(String putInTimeInterval) {
		this.putInTimeInterval = putInTimeInterval;
	}
	
}
