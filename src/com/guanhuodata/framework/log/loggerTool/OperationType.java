package com.guanhuodata.framework.log.loggerTool;

public enum OperationType {
	ADD(1),
	DEL(2),
	UPDATE(3),
	LOGIN(4),
	OTHER(99);
	public final int value;
	OperationType(int value){
		this.value = value;
	}
}
