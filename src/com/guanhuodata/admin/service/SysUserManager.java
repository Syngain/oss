package com.guanhuodata.admin.service;

import java.util.List;
import java.util.Map;
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.admin.dao.SysUser;
import com.guanhuodata.admin.dao.SysUserQueryCondition;
import com.guanhuodata.admin.exception.LoginException;
import com.guanhuodata.framework.exception.BOException;
import com.guanhuodata.framework.exception.DuplicateException;
import com.guanhuodata.framework.exception.NotExistException;
import com.guanhuodata.framework.util.Page;

public interface SysUserManager {
	public List<SysUser> getPagedSysUser(SysUserQueryCondition condition,Page page,LoginUser loginUser);
	public int getPagedSysUserCounts(SysUserQueryCondition condition,LoginUser loginUser);
	public List<SysUser> getAllUsers();
	public SysUser getSysUserById(String id,LoginUser loginUser) throws BOException;
	public void delSysUserById(String[] ids,LoginUser loginUser) throws BOException;
	public void addSysUser(SysUser user,String roleid,LoginUser loginUser) throws DuplicateException,BOException;
	public void updateSysUser(SysUser user,LoginUser loginUser) throws DuplicateException,NotExistException,BOException;
	public void updateSysUserPassword(SysUser user,LoginUser loginUser) throws NotExistException;
	public LoginUser login(String username,String password) throws LoginException,BOException;
	public LoginUser login(String username,String password,Map<String,Object>info) throws LoginException,BOException;
	public List<SysUser> getAllUsersByRoleId(String string);
}
