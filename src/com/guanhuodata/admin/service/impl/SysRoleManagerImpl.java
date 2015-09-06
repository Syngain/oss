package com.guanhuodata.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.admin.ModuleUtil;
import com.guanhuodata.admin.PermissionControl;
import com.guanhuodata.admin.dao.SysRole;
import com.guanhuodata.admin.dao.SysRoleDAO;
import com.guanhuodata.admin.dao.SysRoleQueryCondition;
import com.guanhuodata.admin.service.SysRoleManager;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.core.Module;
import com.guanhuodata.framework.core.ModuleRegistry;
import com.guanhuodata.framework.exception.BOException;
import com.guanhuodata.framework.exception.DuplicateException;
import com.guanhuodata.framework.exception.NotExistException;
import com.guanhuodata.framework.log.LogLevel;
import com.guanhuodata.framework.log.Logger;
import com.guanhuodata.framework.util.IDGenerator;
import com.guanhuodata.framework.util.Page;
import com.guanhuodata.framework.util.Utils;

public class SysRoleManagerImpl implements SysRoleManager {

    private SysRoleDAO sysRoleDao;
    
    public SysRole getSysRoleById(String roleid) throws BOException {
        SysRole sr = sysRoleDao.getSysRoleById(roleid);
        if(sr == null){
            return null;
        }
        List<String> actionList = sr.getActions();
        List<Module> roleModules = new ArrayList<Module>();
        
        ModuleUtil mu = new ModuleUtil();
        for (String s : actionList) {
            Module m = ModuleRegistry.getModuleByFunctionId(s);
            if(m != null){
                String[] part = m.getId().split("\\" + CoreConstants.ID_CONNECTOR);
                Module rootModule = ModuleRegistry.getModule(part[0]);
                Module destModule = mu.filterModuleBySpecFID(rootModule, new Module(), actionList);
                if(destModule == null) continue;
                if (!roleModules.contains(destModule)){
                    roleModules.add(destModule);
                }
            }
        }
        java.util.List<Module> srModules = new java.util.ArrayList<Module>(5);
        for(Module m:roleModules){
            if(mu.checkModuleHasFunction(m)){
                srModules.add(m);
            }
        }
        sr.setModules(srModules);
        return sr;
    }

    @PermissionControl("systemMgr.roleMgr.addRole")
    public void addRole(SysRole role, LoginUser loginUser) throws DuplicateException, BOException {
        Logger.getLogger(SysRoleManagerImpl.class).log(LogLevel.INFO,"Add Role: " + role.getName());
        if (findRoleByName(role.getName())) {
            Logger.getLogger(SysRoleManagerImpl.class).log(LogLevel.ERROR,"Add Role Fail. " + role.getName()
                    + " is already exist.");
            throw new DuplicateException("error.sys.role.exist");
        }
        if (role.getId() == null || role.getId().trim().equals("")) {
            role.setId(IDGenerator.generateId());
        }
        role.setOwnerUserId(loginUser.getSysUser().getId());
        sysRoleDao.addRole(role);
        List<String> actionIds = role.getActions();
        if (actionIds != null && !actionIds.isEmpty()) {
            for (String aid : actionIds) {
                sysRoleDao.addActionToRole(aid,role.getId());
            }
        }
        Logger.getLogger(SysRoleManagerImpl.class).log(LogLevel.INFO,"Add Role: " + role.getName() + " is successful.");
    }

    public List<SysRole> getAllSysRole(String loginId,String ownerId) {
        return sysRoleDao.getAllRole(loginId,ownerId);
    }

    public List<SysRole> getPagedSysRole(SysRoleQueryCondition condition, Page page,LoginUser loginUser) {
        Map<String,Object> paramMap = wrapQueryCondition(condition,loginUser);
        if(page != null){
            paramMap.put("startPage",page.getStartPage());
            paramMap.put("pageSize",page.getPageSize());
        }
        if(!loginUser.getSysUser().getId().equals(CoreConstants.ADMIN_ID)){
            paramMap.put("loginId",loginUser.getSysUser().getId());
            paramMap.put("ownerId",loginUser.getRole().getOwnerUserId());
        }
        return sysRoleDao.getPagedRole(paramMap);
    }

    @PermissionControl("systemMgr.roleMgr.delRole")
    public void delRoleById(String[] roleids, LoginUser loginUser) throws NotExistException, BOException {
        for(String id:roleids){
            if(id.equals(CoreConstants.BUILD_IN_ROLE_ID)){
                throw new BOException("error.del.build-in-role");
            }
        }
        for(String roleID:roleids){
            if(findRoleById(roleID)){
                sysRoleDao.deleteRoleById(roleID);
            }else{
                throw new NotExistException("error.sys.role.notfound");
            }
        }
    }

    @PermissionControl("systemMgr.roleMgr.updateRole")
    public void updateRole(SysRole role, LoginUser loginUser) throws DuplicateException, NotExistException, BOException {
        sysRoleDao.delRoleAction(role.getId());
        sysRoleDao.deleteRoleById(role.getId());
        sysRoleDao.addRole(role);
        for(String actionid:role.getActions()){
            sysRoleDao.addActionToRole(actionid,role.getId());
        }
    }

    public SysRoleDAO getSysRoleDao() {
        return sysRoleDao;
    }

    public void setSysRoleDao(SysRoleDAO sysRoleDao) {
        this.sysRoleDao = sysRoleDao;
    }

    private boolean findRoleByName(String name) {
        SysRole sr = sysRoleDao.getSysRoleByName(name);
        return sr != null;
    }

    private boolean findRoleById(String roleid) {
        SysRole sr = sysRoleDao.getSysRoleById(roleid);
        return sr != null;
    }
    private Map<String,Object> wrapQueryCondition(SysRoleQueryCondition condition,LoginUser loginUser){
        java.util.Map<String, Object> paramMap = new java.util.HashMap<String, Object>();
        if(condition != null){
            if(!Utils.isBlank(condition.getRolename())){
                paramMap.put("rolename", condition.getRolename());
            }
            if(!Utils.isBlank(condition.getRoleDesc())){
                paramMap.put("roleDesc", condition.getRoleDesc());
            }
        }
        if(loginUser != null){
            if(!loginUser.getSysUser().getId().equals(CoreConstants.ADMIN_ID)){
                paramMap.put("ownerUID",loginUser.getRole().getOwnerUserId());
            }
        }
        return paramMap;
    }
}
