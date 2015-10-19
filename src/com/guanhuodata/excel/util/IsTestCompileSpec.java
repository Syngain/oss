package com.guanhuodata.excel.util;

/**
 * 
 * @author 
 * 是否测试编译
 *
 */

public enum IsTestCompileSpec {

	Y("Y","已测"),
	N("N","未测");
	
	private final String isTestAbbreviation;
	private final String isTestFullName;
	
	IsTestCompileSpec(String isTestAbbreviation,String isTestFullName){
		this.isTestAbbreviation = isTestAbbreviation;
		this.isTestFullName = isTestFullName;
	}

	public String getIsTestAbbreviation() {
		return isTestAbbreviation;
	}

	public String getIsTestFullName() {
		return isTestFullName;
	}
	
}
