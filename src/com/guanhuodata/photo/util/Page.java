package com.guanhuodata.photo.util;

import java.io.Serializable;

import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

/**
 * 
 * @author fudk 分页组件
 *
 */
public class Page implements Serializable{

	private static final long serialVersionUID = -7735408756288440044L;

	// 当前页
	@JsonTypeSpec(JsonType.NUMBER)
	private int pageNumber;
	
	// 每页记录数
	@JsonTypeSpec(JsonType.NUMBER)
	private int pageSize;
	
	// 总记录数数
	@JsonTypeSpec(JsonType.NUMBER)
	private int totalRecords;
	
	//总页数
	@JsonTypeSpec(JsonType.NUMBER)
	private int totalPages;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotal() {
		return pageSize * pageNumber;
	}

}
