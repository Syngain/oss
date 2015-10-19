package com.guanhuodata.excel.util;

public enum CrowdInfoCompileSpec {

	crowdG1("人群G1","搜索关键字"),
	crowdH1("人群H1","对其他凤凰色品牌店铺有加购物车行为"),
	crowdF("人群F","沉默用户"),
	crowdE("人群E","180天交易"),
	testCrowd25("测试人群25","7天浏览量大于5"),
	testCrowd12("测试人群12","加购物车"),
	testCrowdR("测试人群R","下单未付款"),
	testCrowdA("测试人群A","180天交易");
	
	
	private final String crowdInfoAbbreviation;
	private final String crowdInfoFullName;
	
	CrowdInfoCompileSpec(String crowdInfoAbbreviation,String crowdInfoFullName) {
		this.crowdInfoAbbreviation = crowdInfoAbbreviation;
		this.crowdInfoFullName = crowdInfoFullName;
	}

	public String getCrowdInfoAbbreviation() {
		return crowdInfoAbbreviation;
	}

	public String getCrowdInfoFullName() {
		return crowdInfoFullName;
	}
	
}
