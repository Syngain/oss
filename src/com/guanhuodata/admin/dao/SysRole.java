package com.guanhuodata.admin.dao;

import com.guanhuodata.framework.core.Module;
import com.guanhuodata.framework.util.JsonType;
import com.guanhuodata.framework.util.JsonTypeSpec;

public class SysRole implements java.io.Serializable{
    private static final long serialVersionUID = -7028217156353726237L;
    @JsonTypeSpec(JsonType.STRING)
    private String id;
    @JsonTypeSpec(JsonType.STRING)
    private String ownerUserId;
    @JsonTypeSpec(JsonType.STRING)
    private String name;
    @JsonTypeSpec(JsonType.STRING)
    private String description;
    @JsonTypeSpec(JsonType.LIST)
    private java.util.List<String> actions;
    @JsonTypeSpec(JsonType.LIST)
    private java.util.Collection<Module> modules;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getOwnerUserId() {
        return ownerUserId;
    }
    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public java.util.List<String> getActions() {
        return actions;
    }
    public void setActions(java.util.List<String> actions) {
        this.actions = actions;
    }
    public java.util.Collection<Module> getModules() {
        return modules;
    }
    public void setModules(java.util.Collection<Module> modules) {
        this.modules = modules;
    }
}
