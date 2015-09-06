package com.guanhuodata.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class DriverTypeUtil {
	private static String filePath;
	private static Map<String,String> driverTypes;
	public DriverTypeUtil() {
	       // 读取xml文件
	 }
	 public static Map<String,String> getInstance() {
		 if(driverTypes==null){
			 getType();
		 }
	       return driverTypes;
	 }
	 
	 public static void getType(){
		 driverTypes = new HashMap<String, String>();
		 try {
				Document document = null;
				File file = new File(filePath);
				BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
				InputSource is = new InputSource(br);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				document = builder.parse(is);
				document.normalize();
				NodeList list = document.getElementsByTagName("Oid");
				if(list.getLength() != 0){
					for(int i= 0;i<list.getLength();i++){
						String key = document.getElementsByTagName("Oid").item(i).getFirstChild().getNodeValue();
						String value = document.getElementsByTagName("Oid").item(i).getParentNode().getAttributes().getNamedItem("name").getNodeValue();
						driverTypes.put(key, value);
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
