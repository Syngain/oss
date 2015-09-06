package com.guanhuodata.admin.dao;

import java.util.List;
import java.util.Map;

public interface SysUserDAO {
	public List<SysUser> getAllUser();
	public List<SysUser> getPagedSysUser(Map<String,Object> paramMap);
	public int getPagedSysUserCounts(Map<String,Object> paramMap);
	public SysUser getUserById(String userid);
	public SysUser getUserByName(String username);
	public SysUser findUpdateSysUser(SysUser user);
	public void addSysUser(SysUser sysuser);
	public void delSysUserById(String userid);
	public void updateSysUser(SysUser sysuser);
	public List<SysUser> getAllUsersByRoleId(String roleid);
}
