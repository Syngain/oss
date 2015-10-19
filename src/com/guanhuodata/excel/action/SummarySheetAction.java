package com.guanhuodata.excel.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.guanhuodata.excel.service.ISummarySheetService;
import com.guanhuodata.framework.core.Action;

/**
 * 
 * @author 报表分析 新定义的拆分方法以及数据导出格式
 *
 */
public class SummarySheetAction implements Action {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SummarySheetAction.class);

	private ISummarySheetService summarySheetService;

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("process excel doc_new Start.");
		String type = request.getParameter("type");
		if ("downloadDirectChart".equals(type)) {
			downloadDirectChart(request, response);
		} else if ("uploadDirectSheet".equals(type)) {
			uploadDirectSheet(request, response);
		} else if ("uploadOriginalitySheet".equals(type)) {
			uploadOriginalitySheet(request, response);
		}
	}

	//
	private void uploadOriginalitySheet(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		LOG.info("upload originality sheet doc start.");
		// 设置编码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charSet=UTF-8");
		HttpSession session = request.getSession();
		PrintWriter pw = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		String path = request.getServletContext().getRealPath("/");
		factory.setSizeThreshold(1024 * 1024);
		factory.setRepository(new File(path));
		// 高水平的API文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("utf-8");
		// 上传文件大小限制
		upload.setSizeMax(10 * 1024 * 1024);
		try {
			pw = response.getWriter();
			// 可上传多个文件
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				// 获取表单的属性名字
				// String name = item.getFieldName();
				// 如果获取的表单信息是普通的文本信息
				if (item.isFormField()) {
					LOG.info("Upload File is a formField.");
					// 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
					// String value = item.getString();
				}
				// 对传入的非简单的字符串进行处理,比如:二进制的图片、电影等
				else {
					/**
					 * 以下三部主要是获取上传文件的名称
					 */
					String fileName = item.getName();
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);// 获取文件名（不包括路径）
					System.out.println("fileName:" + fileName);
					System.out.println("path:" + path);
					// 限制上传文件类型为Excel报表文件
					if (!(".xls".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length())))
							&& !(".xlsx".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length())))) {
						LOG.error("Upload originality sheet is not Excel.");
						pw.print("-1");// 文件不是Excel
					} else {
						// 将文件写入服务器根目录
						// 真正将上传文件写到磁盘上
						String paths = "d:" + File.separator + "temp" + File.separator;
						File filePath = new File(paths + fileName);
						item.write(filePath);// 第三方提供的
						//先解析看看上传的是否是创意报表(根据表头来判断)
						String sheetType = "创意名称";
						if(summarySheetService.isOriginalitySheetStyle(filePath,sheetType)){
							//解析入库
							summarySheetService.readAndInsertToDB(filePath);
							session.setAttribute("originalitySheetName", fileName);
							pw.print("0");
						}else{
							File deleteFile = new File(paths + fileName);
							deleteFile.delete();
							pw.print("-3");
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Upload originality sheet has an Exception: " + e.getLocalizedMessage());
			pw.print("-2");
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
	}

	//上传和入库定向报表
	private void uploadDirectSheet(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("upload direct sheet doc start.");
		// 设置编码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charSet=UTF-8");
		HttpSession session = request.getSession();
		PrintWriter pw = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		String path = request.getServletContext().getRealPath("/");
		factory.setSizeThreshold(1024 * 1024);
		factory.setRepository(new File(path));
		// 高水平的API文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("utf-8");
		// 上传文件大小限制
		upload.setSizeMax(10 * 1024 * 1024);
		try {
			pw = response.getWriter();
			// 可上传多个文件
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				// 获取表单的属性名字
				// String name = item.getFieldName();
				// 如果获取的表单信息是普通的文本信息
				if (item.isFormField()) {
					LOG.info("Upload File is a formField.");
					// 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
					// String value = item.getString();
				}
				// 对传入的非简单的字符串进行处理,比如:二进制的图片、电影等
				else {
					/**
					 * 以下三部主要是获取上传文件的名称
					 */
					String fileName = item.getName();
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);// 获取文件名（不包括路径）
					System.out.println("fileName:" + fileName);
					System.out.println("path:" + path);
					// 限制上传文件类型为Excel报表文件
					if (!(".xls".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length())))
							&& !(".xlsx".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length())))) {
						LOG.error("Upload direct sheet is not Excel.");
						pw.print("-1");// 文件不是Excel
					} else {
						// 将文件写入服务器根目录
						// 真正将上传文件写到磁盘上
						String paths = "d:" + File.separator + "temp" + File.separator;
						File filePath = new File(paths + fileName);
						item.write(filePath);// 第三方提供的
						//先解析看看上传的是否是创意报表(根据表头来判断)
						String sheetType = "定向名称";
						if(summarySheetService.isOriginalitySheetStyle(filePath,sheetType)){
							boolean isInsertToDB = summarySheetService.readAndInsertToDB(filePath);
							if(isInsertToDB){
								session.setAttribute("directSheetName", fileName);
								pw.print("0");
							}else{
								File deleteFile = new File(paths + fileName);
								deleteFile.delete();
								pw.print("-4");
							}
						}else{
							File deleteFile = new File(paths + fileName);
							deleteFile.delete();
							pw.print("-3");
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Upload direct sheet has an Exception: " + e.getLocalizedMessage());
			pw.print("-2");
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
	}

	//根据选择表头下载定向报表
	private void downloadDirectChart(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("read and export direct or originality doc start.");
		/*Enumeration<String> params = request.getParameterNames();
		while(params.hasMoreElements()){
			String param = params.nextElement();
			String paramValue = request.getParameter(param);
			System.out.println(paramValue);
		}*/
		request.setCharacterEncoding("UTF-8");
		String[] params = request.getParameter("paramPoly").split(",");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String downloadName = URLEncoder.encode("自用素材分析报表_" + sdf.format(new Date()) + ".xls");
		String downloadName = new String(("定向报表汇总" + sdf.format(new Date()) + ".xls").getBytes(),"ISO8859-1");
		response.addHeader("Content-Disposition","attachment;filename=\"" + downloadName + "\"");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			summarySheetService.summaryByCondition(params,os);
		} catch (IOException e) {
			LOG.error("download direct sheet has an IOException: " +e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			try{
				os.close();
			}catch(IOException e){
				LOG.error("close outputStream has an IOException: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
		}
	}

	public ISummarySheetService getSummarySheetService() {
		return summarySheetService;
	}

	public void setSummarySheetService(ISummarySheetService summarySheetService) {
		this.summarySheetService = summarySheetService;
	}

}
