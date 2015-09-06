package com.guanhuodata.framework.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

public class CtomsLoggerBean {

	@JsonTypeSpec(JsonType.NUMBER)
	private int id;
	private Date time;
	@JsonTypeSpec(JsonType.STRING)
	private String operator;
	@JsonTypeSpec(JsonType.STRING)
	private String action;
	@JsonTypeSpec(JsonType.STRING)
	private String operateobject;
	@JsonTypeSpec(JsonType.STRING)
	private String result;
	@JsonTypeSpec(JsonType.STRING)
	private String description;
	@JsonTypeSpec(JsonType.STRING)
	private String detail;
	@JsonTypeSpec(JsonType.STRING)
	private String strTime;
	private String startTime;
	private String endTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.setStrTime(sdf.format(time));
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getOperateobject() {
		return operateobject;
	}

	public void setOperateobject(String operateobject) {
		this.operateobject = operateobject;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getStrTime() {
		return strTime;
	}

	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}

	public String getStartTime() {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
