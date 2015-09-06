package com.guanhuodata.framework.core;

import java.util.List;

public class ModuleFunction implements java.io.Serializable{
    private static final long serialVersionUID = 2235161639470140403L;
    private String id;
    private String name;
    private String licenseId;
    private String type;
    private List<ModuleFunction> functionList;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLicenseId() {
        return licenseId;
    }
    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }
    public List<ModuleFunction> getFunctionList() {
        return functionList;
    }
    public void setFunctionList(List<ModuleFunction> functionList) {
        this.functionList = functionList;
    }
    public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ModuleFunction other = (ModuleFunction) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    
}
