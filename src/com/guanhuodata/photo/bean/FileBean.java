package com.guanhuodata.photo.bean;

import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

public class FileBean implements java.io.Serializable{

	private static final long serialVersionUID = -8390506641544107402L;
	
	@JsonTypeSpec(JsonType.STRING)
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
