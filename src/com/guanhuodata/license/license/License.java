package com.guanhuodata.license.license;

import java.util.List;

public class License implements java.io.Serializable{
    private static final long serialVersionUID = -7214923600406546392L;
    private String productName;
    private String version;
    private String customer;
    private String issueDate;
    private long expireDate;
    private String copyright;
    private String signature;
    private List<LicenseModule> licenseModules;
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getCustomer() {
        return customer;
    }
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    public String getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
    public long getExpireDate() {
        return expireDate;
    }
    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }
    public String getCopyright() {
        return copyright;
    }
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public List<LicenseModule> getLicenseModules() {
        return licenseModules;
    }
    public void setLicenseModules(List<LicenseModule> licenseModules) {
        this.licenseModules = licenseModules;
    }
}
