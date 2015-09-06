package com.guanhuodata.web.listener;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.core.Module;
import com.guanhuodata.framework.core.ModuleFunction;
import com.guanhuodata.framework.core.ModuleRegistry;
import com.guanhuodata.framework.core.ServiceContext;
import com.guanhuodata.license.license.LicenseService;

public class FrameworkInitListener implements ServletContextListener {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FrameworkInitListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServiceContext sc = ServiceContext.getInstance();
		sce.getServletContext().setAttribute(CoreConstants.SERVICE_CTX,sc);
		LOG.info("Module Registry Process complete.");
		/*LicenseService licenseService = sc.getService("licenseService",LicenseService.class);
		License lic = licenseService.loadLicense();
		sce.getServletContext().setAttribute("license",lic);
		ModuleLoader moduleLoader = new ModuleLoader();
		List<Module> modules = moduleLoader.loadModules();
		for(Module m:modules){
		    registerModule(m,licenseService);
		}*/
		/*for(Iterator<Module> it=ModuleRegistry.getRegisteredModules().iterator();it.hasNext();){
            Module mod = it.next();
        }*/
	}
	
	private void registerModule(Module m,LicenseService lics){
	    if(lics.isLicensed(m.getId())){
	        List<ModuleFunction> functionList = m.getFunctions();
	        if(functionList!=null && !functionList.isEmpty()){
	            List<ModuleFunction> filteredFuncs = new ArrayList<ModuleFunction>(5);
	            for(ModuleFunction mf:functionList){
	                if(lics.isLicensed(mf.getId())){
	                    filteredFuncs.add(mf);
	                    registerModuleFunction(mf,lics);
	                }
	            }
	            m.getFunctions().clear();
	            m.getFunctions().addAll(filteredFuncs);
	        }
	        if(m.getModuleList()!=null && !m.getModuleList().isEmpty()){
	            List<Module> filteredModules = new ArrayList<Module>();
	            for(Module subM:m.getModuleList()){
	                if(lics.isLicensed(subM.getId())){
	                    filteredModules.add(subM);
	                    registerModule(subM,lics);
	                }
	            }
	            m.getModuleList().clear();
	            m.getModuleList().addAll(filteredModules);
	        }
            ModuleRegistry.registerModule(m);
	    }
	}
	
	private void registerModuleFunction(ModuleFunction mf,LicenseService ls){
	    List<ModuleFunction> subList = mf.getFunctionList();
	    if(subList != null && !subList.isEmpty()){
	        List<ModuleFunction> filteredFuncs = new ArrayList<ModuleFunction>(5);
    	    for(ModuleFunction subMf : subList){
    	        if(ls.isLicensed(subMf.getId())){
    	            filteredFuncs.add(subMf);
    	            registerModuleFunction(subMf,ls);
    	        }
    	    }
            mf.getFunctionList().clear();
            mf.getFunctionList().addAll(filteredFuncs);
	    }
	}
}
