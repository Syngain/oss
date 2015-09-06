package com.guanhuodata.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.guanhuodata.framework.core.CoreConstants;

public class AjaxSessionTimeoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AjaxSessionTimeoutServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session != null){
			Object o = session.getAttribute(CoreConstants.LOGIN_USER);
			if( o == null){
				response.setHeader("session_Timeout", "1");
			}else{
				response.setHeader("session_Timeout", "0");
			}
		}
	}

}
