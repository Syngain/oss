package com.guanhuodata.framework.exception;

/**
 * 
 * @author fudk
 * 在使用字母来匹配简称异常
 *
 */

public class SplitAbbreviationCompileException extends Exception{

	private static final long serialVersionUID = 377191052940029561L;

	public SplitAbbreviationCompileException(){
		super();
	}
	
	public SplitAbbreviationCompileException(String msg){
		super(msg);
	}
}
