package com.guanhuodata.utilHandle.action;

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
import org.apache.log4j.Logger;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.util.PathProperty;
import com.guanhuodata.utilHandle.util.HandleUtil;

public class ScheduleConvertHandlerAction implements Action{

	private static final Logger LOG = Logger.getLogger(ScheduleConvertHandlerAction.class);

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("according type judge which method will be invoke start.");
		String type = request.getParameter("type");
		if("handleScheduleChart".equals(type)){
			handleScheduleChart(request,response);
		}else if("downloadConvertedChart".equals(type)){
			downloadConvertedChart(request,response);
		}else if("readFileNameInfo".equals(type)){
			readFileNameInfo(request,response);
		}else if("getFileNameInfoFromSession".equals(type)){
			getFileNameInfoFromSession(request,response);
		}
	}
	
	private void getFileNameInfoFromSession(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("get material name from session start.");
		// 设置编码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charSet=UTF-8");
		HttpSession session = request.getSession();
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if(session.getAttribute("fileNameInfo") != null){
				out.print("0");
			}else{
				LOG.info("get material name from session is null");
				out.print("-1");
			}
			out.close();
		} catch (IOException e) {
			LOG.error("get file name from session has an IOException: " + e.getLocalizedMessage());
			out.print("-2");
			e.printStackTrace();
		}
	}

	//读取素材图片名称
	private void readFileNameInfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("read material image's file name from zip file start.");
		// 设置编码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charSet=UTF-8");
		HttpSession session = request.getSession();
		PrintWriter pw = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		String path = request.getServletContext().getRealPath("/");
		factory.setSizeThreshold(50*1024 * 1024);
		factory.setRepository(new File(path));
		// 高水平的API文件上传处理
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("utf-8");
		// 上传文件大小限制
		upload.setSizeMax(100 * 1024 * 1024);
		try {
			pw = response.getWriter();
			// 可上传多个文件
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				// 获取表单的属性名字
				//String name = item.getFieldName();
				// 如果获取的表单信息是普通的文本信息
				if (item.isFormField()) {
					LOG.info("Upload File is a formField.");
					// 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
					//String value = item.getString();
				}
				// 对传入的非简单的字符串进行处理,比如:二进制的图片、电影等
				else {
					/**
					 * 以下三部主要是获取上传文件的名称
					 */
					String fileName = item.getName();
					//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);// 获取文件名（不包括路径）
					System.out.println("fileName:" + fileName);
					//System.out.println("path:" + path);
					// 限制上传文件类型为zip压缩包文件
					if (!(".jpg".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length())))) {
						LOG.error("Upload file is not jpg.");
						pw.print("-1");// 文件不是jpg
					} else {
						if(session.getAttribute("fileNameInfo") != null){
							String names = session.getAttribute("fileNameInfo") + "&" + fileName;
							session.setAttribute("fileNameInfo", names);
						}else{
							session.setAttribute("fileNameInfo", fileName);
						}
						pw.print("0");
					}
				}
			}
			pw.close();
		} catch (Exception e) {
			LOG.error("Upload schedule excel has an Exception: " + e.getLocalizedMessage());
			pw.print("-2");
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
	}

	//下载计划转换报表
	private void downloadConvertedChart(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("download converted chart start.");
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String fileName = (String)session.getAttribute("fileName");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String outputFileName = fileName.substring(0, fileName.length() - 1);
		outputFileName = outputFileName.substring(0, outputFileName.lastIndexOf(".")) + sdf.format(new Date()) + ".xls";
		String downloadName = new String(outputFileName.getBytes(),"ISO8859-1");
		response.addHeader("Content-Disposition","attachment;filename=\"" + downloadName + "\"");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		String[] headers = {"计划名称","单元名称","定向方式","定向名称","投放资源位","预算","溢价","创意名称","投放日期","投放方式","投放地域(去掉的)","投放时段(去掉的)"};
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String fileNameInfos = (String)session.getAttribute("fileNameInfo");
			new HandleUtil().readScheduleChartAndWriteToOutputStream(fileName, out,fileNameInfos,headers);
			out.close();
			File file = new File(PathProperty.loadAttribute("scheduleConvertUploadPath") + fileName);
			if(file.exists()){
				file.delete();
			}
			session.removeAttribute("filePath");
			session.removeAttribute("fileNameInfo");
		} catch (IOException e) {
			LOG.error("download converted chart has an IOException.");
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//上传计划报表
	private void handleScheduleChart(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("invoke read form file & handle then write to outputstream start.");
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
				//String name = item.getFieldName();
				// 如果获取的表单信息是普通的文本信息
				if (item.isFormField()) {
					LOG.info("Upload File is a formField.");
					// 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
					//String value = item.getString();
				}
				// 对传入的非简单的字符串进行处理,比如:二进制的图片、电影等
				else {
					/**
					 * 以下三部主要是获取上传文件的名称
					 */
					String fileName = item.getName();
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);// 获取文件名（不包括路径）
					//System.out.println("fileName:" + fileName);
					//System.out.println("path:" + path);
					// 限制上传文件类型为Excel报表文件
					if (!(".xls".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length()))) && !(".xlsx".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length())))) {
						LOG.error("Upload material excel is not Excel.");
						pw.print("-1");// 文件不是Excel
					} else {
						// 将文件写入服务器根目录
						// 真正将上传文件写到磁盘上
						//String paths = "d:" + File.separator + "temp" + File.separator;
						String paths = PathProperty.loadAttribute("scheduleConvertUploadPath");
						File filePath = new File(paths + fileName);
						item.write(filePath);// 第三方提供的
						session.setAttribute("fileName", fileName);
						pw.print("0");
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Upload schedule excel has an Exception: " + e.getLocalizedMessage());
			pw.print("-2");
			e.printStackTrace();
		} finally {
			pw.flush();
			pw.close();
		}
	}
}
