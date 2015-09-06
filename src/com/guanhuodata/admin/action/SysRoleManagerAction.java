package com.guanhuodata.admin.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.admin.PermissionControl;
import com.guanhuodata.admin.PermissionControlException;
import com.guanhuodata.admin.dao.SysRole;
import com.guanhuodata.admin.dao.SysRoleQueryCondition;
import com.guanhuodata.admin.dao.SysUser;
import com.guanhuodata.admin.service.SysRoleManager;
import com.guanhuodata.admin.service.SysUserManager;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.core.Module;
import com.guanhuodata.framework.core.ModuleFunction;
import com.guanhuodata.framework.exception.BOException;
import com.guanhuodata.framework.exception.DuplicateException;
import com.guanhuodata.framework.exception.NotExistException;
import com.guanhuodata.framework.log.CtomsLoger;
import com.guanhuodata.framework.log.CtomsLoggerBean;
import com.guanhuodata.framework.log.CtomsLoggerType;
import com.guanhuodata.framework.util.IDGenerator;
import com.guanhuodata.framework.util.JsonUtil;
import com.guanhuodata.framework.util.Page;

public class SysRoleManagerAction implements Action {
    
    SysRoleManager srm ;
    SysUserManager sum ;
    private CtomsLoger ctomsLogger;
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqType = req.getParameter("aid");
        if (reqType.equals("getAppModules")) {
            getRoleModules(req,resp);
        } else if (reqType.equals("save")) {
            save(req, resp);
        } else if (reqType.equals("showtb")) {
            showAllRole(req, resp);
        } else if (reqType.equals("getallroles")) {
            listAllRole(req, resp);
        } else if (reqType.equals("showDetail")) {
            showRoleDetail(req, resp);
        } else if (reqType.equals("del")) {
            delRole(req, resp);
        } else if (reqType.equals("update")) {
            updateRole(req, resp);
        }
    }
    private void getRoleModules(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        LoginUser lu = (LoginUser)request.getSession().getAttribute(CoreConstants.LOGIN_USER);
        Collection<Module> moduleList = lu.getRole().getModules();
        PrintWriter out = response.getWriter();
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (Module module : moduleList) {
            createRoleData(module,sb);
            sb.append(",");
        }
        dropLastComma(sb);
        sb.append("]");
        out.print(sb.toString());
        out.close();
    }
    
    private void createRoleData(Module module,StringBuffer sb){
        sb.append("{");
        sb.append("\"moduleid\":").append("\"").append(module.getId()).append("\",").append("\"modulename\":").append("\"").append(module.getName()).append("\"");
        List<ModuleFunction> mfs = module.getFunctions();
        if (mfs != null && !mfs.isEmpty()) {
            sb.append(",\"actions\":").append("[");
            for (ModuleFunction mf : mfs) {
                sb.append("{");
                sb.append("\"actionid\":").append("\"").append(mf.getId()).append("\",");
                sb.append("\"actionname\":").append("\"").append(mf.getName()).append("\",");
                sb.append("\"actiontype\":").append("\"").append(mf.getType()).append("\",");
                sb.append("\"licenseid\":").append("\"").append(mf.getLicenseId()).append("\"");
                sb.append("},");
            }
            dropLastComma(sb);
            sb.append("]");
        }
        List<Module> moduleList = module.getModuleList();
        if(moduleList != null && !moduleList.isEmpty()){
            sb.append(",\"modules\":[");
            for(Module m: moduleList){
                createRoleData(m,sb);
                sb.append(",");
            }
            dropLastComma(sb);
            sb.append("]");
        }
        sb.append("}");
    }
    @PermissionControl("systemMgr.roleMgr.addRole")
    private void save(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
    	
    	CtomsLoggerBean clb = new CtomsLoggerBean();
        clb.setTime(new Date());
        clb.setAction(CtomsLoggerType.ADD_OPERATION);
        LoginUser lu = (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER);
        if(lu!=null){
        	clb.setOperator(lu.getSysUser().getUsername());
        }else{
        	clb.setOperator("not login");
        }
        clb.setOperateobject("角色管理");
       
        SysRole sr = new SysRole();
        sr.setId(IDGenerator.generateId());
        String rolename = req.getParameter("rolename");
        clb.setDescription("添加角色："+rolename);
        String roleDesc = req.getParameter("desc");
        sr.setName(rolename);
        sr.setDescription(roleDesc == null ? "" : roleDesc);
        String[] actionids = req.getParameterValues("action_id");
        
        //create ModuleFunction
        List<String> mfs = new ArrayList<String>();
        if(actionids != null){
            for (String aid : actionids) {
               mfs.add(aid);
            }
        }
        sr.setActions(mfs);
        PrintWriter out = response.getWriter();
        if (rolename == null || rolename.trim().equals("")) {
        	clb.setDetail("Role name can not be empty");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("1");//Role name can not be empty.
            out.close();
            return;
        }
        try {
            srm.addRole(sr,lu);
            clb.setDetail("Success");
        	clb.setResult(CtomsLoggerType.SUCCES_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("0");//Success
        } catch (DuplicateException ex) {
        	clb.setDetail("DuplicateException");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("9");
        } catch (BOException boe) {
            if(boe instanceof PermissionControlException){
            	clb.setDetail("没有权限执行此操作");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomsLogger.addLogger(clb);
                out.print("99");
            }else{
            	out.print("10");
            }
        } finally {
            out.close();
        }
    }

    private void delRole(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String[] roleids = req.getParameterValues("roleid");
        CtomsLoggerBean clb = new CtomsLoggerBean();
        clb.setTime(new Date());
        clb.setAction(CtomsLoggerType.DELETE_OPERATION);
        LoginUser lu = (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER);
        if(lu!=null){
        	clb.setOperator(lu.getSysUser().getUsername());
        }else{
        	clb.setOperator("not login");
        }
        clb.setOperateobject("角色管理");
        clb.setDescription("删除角色id为："+roleids);
        if (roleids == null) {
        	clb.setDetail("No roleID");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("1");//No roleID
            out.close();
            return;
        }
        try{
            //TODO　根据rolesids获取userid;如果该角色下有用户，那么就提示登录用户，并不删除，如果没有用户，则删除
        	if(roleids!=null && roleids.length>0){
	        	List<SysUser> sysUserlist=sum.getAllUsersByRoleId(roleids[0]);
	        	if(sysUserlist !=null && sysUserlist.size()>0){
	        		out.print(3);//该角色下含有用户
	        		out.close();
	        		return ;
	        	}
        	}
            srm.delRoleById(roleids,lu);
            clb.setDetail("success");
        	clb.setResult(CtomsLoggerType.SUCCES_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("0");
        }catch(BOException boe){
            if(boe instanceof PermissionControlException){
            	clb.setDetail("没有权限执行此操作");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomsLogger.addLogger(clb);
                out.print("99");
            }else{
                if(boe.getMessage().equals("error.del.build-in-role")){
                	clb.setDetail("不能删除系统内置角色");
                	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
                	ctomsLogger.addLogger(clb);
                    out.print("2");
                }else{
                    out.print(boe.getMessage());
                }
            }
        } catch (NotExistException e) {
        	clb.setDetail("no role");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("1");
        }finally{
        	out.close();
        }
    }

    private void updateRole(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        String rolename = req.getParameter("rolename");
        String roleid = req.getParameter("roleid");
        String roleDesc = req.getParameter("desc");
        
        CtomsLoggerBean clb = new CtomsLoggerBean();
        clb.setTime(new Date());
        clb.setAction(CtomsLoggerType.UPDATE_OPERATION);
        LoginUser lu = (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER);
        if(lu!=null){
        	clb.setOperator(lu.getSysUser().getUsername());
        }else{
        	clb.setOperator("not login");
        }
        clb.setOperateobject("角色管理");
        
        clb.setDescription("更新角色：roleid："+roleid+"rolename:"+rolename+"roleDesc:"+roleDesc);
        
        if (roleid == null || roleid.equals("")) {
        	
        	clb.setDetail("No Role id .");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
        	
            out.print("4");//No Role id .
            out.close();
            return;
        }
        if (rolename == null || rolename.equals("")) {
        	
        	clb.setDetail("rolename is null");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("1");
            out.close();
            return;
        }
        SysRole sr = new SysRole();
        sr.setId(roleid);
        sr.setOwnerUserId(((LoginUser)req.getSession().getAttribute(CoreConstants.LOGIN_USER)).getSysUser().getId());
        sr.setName(rolename);
        sr.setDescription(roleDesc == null ? "" : roleDesc);
        String[] actionids = req.getParameterValues("action_id");

        //create Action
        List<String> al = new ArrayList<String>();
        if(actionids != null){
            for (String aid : actionids) {
                al.add(aid);
            }
        }
        sr.setActions(al);
        try {
            srm.updateRole(sr,(LoginUser)req.getSession().getAttribute(CoreConstants.LOGIN_USER));
            clb.setDetail("success");
        	clb.setResult(CtomsLoggerType.SUCCES_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("0");
        } catch (NotExistException nex) {
        	clb.setDetail("Destination Object does not exist, maybe has been deleted by others.");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("2");//Destination Object does not exist, maybe has been deleted by others.
        } catch (DuplicateException dex) {
        	clb.setDetail("Destination Object is already exist. logically if the two role's name identical, the two role will be treated equals.");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("3");//Destination Object is already exist. logically if the two role's name identical, the two role will be treated equals.
        } catch (BOException boe) {
            if(boe instanceof PermissionControlException){
            	clb.setDetail("没有权限执行此操作");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomsLogger.addLogger(clb);
                out.print("99");
            }
        } finally {
            out.close();
        }
    }

    private void showAllRole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        LoginUser lu = (LoginUser)request.getSession().getAttribute(CoreConstants.LOGIN_USER);
        int size = Integer.parseInt(request.getParameter("size")==null?"8":request.getParameter("size"));
        int startid = (Integer.parseInt(request.getParameter("startid")==null?"1":request.getParameter("startid")) - 1) * (size / 2);
        String rolename = request.getParameter("searchRoleName")==null?"":request.getParameter("searchRoleName");
        String roleDesc = request.getParameter("searchRoleDesc")==null?"":request.getParameter("searchRoleDesc");
        SysRoleQueryCondition condition = new SysRoleQueryCondition();
        condition.setRolename(rolename);
        condition.setRoleDesc(roleDesc);
        Page p = new Page();
        p.setPageSize(size);
        p.setStartPage(startid);
        java.util.List<SysRole> roleList = srm.getPagedSysRole(condition,p, lu);
        String jsonStr = JsonUtil.makeListJson(roleList);
        out.print(jsonStr);
        //System.out.println(jsonStr+"-----------------------------------");
        out.close();
    }

    private void showRoleDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String roleid = request.getParameter("roleid");
        if (roleid == null) {
            return;
        }
        SysRole role = null;
        try{
            role = srm.getSysRoleById(roleid);
        }catch(BOException boe){
            out.print("没有权限执行此操作");
            out.close();
            return;
        }
        List<String> actionList = role.getActions();
        
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"roleid\":").append("\"").append(role.getId()).append("\",");
        sb.append("\"rolename\":").append("\"").append(role.getName()).append("\",");
        sb.append("\"desc\":").append("\"").append(role.getDescription()).append("\",");
        if (actionList != null && !actionList.isEmpty()) {
            sb.append("\"actions\":[");
            for (String a : actionList) {
               sb.append("\"").append(a).append("\",");
            }
            
            dropLastComma(sb);
            sb.append("]");
        }else{
            dropLastComma(sb);
        }
        sb.append("}");
        out.print(sb.toString());
        out.close();
    }

    private void listAllRole(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginUser lu = (LoginUser)request.getSession().getAttribute(CoreConstants.LOGIN_USER);
        List<SysRole> roleList = null;
        if(lu.getSysUser().getId().equals(CoreConstants.ADMIN_ID)){
            roleList = srm.getAllSysRole(null,null);
        }else{
            roleList = srm.getAllSysRole(lu.getSysUser().getId(),lu.getRole().getOwnerUserId());
        }
        PrintWriter out = response.getWriter();
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"roles\":[");
        if (roleList != null) {
            for (SysRole role : roleList) {
                sb.append("{");
                sb.append("\"id\":").append("\"").append(role.getId()).append("\",");
                sb.append("\"name\":").append("\"").append(role.getName()).append("\",");
                sb.append("\"desc\":").append("\"").append(role.getDescription()).append("\"");
                sb.append("},");
            }
            dropLastComma(sb);
        }
        sb.append("]}");
        out.print(sb.toString());
        out.close();
    }
    
    private void dropLastComma(StringBuffer sb){
        if(sb.charAt(sb.length()-1)==','){
            sb.deleteCharAt(sb.length()-1);
        }
    }
    public SysRoleManager getSrm() {
        return srm;
    }
    public void setSrm(SysRoleManager srm) {
        this.srm = srm;
    }
	public void setCtomsLogger(CtomsLoger ctomsLogger) {
		this.ctomsLogger = ctomsLogger;
	}
	public void setSum(SysUserManager sum) {
		this.sum = sum;
	}
	
}
