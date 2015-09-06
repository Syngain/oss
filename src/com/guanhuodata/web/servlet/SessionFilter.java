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
import com.guanhuodata.admin.LoginUser;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.log.LogLevel;
import com.guanhuodata.framework.log.Logger;

/**
 * Servlet Filter implementation class SessionFilter
 */
public class SessionFilter implements Filter {

    /**
     * Default constructor. 
     */
    public SessionFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		//判断session是否过期 过期的话移出session
		HttpServletRequest rqt = (HttpServletRequest)request;
		HttpServletResponse rps = (HttpServletResponse)response;
		//System.out.println(rqt.getRequestURI());
		if(rqt.getRequestURI().indexOf("/ctoms-web/mainFrame.html")!=-1){
			//rqt.removeAttribute(CoreConstants.LOGIN_USER);
			if((LoginUser)rqt.getSession().getAttribute(CoreConstants.LOGIN_USER) == null){
				rqt.removeAttribute(CoreConstants.LOGIN_USER);
				//Logger.getLogger(AAAGroupAction.class).log(LogLevel.DEBUG,  "Session is fait !!!");
				rps.sendRedirect(rqt.getContextPath());
			}
			
			if(rqt.getSession(false)==null){
				rqt.removeAttribute(CoreConstants.LOGIN_USER);
				//Logger.getLogger(AAAGroupAction.class).log(LogLevel.DEBUG,  "Session is fait !!!");
				rps.sendRedirect(rqt.getContextPath());
			}else{
				chain.doFilter(request, response);
			}
		}else{
	        	chain.doFilter(request, response);
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
