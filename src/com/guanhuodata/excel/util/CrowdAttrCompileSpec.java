package com.guanhuodata.excel.util;

/**
 * 
 * @author fudk
 * 人群属性编译
 *
 */

public enum CrowdAttrCompileSpec {

	Q("Q","全网用户"),
	L("L","类目池用户"),
	P("P","品牌池用户"),
	A("A","浏览用户"),
	H("H","访客"),
	C("C","收藏宝贝"),
	B("B","收藏店铺"),
	D("D","未付款"),
	E("E","交易用户"),
	F("F","沉默用户"),
	Z("Z","加购物车");
	
	private final String crowdAttrAbbreviation;
	private final String crowdAttrFullName;
	
	CrowdAttrCompileSpec(String crowdAttrAbbreviation,String crowdAttrFullName){
		this.crowdAttrAbbreviation = crowdAttrAbbreviation;
		this.crowdAttrFullName = crowdAttrFullName;
	}

	public String getCrowdAttrAbbreviation() {
		return crowdAttrAbbreviation;
	}

	public String getCrowdAttrFullName() {
		return crowdAttrFullName;
	}
	
}
