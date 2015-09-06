package com.guanhuodata.framework.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModuleRegistry {
    private static Map<String,Module> modules = new HashMap<String,Module>();
    //Register a module
    public static void registerModule(Module module){
        synchronized(modules){
            Set<String> keySet = modules.keySet();
            for(Iterator<String> it=keySet.iterator();it.hasNext();){
                String key = it.next();
                if(key.equals(module.getId())){
                    return;
                }
            }
            modules.put(module.getId(),module);
        }
    }
    //UnRegister a module
    public static void unRegisterModule(Module module){
        synchronized(modules){
            modules.remove(module.getId());
        }
    }
    //Get Modules that registered by system
    public static Collection<Module> getRegisteredModules(){
        return modules.values();
    }
    
    //Get Module by id
    public static Module getModule(String moduleId){
        return modules.get(moduleId);
    }
    
    public static Module getModuleByFunctionId(String fid) {
        Module tempModule = null;
        StringBuffer tempId = new StringBuffer();
        String[] part = fid.split("\\" + CoreConstants.ID_CONNECTOR);
        if (part.length > 1) {
            for (int i = 0; i < part.length; i++) {
                tempId.append(part[i]).append(CoreConstants.ID_CONNECTOR);
                Module m = ModuleRegistry.getModule(tempId.substring(0,tempId.length() - 1));
                if (m == null) {
                    return tempModule;
                }
                tempModule = m;
            }
        }
        return tempModule;
    }
    
    public static Collection<Module> getRootModule(Collection<Module> modules){
        List<Module> result = new ArrayList<Module>(5);
        for(Iterator<Module> it= modules.iterator();it.hasNext();){
            String moduleId = it.next().getId();
            String[] part = moduleId.split("\\" + CoreConstants.ID_CONNECTOR);
            Module rootModule = ModuleRegistry.getModule(part[0]);
            List<Module> second = rootModule.getModuleList();
            List<Module> subModules = new ArrayList<Module>(5);
            for(Module mm:modules){
                for(Module mmm:second){
                    if(mmm.getId().equals(mm.getId())){
                        subModules.add(mm);
                    }
                }
            }
            rootModule.setModuleList(subModules);
            if(!result.contains(rootModule)){
                result.add(rootModule);
            }
        }
        
        return result;
    }

    public static ModuleFunction compositeFuncs(ModuleFunction mf, List<String> fids) {
        List<ModuleFunction> mfList = mf.getFunctionList();
        if (mfList == null || mfList.isEmpty()) {
            for(String fidd:fids){
                if(mf.getId().equals(fidd)){
                    return mf;
                }
            }
        }
        for (ModuleFunction mf1 : mfList) {
            compositeFuncs(mf1,fids);
        }
        return null;
    }
}
