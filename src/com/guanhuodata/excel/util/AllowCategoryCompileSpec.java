package com.guanhuodata.excel.util;

/**
 * 
 * @author fudk
 * 所属类目编译
 *
 */

public enum AllowCategoryCompileSpec {

	F("F","女装");
	
	private final String allowCategoryAbbreviation;
	private final String allowCategoryFullName;
	
	AllowCategoryCompileSpec(String allowCategoryAbbreviation,String allowCategoryFullName) {
		this.allowCategoryAbbreviation = allowCategoryAbbreviation;
		this.allowCategoryFullName = allowCategoryFullName;
	}

	public String getAllowCategoryAbbreviation() {
		return allowCategoryAbbreviation;
	}

	public String getAllowCategoryFullName() {
		return allowCategoryFullName;
	}
	
}
