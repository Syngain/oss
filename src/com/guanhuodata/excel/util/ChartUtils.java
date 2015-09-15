package com.guanhuodata.excel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.guanhuodata.excel.bean.ExcelBatchBean;
import com.guanhuodata.excel.bean.ExcelBean;

/**
 * 
 * @author fudk
 * utils of excel read & write
 *
 */
public class ChartUtils {

	//判断报表类型：单日/批量
	//区别在于：是否包含日期
	public String checkChartWetherBatch(String path,String excelVersion){
		Workbook wb = null;
		InputStream is = null;
		//返回值：YES/NOT
		String ret = "";
		try{
			is = new FileInputStream(new File(path));
			if(excelVersion.equals(".xls")){
				wb = new HSSFWorkbook(is);
			}else if(excelVersion.equals(".xlsx")){
				wb = new XSSFWorkbook(is);
			}
			Sheet sheet = wb.getSheetAt(0);
			//第一行表头
			Row row_1 = sheet.getRow(0);
			int cellNum = row_1.getPhysicalNumberOfCells();
			List<String> list = new ArrayList<String>();
			String cellVal = "";
			for(int j = 0; j < cellNum; j++){
				Cell cell = row_1.getCell(j);
				if(cell != null){
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_FORMULA:
							break;
						case Cell.CELL_TYPE_NUMERIC:
							cellVal = cell.getNumericCellValue() + "";
							break;
						case Cell.CELL_TYPE_STRING:
							cellVal =cell.getStringCellValue();
							break;
						default:
							cellVal = "0";
						break;
					}
				}
				list.add(cellVal);
			}
			if(list.contains("时间")){
				ret = "YES";
			}else{
				ret = "NOT";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	
	public List<ExcelBean> readExcelNoBatch(String path, String excelVersion) {
		Workbook wb = null;
		InputStream is = null;
		List<ExcelBean> list = new ArrayList<ExcelBean>();
		try{
			is = new FileInputStream(new File(path));
			if(excelVersion.equals(".xls")){
				wb = new HSSFWorkbook(is);
			}else if(excelVersion.equals(".xlsx")){
				wb = new XSSFWorkbook(is);
			}
			//暂时先取10个工作单元
			/*for(int i = 0;i < 10;i++){
				
			}*/
			Sheet sheet = wb.getSheetAt(0);
			//取到sheet下的所有行数
			int rows = sheet.getPhysicalNumberOfRows();
			//读取第一行的表头
			Row row_1 = sheet.getRow(0);
			StringBuffer sb = new StringBuffer();
			int cell_1_Num = row_1.getPhysicalNumberOfCells();
			String value_1 = "";
			for(int p = 0;p < cell_1_Num;p++){
				Cell cell_1 = row_1.getCell(p);
				if(cell_1 != null){
					switch (cell_1.getCellType()) {
						case Cell.CELL_TYPE_FORMULA:
							break;
						case Cell.CELL_TYPE_NUMERIC:
							value_1 = cell_1.getNumericCellValue() + "";
							break;
						case Cell.CELL_TYPE_STRING:
							value_1 =cell_1.getStringCellValue();
							break;
						default:
							value_1 = "0";
						break;
					}
				}
				sb.append(value_1);
			}
			System.out.println(sb.toString());
			//遍历行
			for(int j = 1;j < rows; j++){
				Row row_n = sheet.getRow(j);
				if(row_n != null){
					//取出行中的所有列
					int cells = row_n.getPhysicalNumberOfCells();
					//遍历列
					String colsValue = "";
					for(int k = 0;k < cells; k++){
						//get cell's value
						Cell cell = row_n.getCell(k);
						if(cell != null){
							switch (cell.getCellType()) {
								case Cell.CELL_TYPE_FORMULA:
									break;
								case Cell.CELL_TYPE_NUMERIC:
									colsValue += cell.getNumericCellValue() + ",";
									break;
								case Cell.CELL_TYPE_STRING:
									colsValue +=cell.getStringCellValue() + ",";
									break;
								default:
									colsValue += "0";
								break;
							}
						}
					}
					System.out.println(colsValue);
					String[] cellVal = colsValue.split(",");
					ExcelBean excelBean = new ExcelBean();
					excelBean.setDirectName(cellVal[0]);
					excelBean.setSpreadUnitBasicInfo(cellVal[1]);
					excelBean.setAllowSpreadSchedule(cellVal[2]);
					excelBean.setShow(Long.parseLong(cellVal[3]));
					excelBean.setClick(Long.parseLong(cellVal[4]));
					excelBean.setCTR(Double.parseDouble(cellVal[5]));
					excelBean.setConsume(Double.parseDouble(cellVal[6]));
					excelBean.setShowCostOf1000(Double.parseDouble(cellVal[7]));
					excelBean.setUnitPriceOfClick(Double.parseDouble(cellVal[8]));
					excelBean.setRateOfReturn_3(Double.parseDouble(cellVal[9]));
					excelBean.setRateOfReturn_7(Double.parseDouble(cellVal[10]));
					excelBean.setRateOfReturn_15(Double.parseDouble(cellVal[11]));
					excelBean.setCustomerOrderNum_3(Long.parseLong(cellVal[12]));
					excelBean.setCustomerOrderNum_7(Long.parseLong(cellVal[13]));
					excelBean.setCustomerOrderNum_15(Long.parseLong(cellVal[14]));
					excelBean.setShopCollectNum(Integer.parseInt(cellVal[15]));
					excelBean.setGoodsCollectNum(Integer.parseInt(cellVal[16]));
					excelBean.setVisitor(Long.parseLong(cellVal[17]));
					excelBean.setTouchOfUser(Long.parseLong(cellVal[18]));
					excelBean.setTouchOfFrequency(Double.parseDouble(cellVal[19]));
					excelBean.setCollectUser(Long.parseLong(cellVal[20]));
					excelBean.setShowVisitor_3(Long.parseLong(cellVal[21]));
					excelBean.setShowVisitor_7(Long.parseLong(cellVal[22]));
					excelBean.setShowVisitor_15(Long.parseLong(cellVal[23]));
					excelBean.setShowRateOfReturn_3(Double.parseDouble(cellVal[24]));
					excelBean.setShowRateOfReturn_7(Double.parseDouble(cellVal[25]));
					excelBean.setShowRateOfReturn_15(Double.parseDouble(cellVal[26]));
					excelBean.setOrderSum_7(Double.parseDouble(cellVal[27]));
					excelBean.setOrderSum_15(Double.parseDouble(cellVal[28]));
					list.add(excelBean);
					//将数据入库mysql
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public List<ExcelBatchBean> readExcelWithBatch(String path, String excelVersion) {
		Workbook wb = null;
		InputStream is = null;
		List<ExcelBatchBean> list = new ArrayList<ExcelBatchBean>();
		try{
			is = new FileInputStream(new File(path));
			if(excelVersion.equals(".xls")){
				wb = new HSSFWorkbook(is);
			}else if(excelVersion.equals(".xlsx")){
				wb = new XSSFWorkbook(is);
			}
			//暂时先取10个工作单元
			/*for(int i = 0;i < 10;i++){
				
			}*/
			Sheet sheet = wb.getSheetAt(0);
			//取到sheet下的所有行数
			int rows = sheet.getPhysicalNumberOfRows();
			//读取第一行的表头
			Row row_1 = sheet.getRow(0);
			StringBuffer sb = new StringBuffer();
			int cell_1_Num = row_1.getPhysicalNumberOfCells();
			String value_1 = "";
			for(int p = 0;p < cell_1_Num;p++){
				Cell cell_1 = row_1.getCell(p);
				if(cell_1 != null){
					switch (cell_1.getCellType()) {
						case Cell.CELL_TYPE_FORMULA:
							break;
						case Cell.CELL_TYPE_NUMERIC:
							value_1 = cell_1.getNumericCellValue() + "";
							break;
						case Cell.CELL_TYPE_STRING:
							value_1 =cell_1.getStringCellValue();
							break;
						default:
							value_1 = "0";
						break;
					}
				}
				sb.append(value_1);
			}
			System.out.println(sb.toString());
			//遍历行
			for(int j = 1;j < rows; j++){
				Row row_n = sheet.getRow(j);
				if(row_n != null){
					//取出行中的所有列
					int cells = row_n.getPhysicalNumberOfCells();
					//遍历列
					String colsValue = "";
					for(int k = 0;k < cells; k++){
						//get cell's value
						Cell cell = row_n.getCell(k);
						if(cell != null){
							switch (cell.getCellType()) {
								case Cell.CELL_TYPE_FORMULA:
									break;
								case Cell.CELL_TYPE_NUMERIC:
									colsValue += cell.getNumericCellValue() + ",";
									break;
								case Cell.CELL_TYPE_STRING:
									colsValue +=cell.getStringCellValue() + ",";
									break;
								default:
									colsValue += "0";
								break;
							}
						}
					}
					System.out.println(colsValue);
					String[] cellVal = colsValue.split(",");
					ExcelBatchBean excelBatchBean = new ExcelBatchBean();
					excelBatchBean.setDirectName(cellVal[0]);
					excelBatchBean.setSpreadUnitBasicInfo(cellVal[1]);
					excelBatchBean.setAllowSpreadSchedule(cellVal[2]);
					excelBatchBean.setBatchDateTime(cellVal[3]);
					excelBatchBean.setShow(Long.parseLong(cellVal[4]));
					excelBatchBean.setClick(Long.parseLong(cellVal[5]));
					excelBatchBean.setCTR(Double.parseDouble(cellVal[6]));
					excelBatchBean.setConsume(Double.parseDouble(cellVal[7]));
					excelBatchBean.setShowCostOf1000(Double.parseDouble(cellVal[8]));
					excelBatchBean.setUnitPriceOfClick(Double.parseDouble(cellVal[9]));
					excelBatchBean.setRateOfReturn_3(Double.parseDouble(cellVal[10]));
					excelBatchBean.setRateOfReturn_7(Double.parseDouble(cellVal[11]));
					excelBatchBean.setRateOfReturn_15(Double.parseDouble(cellVal[12]));
					excelBatchBean.setCustomerOrderNum_3(Long.parseLong(cellVal[13]));
					excelBatchBean.setCustomerOrderNum_7(Long.parseLong(cellVal[14]));
					excelBatchBean.setCustomerOrderNum_15(Long.parseLong(cellVal[15]));
					excelBatchBean.setShopCollectNum(Integer.parseInt(cellVal[16]));
					excelBatchBean.setGoodsCollectNum(Integer.parseInt(cellVal[17]));
					excelBatchBean.setVisitor(Long.parseLong(cellVal[18]));
					excelBatchBean.setTouchOfUser(Long.parseLong(cellVal[19]));
					excelBatchBean.setTouchOfFrequency(Double.parseDouble(cellVal[20]));
					excelBatchBean.setCollectUser(Long.parseLong(cellVal[21]));
					excelBatchBean.setShowVisitor_3(Long.parseLong(cellVal[22]));
					excelBatchBean.setShowVisitor_7(Long.parseLong(cellVal[23]));
					excelBatchBean.setShowVisitor_15(Long.parseLong(cellVal[24]));
					excelBatchBean.setShowRateOfReturn_3(Double.parseDouble(cellVal[25]));
					excelBatchBean.setShowRateOfReturn_7(Double.parseDouble(cellVal[26]));
					excelBatchBean.setShowRateOfReturn_15(Double.parseDouble(cellVal[27]));
					excelBatchBean.setOrderSum_7(Double.parseDouble(cellVal[28]));
					excelBatchBean.setOrderSum_15(Double.parseDouble(cellVal[29]));
					list.add(excelBatchBean);
					//将数据入库mysql
				}
			}
		
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		ChartUtils utils = new ChartUtils();
		String path = "d://upload//定向报表-日数据报表20150906172409.xls";
		String isOrNot = utils.checkChartWetherBatch(path,path.substring(path.lastIndexOf('.'),path.length()));
		if(isOrNot.equals("YES")){
			utils.readExcelWithBatch(path,path.substring(path.lastIndexOf('.'),path.length()));
		}else if(isOrNot.equals("NO")){
			utils.readExcelNoBatch(path,path.substring(path.lastIndexOf('.'),path.length()));
		}
	}
}
