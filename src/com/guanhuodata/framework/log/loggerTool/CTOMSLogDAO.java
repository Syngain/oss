package com.guanhuodata.framework.log.loggerTool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;
import com.guanhuodata.framework.log.CtomsLoggerBean;

public class CTOMSLogDAO {
	private DataSource dataSource;
	public void addLog(CtomsLoggerBean clb){
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "insert into ctoms_log_table(time,operator,action,operateobject,result,description,detail) values(?,?,?,?,?,?,?)";
		try {
			con = dataSource.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(clb.getTime().getTime()));
			pstmt.setString(2, clb.getOperator());
			pstmt.setString(3, clb.getAction());
			pstmt.setString(4, clb.getOperateobject());
			pstmt.setString(5, clb.getResult());
			pstmt.setString(6, clb.getDescription());
			pstmt.setString(7, clb.getDetail());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
