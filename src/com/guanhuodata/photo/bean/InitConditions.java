package com.guanhuodata.photo.bean;

import java.io.Serializable;
import java.util.List;
import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

public class InitConditions implements Serializable{

	private static final long serialVersionUID = -8206607631687485637L;
	
	@JsonTypeSpec(JsonType.LIST)
	private List<String> shopNameList;
	
	@JsonTypeSpec(JsonType.LIST)
	private List<String> materialStandAbbreviationList;
	
	@JsonTypeSpec(JsonType.LIST)
	private List<String> materialThemeList;
	
	@JsonTypeSpec(JsonType.LIST)
	private List<String> materialCrowdList;

	public List<String> getShopNameList() {
		return shopNameList;
	}

	public void setShopNameList(List<String> shopNameList) {
		this.shopNameList = shopNameList;
	}

	public List<String> getMaterialStandAbbreviationList() {
		return materialStandAbbreviationList;
	}

	public void setMaterialStandAbbreviationList(List<String> materialStandAbbreviationList) {
		this.materialStandAbbreviationList = materialStandAbbreviationList;
	}

	public List<String> getMaterialThemeList() {
		return materialThemeList;
	}

	public void setMaterialThemeList(List<String> materialThemeList) {
		this.materialThemeList = materialThemeList;
	}

	public List<String> getMaterialCrowdList() {
		return materialCrowdList;
	}

	public void setMaterialCrowdList(List<String> materialCrowdList) {
		this.materialCrowdList = materialCrowdList;
	}
	
}
