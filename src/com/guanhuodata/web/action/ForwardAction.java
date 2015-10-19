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
		}else if("scheduleConvertPage".equals(type)){
			scheduleConvertPage(request,response);
		}else if("summarySheetPage".equals(type)){
			summarySheetPage(request,response);
		}else if("forwardToMainPage".equals(type)){
			forwardToMainPage(request,response);
		}else if("readFileNameInfoPage".equals(type)){
			readFileNameInfoPage(request,response);
		}
	}
	
	private void readFileNameInfoPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to read material image file name page.");
		request.getRequestDispatcher("pages/utils/uploadifyGetFileName.html").forward(request, response);
	}

	private void forwardToMainPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to main page.");
		request.getRequestDispatcher("pages/main/main.html").forward(request, response);
	}

	//报表汇总(定向、创意)
	private void summarySheetPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to summary sheet page.");
		request.getRequestDispatcher("pages/summarysheet/summarysheet.html").forward(request, response);
	}

	//计划报表转换工具
	private void scheduleConvertPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to schedule convert util page.");
		request.getRequestDispatcher("pages/utils/scheduleConvertPage.html").forward(request, response);
	}

	//批量上传
	private void uploadifyPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to uploadify image Page.");
		request.getRequestDispatcher("pages/uploadify/uploadifyPage.html").forward(request, response);
	}

	//素材
	private void photoMaterialPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to photoMaterialPage.");
		response.setCharacterEncoding("text/html;chartset=UTF-8");
		request.getRequestDispatcher("pages/photoManager/photoMaterial.html").forward(request, response);
	}

	//素材图片预览
	private void preViewChartDataPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to preViewChartDataPage.");
		request.getRequestDispatcher("pages/chartImportAndExport/preViewChartData.html").forward(request, response);
	}

	//报表导入导出
	private void chartImpAndExpPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to chartPerspectiveAndViewList page.");
		request.getRequestDispatcher("pages/chartImportAndExport/chartImportAndExport.html").forward(request, response);
	}

	//登陆跳转
	private void enterCheck(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("ForwardAction to limit illegal user enter this project.");
		request.getRequestDispatcher("login.html").forward(request, response);
	}

	//报表透视
	private void chartPerspectivePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("forward to chartPerspectiveAndViewList page.");
		request.getRequestDispatcher("pages/chartPerspective/chartPerspectiveAndViewList.html").forward(request, response);
	}
}
