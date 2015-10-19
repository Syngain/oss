package com.guanhuodata.admin.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.admin.exception.LoginException;
import com.guanhuodata.admin.service.SysUserManager;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.core.ServiceContext;
import com.guanhuodata.framework.exception.BOException;
import com.guanhuodata.framework.log.CtomsLoger;
import com.guanhuodata.framework.log.CtomsLoggerBean;
import com.guanhuodata.framework.log.CtomsLoggerType;
import com.guanhuodata.framework.util.PathProperty;

public class LoginAction implements Action {
	//private CtomsLoger ctomslogger;
	/*public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        long loginTime = System.currentTimeMillis();
        String loginLocation = request.getRemoteAddr();
        Map<String,Object> loginInfo = new HashMap<String,Object>();
        loginInfo.put("loginTime", loginTime);
        loginInfo.put("loginLocation", loginLocation);
        loginInfo.put("randomKey", request.getSession().getAttribute(CoreConstants.MD5_RANDOM_KEY));
        //Todo 验证附加码
        SysUserManager sm = ServiceContext.getInstance().getService("sysUserManager",SysUserManager.class);
        LoginUser lu = null;
        
        CtomsLoggerBean clb = new CtomsLoggerBean();
        clb.setTime(new Date());
        clb.setAction(CtomsLoggerType.Login_OPERATION);
        clb.setDescription("RemoteIpAddress :"+request.getRemoteAddr()+" 登录CTOMS");
        clb.setOperator(username);
        clb.setOperateobject("登录");
        try{
        	  //密码加密
            if (password != null && !"".equals(password)) {
    			DESecb ecb = new DESecb();
    			byte[] k = null;
    			byte data[] = password.getBytes();
    			ecb.setPrKey(prKey);
    			password = ecb.encrypt(k, data);
    		}
        	
            lu = sm.login(username, password,loginInfo);
        }catch(LoginException ex){
            if(ex.getMessage().equals("error.retrytime.exceed")){
            	clb.setDetail("error.retrytime.exceed");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomslogger.addLogger(clb);
                out.print("-3");
                return;
            }else if(ex.getMessage().equals("error.password.mismatch")){
            	clb.setDetail("error.password.mismatch");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomslogger.addLogger(clb);
                out.print("-2");
                return;
            }else if(ex.getMessage().equals("error.user.forbidden")){
            	clb.setDetail("error.user.forbidden");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomslogger.addLogger(clb);
                out.print("-4");
                return;
            }else if(ex.getMessage().equals("error.user.status")){
            	clb.setDetail("error.user.status");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomslogger.addLogger(clb);
                out.print("-6");
                return;
            }else{
            	clb.setDetail("return -5 error");
            	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
            	ctomslogger.addLogger(clb);
                out.print("-5");
                return;
            }
        } catch (BOException e) {
        	clb.setDetail("return -5 error");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomslogger.addLogger(clb);
            out.print("-5");
            return;
        }
        if(lu == null){
        	clb.setDetail("lu is null");
        	clb.setResult(CtomsLoggerType.FAIL_OPERATION);
        	ctomslogger.addLogger(clb);
            out.print("-1");
            return;
        }
        clb.setDetail("succes!");
    	clb.setResult(CtomsLoggerType.SUCCES_OPERATION);
    	ctomslogger.addLogger(clb);
        HttpSession s = request.getSession();
        s.setAttribute(CoreConstants.LOGIN_USER, lu);
        s.removeAttribute("loginFailTime");
        out.print("0");
        out.close();
    }*/
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String imgCode = request.getParameter("imgCode");
        long loginTime = System.currentTimeMillis();
        String loginLocation = request.getRemoteAddr();
        Map<String,Object> loginInfo = new HashMap<String,Object>();
        loginInfo.put("loginTime", loginTime);
        loginInfo.put("loginLocation", loginLocation);
        System.out.println("loginLocation:" + loginLocation);
        String[] usernameProperty = PathProperty.loadAttribute("usernameandpassword").split(",");
        boolean unameFlag = false;
        String unamePwd = "";
        for(String uname : usernameProperty){
        	if(uname.split("_")[0].equals(username)){
        		unamePwd = uname.split("_")[1];
        		unameFlag = true;
        		break;
        	}
        }
		if(unameFlag){
			if(password.equals(unamePwd)){
				if(session.getAttribute("imageCore").equals(imgCode)){
					out.print("0");	//验证通过
					//request.getRequestDispatcher("pages/main/main.html").forward(request, response);
				}else{
					out.print("-1");		//用户名密码正确,验证码不正确
					//request.getRequestDispatcher("login.html").forward(request, response);
				}
			}else{
				out.print("-2");
			}
		}else{
			out.print("-3");	//用户名/密码不正确
			//request.getRequestDispatcher("login.html").forward(request, response);
		}
	}
	
	/*public void setCtomslogger(CtomsLoger ctomslogger) {
		this.ctomslogger = ctomslogger;
	}*/
}
