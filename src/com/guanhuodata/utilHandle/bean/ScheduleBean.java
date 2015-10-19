package com.guanhuodata.utilHandle.bean;

import java.io.Serializable;

/**
 * 
 * @author fudk
 * 计划报表Bean
 *
 */
public class ScheduleBean implements Serializable{

	private static final long serialVersionUID = 6233092282901317887L;

	private String putInPurpose;	//投放目的
	private String scheduleName;	//计划名称
	private String allowCategory;	//所属类目
	private String directoryName;	//定向名称
	private String standAbbreviation;	//展位简称-运营人员用（不同展位需要单独建单元，导出对应的投放资源位）
	private String unitName;	//单元名称（所属类目_定向名称_展位）合成
	private String directoryWay;	//定向方式
	private String budget;	//预算(元)
	private String premium;	//溢价(%)
	private String crowdClass;	//人群类
	//private String originalityName;	//创意名称（检索创意名称中展位简称与资源位的对应关系,符合对应框内单元的创意都添加）
	private String putInDate;	//投放日期
	private String putInWay;	//投放方式
	private String putInRegion;	//投放地域
	private String putInTimeInterval;	//投放时段
	
	public String getPutInPurpose() {
		return putInPurpose;
	}
	public void setPutInPurpose(String putInPurpose) {
		this.putInPurpose = putInPurpose;
	}
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public String getAllowCategory() {
		return allowCategory;
	}
	public void setAllowCategory(String allowCategory) {
		this.allowCategory = allowCategory;
	}
	public String getDirectoryName() {
		return directoryName;
	}
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	public String getStandAbbreviation() {
		return standAbbreviation;
	}
	public void setStandAbbreviation(String standAbbreviation) {
		this.standAbbreviation = standAbbreviation;
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
	public String getCrowdClass() {
		return crowdClass;
	}
	public void setCrowdClass(String crowdClass) {
		this.crowdClass = crowdClass;
	}
	/*public String getOriginalityName() {
		return originalityName;
	}
	public void setOriginalityName(String originalityName) {
		this.originalityName = originalityName;
	}*/
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
