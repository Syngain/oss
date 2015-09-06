package com.guanhuodata.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.util.JsonUtil;

public class LoginInfoAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			PrintWriter out = response.getWriter();
			if (request.getSession().getAttribute(CoreConstants.LOGIN_USER) == null) {
	            response.sendRedirect(request.getContextPath());
	        }
			
			LoginUser loginUser = (LoginUser) request.getSession(false).getAttribute(CoreConstants.LOGIN_USER);
			String result = JsonUtil.makeJson(loginUser);
			out.print(result);
			out.flush();
			out.close();
	}

}
