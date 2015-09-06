package com.guanhuodata.admin.dao;

import java.util.List;
import java.util.Map;

public interface SysRoleDAO {
	public SysRole getSysRoleById(String roleid);
	public SysRole getSysRoleByName(String rolename);
	public SysRole findUpdateSysRole(SysRole role);
//	public List<SysRole> getAllRole(@Param("loginID")String loginId,@Param("ownerId")String ownerId);
	public List<SysRole> getAllRole(String loginId,String ownerId);
	public List<SysRole> getPagedRole(Map<String,Object> paramMap);
	public void addRole(SysRole role);
	public void deleteRoleById(String roleid);
	public void deleteRoleByName(String rolename);
	public void updateRole(SysRole role);
//	public void addActionToRole(@Param("actionid") String actionid,@Param("roleid")String roleid);
	public void addActionToRole(String actionid,String roleid);
	public void delActionFromRole(String actionid,String roleid);
	public void delRoleAction(String roleid);
	public boolean findUsedSysRoleById(String roleid);
}
