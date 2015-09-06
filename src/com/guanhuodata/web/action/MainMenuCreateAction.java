package com.guanhuodata.web.action;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.core.Module;
import com.guanhuodata.framework.core.ModuleFunction;
import com.guanhuodata.framework.core.ModuleRegistry;

public class MainMenuCreateAction implements Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        
        if (request.getSession().getAttribute(CoreConstants.LOGIN_USER) == null) {
            response.sendRedirect(request.getContextPath());
        }
        LoginUser loginUser = (LoginUser) request.getSession(false).getAttribute(CoreConstants.LOGIN_USER);
//        Collection<Module> userModules = loginUser.getRole().getModules();
        Collection<Module> menuModules = loginUser.getRole().getModules();
        Collection<Module> bottomMenuModules = new java.util.ArrayList<Module>(5);
        Collection<Module> topMenuModules = new java.util.ArrayList<Module>(5);
        for(Module m:menuModules){
        	filterBottomPositionModule(m,bottomMenuModules);
        }
        for(Module m:menuModules){
            if(bottomMenuModules != null && !bottomMenuModules.isEmpty()){
            	List<Module> purgedMenuModules = purgeMenuModule(m, bottomMenuModules);
            	for(Module cm: purgedMenuModules){
            		if(!topMenuModules.contains(cm)){
            			topMenuModules.add(cm);
            		}
            	}
            }else{
                topMenuModules.add(m);
            }
        }
        
        //Resovle the Module that not in bottom menu
        for(Module m:menuModules){
            boolean inBottom = false;
            for(Module bm:bottomMenuModules){
                String moduleId =bm.getId();
                String[] part1 = moduleId.split("\\" + CoreConstants.ID_CONNECTOR);
                Module bmRootModule = ModuleRegistry.getModule(part1[0]);
                if(m.getId().equals(bmRootModule.getId())){
                    inBottom = true;
                    break;
                }
            }
            if(!inBottom){
                if(!topMenuModules.contains(m))
                    topMenuModules.add(m);
            }
        }
        List<String> orderRefList = readNavOrder();
        Collection<Module> topMenus = sortMenuItem(topMenuModules,orderRefList);
        Collection<Module> bottomMenus = sortBottomMenuItem(bottomMenuModules,orderRefList);
        StringBuffer sb = new StringBuffer();
        sb.append("{\"top\":");
        sb.append("[");
        for (Module module : topMenus) {
            sb.append(createModuleJson(module));
            sb.append(",");
        }
        dropLastChar(sb);
        sb.append("],\"bottom\":[");
        for (Module module : bottomMenus) {
            sb.append(createModuleJson(module));
            sb.append(",");
        }
        dropLastChar(sb);
        sb.append("]}");
        out.print(sb.toString());
        out.flush();
        out.close();
    }
    
    private void filterBottomPositionModule(Module m,Collection<Module> bottomMenuModules){
    	Properties mp = m.getModuleProperties();
    	if(mp != null){
    		for(Map.Entry entry:mp.entrySet()){
    			if((entry.getKey().equals("position") && entry.getValue().equals("bottom")) || (entry.getKey().equals("position") && entry.getValue().equals("both"))){
    				bottomMenuModules.add(m);
    				return;
    			}
    		}
    	}
    	if(m.getModuleList() != null){
    		for(Module mo:m.getModuleList()){
    			filterBottomPositionModule(mo,bottomMenuModules);
    		}
    	}
    }
    
    private java.util.List<Module> purgeMenuModule(Module srcModule,Collection<Module> bottomModules){
    	java.util.List<Module> topMenuModules = new java.util.ArrayList<Module>(5);
    	String[] bottomModuleIds = new String[bottomModules.size()];
    	int idx = 0;
    	for(Module m: bottomModules){
    	   bottomModuleIds[idx] = m.getId();
    	   idx++;
    	}
    	for(Module m:bottomModules){
    		String moduleId =m.getId();
            String[] part = moduleId.split("\\" + CoreConstants.ID_CONNECTOR);
            Module rootModule = ModuleRegistry.getModule(part[0]);
            //if the bottom module is top level,then return!
            if(moduleId.equals(rootModule.getId())){
            	if(rootModule.getModuleList() != null && !rootModule.getModuleList().isEmpty()){
	                topMenuModules.add(m);
	                break;
            	}else{
            		break;
            	}
            }
    		if(srcModule.getId().equals(rootModule.getId())){
	    		Module destModule = copyModule(srcModule, new Module(), bottomModuleIds);
	    		if(!topMenuModules.contains(destModule)){
	    			topMenuModules.add(destModule);
	    		}
    		}
    	}
    	return topMenuModules;
    }
    
    private Module copyModule(Module src,Module dest,String[] mid){
    	dest.setId(src.getId());
    	dest.setName(src.getName());
    	dest.setVersion(src.getVersion());
    	dest.setModuleProperties(src.getModuleProperties());
    	
    	if(src.getModuleList() != null){
    		java.util.List<Module> subModules = new java.util.ArrayList<Module>(5);
    		for(Module m:src.getModuleList()){
    			Module dest1 = new Module();
    			copyModule(m,dest1,mid);
    			boolean isBoth = false;
    			Properties mp = dest1.getModuleProperties();
    	    	if(mp != null){
    	    		for(Map.Entry entry:mp.entrySet()){
    	    			if(entry.getKey().equals("position") && entry.getValue().equals("both")){
    	    				isBoth = true;
    	    			}
    	    		}
    	    	}
    	    	boolean moduleInBottom = false;
    	    	for(String subMID: mid){
    	    	    if(subMID.equals(dest1.getId())){
    	    	        moduleInBottom = true;
    	    	        break;
                    }
    	    	}
    	    	if(!moduleInBottom || isBoth){
    	    	    subModules.add(dest1);
    	    	}
    		}
    		dest.setModuleList(subModules);
    	}
    	if(src.getFunctions() != null){
    		java.util.List<ModuleFunction> mfList = new java.util.ArrayList<ModuleFunction>(5);
    		for(ModuleFunction mf:src.getFunctions()){
    			ModuleFunction destFunc = new ModuleFunction();
    			copyModuleFunction(mf, destFunc);
    			mfList.add(destFunc);
    		}
    		dest.setFunctions(mfList);
    	}
    	return dest;
    }
    
    private void copyModuleFunction(ModuleFunction src,ModuleFunction dest){
    	dest.setId(src.getId());
    	dest.setName(src.getName());
    	dest.setType(src.getType());
    	dest.setLicenseId(src.getLicenseId());
    	if(src.getFunctionList()!= null){
    		java.util.List<ModuleFunction> mfList = new java.util.ArrayList<ModuleFunction>(5);
    		for(ModuleFunction mf:src.getFunctionList()){
    			ModuleFunction destFunc = new ModuleFunction();
    			copyModuleFunction(mf, destFunc);
    			mfList.add(destFunc);
    		}
    		dest.setFunctionList(mfList);
    	}
    }
    
    private String createModuleJson(Module module) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"module\":{").append("\"id\":").append("\"").append(module.getId()).append("\"")
                .append(",\"name\":").append("\"").append(module.getName()).append("\"");
        if (module.getVersion() != null && !module.getVersion().trim().equals("")) {
            sb.append(",\"version\":").append("\"").append(module.getVersion()).append("\"");
        }
        if (module.getModuleProperties() != null) {
            Properties prop = module.getModuleProperties();
            sb.append(",\"properties\":[");
            for (Map.Entry entry : prop.entrySet()) {
                sb.append("{\"name\":\"").append(entry.getKey().toString()).append("\"");
                sb.append(",\"value\":\"").append(entry.getValue().toString()).append("\"},");
            }
            dropLastChar(sb);
            sb.append("]");
        }
        if(module.getModuleList()!=null && !module.getModuleList().isEmpty()){
            sb.append(",");
            sb.append("\"modules\":[");
            for(Module m:module.getModuleList()){
                sb.append(createModuleJson(m));
                sb.append(",");
            }
            dropLastChar(sb);
            sb.append("]");
        }
        sb.append("}}");
        return sb.toString();
    }

    private void dropLastChar(StringBuffer sb) {
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
    }
    
    private Collection<Module> sortMenuItem(Collection<Module> menuModules,List<String> refList){
        java.util.List<Module> orderedModuleList = new java.util.ArrayList<Module>();
        if(refList.isEmpty()){
            return menuModules;
        }
        for(String ref:refList){
            for(Module m: menuModules){
                if(m.getId().equals(ref)){
                    orderedModuleList.add(m);
                    break;
                }
            }
        }
        menuModules.removeAll(orderedModuleList);
        orderedModuleList.addAll(menuModules);
        return orderedModuleList;
    }
    
    private Collection<Module> sortBottomMenuItem(Collection<Module> menuModules,List<String> refList){
        java.util.List<Module> orderedModuleList = new java.util.ArrayList<Module>();
        if(refList.isEmpty()){
            return menuModules;
        }
        for(String ref:refList){
            for(Module m: menuModules){
                String moduleId =m.getId();
                String[] part1 = moduleId.split("\\" + CoreConstants.ID_CONNECTOR);
                Module rootModule = ModuleRegistry.getModule(part1[0]);
                if(rootModule.getId().equals(ref)){
                    orderedModuleList.add(m);
                }
            }
        }
        menuModules.removeAll(orderedModuleList);
        orderedModuleList.addAll(menuModules);
        return orderedModuleList;
    }
    
    private List<String> readNavOrder(){
        java.util.List<String> orderList = new java.util.ArrayList<String>();
        java.net.URL fileURL = MainMenuCreateAction.class.getClassLoader().getResource("nav.conf");
        if(fileURL != null){
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileURL.getFile()));
                String content = br.readLine();
                while(content != null){
                    if(!content.trim().equals("")){
                        orderList.add(content);
                    }
                    content = br.readLine();
                }
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }
        return orderList;
    }
    
}
