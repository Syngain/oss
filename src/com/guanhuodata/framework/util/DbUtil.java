package com.guanhuodata.framework.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import com.guanhuodata.framework.core.ServiceLocator;

/**
 * 遍历读取配置文件中的数据源
 * @author fudk
 *
 */
public class DbUtil {
	/*******存放数据源<数据源ID,数据源>**************/
	public  static Map<String ,DataSource> dSources =new HashMap<String, DataSource>();
	
	/**
	 * 将读取到的数据放在dSources中
	 */
	public void init(){
		ServiceLocator service = ServiceLocator.getInstance();
		int  i = 1 ;
		while(true){
			DataSource dataSource;
			try {
				dataSource = (DataSource) service.getService("dataSource"+i);
				dSources.put("dataSource"+i,dataSource);
				i++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				break;
			}
			
		}
	}

	public static void close1(ResultSet rs, Statement stat, Connection con) {
		// TODO Auto-generated method stub
		if(rs!=null){try {rs.close();} catch (SQLException e) {e.printStackTrace();}}
		if(stat!=null){try {stat.close();} catch (SQLException e) {e.printStackTrace();}}
		if(con!=null){try {con.close();} catch (SQLException e) {e.printStackTrace();}}
	}
	public static void close2(ResultSet rs, PreparedStatement stat, Connection con) {
		// TODO Auto-generated method stub
		if(rs!=null){try {rs.close();} catch (SQLException e) {e.printStackTrace();}}
		if(stat!=null){try {stat.close();} catch (SQLException e) {e.printStackTrace();}}
		if(con!=null){try {con.close();} catch (SQLException e) {e.printStackTrace();}}
	}
	public static void close3( PreparedStatement stat, Connection con) {
		// TODO Auto-generated method stub
		if(stat!=null){try {stat.close();} catch (SQLException e) {e.printStackTrace();}}
		if(con!=null){try {con.close();} catch (SQLException e) {e.printStackTrace();}}
	}
}
