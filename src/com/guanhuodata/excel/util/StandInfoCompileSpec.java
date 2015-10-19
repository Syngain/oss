package com.guanhuodata.excel.util;

public enum StandInfoCompileSpec {

	A("A","淘宝app"),
	B("B","淘宝首焦"),
	H1("H1","天猫通栏1"),
	E1("E1","天猫精选1"),
	E2("E2","天猫精选2"),
	F("F","3屏通栏大b"),
	C("C","淘宝首焦右b2"),
	D("D","淘宝首页2屏右"),
	R("R","天猫首焦"),
	J("J","凤凰网首页通栏03"),
	K("K","腾讯首页通栏"),
	L("L","新浪首页左侧画中画"),
	M("M","新浪网图集通栏"),
	N("N","中国网江苏频道右下浮窗"),
	O("O","搜狐首页第二擎天柱"),
	P1("P1","腾讯文章页画中画01"),
	P4("P4","腾讯文章页画中画04"),
	Q("Q","腾讯新闻擎天柱");
	
	
	private final String standInfoAbbreviation;
	private final String standInfoFullName;
	
	StandInfoCompileSpec(String standInfoAbbreviation,String standInfoFullName){
		this.standInfoAbbreviation = standInfoAbbreviation;
		this.standInfoFullName = standInfoFullName;
	}

	public String getStandInfoAbbreviation() {
		return standInfoAbbreviation;
	}

	public String getStandInfoFullName() {
		return standInfoFullName;
	}
	
}
