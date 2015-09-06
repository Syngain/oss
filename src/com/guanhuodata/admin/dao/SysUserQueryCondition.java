package com.guanhuodata.admin.dao;

public class SysUserQueryCondition implements java.io.Serializable{
    private static final long serialVersionUID = 9149715298963725208L;
    private String username;
    private String role;
    private int status = -1;
    private String userDesc;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getUserDesc() {
        return userDesc;
    }
    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }
	
}
