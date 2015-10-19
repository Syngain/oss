package com.guanhuodata.framework.exception;

/**
 * 
 * @author fudk
 *	单元/计划命名不规范异常
 */
public class NameSchemaException extends Exception{

	private static final long serialVersionUID = -6276818794872388644L;

	public NameSchemaException() {
		super();
	}

	public NameSchemaException(String msg) {
		super(msg);
	}
}
