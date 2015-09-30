package com.guanhuodata.photo.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/*import java.util.zip.ZipOutputStream;*/
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipOutputStream;

import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.util.JsonUtil;
import com.guanhuodata.photo.bean.InitConditions;
import com.guanhuodata.photo.bean.MaterialChartSplitBean;
import com.guanhuodata.photo.bean.QueryCondition;
import com.guanhuodata.photo.service.IMaterialService;
import com.guanhuodata.photo.util.ActionUtils;
import com.guanhuodata.photo.util.ExportExcelCustomer;
import com.guanhuodata.photo.util.ExportExcelSelf;
import com.guanhuodata.photo.util.MaterialChartUtil;
import com.guanhuodata.photo.util.Page;

public class PhotoMaterialAction implements Action {

	private  IMaterialService materialService;
	
	private static final Logger LOG = Logger.getLogger(PhotoMaterialAction.class);
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String type = request.getParameter("type");
		LOG.info("according type judge the page to forward start.");
		if("getImgPaths".equals(type)){
			getImgPaths(request,response);
		}else if("getImageById".equals(type)){
			getImageById(request,response);
		}else if("getQImgInfo".equals(type)){
			getQImgInfo(request,response);
		}else if("uploadMaterialExcel".equals(type)){
			uploadMaterialExcel(request,response);
		}else if("getPaginationInfo".equals(type)){
			getPaginationInfo(request,response);
		}else if("getPaginationInfoByCondition".equals(type)){
			getPaginationInfoByCondition(request,response);
		}else if("initConditions".equals(type)){
			initConditions(request,response);
		}else if("findByName".equals(type)){
			findByName(request,response);
		}else if("getPaginationInfoByName".equals(type)){
			getPaginationInfoByName(request,response);
		}else if("downloadCustomerExcel".equals(type)){
			downloadCustomerExcel(request,response);
		}else if("downloadSelfExcel".equals(type)){
			downloadSelfExcel(request,response);
		}else if("uploadifyImages".equals(type)){
			uploadifyImages(request,response);
		}else if("downloadImageById".equals(type)){
			downloadImageById(request,response);
		}else if("batchDownloadMaterialImage".equals(type)){
			batchDownloadMaterialImage(request,response);
		}
	}
	
	private void batchDownloadMaterialImage(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("batch download material image start.");
		request.setCharacterEncoding("UTF-8");
		QueryCondition queryCondition = new QueryCondition();
		queryCondition.setShopName(request.getParameter("shopName"));
		queryCondition.setStandSize(request.getParameter("standSize"));
		queryCondition.setActivityName(request.getParameter("activityName"));
		queryCondition.setPutInCrowd(request.getParameter("putInCrowd"));
		queryCondition.setPutInDateTime(request.getParameter("putInDateTime").equals("") || request.getParameter("putInDateTime") == null ? "" : request.getParameter("putInDateTime"));
		queryCondition.setCTR(request.getParameter("CTR").equals("") || request.getParameter("CTR") == null ? "" : request.getParameter("CTR"));
		queryCondition.setCTROrder(request.getParameter("CTROrder").equals("") || request.getParameter("CTROrder") == null ? "" : request.getParameter("CTROrder"));
		queryCondition.setReveal(request.getParameter("reveal").equals("") || request.getParameter("reveal") == null ? "" : request.getParameter("reveal"));
		queryCondition.setRevealOrder(request.getParameter("revealOrder").equals("") || request.getParameter("revealOrder") == null ? "" : request.getParameter("revealOrder"));
		queryCondition.setConsume(request.getParameter("consume").equals("") || request.getParameter("consume") == null ? "" : request.getParameter("consume"));
		queryCondition.setConsumeOrder(request.getParameter("consumeOrder").equals("") || request.getParameter("consumeOrder") == null ? "" : request.getParameter("consumeOrder"));
		queryCondition.setShowROI(request.getParameter("showROI").equals("") || request.getParameter("showROI") == null ? "" : request.getParameter("showROI"));
		queryCondition.setShowROIOrder(request.getParameter("showROIOrder").equals("") || request.getParameter("showROIOrder") == null ? "" : request.getParameter("showROIOrder"));
		queryCondition.setClickOutROI(request.getParameter("clickOutROI").equals("") || request.getParameter("clickOutROI") == null ? "" : request.getParameter("clickOutROI"));
		queryCondition.setClickOutROIOrder(request.getParameter("clickOutROIOrder").equals("") || request.getParameter("clickOutROIOrder") == null ? "" : request.getParameter("clickOutROIOrder"));
		queryCondition.setCPC(request.getParameter("CPC").equals("") || request.getParameter("CPC") == null ? "" : request.getParameter("CPC"));
		queryCondition.setCPCOrder(request.getParameter("CPCOrder").equals("") || request.getParameter("CPCOrder") == null ? "" : request.getParameter("CPCOrder"));
		OutputStream os = null;
		int index = 0;
		long fileLength = 0;
		try {
			os = response.getOutputStream();
			List<MaterialChartSplitBean> list = materialService.getListByCondition(queryCondition);
			List<File> files = new ArrayList<File>();
			for(MaterialChartSplitBean bean : list){
				String path = "d:" + File.separator + "img" + File.separator + bean.getOriginalityName() + ".jpg";
				File file = new File(path);
				files.add(file);
				fileLength += file.length();
				index++;
			}
			String fileName = UUID.randomUUID().toString() + ".zip";
			//在服务器端创建打包下载的临时文件
	        String outFilePath = "d:\\" + fileName;
	        File file = new File(outFilePath);
	        //文件输出流
	        FileOutputStream outStream = new FileOutputStream(file);
	        //压缩流
	        ZipOutputStream toClient = new ZipOutputStream(outStream);
	        toClient.setEncoding("gbk");
	        MaterialChartUtil.zipFile(files, toClient);
	        toClient.close();
	        outStream.close();
	        this.downloadZip(file, response);
		} catch (IOException e) {
			LOG.error("batch download material image has an IOException: " +e.getLocalizedMessage());
			e.printStackTrace();
		}catch(ServletException e){
			LOG.error("batch download material image has an ServletException: " +e.getLocalizedMessage());
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

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * 前台prettyphoto根据imgName请求创建点击图片预览
	 * 
	 */
	private void downloadImageById(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("download image by image id.");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("image/*;charset=UTF-8");
		response.setCharacterEncoding("text/html;charset=UTF-8");
		//String imageName = request.getParameter("imageName");
		//String transedImageName=new String(imageName.getBytes("ISO8859-1"),"UTF-8");
		long imageId = Long.parseLong(request.getParameter("imageId"));
		String imageName = materialService.getOriginalityNameById(imageId);
		String path = "d:" + File.separator + "img" + File.separator + imageName + "." + imageName.split("_")[imageName.split("_").length - 1];
		//response.setHeader("content-disposition", "attachment;fileName="+URLEncoder.encode(path, "UTF-8"));
		response.setHeader("content-disposition", "attachment;fileName=" + (new String((imageName + ".jpg").getBytes(),"ISO8859-1")));
		InputStream in = null;
		OutputStream out = null;
		File file = new File(path);
		if(file.exists()){
			try{
				in = new BufferedInputStream(new FileInputStream(file));
				out = response.getOutputStream();
				byte[] buf = new byte[1024];
				int len = 0;
				while((len = in.read(buf)) != -1){
					out.write(buf, 0, len);
				}
				out.flush();
				LOG.info("get image by name invoke success");
			}catch(Exception e){
				LOG.error("read image has a exception: " + e.getLocalizedMessage());
				e.printStackTrace();
			}finally{
				try{
					in.close();
					out.close();
				}catch(Exception e){
					LOG.error("close stream has a exception: " + e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		}else{
			try {
				LOG.error("image is not exist.");
				throw new FileNotFoundException("图片不存在.");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void uploadifyImages(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LOG.info("upload material images .");
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
		upload.setSizeMax(1024 * 1024);
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
					//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					// 重命名上传文件名称：源文件名称加下划线与当前日期yyyyMMddHHmmss
					//fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + sdf.format(new Date()) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
					// 限制上传文件类型为Excel报表文件
					if (!(".jpg".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length()))) && !(".jpeg".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length())))) {
						LOG.error("Upload material image is not jpg/jpeg.");
						pw.print("-1");// 文件不是jpg图片
					} else {
						// 将文件写入指定位置
						//目前不确定到底是怎么区分店铺，如果区分店铺的话，上传路径就是该店铺的目录
						//String savePath = "d:" + File.separator  + "materialImagesRepo" + File.separator;
						String savePath = "d:" + File.separator  + "img" + File.separator;
						// 真正将上传文件写到磁盘上
						File filePath = new File(savePath + fileName);
						item.write(filePath);// 第三方提供的
						LOG.info("Upload material images Success.");
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
						
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Upload material images has an Exception: " + e.getLocalizedMessage());
			pw.print("-2");
			e.printStackTrace();
		} finally {
			pw.flush();
		}
	}

	private void downloadCustomerExcel(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("download material excel use to ourself start.");
		request.setCharacterEncoding("UTF-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ExportExcelCustomer<MaterialChartSplitBean> ex = new ExportExcelCustomer<MaterialChartSplitBean>();
		String[] headersCustomer = {"创意名称","推广人群","展现","点击","消耗","15天点击产出","15天展示产出","点击率(%)","千次展现成本(元)","点击单价(元)","15天回报率","15天展示回报率","15天顾客订单数","店辅收藏数","宝贝收藏数","访客","15天展示访客","15天订单金额"};
		//String downloadName = URLEncoder.encode("自用素材分析报表_" + sdf.format(new Date()) + ".xls");
		String downloadName = new String(("客户素材分析报表" + sdf.format(new Date()) + ".xls").getBytes(),"ISO8859-1");
		QueryCondition queryCondition = new QueryCondition();
		queryCondition.setShopName(request.getParameter("shopName"));
		queryCondition.setStandSize(request.getParameter("standSize"));
		queryCondition.setActivityName(request.getParameter("activityName"));
		queryCondition.setPutInCrowd(request.getParameter("putInCrowd"));
		queryCondition.setPutInDateTime(request.getParameter("putInDateTime").equals("") || request.getParameter("putInDateTime") == null ? "" : request.getParameter("putInDateTime"));
		queryCondition.setCTR(request.getParameter("CTR").equals("") || request.getParameter("CTR") == null ? "" : request.getParameter("CTR"));
		queryCondition.setCTROrder(request.getParameter("CTROrder").equals("") || request.getParameter("CTROrder") == null ? "" : request.getParameter("CTROrder"));
		queryCondition.setReveal(request.getParameter("reveal").equals("") || request.getParameter("reveal") == null ? "" : request.getParameter("reveal"));
		queryCondition.setRevealOrder(request.getParameter("revealOrder").equals("") || request.getParameter("revealOrder") == null ? "" : request.getParameter("revealOrder"));
		queryCondition.setConsume(request.getParameter("consume").equals("") || request.getParameter("consume") == null ? "" : request.getParameter("consume"));
		queryCondition.setConsumeOrder(request.getParameter("consumeOrder").equals("") || request.getParameter("consumeOrder") == null ? "" : request.getParameter("consumeOrder"));
		queryCondition.setShowROI(request.getParameter("showROI").equals("") || request.getParameter("showROI") == null ? "" : request.getParameter("showROI"));
		queryCondition.setShowROIOrder(request.getParameter("showROIOrder").equals("") || request.getParameter("showROIOrder") == null ? "" : request.getParameter("showROIOrder"));
		queryCondition.setClickOutROI(request.getParameter("clickOutROI").equals("") || request.getParameter("clickOutROI") == null ? "" : request.getParameter("clickOutROI"));
		queryCondition.setClickOutROIOrder(request.getParameter("clickOutROIOrder").equals("") || request.getParameter("clickOutROIOrder") == null ? "" : request.getParameter("clickOutROIOrder"));
		queryCondition.setCPC(request.getParameter("CPC").equals("") || request.getParameter("CPC") == null ? "" : request.getParameter("CPC"));
		queryCondition.setCPCOrder(request.getParameter("CPCOrder").equals("") || request.getParameter("CPCOrder") == null ? "" : request.getParameter("CPCOrder"));
		response.addHeader("Content-Disposition","attachment;filename=\"" + downloadName + "\"");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			List<MaterialChartSplitBean> list = materialService.getListByCondition(queryCondition);
			Map<String,List<MaterialChartSplitBean>> map = MaterialChartUtil.partitionCustomer(list);
			ex.exportExcel(headersCustomer, os,map);
		} catch (IOException e) {
			LOG.error("download material excel has an IOException: " +e.getLocalizedMessage());
			e.printStackTrace();
		}catch(SQLException e){
			LOG.error("download material excel has an SQLException: " +e.getLocalizedMessage());
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
	
	private void downloadSelfExcel(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("download material excel use to ourself start.");
		request.setCharacterEncoding("UTF-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ExportExcelSelf<MaterialChartSplitBean> ex = new ExportExcelSelf<MaterialChartSplitBean>();
		String[] headersSelf = {"活动主题","创意图","创意名称","推广人群","推广时间","展现","点击","触达用户","触达频次","消耗","15天点击产出","15天展示产出","点击率(%)","千次展现成本(元)","点击单价(元)","15天回报率","15天展示回报率","15天顾客订单数","店辅收藏数","宝贝收藏数","访客","15天展示访客","15天订单金额"};
		//String downloadName = URLEncoder.encode("自用素材分析报表_" + sdf.format(new Date()) + ".xls");
		String downloadName = new String(("自用素材分析报表" + sdf.format(new Date()) + ".xls").getBytes(),"ISO8859-1");
		QueryCondition queryCondition = new QueryCondition();
		queryCondition.setShopName(request.getParameter("shopName"));
		queryCondition.setStandSize(request.getParameter("standSize"));
		queryCondition.setActivityName(request.getParameter("activityName"));
		queryCondition.setPutInCrowd(request.getParameter("putInCrowd"));
		queryCondition.setPutInDateTime(request.getParameter("putInDateTime").equals("") || request.getParameter("putInDateTime") == null ? "" : request.getParameter("putInDateTime"));
		queryCondition.setCTR(request.getParameter("CTR").equals("") || request.getParameter("CTR") == null ? "" : request.getParameter("CTR"));
		queryCondition.setCTROrder(request.getParameter("CTROrder").equals("") || request.getParameter("CTROrder") == null ? "" : request.getParameter("CTROrder"));
		queryCondition.setReveal(request.getParameter("reveal").equals("") || request.getParameter("reveal") == null ? "" : request.getParameter("reveal"));
		queryCondition.setRevealOrder(request.getParameter("revealOrder").equals("") || request.getParameter("revealOrder") == null ? "" : request.getParameter("revealOrder"));
		queryCondition.setConsume(request.getParameter("consume").equals("") || request.getParameter("consume") == null ? "" : request.getParameter("consume"));
		queryCondition.setConsumeOrder(request.getParameter("consumeOrder").equals("") || request.getParameter("consumeOrder") == null ? "" : request.getParameter("consumeOrder"));
		queryCondition.setShowROI(request.getParameter("showROI").equals("") || request.getParameter("showROI") == null ? "" : request.getParameter("showROI"));
		queryCondition.setShowROIOrder(request.getParameter("showROIOrder").equals("") || request.getParameter("showROIOrder") == null ? "" : request.getParameter("showROIOrder"));
		queryCondition.setClickOutROI(request.getParameter("clickOutROI").equals("") || request.getParameter("clickOutROI") == null ? "" : request.getParameter("clickOutROI"));
		queryCondition.setClickOutROIOrder(request.getParameter("clickOutROIOrder").equals("") || request.getParameter("clickOutROIOrder") == null ? "" : request.getParameter("clickOutROIOrder"));
		queryCondition.setCPC(request.getParameter("CPC").equals("") || request.getParameter("CPC") == null ? "" : request.getParameter("CPC"));
		queryCondition.setCPCOrder(request.getParameter("CPCOrder").equals("") || request.getParameter("CPCOrder") == null ? "" : request.getParameter("CPCOrder"));
		response.addHeader("Content-Disposition","attachment;filename=\"" + downloadName + "\"");
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			List<MaterialChartSplitBean> list = materialService.getListByCondition(queryCondition);
			Map<String,List<MaterialChartSplitBean>> map = MaterialChartUtil.partitionSelf(list);
			ex.exportExcel(headersSelf, os,map);
		} catch (IOException e) {
			LOG.error("download material excel has an IOException: " +e.getLocalizedMessage());
			e.printStackTrace();
		}catch(SQLException e){
			LOG.error("download material excel has an SQLException: " +e.getLocalizedMessage());
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

	private void getPaginationInfoByName(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("get paginationInfo by name start.");
		//封装查询条件
		Page page = new Page();
		page.setPageNumber(Integer.parseInt(request.getParameter("pageNumber")));
		page.setPageSize(21);
		String originalityName = new String(request.getParameter("originalityName").getBytes("ISO8859-1"),"UTF-8");
		PrintWriter out = null;
		try{
			out = response.getWriter();
			page = materialService.getPaginationInfoByName(page,originalityName);
			String ret = JsonUtil.makeJson(page);
			out.print(ret);
		}catch(Exception e){
			LOG.error("PrintWriter out has an IOException: " + e.getLocalizedMessage());
			out.print("-1");
			e.printStackTrace();
		}
	}

	private void findByName(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("find by img name start.");
		//封装查询条件
		Page page = new Page();
		page.setPageNumber(Integer.parseInt(request.getParameter("pageNumber")));
		page.setPageSize(21);
		String originalityName = new String(request.getParameter("originalityName").getBytes("ISO8859-1"),"UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			//分页取出
			List<MaterialChartSplitBean> list = materialService.findByName(page,originalityName);
			//根据bean查找
			ActionUtils actionUtils = new ActionUtils();
			String ret = actionUtils.getImgPathsByListBean(list);
			out.print(ret);
			out.close();
		} catch (Exception e) {
			LOG.error("PrintWriter has an exception.");
			out.print("-1");
			e.printStackTrace();
		}
	}

	//页面条件的初始化
	private void initConditions(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("init conditions to page.");
		PrintWriter out = null;
		try {
			 out = response.getWriter();
			 InitConditions initConditions = materialService.getInitConditions();
			 String ret = JsonUtil.makeJson(initConditions);
			 System.out.println(ret);
			 out.print(ret);
		} catch (Exception e) {
			LOG.error("PrintWriter out has an IOException: " + e.getLocalizedMessage());
			out.print("-1");
			e.printStackTrace();
		}
	}

	/*获取分页信息*/
	private void getPaginationInfo(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("getPaginationInfo invoke.");
		PrintWriter out = null;
		try{
			out = response.getWriter();
			Page page = new Page();
			page.setPageSize(21);
			page = materialService.getPaginationInfo(page);
			String ret = JsonUtil.makeJson(page);
			out.print(ret);
		}catch(Exception e){
			LOG.error("PrintWriter out has an IOException: " + e.getLocalizedMessage());
			out.print("-1");
			e.printStackTrace();
		}
	}
	
	/*获取分页信息*/
	private void getPaginationInfoByCondition(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("getPaginationInfo invoke.");
		//封装查询条件
		Page page = new Page();
		page.setPageNumber(Integer.parseInt(request.getParameter("pageNumber")));
		page.setPageSize(21);
		QueryCondition queryCondition = new QueryCondition();
		queryCondition.setShopName(new String(request.getParameter("shopName").getBytes("ISO8859-1"),"UTF-8"));
		queryCondition.setStandSize(request.getParameter("standSize"));
		queryCondition.setActivityName(new String(request.getParameter("activityName").getBytes("ISO8859-1"),"UTF-8"));
		queryCondition.setPutInCrowd(new String(request.getParameter("putInCrowd").getBytes("ISO8859-1"),"UTF-8"));
		queryCondition.setPutInDateTime(request.getParameter("putInDateTime").equals("") || request.getParameter("putInDateTime") == null ? "" : request.getParameter("putInDateTime"));
		queryCondition.setCTR(request.getParameter("CTR").equals("") || request.getParameter("CTR") == null ? "" : request.getParameter("CTR"));
		queryCondition.setCTROrder(request.getParameter("CTROrder").equals("") || request.getParameter("CTROrder") == null ? "" : request.getParameter("CTROrder"));
		queryCondition.setReveal(request.getParameter("reveal").equals("") || request.getParameter("reveal") == null ? "" : request.getParameter("reveal"));
		queryCondition.setRevealOrder(request.getParameter("revealOrder").equals("") || request.getParameter("revealOrder") == null ? "" : request.getParameter("revealOrder"));
		queryCondition.setConsume(request.getParameter("consume").equals("") || request.getParameter("consume") == null ? "" : request.getParameter("consume"));
		queryCondition.setConsumeOrder(request.getParameter("consumeOrder").equals("") || request.getParameter("consumeOrder") == null ? "" : request.getParameter("consumeOrder"));
		queryCondition.setShowROI(request.getParameter("showROI").equals("") || request.getParameter("showROI") == null ? "" : request.getParameter("showROI"));
		queryCondition.setShowROIOrder(request.getParameter("showROIOrder").equals("") || request.getParameter("showROIOrder") == null ? "" : request.getParameter("showROIOrder"));
		queryCondition.setClickOutROI(request.getParameter("clickOutROI").equals("") || request.getParameter("clickOutROI") == null ? "" : request.getParameter("clickOutROI"));
		queryCondition.setClickOutROIOrder(request.getParameter("clickOutROIOrder").equals("") || request.getParameter("clickOutROIOrder") == null ? "" : request.getParameter("clickOutROIOrder"));
		queryCondition.setCPC(request.getParameter("CPC").equals("") || request.getParameter("CPC") == null ? "" : request.getParameter("CPC"));
		queryCondition.setCPCOrder(request.getParameter("CPCOrder").equals("") || request.getParameter("CPCOrder") == null ? "" : request.getParameter("CPCOrder"));
		PrintWriter out = null;
		try{
			out = response.getWriter();
			page = materialService.getPaginationInfoByCondition(page,queryCondition);
			String ret = JsonUtil.makeJson(page);
			out.print(ret);
		}catch(Exception e){
			LOG.error("PrintWriter out has an IOException: " + e.getLocalizedMessage());
			out.print("-1");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * 前台qTips根据图片名称qName请求创建对应素材图片的提示框
	 */
	private void getQImgInfo(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("view img info with qTips by imgName. ");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("image/*;charset=UTF-8");
		//String qImgName = request.getParameter("qImgName");
		//String transedQImageName=new String(qImgName.getBytes("ISO8859-1"),"UTF-8");
		long qImgId = Long.parseLong(request.getParameter("qImgId"));
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String ret = materialService.getQImageInfoByQImageId(qImgId);
			out.print(ret);
		} catch (Exception e) {
			LOG.error("view img info has a exception: " + e.getLocalizedMessage());
			out.print("-1");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws UnsupportedEncodingException
	 * 前台prettyphoto根据imgName请求创建点击图片预览
	 * 
	 */
	private void getImageById(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("get image by image name.");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("image/*;charset=UTF-8");
		//String imageName = request.getParameter("imageName");
		//String transedImageName=new String(imageName.getBytes("ISO8859-1"),"UTF-8");
		long imageId = Long.parseLong(request.getParameter("imageId"));
		String imageName = materialService.getOriginalityNameById(imageId);
		String path = "d:" + File.separator + "img" + File.separator + imageName + "." + imageName.split("_")[imageName.split("_").length - 1];
		InputStream in = null;
		OutputStream out = null;
		File file = new File(path);
		if(file.exists()){
			try{
				in = new BufferedInputStream(new FileInputStream(file));
				out = response.getOutputStream();
				byte[] buf = new byte[1024];
				int len = 0;
				while((len = in.read(buf)) != -1){
					out.write(buf, 0, len);
				}
				out.flush();
				LOG.info("get image by name invoke success");
			}catch(Exception e){
				LOG.error("read image has a exception: " + e.getLocalizedMessage());
				e.printStackTrace();
			}finally{
				try{
					in.close();
					out.close();
				}catch(Exception e){
					LOG.error("close stream has a exception: " + e.getLocalizedMessage());
					e.printStackTrace();
				}
			}
		}else{
			try {
				LOG.error("image is not exist.");
				throw new FileNotFoundException("图片不存在.");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * 服务器端返回图片存储路径
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	private void getImgPaths(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("get image paths start.");
		//封装查询条件
		Page page = new Page();
		page.setPageNumber(Integer.parseInt(request.getParameter("pageNumber")));
		page.setPageSize(21);
		QueryCondition queryCondition = new QueryCondition();
		queryCondition.setShopName(new String(request.getParameter("shopName").getBytes("ISO8859-1"),"UTF-8"));
		queryCondition.setStandSize(request.getParameter("standSize"));
		queryCondition.setActivityName(new String(request.getParameter("activityName").getBytes("ISO8859-1"),"UTF-8"));
		queryCondition.setPutInCrowd(new String(request.getParameter("putInCrowd").getBytes("ISO8859-1"),"UTF-8"));
		queryCondition.setPutInDateTime(request.getParameter("putInDateTime").equals("") || request.getParameter("putInDateTime") == null ? "" : request.getParameter("putInDateTime"));
		queryCondition.setCTR(request.getParameter("CTR").equals("") || request.getParameter("CTR") == null ? "" : request.getParameter("CTR"));
		queryCondition.setCTROrder(request.getParameter("CTROrder").equals("") || request.getParameter("CTROrder") == null ? "" : request.getParameter("CTROrder"));
		queryCondition.setReveal(request.getParameter("reveal").equals("") || request.getParameter("reveal") == null ? "" : request.getParameter("reveal"));
		queryCondition.setRevealOrder(request.getParameter("revealOrder").equals("") || request.getParameter("revealOrder") == null ? "" : request.getParameter("revealOrder"));
		queryCondition.setConsume(request.getParameter("consume").equals("") || request.getParameter("consume") == null ? "" : request.getParameter("consume"));
		queryCondition.setConsumeOrder(request.getParameter("consumeOrder").equals("") || request.getParameter("consumeOrder") == null ? "" : request.getParameter("consumeOrder"));
		queryCondition.setShowROI(request.getParameter("showROI").equals("") || request.getParameter("showROI") == null ? "" : request.getParameter("showROI"));
		queryCondition.setShowROIOrder(request.getParameter("showROIOrder").equals("") || request.getParameter("showROIOrder") == null ? "" : request.getParameter("showROIOrder"));
		queryCondition.setClickOutROI(request.getParameter("clickOutROI").equals("") || request.getParameter("clickOutROI") == null ? "" : request.getParameter("clickOutROI"));
		queryCondition.setClickOutROIOrder(request.getParameter("clickOutROIOrder").equals("") || request.getParameter("clickOutROIOrder") == null ? "" : request.getParameter("clickOutROIOrder"));
		queryCondition.setCPC(request.getParameter("CPC").equals("") || request.getParameter("CPC") == null ? "" : request.getParameter("CPC"));
		queryCondition.setCPCOrder(request.getParameter("CPCOrder").equals("") || request.getParameter("CPCOrder") == null ? "" : request.getParameter("CPCOrder"));
		//素材路径
		//String path = "d:" + File.separator  + "img";
		PrintWriter out = null;
		try {
			out = response.getWriter();
			//分页取出
			List<MaterialChartSplitBean> list = materialService.getPagedMaterialInfos(page,queryCondition);
			//根据bean查找
			ActionUtils actionUtils = new ActionUtils();
			String ret = actionUtils.getImgPathsByListBean(list);
			out.print(ret);
			out.close();
		} catch (Exception e) {
			LOG.error("PrintWriter has an exception.");
			out.print("-1");
			e.printStackTrace();
		}
	}

	/**
	 * @author fudk
	 * @throws IOException
	 * 素材报表上传
	 */
	private void uploadMaterialExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LOG.info("upload material excel .");
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
					//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					// 重命名上传文件名称：源文件名称加下划线与当前日期yyyyMMddHHmmss
					//fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + sdf.format(new Date()) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
					// 限制上传文件类型为Excel报表文件
					if (!(".xls".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length()))) && !(".xlsx".equals(fileName.substring(fileName.lastIndexOf("."), fileName.length())))) {
						LOG.error("Upload material excel is not Excel.");
						pw.print("-1");// 文件不是Excel
					} else {
						// 将文件写入指定位置
						//目前不确定到底是怎么区分店铺，如果区分店铺的话，上传路径就是该店铺的目录
						String savePath = "d:" + File.separator  + "materialRepo" + File.separator;
						// 真正将上传文件写到磁盘上
						File filePath = new File(savePath + fileName);
						item.write(filePath);// 第三方提供的
						LOG.info("Upload material excel File Success.");
						LOG.info("start to read material excel & insert to db.");
						//在上传素材报表文件结束后开始读取文件并入库MySQL
						boolean flag = materialService.readMaterialExcelInsertToDB(filePath);
						//boolean flag = true;
						if(flag){
							pw.print("0");
							LOG.info("Upload material excel & read material excel & insert to mysql db Success.");
						}else{
							pw.print("-3");
							LOG.error("Upload material excel success but read material excel & insert to mysql db fail.");
						}
						//chartService.readChart(savePath + fileName);
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
						
					}
				}
			}
		} catch (Exception e) {
			LOG.error("Upload material excel has an Exception: " + e.getLocalizedMessage());
			pw.print("-2");
			e.printStackTrace();
		} finally {
			pw.flush();
		}
	}

	/**
	 * 根据请求封装查询条件
	 * @return
	 */
	public QueryCondition getQueryCondition(HttpServletRequest request){
		QueryCondition queryCondition = new QueryCondition();
		queryCondition.setShopName(request.getParameter("shopName"));
		queryCondition.setStandSize(request.getParameter("standSize"));
		queryCondition.setActivityName(request.getParameter("activityName"));
		queryCondition.setPutInCrowd(request.getParameter("putInCrowd"));
		queryCondition.setPutInDateTime(request.getParameter("putInDateTime"));
		queryCondition.setCTR(request.getParameter("CTR"));
		queryCondition.setReveal(request.getParameter("reveal"));
		queryCondition.setConsume(request.getParameter("consume"));
		queryCondition.setShowROI(request.getParameter("showROI"));
		queryCondition.setClickOutROI(request.getParameter("clickOutROI"));
		queryCondition.setCPC(request.getParameter("CPC"));
		return queryCondition;
	}
	
	/**
	 * 下载打包的文件
	 * @param file
	 * @param response
	 */
	public void downloadZip(File file, HttpServletResponse response) {
		try {
			// 以流的形式下载文件。
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(file.getPath()));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			file.delete(); // 将生成的服务器端文件删除
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public IMaterialService getMaterialService() {
		return materialService;
	}

	public void setMaterialService(IMaterialService materialService) {
		this.materialService = materialService;
	}
	
}
