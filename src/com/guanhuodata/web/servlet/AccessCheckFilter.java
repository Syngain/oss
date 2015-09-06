package com.guanhuodata.web.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccessCheckFilter implements Filter{
	FilterConfig fc = null;
	
	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hsr = (HttpServletRequest)req;
		if(hsr.getRequestURI().endsWith("login.html")){
			chain.doFilter(hsr, resp);
			return;
		}
		HttpServletResponse response = (HttpServletResponse)resp;
		HttpSession session = hsr.getSession();
		if(session != null){
			Object o = session.getAttribute("loginUser");
			if(o == null){
				response.sendRedirect(fc.getServletContext().getContextPath()+"/login.html");
			}
		}else{
			response.sendRedirect(fc.getServletContext().getContextPath()+"/login.html");
		}
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig fc) throws ServletException {
		this.fc = fc;
	}

}
