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
		}else if("chartPerspectivePage".equals(type)){
			chartPerspectivePage(request, response);
		}else if("chartImpAndExpPage".equals(type)){
			chartImpAndExpPage(request,response);
		}else if("preViewChartDataPage".equals(type)){
			preViewChartDataPage(request,response);
		}else if("photoMaterialPage".equals(type)){
			photoMaterialPage(request,response);
		}else if("uploadifyPage".equals(type)){
			uploadifyPage(request,response);
		}
	}
	
	private void uploadifyPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to uploadify image Page.");
		request.getRequestDispatcher("pages/uploadify/uploadifyPage.html").forward(request, response);
	}

	private void photoMaterialPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to photoMaterialPage.");
		request.getRequestDispatcher("pages/photoManager/photoMaterial.html").forward(request, response);
	}

	private void preViewChartDataPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to preViewChartDataPage.");
		request.getRequestDispatcher("pages/chartImportAndExport/preViewChartData.html").forward(request, response);
	}

	private void chartImpAndExpPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to chartPerspectiveAndViewList page.");
		request.getRequestDispatcher("pages/chartImportAndExport/chartImportAndExport.html").forward(request, response);
	}

	private void enterCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("ForwardAction to limit illegal user enter this project.");
		request.getRequestDispatcher("pages/main/main.html").forward(request, response);
	}

	private void chartPerspectivePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to chartPerspectiveAndViewList page.");
		request.getRequestDispatcher("pages/chartPerspective/chartPerspectiveAndViewList.html").forward(request, response);
	}
}
