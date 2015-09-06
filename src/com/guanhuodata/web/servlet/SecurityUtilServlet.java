package com.guanhuodata.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.guanhuodata.framework.core.CoreConstants;

public class SecurityUtilServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public SecurityUtilServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		HttpSession session = request.getSession();
		UUID uuid = new UUID(0, Long.MAX_VALUE);
//		UUID uuid = new UUID(5,5);
		String randomKey = uuid.toString();
		session.setAttribute(CoreConstants.MD5_RANDOM_KEY,randomKey);
		PrintWriter out = response.getWriter();
		out.print(randomKey);
		out.flush();
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

}
