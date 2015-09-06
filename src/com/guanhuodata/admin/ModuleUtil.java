package com.guanhuodata.admin;

import java.util.List;
import com.guanhuodata.framework.core.Module;
import com.guanhuodata.framework.core.ModuleFunction;

public class ModuleUtil {
    
    public Module filterModuleBySpecFID(Module src,Module dest,List<String> functionIds){
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setVersion(src.getVersion());
        dest.setModuleProperties(src.getModuleProperties());
        
        if(src.getModuleList() != null){
            java.util.List<Module> subModules = new java.util.ArrayList<Module>(5);
            for(Module m:src.getModuleList()){
                for(String s: functionIds){
                    if(checkFIDInModule(s,m)){
                        Module dest1 = new Module();
                        filterModuleBySpecFID(m,dest1,functionIds);
                        if(!subModules.contains(dest1)){
                            subModules.add(dest1);
                        }
                    }
                }
                
            }
            dest.setModuleList(subModules);
        }
        if(src.getFunctions() != null){
            java.util.List<ModuleFunction> mfList = new java.util.ArrayList<ModuleFunction>(5);
            for(ModuleFunction mf:src.getFunctions()){
                ModuleFunction destFunc = new ModuleFunction();
                filterModuleFunction(mf, destFunc,functionIds);
                for(String s:functionIds){
                    if(s.equals(destFunc.getId())){
                        if(!mfList.contains(destFunc)){
                            mfList.add(destFunc);
                        }
                    }
                }
            }
            dest.setFunctions(mfList);
        }
        return dest;
    }
    
    public Module copyModule(Module src,Module dest){
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setVersion(src.getVersion());
        dest.setModuleProperties(src.getModuleProperties());
        
        if(src.getModuleList() != null){
            java.util.List<Module> subModules = new java.util.ArrayList<Module>(5);
            for(Module m:src.getModuleList()){
                Module dest1 = new Module();
                copyModule(m,dest1);
                subModules.add(dest1);
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
    
    public boolean checkModuleHasFunction(Module m){
        boolean exist = false;
        if(m.getFunctions()!= null && !m.getFunctions().isEmpty()){
            exist = true;
            return exist;
        }else{
            if(m.getModuleList() != null){
                for(Module subM: m.getModuleList()){
                    if(checkModuleHasFunction(subM)){exist = true;break;}
                }
            }
        }
        return exist;
    }
    
    public boolean checkFIDInModule(String fid ,Module m){
        boolean exist = false;
        if(m.getFunctions()!= null && !m.getFunctions().isEmpty()){
            for(ModuleFunction subMF: m.getFunctions()){
                if(subMF.getId().equals(fid)){
                    exist = true;
                    return exist;
                }
            }
        }else{
            if(m.getModuleList() != null){
                for(Module subM: m.getModuleList()){
                    if(checkFIDInModule(fid,subM)){exist = true;break;}
                }
            }
        }
        return exist;
    }
    
    private void filterModuleFunction(ModuleFunction src,ModuleFunction dest,List<String> functionIds){
        dest.setId(src.getId());
        dest.setName(src.getName());
        dest.setType(src.getType());
        dest.setLicenseId(src.getLicenseId());
        java.util.List<ModuleFunction> mfList = new java.util.ArrayList<ModuleFunction>(5);
        if(src.getFunctionList()!= null && !src.getFunctionList().isEmpty()){
            for(ModuleFunction mf:src.getFunctionList()){
                ModuleFunction destFunc = new ModuleFunction();
                filterModuleFunction(mf, destFunc,functionIds);
                for(String s : functionIds){
                    if(s.equals(mf.getId())){
                        if(!mfList.contains(destFunc)){
                            mfList.add(destFunc);
                        }
                    }
                }
            }
        }else{
            for(String s : functionIds){
                if(s.equals(src.getId())){
                    if(!mfList.contains(src)){
                        mfList.add(src);
                    }
                }
            }
        }
        dest.setFunctionList(mfList);
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
}
