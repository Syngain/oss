package com.guanhuodata.web.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.guanhuodata.framework.core.Action;

public class ForwardAction implements Action {
	
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ForwardAction.class);
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String type = request.getParameter("type");
		if("enterCheck".equals(type)){
			enterCheck(request,response);
		}
	}
	
	private void enterCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		LOG.info("ForwardAction to limit illegal user enter this project.");
		request.getServletContext().getRequestDispatcher("/pages/main/main.html").forward(request, response);
	}

}
