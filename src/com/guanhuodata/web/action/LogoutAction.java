package com.guanhuodata.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.core.CoreConstants;

public class LogoutAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		if(session != null){
			LoginUser lu = (LoginUser)session.getAttribute("loginUser");
			if(lu != null){
				session.removeAttribute(CoreConstants.LOGIN_USER);
				session.invalidate();
				out.write("Logout");
			}
		}	
		out.flush();
		out.close();
	}
}
