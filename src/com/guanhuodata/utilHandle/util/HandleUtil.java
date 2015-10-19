package com.guanhuodata.utilHandle.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.guanhuodata.framework.util.PathProperty;
import com.guanhuodata.utilHandle.bean.ScheduleBean;
import com.guanhuodata.utilHandle.bean.ScheduleConvertedBean;

/**
 * 
 * @author fudk 计划转换工具类
 */
public class HandleUtil {

	private static final Logger LOG = Logger.getLogger(HandleUtil.class);
	private Workbook wb;
	private Sheet sheet;

	public void readScheduleChartAndWriteToOutputStream(String fileName,OutputStream out,String fileNameInfos,String[] headers) throws FileNotFoundException{
		//String path = "d:" + File.separator + "temp" + File.separator + fileName;
		String path = PathProperty.loadAttribute("scheduleConvertUploadPath") + fileName;
		//InputStream is = new FileInputStream(new File("d://temp//施华蔻-聚变上海-计划-模板.xlsx"));
		InputStream is = new FileInputStream(new File(path));
		try {
			wb = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		/*List<String[]> titleArray = getTitles(is, wb,sheet);
		String titlen = "";
		for (String[] titles : titleArray) {
			for (String title : titles) {
				titlen += title + ",";
				System.out.println(title);
			}
			System.out.println("************************");
		}
		System.out.println("titlen:" + titlen);
		String[] oriHeaders = titlen.split(",");
		String wipeoff = "";
		for(String tit : oriHeaders){
			if(!tit.equals(" ")){
				if(tit.equals("计划名称") || tit.equals("单元名称") || tit.equals("定向方式") || tit.equals("定向名称") || tit.equals("投放资源位") || tit.equals("预算") || tit.equals("溢价") || tit.equals("创意名称") || tit.equals("投放日期") || tit.equals("投放方式") || tit.equals("投放地域(去掉的)") || tit.equals("投放时段(去掉的)")){
					wipeoff += tit + ",";
				}
			}
		}
		System.out.println("wipeoff: " + wipeoff);
		String[] headers = wipeoff.substring(0, wipeoff.lastIndexOf(",")).split(",");*/
		
		List<ScheduleBean> list = new ArrayList<ScheduleBean>();
		Row row = null;
		for (int i = 2; i <= sheet.getLastRowNum(); i++) {
			String rs = "";
			row = sheet.getRow(i);
			for (Cell c : row) {
				boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
				// 判断是否具有合并单元格
				if (isMerge) {
					rs += getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex()) + "//";
					//System.out.print(rs + "//");
				} else {
					rs += c.getRichStringCellValue() + "//";
					//System.out.print(c.getRichStringCellValue() + "//");
				}
			}
			//组装ScheduleBean
			ScheduleBean scheduleBean = wrapScheduleBean(rs);
			list.add(scheduleBean);
		}
		List<ScheduleConvertedBean> convertList = new ArrayList<ScheduleConvertedBean>();
		for(ScheduleBean bean : list){
			String[] standAbbreviationArray = bean.getStandAbbreviation().replace("\n", "").split(",");
			for(String arr : standAbbreviationArray){
				ScheduleConvertedBean convertBean = wrapScheduleConvertedBean(bean,arr,fileNameInfos);
				convertList.add(convertBean);
				//System.out.println("详细：" + regexpPutInResource(arr));
			}
		}
		System.out.println("convert bean list size: " + convertList.size());
		//根据计划与展位相乘查看计划单元数是否超过100，超过100则记录错误行号以及计划名称
		int numRegions = sheet.getNumMergedRegions();
		String count = "";
		for (int k = 0; k < numRegions; k++) {
			CellRangeAddress c = sheet.getMergedRegion(k);
			if (c.getFirstColumn() == 1 && c.getLastColumn() == 1) {
				Row judgeRow = sheet.getRow(c.getFirstRow());
				String cellVal = judgeRow.getCell(4).getStringCellValue().replaceAll("\n", "");
				if (!cellVal.trim().equals("")) {
					if(cellVal.split(",").length * ((c.getLastRow() - c.getFirstRow()) + 1) > 100){
						count += (c.getFirstRow() + 1) + "," + (c.getLastRow() + 1) + "," + judgeRow.getCell(1).getStringCellValue() + "&";
					}
				}
			}
		}
		if(!"".equals(count)){
			count = count.substring(0, count.lastIndexOf("&"));
		}
		System.out.println(count);
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//开始导出
		String title = "导出计划模板";
		createWorkbook(title,convertList,headers,out,count);
	}
	
	private void createWorkbook(String title,List<ScheduleConvertedBean> convertList, String[] headers,OutputStream out,String count) {
		// TODO Auto-generated method stub
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
	    sheet.setDefaultColumnWidth(45);
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
		
		//填充数据
		int index = 0;
		for(ScheduleConvertedBean bean : convertList){
			index++;
			row = sheet.createRow(index);
			for(int j = 0;j < headers.length; j++){
				   HSSFCell cell = row.createCell(j);
				   switch(j){
				   		case 0:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(bean.getScheduleName());	//设计名称
				   			break;
				   		case 1:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(bean.getUnitName());	//单元名称
				   			break;
				   		case 2:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(bean.getDirectoryWay());	//定向方式
				   			break;
				   		case 3:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(bean.getDirectoryName());	//定向名称
				   			break;
				   		case 4:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(bean.getPutInResource());	//投放资源位
				   			break;
				   		case 5:
				   			cell.setCellStyle(cellStyle);
				   			String budget = bean.getBudget().substring(0, bean.getBudget().lastIndexOf("."));
				   			cell.setCellValue(budget);	//预算
				   			break;
				   		case 6:
				   			cell.setCellStyle(cellStyle);
				   			String premium = bean.getPremium().substring(0, bean.getPremium().lastIndexOf("."));
				   			cell.setCellValue(premium);	//溢价
				   			break;
				   		case 7:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(bean.getOriginalityName());	//创意名称
				   			break;
				   		case 8:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(bean.getPutInDate());//投放日期
				   			break;
				   		case 9:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(bean.getPutInWay());	//投放方式
				   			break;
				   		case 10:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(bean.getPutInRegion());	//投放地域
				   			break;
				   		case 11:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(bean.getPutInTimeInterval());	//投放时段
				   			break;
				   		default:
				   			cell.setCellStyle(cellStyle);
				   			cell.setCellValue(0);
				   			break;
				   }
			   }
		}
		if(!"".equals(count)){
			String exceptionTitle = "大于100的单元错误信息";
			String[] exceptionHeaders = {"计划名称","开始行","结束行"};
			// 生成一个表格
			HSSFSheet sheet1 = workbook.createSheet(exceptionTitle);
			// 创建一个居中格式  
		    HSSFCellStyle exceptionCellStyle = workbook.createCellStyle();
		    exceptionCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		    exceptionCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		    exceptionCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		    exceptionCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
	        //水平居左
		    exceptionCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//垂直居中
		    exceptionCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			
	        //产生Excel报表标题行，创建单元格，并设置值表头 设置表头居中 
			HSSFRow exceptionRow = sheet1.createRow(0);
			for (short i = 0; i < exceptionHeaders.length; i++) {
		        HSSFCell exceptionCell = exceptionRow.createCell(i);
		        exceptionCell.setCellStyle(cellStyle);
		        HSSFRichTextString exceptionText = new HSSFRichTextString(exceptionHeaders[i]);
		        exceptionCell.setCellValue(exceptionText);
			}
			String[] exceptionVal = count.split("&");
			//填充数据
			int idx = 0;
			for(String val : exceptionVal){
				idx++;
				row = sheet1.createRow(idx);
				for(int j = 0;j < exceptionHeaders.length; j++){
					   HSSFCell exceptionCells = row.createCell(j);
					   switch(j){
					   		case 0:
					   			exceptionCells.setCellStyle(exceptionCellStyle);
					   			exceptionCells.setCellValue(val.split(",")[2]);	//设计名称
					   			break;
					   		case 1:
					   			exceptionCells.setCellStyle(exceptionCellStyle);
					   			exceptionCells.setCellValue(val.split(",")[0]);	//开始行
					   			break;
					   		case 2:
					   			exceptionCells.setCellStyle(exceptionCellStyle);
					   			exceptionCells.setCellValue(val.split(",")[1]);	//结束行
					   			break;
					   		default:
					   			exceptionCells.setCellStyle(exceptionCellStyle);
					   			exceptionCells.setCellValue(0);
					   			break;
					   }
				   }
			}
		}
		workbook.writeProtectWorkbook("123456", "fudk");
	    try{
	    	workbook.write(out);
	    }catch(IOException e){
	    	e.printStackTrace();
	    }
	}

	//包装
	private ScheduleConvertedBean wrapScheduleConvertedBean(ScheduleBean bean,String arr,String fileNameInfos) {
		ScheduleConvertedBean convertBean = new ScheduleConvertedBean();
		convertBean.setScheduleName(bean.getScheduleName());	//计划名称
		//单元名称（所属类目_定向名称_展位）合成
		if(!"".equals(bean.getAllowCategory())){
			convertBean.setUnitName(bean.getAllowCategory() + "_" + bean.getDirectoryName() + "_" + arr);	//单元名称
		}else{
			convertBean.setUnitName(bean.getDirectoryName() + "_" + arr);	//单元名称
		}
		convertBean.setDirectoryWay(bean.getDirectoryWay());	//定向方式
		convertBean.setDirectoryName(bean.getDirectoryName());	//定向名称
		convertBean.setPutInResource(regexpPutInResource(arr));	//投放资源位
		convertBean.setBudget(bean.getBudget());	//预算
		convertBean.setPremium(bean.getPremium());	//溢价
		/**
		 * 根据条件判断创意名称
		 * 特殊情况：会包含换行符\n
		 */
		/**
		 * 修改后的规则：
		 * 根据人群类与展位简称来判断创意名称
		 * 正常情况下，是按照人群类与展位简称来判断创意名称
		 * 下面是特殊情况
		 * 创意名称第一列为GH则不判断人群类->所有人群都会添加
		 * 创意名称第一列为GHP，则先去判断是否是品宣，然后判断展位
		 * 创意名称第一列为GHX，则先去判断是否是销售，然后判断展位
		 */
		//拆分人群类
		String[] crowdClass = bean.getCrowdClass().trim().replace("\n", "").split(",");
		String[] fileNameInfoSplit = fileNameInfos.split("&");
		String originalityName = "";
		for(String crowdClazz : crowdClass){
			for(String fileNameInfo : fileNameInfoSplit){
				//正常情况下的判断：人群类&展位简称匹配
				if(fileNameInfo.split("_")[0].equals(crowdClazz) && fileNameInfo.split("_")[3].equals(regexpStandAbbreviationWithMaterial(arr))){
					originalityName += fileNameInfo + ",";
					continue;
				}
				//情况I：当创意名称按照下划线(_)拆分后第一列为GH时,不需要判断人群类
				if(fileNameInfo.split("_")[0].equals("GH") && fileNameInfo.split("_")[3].equals(regexpStandAbbreviationWithMaterial(arr))){
					originalityName += fileNameInfo + ",";
					continue;
				}
				//情况2：当创意名称按照下划线(_)拆分后第一列为GHP时，不需要判断人群类，需要判断投放目的：品宣
				if(fileNameInfo.split("_")[0].equals("GHP") && bean.getPutInPurpose().equals("品宣") && fileNameInfo.split("_")[3].equals(regexpStandAbbreviationWithMaterial(arr))){
					originalityName += fileNameInfo + ",";
					continue;
				}
				//情况3：当创意名称按照下划线(_)拆分后第一列为GHX时，不需要判断人群类，需要判断投放目的：销售
				if(fileNameInfo.split("_")[0].equals("GHX") && bean.getPutInPurpose().equals("销售") && fileNameInfo.split("_")[3].equals(regexpStandAbbreviationWithMaterial(arr))){
					originalityName += fileNameInfo + ",";
					continue;
				}
			}
		}
		String refreshOriginalityName = "";
		if(!"".equals(originalityName)){
			refreshOriginalityName = originalityName.substring(0, originalityName.lastIndexOf(","));
		}
		/*String originalityName = bean.getOriginalityName();
		originalityName = originalityName.replace("\n", "");
		String[] originalityNameArray = originalityName.split(",");
		String refreshOriginalityName = "";
		for(String oname : originalityNameArray){
			if(oname.contains(regexpStandAbbreviationWithMaterial(arr))){
				refreshOriginalityName += oname + ",";
			}
		}
		if(!"".equals(refreshOriginalityName)){
			refreshOriginalityName = refreshOriginalityName.substring(0, refreshOriginalityName.lastIndexOf(","));
		}*/
		convertBean.setOriginalityName(refreshOriginalityName);	//创意名称
		convertBean.setPutInDate(bean.getPutInDate());	//投放日期
		convertBean.setPutInWay(bean.getPutInWay());	//投放方式
		convertBean.setPutInRegion(bean.getPutInRegion());	//投放地域
		convertBean.setPutInTimeInterval(bean.getPutInTimeInterval());	//投放时段
		return convertBean;
	}

	//包装
	private ScheduleBean wrapScheduleBean(String rs) {
		rs = rs.substring(0, rs.lastIndexOf("//"));
		String[] oriArray = rs.split("//");
		ScheduleBean scheduleBean = new ScheduleBean();
		scheduleBean.setPutInPurpose(oriArray[0]);
		scheduleBean.setScheduleName(oriArray[1]);
		scheduleBean.setAllowCategory(oriArray[2]);
		scheduleBean.setDirectoryName(oriArray[3]);
		scheduleBean.setStandAbbreviation(oriArray[4]);
		scheduleBean.setUnitName(oriArray[2] + "_" + oriArray[3] + "_" + oriArray[4]);
		scheduleBean.setDirectoryWay(oriArray[5]);
		scheduleBean.setBudget(oriArray[6]);
		scheduleBean.setPremium(oriArray[7]);
		scheduleBean.setCrowdClass(oriArray[8]);
		//scheduleBean.setOriginalityName(oriArray[8]);
		scheduleBean.setPutInDate(oriArray[9]);
		scheduleBean.setPutInWay(oriArray[10]);
		scheduleBean.setPutInRegion(oriArray[11]);
		scheduleBean.setPutInTimeInterval(oriArray[12]);
		return scheduleBean;
	}

	/**   
	* 获取合并单元格的值   
	* @param sheet   
	* @param row   
	* @param column   
	* @return   
	*/    
	public String getMergedRegionValue(Sheet sheet ,int row , int column){    
	    int sheetMergeCount = sheet.getNumMergedRegions();    
	        
	    for(int i = 0 ; i < sheetMergeCount ; i++){    
	        CellRangeAddress ca = sheet.getMergedRegion(i);    
	        int firstColumn = ca.getFirstColumn();    
	        int lastColumn = ca.getLastColumn();    
	        int firstRow = ca.getFirstRow();    
	        int lastRow = ca.getLastRow();    
	            
	        if(row >= firstRow && row <= lastRow){    
	                
	            if(column >= firstColumn && column <= lastColumn){    
	                Row fRow = sheet.getRow(firstRow);    
	                Cell fCell = fRow.getCell(firstColumn);    
	                return getCellValue(fCell) ;    
	            }    
	        }    
	    }    
	        
	    return null ;    
	}    
	  
	/**   
	* 获取单元格的值   
	* @param cell   
	* @return   
	*/    
	public String getCellValue(Cell cell) {
		if (cell == null)
			return "";
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return cell.getCellFormula();
		} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return String.valueOf(cell.getNumericCellValue());
		}
		return "";
	}
	
	/**  
	* 判断合并了行  
	* @param sheet  
	* @param row  
	* @param column  
	* @return  
	*/  
	@SuppressWarnings("unused")
	private boolean isMergedRow(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row == firstRow && row == lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	} 
	  
	/**  
	* 判断指定的单元格是否是合并单元格  
	* @param sheet   
	* @param row 行下标  
	* @param column 列下标  
	* @return  
	*/  
	private boolean isMergedRegion(Sheet sheet, int row, int column) {
		int sheetMergeCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeCount; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			int firstColumn = range.getFirstColumn();
			int lastColumn = range.getLastColumn();
			int firstRow = range.getFirstRow();
			int lastRow = range.getLastRow();
			if (row >= firstRow && row <= lastRow) {
				if (column >= firstColumn && column <= lastColumn) {
					return true;
				}
			}
		}
		return false;
	}
	  
	/**  
	* 判断sheet页中是否含有合并单元格   
	* @param sheet   
	* @return  
	*/  
	@SuppressWarnings("unused")
	private boolean hasMerged(Sheet sheet) {
		return sheet.getNumMergedRegions() > 0 ? true : false;
	}
	
	/**
	 * 读取Excel表格表头的内容
	 * 
	 * @param InputStream
	 * @return String 表头内容的数组
	 * @throws FileNotFoundException
	 */
	public List<String[]> getTitles(InputStream is, Workbook wb, Sheet sheet) throws FileNotFoundException {
		LOG.info("handle util to read schedule chart and write to another by pattern start.");
		List<String[]> listArray = new ArrayList<String[]>();
		// 表头第一行
		Row row = sheet.getRow(0);
		// 表头第二行
		Row mergedRow = sheet.getRow(1);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		int mergedColNum = mergedRow.getPhysicalNumberOfCells();
		//System.out.println("colNum:" + colNum);
		//System.out.println("mergedColNum:" + mergedColNum);
		String[] title_1 = new String[colNum];
		String[] title_2 = new String[mergedColNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title_1[i] = getCellFormatValue(row.getCell((short) i));
		}
		for (int j = 0; j < mergedColNum; j++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title_2[j] = getCellFormatValue(mergedRow.getCell((short) j));
		}
		listArray.add(title_1);
		listArray.add(title_2);
		return listArray;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(Cell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case XSSFCell.CELL_TYPE_NUMERIC:
			case XSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);
				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
				// 如果当前Cell的Type为STRIN
			case XSSFCell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;
	}

	/**
	 * 获取单元格数据内容为日期类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	private String getDateCellValue(Cell cell) {
		String result = "";
		try {
			int cellType = cell.getCellType();
			if (cellType == XSSFCell.CELL_TYPE_NUMERIC) {
				Date date = cell.getDateCellValue();
				result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
			} else if (cellType == XSSFCell.CELL_TYPE_STRING) {
				String date = getStringCellValue(cell);
				result = date.replaceAll("[年月]", "-").replace("日", "").trim();
			} else if (cellType == XSSFCell.CELL_TYPE_BLANK) {
				result = "";
			}
		} catch (Exception e) {
			System.out.println("日期格式不正确!");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	@SuppressWarnings("unused")
	private String getStringCellValue(Cell cell) {
		String strCell = "";
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}

	/**
	 * 
	 * 匹配投放资源位
	 * @param args
	 * 
	 */
	public static String regexpPutInResource(String standAbbreviation) {
		String putInResource = "";
		for (StandAbbrVsResNameCorrespondSpec spec : StandAbbrVsResNameCorrespondSpec.values()) {
			if (spec.getStandAbbreviation_operator().equals(standAbbreviation)) {
				putInResource = spec.getResourceName();
				break;
			} else {
				continue;
			}
		}
		if (putInResource.equals("")) {
			putInResource = "未匹配";
		}
		return putInResource;
	}
	
	/**
	 * 
	 * 匹配展位简称-素材命名用
	 * 
	 * @param args
	 * 
	 */
	public static String regexpStandAbbreviationWithMaterial(String standAbbreviation) {
		String standAbbreviationMaterial = "";
		for (StandAbbrVsResNameCorrespondSpec spec : StandAbbrVsResNameCorrespondSpec.values()) {
			if (spec.getStandAbbreviation_operator().equals(standAbbreviation)) {
				standAbbreviationMaterial = spec.getStandAbbreviation_material();
				break;
			} else {
				continue;
			}
		}
		if (standAbbreviationMaterial.equals("")) {
			standAbbreviationMaterial = "未匹配";
		}
		return standAbbreviationMaterial;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		new HandleUtil().readScheduleChartAndWriteToOutputStream("施华蔻-聚变上海-计划-模板.xlsx", new FileOutputStream(new File("d://temp//test.xls")),"",new String[10]);
	}
}
