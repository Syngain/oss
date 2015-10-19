package com.guanhuodata.excel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.guanhuodata.framework.util.DbUtil;

public class Test {

	public static void read(){
		Connection conn = DbUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select *,sum(reveal) as sumreveal from directsheet group by userOrigin,crowdLayer,crowdAttr with rollup";
		try{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int index = 0;
			while(rs.next()){
				index++;
				System.out.println("结果集" + index + ": " + rs.getLong(1) + " " +rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + " " + rs.getString("sumreveal"));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
		//判断报表格式
		public static void readSheetTest(File filePath) {
			InputStream is = null;
			Workbook wb = null;
			Sheet sheet = null;
			String excelVersion = filePath.getName().substring(filePath.getName().lastIndexOf("."),filePath.getName().length());
			try {
				is = new FileInputStream(filePath);
				if(excelVersion.equals(".xls")){
					wb = new HSSFWorkbook(is);
				}else if(excelVersion.equals(".xlsx")){
					wb = new XSSFWorkbook(is);
				}
				sheet = wb.getSheetAt(0);
				//合并单元格
				int startRowNum = sheet.getFirstRowNum() + 1;	//去表头
				int endRowNum = sheet.getLastRowNum();
				//需要合并的索引数组
				String record = "";
				int sidx = 0;
				String ss = "";
				for(int d = startRowNum;d <=endRowNum;d++){
					//一行中需要汇总合并的列的数目
					Row mRow = sheet.getRow(d);
					if(record.equals("")){
						record = mRow.getCell(0).getStringCellValue();
						sidx = startRowNum;
					}else{
						if(record.equals(mRow.getCell(0).getStringCellValue())){
							continue;
						}else{
							ss += sidx + "-";
							sidx = d;
							ss += (sidx - 1) + ",";
							record = mRow.getCell(0).getStringCellValue();
						}
					}
					System.out.println(record);
				}
				System.out.println(ss);
				for(int e = 0;e < ss.split(",").length;e++){
					int s = Integer.parseInt(ss.split(",")[e].split("-")[0]);
					int s1 = Integer.parseInt(ss.split(",")[e].split("-")[1]);
					sheet.addMergedRegion(new CellRangeAddress(s, s1, 0, 0));
				}
				String[] params = {"a","b","c","d"};
				for(int k = startRowNum;k<endRowNum;k++){
					Row rown = sheet.getRow(k);
					System.out.println("第" + k + "行：");
					String colIdx = "";
					for(int s = 0;s < params.length;s++){
						if(rown.getCell(s).getStringCellValue().equals("汇总")){
							colIdx += s + "-";
							System.out.println("第" + s + "列");
						}
					}
					System.out.println("colIdx:" + colIdx + "isNull:" + (colIdx.equals("")));
					if(!colIdx.equals("")){
						if(colIdx.split("-").length > 1){
							System.out.println(colIdx.split("-")[colIdx.split("-").length - 1]);
							sheet.addMergedRegion(new CellRangeAddress(k, k, Integer.parseInt(colIdx.split("-")[0]), Integer.parseInt(colIdx.split("-")[colIdx.split("-").length - 1])));
						}
					}
				}
				sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),sheet.getLastRowNum(),0,params.length - 1));
				wb.write(new FileOutputStream(new File("d://temp//merge.xls")));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				try{
					is.close();
					wb.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	
	public static void main(String[] args) {
		//read();
		File file = new File("d://temp//定向报表汇总2015-10-07 10-09-11.xls");
		readSheetTest(file);
	}
}
