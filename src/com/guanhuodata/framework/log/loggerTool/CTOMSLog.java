package com.guanhuodata.framework.log.loggerTool;

import com.guanhuodata.framework.log.Log;

public class CTOMSLog extends Log {

	private static final long serialVersionUID = -7202988263769047398L;
	
	private String operator;
	private OperationType operationType;
	private String operatorIP;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public String getOperatorIP() {
		return operatorIP;
	}

	public void setOperatorIP(String operatorIP) {
		this.operatorIP = operatorIP;
	}

}
