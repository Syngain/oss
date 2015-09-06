package com.guanhuodata.web.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.guanhuodata.framework.core.Action;

public class ImageAction implements Action{
	public void execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type = request.getParameter("type");
		if(type==null||type.equals("")){
			get(request, response);
		}else{
			check(request, response);
		}
	}
	public void get(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			/**
		    * 验证码图片的宽度。 
		    */
		  int width = 60; 
		  /**
		    *  验证码图片的高度。
		    */
		  int height = 20;
		  char[] codeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };  
		  BufferedImage image = new BufferedImage(width, height, 
		  BufferedImage.TYPE_INT_RGB); 
		   // 获取图形上下文 
		   Graphics g = image.getGraphics(); 
		   // 设定背景色 
		   g.setColor(Color.white); 
		   g.fillRect(0, 0, width, height); 
		   //画边框 
		   g.setColor(Color.white); 
		   g.drawRect(0,0,width-1,height-1); 
		   // 取随机产生的认证码
		   String strEnsure = "";
		   // 4代表4位验证码,如果要生成更多位的认证码,则加大数值
		   for(int i=0; i<4; ++i) {
			   strEnsure+=codeSequence[(int)(codeSequence.length*Math.random())];
		   }  
		   request.getSession().setAttribute("imageCore", strEnsure);
		   // 　　将认证码显示到图像中,如果要生成更多位的认证码,增加drawString语句
		   g.setColor(Color.black); 
		   g.setFont(new Font("Arial",0,18)); 
		   String str = strEnsure.substring(0,1); 
		   g.drawString(str,4,18); 
		   str = strEnsure.substring(1,2); 
		   g.drawString(str,17,18); 
		   str = strEnsure.substring(2,3); 
		   g.drawString(str,30,18);   
		   str = strEnsure.substring(3,4); 
		   g.drawString(str,43,18); 
		   // 释放图形上下文
		   g.dispose();   
		    // 输出图像到页面 
		   ServletOutputStream responseOutputStream = response.getOutputStream();
		   ImageIO.write(image, "JPEG", responseOutputStream);
		    response.setHeader("Cache-Control", "no-store"); 
			response.setHeader("Pragma", "no-cache"); 
			response.setDateHeader("Expires", 0); 
			response.setContentType("image/jpeg"); 
			responseOutputStream.flush(); 
			responseOutputStream.close();
		   }
	public void check(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String imageStr  = request.getParameter("imageCoke");
		PrintWriter out = response.getWriter();
		if(imageStr!=null){
			String imageCore = (String)request.getSession().getAttribute("imageCore");
			int result = 0;
			if(imageStr.equalsIgnoreCase(imageCore)){
				result= 1;
			}
			out.print(result);
			out.flush();
			out.close();
		}else{
			out.print(-1);
			out.flush();
			out.close();
		}
	}
}
