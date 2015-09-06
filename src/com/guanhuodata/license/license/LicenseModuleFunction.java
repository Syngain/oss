package com.guanhuodata.license.license;

import java.util.List;

public class LicenseModuleFunction implements java.io.Serializable{
    private static final long serialVersionUID = -886391590960120560L;
    private String id;
    private String description;
    private boolean enabled;
    private List<LicenseModuleFunction> licenseModuleFunctions;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public List<LicenseModuleFunction> getLicenseModuleFunctions() {
        return licenseModuleFunctions;
    }
    public void setLicenseModuleFunctions(List<LicenseModuleFunction> licenseModuleFunctions) {
        this.licenseModuleFunctions = licenseModuleFunctions;
    }
    
}
