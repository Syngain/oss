package com.guanhuodata.admin;

import java.io.Serializable;
import java.util.List;
import com.guanhuodata.admin.dao.SysRole;
import com.guanhuodata.admin.dao.SysUser;
import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

public class LoginUser implements Serializable {
    private static final long serialVersionUID = -6748413567391577469L;
    @JsonTypeSpec(JsonType.OBJECT)
    private SysUser sysUser;
    @JsonTypeSpec(JsonType.OBJECT)
    private SysRole role;
    @JsonTypeSpec(JsonType.STRING)
    private String loginLocation;
    @JsonTypeSpec(JsonType.NUMBER)
    private long loginTime;
    @JsonTypeSpec(JsonType.STRING)
    private String lastLoginLocation;
    @JsonTypeSpec(JsonType.NUMBER)
    private long lastLoginTime;
    @JsonTypeSpec(JsonType.LIST)
    private List<String> actions;
    public SysUser getSysUser() {
        return sysUser;
    }
    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }
    public SysRole getRole() {
        return role;
    }
    public void setRole(SysRole role) {
        this.role = role;
    }
    public String getLoginLocation() {
        return loginLocation;
    }
    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }
    public List<String> getActions() {
        return actions;
    }
    public void setActions(List<String> actions) {
        this.actions = actions;
    }
    
    public long getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
    public String getLastLoginLocation() {
        return lastLoginLocation;
    }
    public void setLastLoginLocation(String lastLoginLocation) {
        this.lastLoginLocation = lastLoginLocation;
    }
    public long getLastLoginTime() {
        return lastLoginTime;
    }
    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
