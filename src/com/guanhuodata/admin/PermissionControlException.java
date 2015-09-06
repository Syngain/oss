package com.guanhuodata.admin;

import com.guanhuodata.framework.exception.BOException;

public class PermissionControlException extends BOException {
	private static final long serialVersionUID = -4243903005345184530L;
	public PermissionControlException(String msg){
		super(msg);
	}
	public PermissionControlException(){
		super();
	}
}
