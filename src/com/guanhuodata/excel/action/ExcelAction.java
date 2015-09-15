package com.guanhuodata.excel.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.guanhuodata.excel.bean.FileBean;
import com.guanhuodata.excel.service.IChartService;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.util.JsonUtil;

/**
 * @author fudk Excel报表处理Action
 */
public class ExcelAction implements Action {

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ExcelAction.class);

	private IChartService chartService;
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("process excel doc Start.");
		String type = request.getParameter("type");
		if ("getExcelList".equals(type)) {
			getExcelList(request, response);
		} else if ("uploadExcel".equals(type)) {
			uploadExcel(request, response);
		}
		LOG.info("process excel doc Finish.");
	}

	// 获取上传的excel报表信息
	@SuppressWarnings("null")
	private void getExcelList(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("get uploaded excel infos.");
		PrintWriter out = null;
		String filePath = "d://upload//";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String operator = "administrator";
		List<FileBean> list = new ArrayList<FileBean>();
		try {
			out = response.getWriter();
			File file = new File(filePath);
			int i = 0;
			if (file.isDirectory()) {
				for (File files : file.listFiles()) {
					i++;
					System.out.println("file name:" + file.getName());
					FileBean fileBean = new FileBean();
					fileBean.setId(i);
					fileBean.setFileName(files.getName());
					fileBean.setOperator(operator);
					fileBean.setOperateTime(sdf.format(new Date()));
					fileBean.setPieChart("pieChart");
					fileBean.setLineChart("lineChart");
					list.add(fileBean);
				}
			}
			String result = JsonUtil.makeListJson(list);
			System.out.println(result);
			out.print(result);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			out.print("-1");
			LOG.error("get uploaded excel infos has an exception: " + e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			out.close();
		}

	}

	/**
	 * @author fudk
	 * @throws IOException
	 */
	private void uploadExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 设置编码
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charSet=UTF-8");
		PrintWriter pw = response.getWriter();
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
			// 可上传多个文件
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {
				// 获取表单的属性名字
				String name = item.getFieldName();
				// 如果获取的表单信息是普通的文本信息
				if (item.isFormField()) {
					LOG.info("Upload File is a formField.");
					// 获取用户具体输入的字符串 ，名字起得挺好，因为表单提交过来的是 字符串类型的
					String value = item.getString();
					request.setAttribute(name, value);
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
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					// 重命名上传文件名称：源文件名称加下划线与当前日期yyyyMMddHHmmss
					fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + sdf.format(new Date())
							+ fileName.substring(fileName.lastIndexOf("."), fileName.length());
					// 限制上传文件类型为Excel报表文件
					if (!(".xls".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length())))
							&& !(".xlsx".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length())))) {
						LOG.error("Upload File is Not Excel.");
						pw.print("-1");// 文件不是Excel
					} else {
						// 将文件写入指定位置
						String savePath = "d://upload//";
						// 真正将上传文件写到磁盘上
						File filePath = new File(savePath + fileName);
						item.write(filePath);// 第三方提供的
						chartService.readChart(savePath + fileName);
						// 我们自己来写一个
						/*
						 * InputStream in = item.getInputStream();
						 * FileOutputStream out = new
						 * FileOutputStream(filePath); int length = 0; byte[]
						 * buf = new byte[1024]; System.out.println("获取上传文件总大小："
						 * + item.getSize()); //in.read(buf); while((length =
						 * in.read(buf)) != -1){ //从buf数组中取出数据写到磁盘上
						 * out.write(buf, 0, length); } in.close(); out.close();
						 */
						LOG.info("Upload File Success.");
						pw.print("0");
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Upload File has an Exception.");
			pw.print("-2");
			e.printStackTrace();
		} finally {
			pw.flush();
		}
	}

	public IChartService getChartService() {
		return chartService;
	}

	public void setChartService(IChartService chartService) {
		this.chartService = chartService;
	}

}
