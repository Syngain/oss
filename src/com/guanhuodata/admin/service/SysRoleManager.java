package com.guanhuodata.admin.service;

import java.util.List;
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.admin.dao.SysRole;
import com.guanhuodata.admin.dao.SysRoleQueryCondition;
import com.guanhuodata.framework.exception.BOException;
import com.guanhuodata.framework.exception.DuplicateException;
import com.guanhuodata.framework.exception.NotExistException;
import com.guanhuodata.framework.util.Page;

public interface SysRoleManager {
    public SysRole getSysRoleById(String roleid)  throws BOException;
    public void addRole(SysRole role,LoginUser loginUser) throws DuplicateException,BOException;
    public List<SysRole> getAllSysRole(String loginId,String ownerId);
    public List<SysRole> getPagedSysRole(SysRoleQueryCondition condition,Page page,LoginUser loginUser);
    public void delRoleById(String[] roleids,LoginUser loginUser) throws NotExistException,BOException;
    public void updateRole(SysRole role,LoginUser loginUser) throws DuplicateException,NotExistException,BOException;
}
