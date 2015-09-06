package com.guanhuodata.admin.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

public class SysUser implements Serializable {
    private static final long serialVersionUID = -1467974903300951357L;
    final static Map<Integer,String> userStatusMap = new HashMap<Integer,String>();
    @JsonTypeSpec(JsonType.STRING)
    private String id;
    @JsonTypeSpec(JsonType.STRING)
    private String pid;
    @JsonTypeSpec(JsonType.STRING)
    private String username;
    @JsonTypeSpec(JsonType.STRING)
    private String password;
    @JsonTypeSpec(JsonType.NUMBER)
    private int status;// 1：启用；0：禁用
    @JsonTypeSpec(JsonType.NUMBER)
    private long lastLoginTime;
    @JsonTypeSpec(JsonType.STRING)
    private String lastLoginLocation;
    @JsonTypeSpec(JsonType.NUMBER)
    private int retryTime;// 单位：次
    @JsonTypeSpec(JsonType.NUMBER)
    private int failDelay;// 单位：分钟
    @JsonTypeSpec(JsonType.STRING)
    private String fullname;
    @JsonTypeSpec(JsonType.STRING)
    private String email;
    @JsonTypeSpec(JsonType.STRING)
    private String telephone;
    @JsonTypeSpec(JsonType.STRING)
    private String desc;
    @JsonTypeSpec(JsonType.NUMBER)
    private int failedTimes;
    @JsonTypeSpec(JsonType.NUMBER)
    private long frozenTime;
    @JsonTypeSpec(JsonType.STRING)
    private String roleId;
    @JsonTypeSpec(JsonType.STRING)
    private String rolename;

    public String getId() {
        return id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    
    public String getPid() {
        return pid;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginLocation() {
        return lastLoginLocation;
    }

    public void setLastLoginLocation(String lastLoginLocation) {
        this.lastLoginLocation = lastLoginLocation;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    public int getFailDelay() {
        return failDelay;
    }

    public void setFailDelay(int failDelay) {
        this.failDelay = failDelay;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getFailedTimes() {
        return failedTimes;
    }

    public void setFailedTimes(int failedTimes) {
        this.failedTimes = failedTimes;
    }

    public long getFrozenTime() {
        return frozenTime;
    }

    public void setFrozenTime(long frozenTime) {
        this.frozenTime = frozenTime;
    }
    public String getRoleId() {
        return roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
    
}
