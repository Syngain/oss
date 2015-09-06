package com.guanhuodata.admin.dao;

public class SysRoleQueryCondition implements java.io.Serializable{
    private static final long serialVersionUID = -882982264064925816L;
    private String rolename;
    private String roleDesc;
    private String ownerUID;
    public String getRolename() {
        return rolename;
    }
    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
    public String getRoleDesc() {
        return roleDesc;
    }
    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
    public String getOwnerUID() {
        return ownerUID;
    }
    public void setOwnerUID(String ownerUID) {
        this.ownerUID = ownerUID;
    }
    
}
