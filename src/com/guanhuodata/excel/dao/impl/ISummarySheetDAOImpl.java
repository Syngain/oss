package com.guanhuodata.excel.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import com.guanhuodata.excel.bean.DirectSheetBean;
import com.guanhuodata.excel.dao.ISummarySheetDAO;
import com.guanhuodata.excel.util.SummarySheetUtil;
import com.guanhuodata.framework.util.DbUtil;

public class ISummarySheetDAOImpl implements ISummarySheetDAO{

	private SessionFactory sessionFactory;

	/*@Override
	public void summaryByCondition(String[] params, OutputStream os) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(DirectSheetBean.class);
		String groupbys = "";
		for(String param : params){
			//criteria.setProjection(Projections.groupProperty(param));
			groupbys += param + ","; 
		}
		String groupby = groupbys.substring(0, groupbys.lastIndexOf(","));
		参数1 查询的字段名，参数2 分组的字段，参数3 查询结果返回的列名，参数3，返回结果的字段类型  
		criteria.setProjection(Projections.sqlGroupProjection("*", groupby, new String[]{"id","directoryName"}, new Type[]{IntegerType.INSTANCE,StringType.INSTANCE}));
		ProjectionList plist = Projections.projectionList();
		plist.add(Projections.)
		List<DirectSheetBean> list = criteria.list();
		System.out.println(list.size());
		//使用占位符
		StringBuffer hql = new StringBuffer("from DirectSheetBean group by ");
		for(String param : params){
			hql.append(param).append(",");
		}
		String hqls = hql.toString().substring(0, hql.toString().lastIndexOf(",")) + " with rollup";
		Query query = session.createQuery(hqls);
		List<DirectSheetBean> list = query.list();
		System.out.println("list size:" + list.size());
	}*/
	
	public void summaryByCondition(String[] params, OutputStream os) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Connection conn = DbUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String groupbys = "";
		for(String param : params){
			groupbys += param + ","; 
		}
		String groupby = groupbys.substring(0, groupbys.lastIndexOf(","));
		String sql = "select *,sum(reveal),sum(click),sum(CTR),sum(consume),sum(unitPriceOfClick),sum(getUserCost),sum(showRateOfReturn_15),sum(shopCollectNum),sum(goodsCollectNum),sum(collectUser),sum(showCostOf1000) from directsheet group by " + groupby + " with rollup";
		String title = "定向报表汇总";
		groupbys += "reveal,click,CTR,consume,unitPriceOfClick,getUserCost,showRateOfReturn_15,shopCollectNum,goodsCollectNum,collectUser,showCostOf1000";
		String[] headers = SummarySheetUtil.convertENToCN(groupbys);
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为20个字节
	    sheet.setDefaultColumnWidth(20);
		// 创建一个居中格式  
	    HSSFCellStyle cellStyle = workbook.createCellStyle();
	    cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //水平居左
	    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//垂直居中
	    cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
        //产生Excel报表标题行，创建单元格，并设置值表头 设置表头居中 
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
	        HSSFCell cell = row.createCell(i);
	        cell.setCellStyle(cellStyle);
	        HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	        cell.setCellValue(text);
		}
		try{
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			//填充数据
			int index = 0;
			while(rs.next()){
				index++;
				row = sheet.createRow(index);
				//是否是汇总行
				boolean isSumRow = false;
				for(int j = 0;j < headers.length; j++){
					HSSFCell cell = row.createCell(j);
				    //表头对应的字段类型
					String groupByField = groupbys.split(",")[j];
				    String type = SummarySheetUtil.getFieldType(groupByField);
				    if(type.equals("String")){
				    	cell.setCellStyle(cellStyle);
			   		    if(rs.getString(groupByField) == null){
			   		    	isSumRow = true;
			   		    	cell.setCellValue("汇总");
			   		    }else{
			   		    	cell.setCellValue(rs.getString(groupByField));
			   		    }
				    }else if(type.equals("int")){
				    	cell.setCellStyle(cellStyle);
				    	cell.setCellValue(rs.getInt("sum(" + groupByField + ")"));
			   		    /*if(isSumRow){
			   		    	cell.setCellValue(rs.getInt("sum(" + groupByField + ")"));
			   		    }else{
			   		    	cell.setCellValue(rs.getInt(groupByField));
			   		    }*/
				    }else if(type.equals("long")){
				    	cell.setCellStyle(cellStyle);
				    	cell.setCellValue(rs.getLong("sum(" + groupByField + ")"));
				    	/*if(isSumRow){
				    		cell.setCellValue(rs.getLong("sum(" + groupByField + ")"));
				    	}else{
				    		cell.setCellValue(rs.getLong(groupByField));
				    	}*/
				    }else if(type.equals("double")){
				    	cell.setCellStyle(cellStyle);
				    	cell.setCellValue(rs.getDouble("sum(" + groupByField + ")"));
				    	/*if(isSumRow){
				    		cell.setCellValue(rs.getDouble("sum(" + groupByField + ")"));
				    	}else{
				    		cell.setCellValue(rs.getDouble(groupByField));
				    	}*/
				    }else if(type.equals("Date")){
				    	cell.setCellStyle(cellStyle);
			   		    if(rs.getDate(groupByField) == null){
			   		    	cell.setCellValue("汇总");
			   		    }else{
			   		    	cell.setCellValue(sdf.format(rs.getDate(groupByField)));	
			   		    }
				    }
				}
			}
			
			//合并单元格
			int startRowNum = sheet.getFirstRowNum() + 1;	//去表头
			int endRowNum = sheet.getLastRowNum();
			for(int w = 0;w < params.length;w++){
				//需要合并的索引数组
				String record = "";
				int sidx = 0;
				String ss = "";
				for(int d = startRowNum;d <=endRowNum;d++){
					//一行中需要汇总合并的列的数目
					Row mRow = sheet.getRow(d);
					if(record.equals("")){
						record = mRow.getCell(w).getStringCellValue();
						sidx = startRowNum;
					}else{
						if(record.equals(mRow.getCell(w).getStringCellValue())){
							/*if(!record.equals("汇总")){
								continue;
							}*/
							continue;
						}else{
							ss += sidx + "-";
							sidx = d;
							ss += (sidx - 1) + ",";
							record = mRow.getCell(w).getStringCellValue();
						}
					}
					System.out.println(record);
				}
				System.out.println(ss);
				ss = ss.substring(0, ss.lastIndexOf(","));
				//去除里面的汇总
				String wipeOff = "";
				for(int t = 0;t < ss.split(",").length;t++){
					int s = Integer.parseInt(ss.split(",")[t].split("-")[0]);
					int s1 = Integer.parseInt(ss.split(",")[t].split("-")[1]);
					if(!sheet.getRow(s).getCell(w).getStringCellValue().equals("汇总") && !sheet.getRow(s1).getCell(w).getStringCellValue().equals("汇总")){
						wipeOff += ss.split(",")[t] + ",";
					}
				}
				for(int e = 0;e < wipeOff.split(",").length;e++){
					int s = Integer.parseInt(wipeOff.split(",")[e].split("-")[0]);
					int s1 = Integer.parseInt(wipeOff.split(",")[e].split("-")[1]);
					sheet.addMergedRegion(new CellRangeAddress(s, s1, w, w));
				}
			}
			//开始合并汇总的
			for(int k = startRowNum;k <= endRowNum;k++){
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
					colIdx = colIdx.substring(0, colIdx.lastIndexOf("-"));
					if(colIdx.split("-").length > 1){
						System.out.println("colidx: " + colIdx);
						System.out.println("第一个：" + colIdx.split("-")[0] + "第二个：" + colIdx.split("-")[colIdx.split("-").length - 1]);
						System.out.println(colIdx.split("-")[colIdx.split("-").length - 1]);
						int startCol = Integer.parseInt(colIdx.split("-")[0]);
						int endCol = Integer.parseInt(colIdx.split("-")[colIdx.split("-").length - 1]);
						System.out.println("startCol: " + startCol + " endCol: " + endCol);
						sheet.addMergedRegion(new CellRangeAddress(k, k, startCol, endCol));
						//sheet.addMergedRegion(new Region(k,(short)Integer.parseInt(colIdx.split("-")[0]), k, (short)Integer.parseInt(colIdx.split("-")[colIdx.split("-").length - 1])));
					}
				}
			}
			/*int mergedRegions = sheet.getNumMergedRegions();
			System.out.println("getNumMergedRegions: " + sheet.getNumMergedRegions());
			for(int p =0;p < mergedRegions;p++){
				System.out.println("lineFirstNumber:" + sheet.getMergedRegion(p).getFirstRow() + " lineLastNumber:" + sheet.getMergedRegion(p).getLastRow() + " firstCol: " + sheet.getMergedRegion(p).getFirstColumn() + " lastCol: " + sheet.getMergedRegion(p).getLastColumn());
			}*/
			//sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),sheet.getLastRowNum(),0,params.length - 1));
			workbook.writeProtectWorkbook("123456", "fudk");
		    try{
		    	workbook.write(os);
		    }catch(IOException e){
		    	e.printStackTrace();
		    }
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			DbUtil.close(conn, pstmt, rs);
			try {
				workbook.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean readAndInsertToDB(File filePath) {
		boolean flag = false;
		SummarySheetUtil util = new SummarySheetUtil();
		List<DirectSheetBean> list = util.readDirectSheet(filePath);
		if(list.size() > 0){
			for(DirectSheetBean bean : list){
				Session session = getSession();
				Transaction ts = session.beginTransaction();
				session.save(bean);
				ts.commit();
				session.close();
			}
			flag = true;
		}
		return flag;
	}
	
	//判断报表格式
	@Override
	public boolean isOriginalitySheetStyle(File filePath,String sheetType) {
		SummarySheetUtil util = new SummarySheetUtil();
		InputStream is = null;
		Workbook wb = null;
		Sheet sheet = null;
		boolean flag = false;
		String excelVersion = filePath.getName().substring(filePath.getName().lastIndexOf("."),filePath.getName().length());
		try {
			is = new FileInputStream(filePath);
			if(excelVersion.equals(".xls")){
				wb = new HSSFWorkbook(is);
			}else if(excelVersion.equals(".xlsx")){
				wb = new XSSFWorkbook(is);
			}
			sheet = wb.getSheetAt(0);
			String[] titles = util.getTitles(is, wb, sheet);
			for(String title : titles){
				if(title.equals(sheetType)){
					flag = true;
					break;
				}
			}
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
		return flag;
	}
	
	public Session getSession(){
		return sessionFactory.openSession();
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
}
