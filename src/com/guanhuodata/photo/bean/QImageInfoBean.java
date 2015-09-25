package com.guanhuodata.photo.bean;

import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

/**
 * 
 * @author fudk
 *	//图片提示信息Bean
 */
public class QImageInfoBean implements java.io.Serializable{

	private static final long serialVersionUID = 2241922944007750526L;

	@JsonTypeSpec(JsonType.STRING)
	private String shop;	//店铺
	
	@JsonTypeSpec(JsonType.STRING)
	private String title;	//日常活动
	
	@JsonTypeSpec(JsonType.STRING)
	private String imageSize;	//素材图片尺寸
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double CTR;	//CTR(Click-through Rate)点击率%
	
	@JsonTypeSpec(JsonType.NUMBER)
	private double ROI;	//CTR(Click-through Rate)ROI%
	
	@JsonTypeSpec(JsonType.STRING)
	private String putInTime;	//投放时间
	
	@JsonTypeSpec(JsonType.STRING)
	private String putInCrowd;	//投放人群
	
	@JsonTypeSpec(JsonType.STRING)
	private String linkAddress;	//链接页面
	
	@JsonTypeSpec(JsonType.STRING)
	private String originalityName;	//创意名称

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public double getCTR() {
		return CTR;
	}

	public void setCTR(double cTR) {
		CTR = cTR;
	}

	public double getROI() {
		return ROI;
	}

	public void setROI(double rOI) {
		ROI = rOI;
	}

	public String getPutInTime() {
		return putInTime;
	}

	public void setPutInTime(String putInTime) {
		this.putInTime = putInTime;
	}

	public String getPutInCrowd() {
		return putInCrowd;
	}

	public void setPutInCrowd(String putInCrowd) {
		this.putInCrowd = putInCrowd;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public String getOriginalityName() {
		return originalityName;
	}

	public void setOriginalityName(String originalityName) {
		this.originalityName = originalityName;
	}
	
}
