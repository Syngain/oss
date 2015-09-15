package com.guanhuodata.photo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.guanhuodata.photo.bean.MaterialChartBean;

/**
 * 
 * @author fudk
 * 素材报表处理工具类
 *
 */
public class MaterialChartUtil {

	private static final Logger LOG = Logger.getLogger(MaterialChartUtil.class);
	
	/*
	 * 素材报表处理
	 */
	public static List<MaterialChartBean> readMaterialExcel(File file) {
		LOG.info("MaterialChartUtil readMaterialExcel fileName:" + file.getName() + " start.");
		//判断报表版本：.xls/.xlsx
		String fileName = file.getName();
		String suffix = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		Workbook wb = null;
		InputStream is = null;
		List<MaterialChartBean> list = new ArrayList<MaterialChartBean>();
		try{
			is = new FileInputStream(file);
			if(suffix.equals(".xls")){
				wb = new HSSFWorkbook(is);
			}else if(suffix.equals(".xlsx")){
				wb = new XSSFWorkbook(is);
			}
			//暂时先取10个工作单元
			/*for(int i = 0;i < 10;i++){
				
			}*/
			//int sheetNum = wb.getNumberOfSheets();
			//String sheetName = wb.getSheetName(0);
			//System.out.println("sheetNum: " + sheetNum + " sheetName: " + sheetName);
			Sheet sheet = wb.getSheetAt(0);
			//wb.getSheetName(arg0)
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
					for(int k = 0;k <= cells; k++){
						//get cell's value
						Cell cell = row_n.getCell(k);
						if(cell != null){
							switch (cell.getCellType()) {
								case Cell.CELL_TYPE_FORMULA:	//公式类型
									colsValue += cell.getNumericCellValue() + ",";
									break;
								case Cell.CELL_TYPE_NUMERIC:	//数值类型
									colsValue += cell.getNumericCellValue() + ",";
									break;
								case Cell.CELL_TYPE_STRING:	//字符串类型
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
					MaterialChartBean materialChartBean = new MaterialChartBean();
					materialChartBean.setOriginalityName(cellVal[0]);
					materialChartBean.setSpreadUnitBasicInfo(cellVal[1]);
					materialChartBean.setAllowSpreadSchedule(cellVal[2]);
					materialChartBean.setDateTime(cellVal[3]);
					//在读取该cell时取出的数据为double类型的，需要转换为long
					materialChartBean.setShow((long)Double.parseDouble(cellVal[4]));
					materialChartBean.setClick((long)Double.parseDouble(cellVal[5]));
					
					materialChartBean.setCTR(Double.parseDouble(cellVal[6]));
					materialChartBean.setConsume(Double.parseDouble(cellVal[7]));
					materialChartBean.setShowCostOf1000(Double.parseDouble(cellVal[8]));
					materialChartBean.setUnitPriceOfClick(Double.parseDouble(cellVal[9]));
					materialChartBean.setRateOfReturn_3(Double.parseDouble(cellVal[10]));
					materialChartBean.setRateOfReturn_7(Double.parseDouble(cellVal[11]));
					materialChartBean.setRateOfReturn_15(Double.parseDouble(cellVal[12]));
					//在读取该cell时取出的数据为double类型的，需要转换为long/int
					materialChartBean.setCustomerOrderNum_3((long)Double.parseDouble(cellVal[13]));
					materialChartBean.setCustomerOrderNum_7((long)Double.parseDouble(cellVal[14]));
					materialChartBean.setCustomerOrderNum_15((long)Double.parseDouble(cellVal[15]));
					materialChartBean.setShopCollectNum((int)Double.parseDouble(cellVal[16]));
					materialChartBean.setGoodsCollectNum((int)Double.parseDouble(cellVal[17]));
					materialChartBean.setVisitor((long)Double.parseDouble(cellVal[18]));
					materialChartBean.setTouchOfUser((long)Double.parseDouble(cellVal[19]));
					
					materialChartBean.setTouchOfFrequency(Double.parseDouble(cellVal[20]));
					
					materialChartBean.setCollectUser((long)Double.parseDouble(cellVal[21]));
					materialChartBean.setShowVisitor_3((long)Double.parseDouble(cellVal[22]));
					materialChartBean.setShowVisitor_7((long)Double.parseDouble(cellVal[23]));
					materialChartBean.setShowVisitor_15((long)Double.parseDouble(cellVal[24]));
					materialChartBean.setShowRateOfReturn_3(Double.parseDouble(cellVal[25]));
					materialChartBean.setShowRateOfReturn_7(Double.parseDouble(cellVal[26]));
					materialChartBean.setRateOfReturn_15(Double.parseDouble(cellVal[27]));
					materialChartBean.setOrderSum_7(Double.parseDouble(cellVal[28]));
					materialChartBean.setOrderSum_15(Double.parseDouble(cellVal[29]));
					materialChartBean.setClickSum_15(Double.parseDouble(cellVal[30]));
					materialChartBean.setShowSum_15(Double.parseDouble(cellVal[31]));
					list.add(materialChartBean);
					//将数据入库mysql
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public static void main(String[] args) {
		String path = "d:" + File.separator + "materialRepo" + File.separator + "雪肌精表_20150914195405.xlsx";
		File file = new File(path);
		List<MaterialChartBean> list = MaterialChartUtil.readMaterialExcel(file);
		System.out.println(list.size());
	}
}
