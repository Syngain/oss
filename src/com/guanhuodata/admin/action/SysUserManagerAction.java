package com.guanhuodata.admin.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.admin.PermissionControlException;
import com.guanhuodata.admin.dao.SysUser;
import com.guanhuodata.admin.dao.SysUserQueryCondition;
import com.guanhuodata.admin.service.SysRoleManager;
import com.guanhuodata.admin.service.SysUserManager;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.exception.BOException;
import com.guanhuodata.framework.exception.DuplicateException;
import com.guanhuodata.framework.exception.NotExistException;
import com.guanhuodata.framework.log.CtomsLoger;
import com.guanhuodata.framework.log.CtomsLoggerBean;
import com.guanhuodata.framework.log.CtomsLoggerType;
import com.guanhuodata.framework.log.LogLevel;
import com.guanhuodata.framework.log.Logger;
import com.guanhuodata.framework.util.DESecb;
import com.guanhuodata.framework.util.IDGenerator;
import com.guanhuodata.framework.util.JsonUtil;
import com.guanhuodata.framework.util.Page;
import com.guanhuodata.framework.util.Utils;

public class SysUserManagerAction implements Action {

    SysRoleManager srm ;
    SysUserManager sum ;
    private String prKey;
    private CtomsLoger ctomsLogger;
    public void setPrKey(String prKey) {
		this.prKey = prKey;
	}

	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqType = req.getParameter("aid");
        if (reqType.equals("save")) {
            save(req, resp);
        } else if (reqType.equals("showtb")) {
            showAllAdmin(req, resp);
        } else if (reqType.equals("showDetail")) {
            showSysUserDetail(req, resp);
        } else if (reqType.equals("del")) {
            delAdmin(req, resp);
        } else if (reqType.equals("update")) {
            updateAdmin(req, resp);
        } else if (reqType.equals("updatePassword")){
        	updatePassword(req, resp);
        }
    }
    
    private void showSysUserDetail(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String userid = req.getParameter("userid");
        PrintWriter out = response.getWriter();
        SysUser su = null;
        LoginUser lu = null;
        String userLastLoginLocation="未知";
        long userLastLoginTime = 0L;
        String userLoginTimeStr = "未知";
        try {
            lu = (LoginUser)req.getSession().getAttribute(CoreConstants.LOGIN_USER);
            if (userid == null) {
            	userid = lu.getSysUser().getId(); 
            }
            su = sum.getSysUserById(userid, lu);
            if(!lu.getSysUser().getId().equals(su.getId())){
                userLastLoginLocation = su.getLastLoginLocation();
                userLastLoginTime = su.getLastLoginTime();
            }else{
                userLastLoginLocation = lu.getLastLoginLocation();
                userLastLoginTime = lu.getLastLoginTime();
            }
            if(userLastLoginTime!=0L){
                userLoginTimeStr = Utils.timeToDateString(userLastLoginTime);
            }
        } catch (BOException boe) {
            out.print("没有权限执行此操作");
            out.close();
            return;
        }
        /*if (userid == null) {
            return;
        }*/
        String[] keys = new String[]{"id", "username", "password", "status", "lastLoginTime",
            "lastLoginLocation", "retryTime", "failDelay", "fullname", "email",
            "telephone", "desc", "roleid"};
        Object[] values = new Object[]{su.getId(), su.getUsername(), su.getPassword(),
            su.getStatus(), userLoginTimeStr,
            userLastLoginLocation, su.getRetryTime(),
            su.getFailDelay(), su.getFullname(), su.getEmail(), su.getTelephone(),
            su.getDesc(), su.getRoleId()};
        String json = JsonUtil.simpleTranslateEntityToJSON(keys, values);
        out.print(json);
        out.close();

    }

    private void save(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        //message:
        //0:成功
        //1:用户名不能为空
        //2:密码不能为空
        //10:用户已经存在
    	//11:密码不一致
    	
    	CtomsLoggerBean clb = new CtomsLoggerBean();
        clb.setTime(new Date());
        clb.setAction(CtomsLoggerType.ADD_OPERATION);
        LoginUser lu = (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER);
        if(lu!=null){
        	clb.setOperator(lu.getSysUser().getUsername());
        }else{
        	clb.setOperator("not login");
        }
        clb.setOperateobject("用户管理");
        PrintWriter out = response.getWriter();
        SysUser su = new SysUser();
        su.setId(IDGenerator.generateId());
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        clb.setDescription("添加用户:"+username);
        String confirmPasswd = req.getParameter("confirmpasswd");
        if(!password.equals(confirmPasswd)){
        	
        	clb.setDetail("password != confirmpasswd");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("11");
            return;
        }
        String status = req.getParameter("status");
        String loginRetryTime = req.getParameter("retryTime");
        if (loginRetryTime == null || loginRetryTime.trim().equals("")) {
            loginRetryTime = "0";
        }
        String loginFailDelay = req.getParameter("failDelay");
        if (loginFailDelay == null || loginFailDelay.trim().equals("")) {
            loginFailDelay = "0";
        }
        String roleid = req.getParameter("roleid");
        if (username == null || username.trim().length() == 0) {
        	
        	clb.setDetail("username is null!");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
        	
            out.print("1");
            return;
        }
        if (password == null || password.trim().length() == 0) {
        	clb.setDetail("password is null!");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
        	
            out.print("2");
            return;
        }
        su.setUsername(username);
        //密码加密
        if (password != null && !"".equals(password)) {
			DESecb ecb = new DESecb();
			byte[] k = null;
			byte data[] = password.getBytes();
			ecb.setPrKey(prKey);
			password = ecb.encrypt(k, data);
		}
        
        su.setPassword(password);
        su.setStatus(Integer.parseInt(status));
        su.setRetryTime(Integer.parseInt(loginRetryTime));
        su.setFailDelay(Integer.parseInt(loginFailDelay));
        su.setFullname(req.getParameter("fullname"));
        su.setEmail(req.getParameter("email"));
        su.setTelephone(req.getParameter("tel"));
        su.setDesc(req.getParameter("desc"));
        try {
            sum.addSysUser(su, roleid, (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER));
            
            clb.setDetail("succes!");
        	clb.setResult(CtomsLoggerType.SUCCES_OPERATION);
        	ctomsLogger.addLogger(clb);
        	
            out.print("0");
            out.flush();
        } catch (DuplicateException e) {
        	clb.setDetail("user is have");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
        	
            out.print("9");
            out.flush();
        } catch (BOException boe) {
            if (boe instanceof PermissionControlException) {
            	clb.setDetail("没有权限执行此操作");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomsLogger.addLogger(clb);
                out.print("99");
                out.flush();
            }else {
            	out.print("10");
            }
        }
        out.close();
    }

    private void showAllAdmin(HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            int size = Integer.parseInt(request.getParameter("size"));
            int startid = ((Integer.parseInt(request.getParameter("startid"))-1)*size);
            out = response.getWriter();
            SysUserQueryCondition condition = new SysUserQueryCondition();
            if(request.getParameter("searchSysUserName")!=null && !request.getParameter("searchSysUserName").trim().equals("")){
                condition.setUsername(request.getParameter("searchSysUserName").trim());
            }
            if(request.getParameter("searchSysUserStatus")!=null && !request.getParameter("searchSysUserStatus").equals("-1")){
                condition.setStatus(Integer.parseInt((request.getParameter("searchSysUserStatus").trim())));
            }
            if(request.getParameter("searchSysUserRole")!= null && !request.getParameter("searchSysUserRole").equals("-1")){
                condition.setRole(request.getParameter("searchSysUserRole"));
            }
            if(request.getParameter("searchSysUserDesc")!=null && !request.getParameter("searchSysUserDesc").trim().equals("")){
                condition.setUserDesc(request.getParameter("searchSysUserDesc").trim());
            }
            Page p = new Page();
            p.setPageSize(size);
            p.setStartPage(startid);
            java.util.List<SysUser> suList = sum.getPagedSysUser(condition,p,(LoginUser)request.getSession().getAttribute(CoreConstants.LOGIN_USER));
            int total = sum.getPagedSysUserCounts(condition, (LoginUser)request.getSession().getAttribute(CoreConstants.LOGIN_USER));//查出总页数给页面
            String jsonStr = JsonUtil.makeListJsonCounts(suList,total);
            out.print(jsonStr);
            out.close();
        } catch (Exception ex) {
            Logger.getLogger(SysUserManagerAction.class).log(LogLevel.ERROR,ex.getMessage());
        } finally {
            out.close();
        }
    }

    private void delAdmin(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        String[] selUserIDs = req.getParameterValues("uid");
        
        CtomsLoggerBean clb = new CtomsLoggerBean();
        clb.setTime(new Date());
        clb.setAction(CtomsLoggerType.DELETE_OPERATION);
        LoginUser lu = (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER);
        if(lu!=null){
        	clb.setOperator(lu.getSysUser().getUsername());
        }else{
        	clb.setOperator("not login");
        }
        clb.setOperateobject("用户管理");
        
        clb.setDescription("删除用户:"+selUserIDs);
        
        if (selUserIDs == null) {
        	
        	clb.setDetail("delete userid is null");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
        	
            out.print("1");
            out.flush();
            out.close();
            return;
        }
        try {
            sum.delSysUserById(selUserIDs,lu);
            
            clb.setDetail("success!");
        	clb.setResult(CtomsLoggerType.SUCCES_OPERATION);
        	ctomsLogger.addLogger(clb);

        	out.print("0");
        } catch (BOException boe) {
            if (boe instanceof PermissionControlException) {
            	clb.setDetail("没有权限执行此操作");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomsLogger.addLogger(clb);
                out.print("99");
                out.flush();
            }else if(boe.getMessage().equals("error.del.build-in-admin")){
            	clb.setDetail("不能删除系统内置管理员");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomsLogger.addLogger(clb);
                out.print("9");
                out.flush();
            }else{
            	clb.setDetail(boe.getMessage());
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomsLogger.addLogger(clb);
                out.print("10");
                out.flush();
            }
        } finally {
            out.close();
        }
    }

    private void updateAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        String userid = req.getParameter("uid");
        SysUser su;
        
        CtomsLoggerBean clb = new CtomsLoggerBean();
        clb.setTime(new Date());
        clb.setAction(CtomsLoggerType.UPDATE_OPERATION);
        LoginUser lu = (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER);
        if(lu!=null){
        	clb.setOperator(lu.getSysUser().getUsername());
        }else{
        	clb.setOperator("not login");
        }
        clb.setOperateobject("用户管理");
        
        try {
            su = sum.getSysUserById(userid, (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER));
        } catch (BOException boe) {
            if (boe instanceof PermissionControlException) {
            	clb.setDetail("没有权限执行此操作");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomsLogger.addLogger(clb);
            	
                out.print("99");
                out.flush();
            }
            return;
        }
        if (su == null) {
        	clb.setDetail("更新帐号不存在");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.write("2");//更新帐号不存在
            out.flush();
            return;
        }
        String username = req.getParameter("username");
        String roleid = req.getParameter("roleid");
        String rolename = req.getParameter("rolename");
        String status = req.getParameter("status");
        String password = req.getParameter("password");
        String confirmPasswd = req.getParameter("confirmpasswd");
        clb.setDescription("修改该用户："+su.getUsername()+" to " + username);
        
        if(!password.equals(confirmPasswd)){
        	clb.setDetail("password != confirmpasswd");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.print("11");
            out.flush();
            return;
        }
        String retryTime = req.getParameter("retryTime");
        if (retryTime == null || retryTime.trim().equals("")) {
            retryTime = "0";
        }
        String failDelay = req.getParameter("failDelay");
        if (failDelay == null || failDelay.trim().equals("")) {
            failDelay = "0";
        }
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String tel = req.getParameter("tel");
        String desc = req.getParameter("desc");
        su.setId(userid);
        su.setRoleId(roleid);
        su.setRolename(rolename);
        su.setUsername(username);
        su.setPassword(password);
        su.setStatus(Integer.parseInt(status));
        su.setRetryTime(Integer.parseInt(retryTime));
        su.setFailDelay(Integer.parseInt(failDelay));
        su.setFullname(fullname);
        su.setEmail(email);
        su.setTelephone(tel);
        su.setDesc(desc);
        try {
            sum.updateSysUser(su, (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER));
            clb.setDetail("seccess");
        	clb.setResult(CtomsLoggerType.SUCCES_OPERATION);
        	ctomsLogger.addLogger(clb);
            out.write("0");//成功
            out.flush();
        } catch (DuplicateException e) {
        	clb.setDetail("name is have ");
         	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
         	ctomsLogger.addLogger(clb);
            out.write("1");//重名错误
            out.flush();
        } catch (NotExistException e) {
        	clb.setDetail("更新帐号不存在");
         	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
         	ctomsLogger.addLogger(clb);
            out.write("2");//更新帐号不存在
            out.flush();
        } catch (BOException boe) {
            if (boe instanceof PermissionControlException) {
            	clb.setDetail("没有权限执行此操作");
             	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
             	ctomsLogger.addLogger(clb);
                out.print("99");
                out.flush();
            }else{
            	out.print("10");
            }
        } finally {
            out.close();
        }
    }
    private void updatePassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	//修改登录者的密码
    	PrintWriter out = resp.getWriter();
    	LoginUser su ;
    	
    	CtomsLoggerBean clb = new CtomsLoggerBean();
        clb.setTime(new Date());
        clb.setAction(CtomsLoggerType.UPDATE_OPERATION);
        LoginUser lu = (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER);
        if(lu!=null){
        	clb.setOperator(lu.getSysUser().getUsername());
        }else{
        	clb.setOperator("not login");
        }
        clb.setOperateobject("用户管理");
    	
        String password = req.getParameter("password");
        try {
            su =  (LoginUser) req.getSession().getAttribute(CoreConstants.LOGIN_USER);
        } catch (Exception boe) {
        	
        	clb.setDetail("没有权限执行此操作");
         	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
         	ctomsLogger.addLogger(clb);
        	
            out.print("没有权限执行此操作");
            out.flush();
            out.close();
            return;
        }
        if (su == null) {
        	
        	clb.setDetail("更新帐号不存在");
         	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
         	ctomsLogger.addLogger(clb);
        	
            out.write("更新帐号不存在");//更新帐号不存在
            out.flush();
            out.close();
            return;
        }
        //密码加密
        if (password != null && !"".equals(password)) {
			DESecb ecb = new DESecb();
			byte[] k = null;
			byte data[] = password.getBytes();
			ecb.setPrKey(prKey);
			password = ecb.encrypt(k, data);
		}
        clb.setDescription("更新密码:"+su.getSysUser().getUsername());
        SysUser syu = su.getSysUser();
        syu.setPassword(password);
        //更新数据库
        try {
			sum.updateSysUserPassword(syu, su);
		} catch (NotExistException e) {
			// TODO Auto-generated catch block
			clb.setDetail("更新帐号不存在");
         	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
         	ctomsLogger.addLogger(clb);
			 out.write("更新账号不存在！");//更新帐号不存在
	         out.flush();
	         out.close();
	         return;
		}
			//out.close();
        clb.setDetail("succes!");
     	clb.setResult(CtomsLoggerType.SUCCES_OPERATION);
     	ctomsLogger.addLogger(clb);
        out.write("0");//
        out.flush();
        out.close();
    }
    public SysRoleManager getSrm() {
        return srm;
    }

    public void setSrm(SysRoleManager srm) {
        this.srm = srm;
    }

    public SysUserManager getSum() {
        return sum;
    }

    public void setSum(SysUserManager sum) {
        this.sum = sum;
    }

	public void setCtomsLogger(CtomsLoger ctomsLogger) {
		this.ctomsLogger = ctomsLogger;
	}
    
}
