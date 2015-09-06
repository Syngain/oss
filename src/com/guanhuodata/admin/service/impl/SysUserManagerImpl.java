package com.guanhuodata.admin.service.impl;

import java.util.List;
import java.util.Map;
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.admin.PermissionControl;
import com.guanhuodata.admin.dao.SysRole;
import com.guanhuodata.admin.dao.SysUser;
import com.guanhuodata.admin.dao.SysUserDAO;
import com.guanhuodata.admin.dao.SysUserQueryCondition;
import com.guanhuodata.admin.exception.LoginException;
import com.guanhuodata.admin.service.SysRoleManager;
import com.guanhuodata.admin.service.SysUserManager;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.exception.BOException;
import com.guanhuodata.framework.exception.DuplicateException;
import com.guanhuodata.framework.exception.NotExistException;
import com.guanhuodata.framework.util.DESecb;
import com.guanhuodata.framework.util.Page;
import com.guanhuodata.framework.util.Utils;

public class SysUserManagerImpl implements SysUserManager {
    SysUserDAO sysUserDao;
    SysRoleManager roleManager;
    private String prKey;
    
    public void setPrKey(String prKey) {
		this.prKey = prKey;
	}

	public List<SysUser> getAllUsers() {
        return sysUserDao.getAllUser();
    }

    public LoginUser login(String username, String password) throws LoginException ,BOException{
        return login(username,password,null);
    }

    public LoginUser login(String username, String password, Map<String,Object> info) throws LoginException,BOException {
        SysUser su = sysUserDao.getUserByName(username);
        if (su == null) {
            return null;
        }
        boolean activedStatus = false;
        int retryTime = su.getFailedTimes();
        if (su.getStatus() == 0 && su.getFrozenTime() > 0) {
            long currentTime = System.currentTimeMillis();
            long delay = su.getFailDelay() * 1000 * 60;
            if ((currentTime - delay - su.getFrozenTime()) > 0) {
                su.setStatus(1);
                su.setFrozenTime(0L);
                su.setFailedTimes(0);
                sysUserDao.updateSysUser(su);
                retryTime = 0;
                activedStatus = true;
            }
        }
        if (su.getRetryTime() > 0 && !activedStatus) {
            if (retryTime - su.getRetryTime() >= 0) {
                su.setStatus(0);
                su.setFrozenTime(System.currentTimeMillis());
                sysUserDao.updateSysUser(su);
                throw new LoginException("error.retrytime.exceed");
            }
        }
        String passwordInDB = su.getPassword();
        String hashPasswd = passwordInDB;
        //des解密
        byte[] k = null;
		DESecb ecb = new DESecb();
		ecb.setPrKey(prKey);
		if (passwordInDB != null) {
			passwordInDB = ecb.decrypt(k, passwordInDB);
		}
		//System.out.println("hashPasswd!!!!"+passwordInDB);
        if(info != null){
        	if(info.get("randomKey") != null){
        		String md5RandomData = (String) info.get("randomKey");
        		hashPasswd = Utils.getMD5Str(passwordInDB.trim() + md5RandomData);
        	}else{
        		hashPasswd = Utils.getMD5Str(passwordInDB.trim());
        	}
        }
        //System.out.println("hashPasswd----"+hashPasswd);
       // System.out.println("password----"+password);
        if (!hashPasswd.equals(password)) {
            su.setFailedTimes(++retryTime);
            sysUserDao.updateSysUser(su);
            throw new LoginException("error.password.mismatch");
        }
        if (su.getStatus() == 0) {
            su.setFailedTimes(0);
            su.setFrozenTime(0);
            sysUserDao.updateSysUser(su);
            throw new LoginException("error.user.forbidden");
        }
        SysRole role = roleManager.getSysRoleById(su.getRoleId());
        if(role == null){
            throw new LoginException("error.user.status");
        }
        List<String> actionIds = role.getActions();
        LoginUser lu = new LoginUser();
        lu.setSysUser(su);
        lu.setLastLoginLocation(su.getLastLoginLocation());
        lu.setLastLoginTime(su.getLastLoginTime());
        lu.setRole(role);
        lu.setActions(actionIds);
        if (info != null) {
            String lastLoginLocation = (String) info.get("loginLocation") == null ? "" : (String) info
                    .get("loginLocation");
            long lastLoginTime = (Long) info.get("loginTime") == null ? 0 : (Long) info.get("loginTime");
            su.setLastLoginLocation(lastLoginLocation);
            su.setLastLoginTime(lastLoginTime);
            lu.setLoginLocation(lastLoginLocation);
            lu.setLoginTime(lastLoginTime);
        }
        su.setFailedTimes(0);
        su.setFrozenTime(0);
        sysUserDao.updateSysUser(su);
        // Log
      /*  CTOMSLog log = new CTOMSLog();
        log.setOperator(lu.getSysUser().getUsername());
        log.setOperationType(OperationType.LOGIN);
        log.setLogTime(System.currentTimeMillis());
        log.setOperatorIP(lu.getLoginLocation());
        log.setResult(LogResult.SUCCESS);
        log.setContent("-");
        Logger.getLogger(SysUserManagerImpl.class).record(log);*/
        // End of log
        return lu;
    }
    
    @PermissionControl("systemMgr.userMgr.addAdmin")
    public void addSysUser(SysUser user, String roleid, LoginUser loginUser) throws DuplicateException,BOException {
//        CTOMSLog log = new CTOMSLog();
//        log.setOperationType(OperationType.ADD);
//        log.setLogTime(System.currentTimeMillis());
//        log.setOperatorIP(loginUser.getLoginLocation());
//        log.setOperator(loginUser.getSysUser().getUsername());
        SysUser su = sysUserDao.getUserByName(user.getUsername());
        if (su != null) {
//            log.setContent("添加管理员：" + user.getUsername() + " 失败。原因：管理员名称已经存在");
//            log.setResult(LogResult.FAIL);
//            Logger.getLogger(SysUserManagerImpl.class).record(log);
            throw new DuplicateException("error.sys.user.exist");
        }
        user.setRoleId(roleid);
        user.setPid(loginUser.getSysUser().getId());
        sysUserDao.addSysUser(user);
//        log.setContent("添加管理员：" + user.getUsername() + " 成功。");
//        log.setResult(LogResult.SUCCESS);
//        Logger.getLogger(SysRoleManagerImpl.class).record(log);
    }

    @PermissionControl("systemMgr.userMgr.updateAdmin")
    public void updateSysUser(SysUser user, LoginUser loginUser) throws DuplicateException,
            NotExistException,BOException {
//        CTOMSLog log = new CTOMSLog();
//        log.setOperationType(OperationType.UPDATE);
//        log.setLogTime(System.currentTimeMillis());
//        log.setOperatorIP(loginUser.getLoginLocation());
//        log.setOperator(loginUser.getSysUser().getUsername());
        SysUser u1 = getSysUserById(user.getId(),loginUser);
        if (u1 == null) {
//            log.setContent("更新管理员：" + user.getUsername() + " 失败。原因：更新的管理员不存在");
//            log.setResult(LogResult.FAIL);
//            Logger.getLogger(SysUserManagerImpl.class).record(log);
            throw new NotExistException();
        }
        SysUser u = sysUserDao.findUpdateSysUser(user);
        if (u != null) {
//            log.setContent("更新管理员：" + u1.getUsername() + " 失败。原因：管理员名称 " + user.getUsername() + " 已经存在");
//            log.setResult(LogResult.FAIL);
//            Logger.getLogger(SysUserManagerImpl.class).record(log);
            throw new DuplicateException();
        }
        sysUserDao.updateSysUser(user);
        String updateContent = "";
        updateContent += "更新管理员：" + u1.getUsername() + " 成功。";
        if (!u1.getUsername().equals(user.getUsername())) {
            updateContent += "*管理员名称被更改 [" + u1.getUsername() + " --> " + user.getUsername() + "]*";
        }
        if (!u1.getPassword().equals(user.getPassword())) {
            updateContent += "*管理员密码被修改*";
        }
        if(!u1.getRoleId().equals(user.getRoleId())){
        	updateContent += "*管理员角色被修改 [ " + u1.getRolename() + " --> " + user.getRolename() + "]*";
        }
//        log.setResult(LogResult.SUCCESS);
//        log.setContent(updateContent);
//        Logger.getLogger(SysUserManagerImpl.class).record(log);
    }


    /**
     * 
     * @param startPage
     *            :start from index 0.
     * @param pageSize
     *            : the number a response contained from db.
     * @return
     */
    public List<SysUser> getPagedSysUser(SysUserQueryCondition condition,Page page,LoginUser loginUser) {
        Map<String,Object> paramMap = wrapQueryCondition(condition);
        if(!loginUser.getSysUser().getId().equals(CoreConstants.ADMIN_ID)){
//            paramMap.put("roleid",loginUser.getRole().getId());
            paramMap.put("ownerUID",loginUser.getSysUser().getId());
        }
        if(page != null){
            paramMap.put("startPage", page.getStartPage());
            paramMap.put("pageSize", page.getPageSize());
        }
        return sysUserDao.getPagedSysUser(paramMap);
    }

    public int getPagedSysUserCounts(SysUserQueryCondition condition,LoginUser loginUser){
    	   Map<String,Object> paramMap = wrapQueryCondition(condition);
           if(!loginUser.getSysUser().getId().equals(CoreConstants.ADMIN_ID)){
//               paramMap.put("roleid",loginUser.getRole().getId());
               paramMap.put("ownerUID",loginUser.getSysUser().getId());
           }
           return sysUserDao.getPagedSysUserCounts(paramMap);
    };
    
    
    public SysUser getSysUserById(String id, LoginUser loginUser) throws BOException{
        return sysUserDao.getUserById(id);
    }
    
    @PermissionControl("systemMgr.userMgr.delAdmin")
    public void delSysUserById(String[] ids, LoginUser loginUser) throws BOException{
        for(String id: ids){
            if(id.equals(CoreConstants.ADMIN_ID)){
                throw new BOException("error.del.build-in-admin");
            }
        }
//        CTOMSLog log = new CTOMSLog();
//        log.setOperationType(OperationType.DEL);
//        log.setLogTime(System.currentTimeMillis());
//        log.setOperatorIP(loginUser.getLoginLocation());
//        log.setOperator(loginUser.getSysUser().getUsername());
        StringBuffer sb = new StringBuffer();
        sb.append("删除管理员\t");
        for (String id : ids) {
            sb.append("[");
            sb.append(getSysUserById(id,loginUser).getUsername());
            sb.append("]");
            sb.append("\t");
            sysUserDao.delSysUserById(id);
        }
        sb.deleteCharAt(sb.length() - 1);
//        log.setContent(sb.toString());
//        log.setResult(LogResult.SUCCESS);
//        Logger.getLogger(SysUserManagerImpl.class).record(log);
    }
    
    public void updateSysUserPassword(SysUser user,LoginUser loginUser) throws NotExistException{
//        CTOMSLog log = new CTOMSLog();
//        log.setOperationType(OperationType.UPDATE);
//        log.setLogTime(System.currentTimeMillis());
//        log.setOperatorIP(loginUser.getLoginLocation());
//        log.setOperator(loginUser.getSysUser().getUsername());
        SysUser u1 = sysUserDao.getUserById(user.getId());
        if (u1 == null) {
//            log.setContent("管理员：" + user.getUsername() + " 修改密码失败。原因：更新的管理员不存在");
//            log.setResult(LogResult.FAIL);
//            Logger.getLogger(SysUserManagerImpl.class).record(log);
            throw new NotExistException();
        }
        sysUserDao.updateSysUser(user);
        String updateContent = "";
        updateContent += "管理员：" + u1.getUsername() + " 修改密码成功。";
//        log.setResult(LogResult.SUCCESS);
//        log.setContent(updateContent);
//        Logger.getLogger(SysUserManagerImpl.class).record(log);
    }
    
    private Map<String,Object> wrapQueryCondition(SysUserQueryCondition condition){
        java.util.Map<String, Object> paramMap = new java.util.HashMap<String, Object>();
        if(condition != null){
            if(!Utils.isBlank(condition.getUsername())){
                paramMap.put("username", condition.getUsername());
            }
            paramMap.put("status", condition.getStatus());
            if(!Utils.isBlank(condition.getRole())){
                paramMap.put("roleid",condition.getRole());
            }
        }
        return paramMap;
    }

    public SysUserDAO getSysUserDao() {
        return sysUserDao;
    }

    public void setSysUserDao(SysUserDAO sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    public SysRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(SysRoleManager roleManager) {
        this.roleManager = roleManager;
    }

	@Override
	public List<SysUser> getAllUsersByRoleId(String roleid) {
		// TODO Auto-generated method stub
		return sysUserDao.getAllUsersByRoleId(roleid);
	}
    
}
