package com.guanhuodata.framework.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import com.guanhuodata.framework.core.ServiceLocator;

/**
 * 遍历读取配置文件中的数据源
 * @author fudk
 *
 */
public class DbUtil {
	
	private static final Logger LOG = Logger.getLogger(DbUtil.class);
	
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
	
	// JDBC MYSQL
		public static Connection getConnection() {
			LOG.info("JDBC connect to localhost mysql db.");
			Connection conn = null;
			String url = "jdbc:mysql://127.0.0.1:3306/guanhuodata?user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(url);
			} catch (ClassNotFoundException e) {
				LOG.error("ClassNotFoundException(com.mysql.jdbc.Driver): " + e.getLocalizedMessage());
				e.printStackTrace();
			} catch (SQLException e) {
				LOG.error(
						"SQLException for url:jdbc:mysql://127.0.0.1:3306/guanhuodata?user=root&password=123456&useUnicode=true&characterEncoding=UTF8");
				e.printStackTrace();
			}
			return conn;
		}

		// close
		public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
			LOG.info("close connection,preparestatement,resultset.");
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOG.error("close connection,preparestatement,resultset has SQLException.");
				e.printStackTrace();
			}
		}
}
