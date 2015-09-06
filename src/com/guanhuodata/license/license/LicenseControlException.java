package com.guanhuodata.license.license;

public class LicenseControlException extends Exception{
    private static final long serialVersionUID = 5858143177962115849L;
    public LicenseControlException(String msg){
        super(msg);
    }
    public LicenseControlException(){
        super();
    }
}
