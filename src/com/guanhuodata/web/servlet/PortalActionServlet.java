package com.guanhuodata.web.servlet;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.core.ActionLookup;
import com.guanhuodata.framework.core.CoreConstants;
import com.guanhuodata.framework.core.ServiceContext;

public class PortalActionServlet extends HttpServlet {
	private static final long serialVersionUID = 4412304307988785498L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	    resp.setContentType("text/html;charset=utf-8");
		String actionid = req.getParameter(CoreConstants.ACTION_ID_PARAMETER);
		ServletContext sc = this.getServletContext();
		ServiceContext svrCtx = (ServiceContext) sc.getAttribute(CoreConstants.SERVICE_CTX);
		if(svrCtx == null) return;
		ActionLookup actionLookup = (ActionLookup) svrCtx.getService(CoreConstants.ACTION_LOOKUP_ID);
		Action action = actionLookup.getAction(actionid, svrCtx);
		if(action == null) return;
		action.execute(req, resp);
	}
}
