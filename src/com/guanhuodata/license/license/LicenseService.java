package com.guanhuodata.license.license;

public interface LicenseService {
    public License loadLicense();
    public boolean isLicensed(String id);
    
}
