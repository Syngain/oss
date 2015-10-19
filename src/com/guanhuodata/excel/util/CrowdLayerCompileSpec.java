package com.guanhuodata.excel.util;

/**
 * 
 * @author fudk
 * 人群分层编译
 *
 */

public enum CrowdLayerCompileSpec {
	
	A("A","浏览用户"),
	B("B","关注用户"),
	//C("C","UNKNOWN"),
	D("D","未付款用户"),
	E("E","交易用户"),
	F("F","沉默用户"),
	G("G","目标用户");
	
	private final String crowdLayerAbbreviation;
	private final String crowdLayerFullName;
	
	CrowdLayerCompileSpec(String crowdLayerAbbreviation,String crowdLayerFullName){
		this.crowdLayerAbbreviation = crowdLayerAbbreviation;
		this.crowdLayerFullName = crowdLayerFullName;
	}

	public String getCrowdLayerAbbreviation() {
		return crowdLayerAbbreviation;
	}

	public String getCrowdLayerFullName() {
		return crowdLayerFullName;
	}
	
}
