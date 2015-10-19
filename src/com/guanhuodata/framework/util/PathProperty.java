package com.guanhuodata.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class PathProperty {

	private static final Logger LOG = Logger.getLogger(PathProperty.class);
	//根据资源配置文件来获取对应的路径名称
	public static String loadAttribute(String attrName){
		LOG.info("get path from path properties by attrName.");
		//File file = new File("/usr/local/project/property/path.properties");
		File file = new File("d://path.properties");
		FileInputStream is = null;
		String path = "";
		try {
			 is = new FileInputStream(file);
			 Properties props = new Properties();
			 props.load(is);
			 path = props.getProperty(attrName);
		} catch (FileNotFoundException e) {
			LOG.error("file path.properties in /usr/local/project/property/ is not found.");
			e.printStackTrace();
		}catch(IOException e){
			LOG.error("load properties has an IOException." + e.getLocalizedMessage());
			e.printStackTrace();
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				LOG.error("close FileInputStream has an IOException: " + e.getLocalizedMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return path;
	}
	
}
