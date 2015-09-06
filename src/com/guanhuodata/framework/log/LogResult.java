package com.guanhuodata.framework.log;

public enum LogResult {
	SUCCESS(0),
	FAIL(1);
	public final int value;
	LogResult(int value){
		this.value = value;
	}
}
