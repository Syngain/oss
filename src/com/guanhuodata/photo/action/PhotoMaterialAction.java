package com.guanhuodata.photo.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
import org.apache.log4j.Logger;
import com.guanhuodata.framework.core.Action;
import com.guanhuodata.framework.util.JsonUtil;
import com.guanhuodata.photo.bean.FileBean;
import com.guanhuodata.photo.bean.QImageInfoBean;
import com.guanhuodata.photo.service.IMaterialService;

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
		}else if("getImageByName".equals(type)){
			getImageByName(request,response);
		}else if("getQImgInfo".equals(type)){
			getQImgInfo(request,response);
		}else if("uploadMaterialExcel".equals(type)){
			uploadMaterialExcel(request,response);
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
		String qImgName = request.getParameter("qImgName");
		System.out.println("qImgName: " + qImgName);
		String transedQImageName=new String(qImgName.getBytes("ISO8859-1"),"UTF-8");
		System.out.println("transedQImageName: " + transedQImageName);
		PrintWriter out = null;
		String ret = "";
		try {
			out = response.getWriter();
			if(transedQImageName.equals("14.jpg")){
				QImageInfoBean qBean = new QImageInfoBean();
				qBean.setShop("施华蔻");
				qBean.setCTR(5.5);
				qBean.setPutInCrowd("浏览用户");
				qBean.setTitle("日常活动");
				qBean.setROI(8.8);
				qBean.setImageSize("520*360");
				qBean.setPutInTime("2015-09-14");
				qBean.setLinkAddress("http://ju.taobao.com/tg/brand_items.html");
				ret = JsonUtil.makeJson(qBean);
			}else{
				QImageInfoBean qBean = new QImageInfoBean();
				qBean.setShop("雪肌精");
				qBean.setCTR(1.8);
				qBean.setPutInCrowd("浏览用户");
				qBean.setTitle("日常活动");
				qBean.setROI(6.6);
				qBean.setImageSize("640*480");
				qBean.setPutInTime("2015-09-14");
				qBean.setLinkAddress("http://ju.taobao.com/tg/brand_items.html");
				ret = JsonUtil.makeJson(qBean);
			}
			out.print(ret);
		} catch (IOException e) {
			LOG.error("view img info has a exception: " + e.getLocalizedMessage());
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
	private void getImageByName(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		LOG.info("get image by image name.");
		request.setCharacterEncoding("UTF-8");
		response.setContentType("image/*;charset=UTF-8");
		String imageName = request.getParameter("imageName");
		System.out.println("imageName: " + imageName);
		String transedImageName=new String(imageName.getBytes("ISO8859-1"),"UTF-8");
		System.out.println("name: " + transedImageName);
		String path = "d:" + File.separator + "img" + File.separator + transedImageName;
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
	 * 
	 */
	private void getImgPaths(HttpServletRequest request, HttpServletResponse response) {
		LOG.info("get image paths start.");
		PrintWriter out = null;
		List<FileBean> fileBeanList = new ArrayList<FileBean>();
		try {
			out = response.getWriter();
			String path = "d:" + File.separator  + "img";
			File files = new File(path);
			if(files.exists() && files.isDirectory()){
				for(File file:files.listFiles()){
					System.out.println(file.getPath());
					FileBean fileBean = new FileBean();
					fileBean.setFileName(file.getName());
					fileBeanList.add(fileBean);
				}
				String ret = JsonUtil.makeListJson(fileBeanList);
				System.out.println(ret);
				out.print(ret);
			}else{
				LOG.error("directory is not exist. ");
				out.print("-1");
				throw new FileNotFoundException("目录不存在.");
			}
		} catch (Exception e) {
			LOG.error("search image path has a exception: " + e.getLocalizedMessage());
			out.print("-2");
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

	public IMaterialService getMaterialService() {
		return materialService;
	}

	public void setMaterialService(IMaterialService materialService) {
		this.materialService = materialService;
	}
	
}
