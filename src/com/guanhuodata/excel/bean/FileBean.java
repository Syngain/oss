package com.guanhuodata.excel.bean;

import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

/**
 * 
 * @author fudk
 *	读取上传报表文件返回前台构造FileBean
 */
public class FileBean implements java.io.Serializable{

	private static final long serialVersionUID = -5224664221245042147L;

	@JsonTypeSpec(JsonType.NUMBER)
	private int id;
	@JsonTypeSpec(JsonType.STRING)
	private String fileName;
	@JsonTypeSpec(JsonType.STRING)
	private String operator;
	@JsonTypeSpec(JsonType.STRING)
	private String operateTime;
	@JsonTypeSpec(JsonType.STRING)
	private String pieChart;
	@JsonTypeSpec(JsonType.STRING)
	private String lineChart;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public String getPieChart() {
		return pieChart;
	}
	public void setPieChart(String pieChart) {
		this.pieChart = pieChart;
	}
	public String getLineChart() {
		return lineChart;
	}
	public void setLineChart(String lineChart) {
		this.lineChart = lineChart;
	}
}
