package com.guanhuodata.framework.log.loggerTool;

public class CTOMSOpLog implements java.io.Serializable {

	private static final long serialVersionUID = -8666613634414173742L;

	private String id;
	private String operator;
	private int opType;
	private int opResult;
	private long opTime;
	private String opContent;
	private String opIp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getOpType() {
		return opType;
	}

	public void setOpType(int opType) {
		this.opType = opType;
	}

	public int getOpResult() {
		return opResult;
	}

	public void setOpResult(int opResult) {
		this.opResult = opResult;
	}

	public long getOpTime() {
		return opTime;
	}

	public void setOpTime(long opTime) {
		this.opTime = opTime;
	}

	public String getOpContent() {
		return opContent;
	}

	public void setOpContent(String opContent) {
		this.opContent = opContent;
	}

	public String getOpIp() {
		return opIp;
	}

	public void setOpIp(String opIp) {
		this.opIp = opIp;
	}
}
