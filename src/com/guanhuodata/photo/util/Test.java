package com.guanhuodata.photo.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.guanhuodata.framework.util.JsonUtil;
import com.guanhuodata.photo.bean.FileBean;
import com.guanhuodata.photo.bean.QImageInfoBean;

public class Test {

	public static void main(String[] args) {
		//materialRepository
				/*File s = new File("d:\\img\\14.jpg");
				File t = new File("d:\\eg.jpg");
				InputStream in = null;
				OutputStream out = null;
				try{
					in = new BufferedInputStream(new FileInputStream(s));
					out = new BufferedOutputStream(new FileOutputStream(t));
					byte[] buf = new byte[2048];
					int len = 0;
					while((len = in.read(buf)) != -1){
						out.write(buf, 0, len);
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try{
						in.close();
						out.close();
					}catch(IOException e){
						e.printStackTrace();
					}
				}*/
		/*File file = new File("D:\\tomcat7\\apache-tomcat-7.0.62\\webapps\\statement-analysis");
		List<File> fileList = Arrays.asList(file.listFiles());
		if(fileList.contains("materialRepository")){
			for(File files:fileList){
				System.out.println(files.getName());
			}
		}else{
			for(File files:fileList){
				System.out.println(files.getName());
			}
			File material = new File("D:\\tomcat7\\apache-tomcat-7.0.62\\webapps\\statement-analysis\\materialRepository");
			boolean is = material.mkdir();
			File filen = new File("d:\\aaa");
			filen.mkdir();
			System.out.println(is);
		}*/
		/*String path = "d:" +File.separator + "img";
		File filen = new File(path);
		List<FileBean> fileBeanList = new ArrayList<FileBean>();
		for(File fileName:filen.listFiles()){
			System.out.println(fileName.getPath());
			FileBean fileBean = new FileBean();
			fileBean.setFileName(fileName.getName());
			fileBeanList.add(fileBean);
		}
		String ret = JsonUtil.makeListJson(fileBeanList);
		System.out.println(ret);*/
		/*System.out.println(filen.exists());
		
		filen.mkdir();
		System.out.println(filen.exists());*/
		/*String name = "01.jpg";
		String nameSubString = name.substring(0, name.lastIndexOf("."));
		System.out.println(nameSubString);*/
		QImageInfoBean qBean = new QImageInfoBean();
		qBean.setShop("施华蔻");
		qBean.setCTR(5.5);
		qBean.setPutInCrowd("浏览用户");
		qBean.setTitle("日常活动");
		qBean.setROI(8.8);
		qBean.setImageSize("520*360");
		String s = JsonUtil.makeJson(qBean);
		System.out.println(s);
	}
}
