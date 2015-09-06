package com.guanhuodata.framework.log;

public enum LogLevel {
	DEBUG("DEBUG"),	
	INFO("INFO"),
	WARN("WARN"),
	ERROR("ERROR");
	public final String value;
	LogLevel(String value){
		this.value = value;
	}
}
