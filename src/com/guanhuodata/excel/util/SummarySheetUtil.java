package com.guanhuodata.excel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.guanhuodata.excel.bean.DirectSheetBean;
import com.guanhuodata.framework.exception.NameSchemaException;
import com.guanhuodata.framework.exception.SplitAbbreviationCompileException;

public class SummarySheetUtil {

	private static final Logger LOG = Logger.getLogger(SummarySheetUtil.class);
	
	/**
	 * 解析定向报表
	 */
	public List<DirectSheetBean> readDirectSheet(File file){
		Workbook wb = null;
		InputStream is = null;
		String excelVersion = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
		List<DirectSheetBean> list = new ArrayList<DirectSheetBean>();
		SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			is = new FileInputStream(file);
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
							value_1 = cell_1.getNumericCellValue() + ",";
							break;
						case Cell.CELL_TYPE_STRING:
							value_1 =cell_1.getStringCellValue() + ",";
							break;
						default:
							value_1 = "0,";
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
					DirectSheetBean bean = new DirectSheetBean();
					bean.setDirectoryName(cellVal[0]);	//定向名称
					bean.setUnitName(cellVal[1]);	//单元名称
					bean.setSpreadSchedule(cellVal[2]);	//计划名称
					//单元名称为3列时：品宣，2列时：销售
					//单元名称拆分
					//计划为4列时：品宣，3列时：销售
					//计划拆分
					String[] unitNameSplit = cellVal[1].split("_");
					String[] spreadScheduleSplit = cellVal[2].split("_");
					if(unitNameSplit.length == 2 && spreadScheduleSplit.length == 3){	//销售
						bean.setPutInPurpose("销售");	//投放目的
						bean.setCrowdInfo(regexpCrowdInfo(unitNameSplit[0]));	//人群信息
						bean.setStandInfo(isChinese(unitNameSplit[1]) ? unitNameSplit[1] : regexpStandInfo(unitNameSplit[1]));	//展位信息
						
						//销售	计划拆分：推广主题、人群分层、投放时段
						bean.setAllowdSpreadTheme(spreadScheduleSplit[0]);	//所属推广主题
						bean.setCrowdLayer(isChinese(spreadScheduleSplit[1]) ? spreadScheduleSplit[1] : regexpCrowdLayer(spreadScheduleSplit[1]));	//人群分层
						bean.setTimeInterval(spreadScheduleSplit[2]);	//投放时段
						
						//人群分层
						String crowdLayer = isChinese(spreadScheduleSplit[1].trim()) ? spreadScheduleSplit[1].trim() : regexpCrowdLayer(spreadScheduleSplit[1].trim());
						//人群分层为：浏览用户、关注用户、未付款用户时用户来源为店铺自有非交易用户，人群分层为：交易用户、沉默用户时为店铺自有交易用户
						if(crowdLayer.contains("浏览用户") || crowdLayer.contains("关注用户") || crowdLayer.contains("未付款用户")){
							bean.setUserOrigin("店铺自有非交易用户");	//用户来源
							bean.setAllowSpreadSchedule("用户转化");	//所属推广计划
							
							//浏览用户（销售人群信息包含"浏览"）
							if(regexpCrowdInfo(unitNameSplit[0].trim()).contains("浏览")){
								bean.setCrowdAttr("浏览用户");
							}else if(regexpCrowdInfo(unitNameSplit[0].trim()).contains("访客") || regexpCrowdInfo(unitNameSplit[0].trim()).contains("定向店铺")){	//访客（包含"访客和定向店铺自己"）
								bean.setCrowdAttr("访客");
							}else if(regexpCrowdInfo(unitNameSplit[0].trim()).contains("收藏宝贝")){	//收藏宝贝（包含"收藏宝贝"）
								bean.setCrowdAttr("收藏宝贝");
							}else if(regexpCrowdInfo(unitNameSplit[0].trim()).contains("收藏店铺") || regexpCrowdInfo(unitNameSplit[0].trim()).contains("微淘粉丝") || regexpCrowdInfo(unitNameSplit[0].trim()).contains("有店铺优惠券")){	//收藏店铺（包含"收藏店铺，微淘粉丝，有店铺优惠券"）
								bean.setCrowdAttr("收藏店铺");
							}else if(regexpCrowdInfo(unitNameSplit[0].trim()).contains("加购物车")){	//加购物车（包含"加购物车"）
								bean.setCrowdAttr("加购物车");
							}else if(regexpCrowdInfo(unitNameSplit[0].trim()).contains("下单未付款")){	//下单未付款（包含"下单未付款"）
								bean.setCrowdAttr("下单未付款");
							}else if(regexpCrowdInfo(unitNameSplit[0].trim()).contains("沉默用户")){
								bean.setCrowdAttr("沉默用户");
							}
							else {
								LOG.error("crowd info has not contains pattern.");
								throw new NameSchemaException("人群信息中未包含给定的匹配信息:用户转化(unknown crowd info.)");
							}
						}else if(crowdLayer.contains("交易用户") || crowdLayer.contains("沉默用户")){
							bean.setUserOrigin("店铺自有交易用户");	//用户来源
							bean.setAllowSpreadSchedule("用户保持");	//所属推广计划
							
							//交易用户（包含“交易”不包含“180至”）
							if(regexpCrowdInfo(unitNameSplit[0].trim()).contains("交易") && !(regexpCrowdInfo(unitNameSplit[0].trim()).contains("180至"))){	
								bean.setCrowdAttr("交易用户");
							}else if(regexpCrowdInfo(unitNameSplit[0].trim()).contains("交易") && regexpCrowdInfo(unitNameSplit[0].trim()).contains("180至")){//沉默用户(包含“交易”且包含“180至”）
								bean.setCrowdAttr("沉默用户");
							}else if(regexpCrowdInfo(unitNameSplit[0].trim()).contains("沉默用户")){
								bean.setCrowdAttr("沉默用户");
							}else{
								LOG.error("crowd info has not contains pattern.");
								throw new NameSchemaException("人群信息中未包含给定的匹配信息:用户保持(unknown crowd info.)");
							}
						}else{
							LOG.error("crowd layer is not in pattern.");
							throw new NameSchemaException("人群分层的命名不符合规范(unknown name when set user origin().)");
						}
					}else if(unitNameSplit.length == 3 && spreadScheduleSplit.length == 4){	//品宣
						bean.setPutInPurpose("品宣");	//投放目的
						bean.setAllowCategory(isChinese(unitNameSplit[0]) ? unitNameSplit[0] : regexpAllowCategory(unitNameSplit[0]));	//所属类目
						bean.setCrowdInfo(isChinese(unitNameSplit[1]) ? unitNameSplit[1] : regexpCrowdInfo(unitNameSplit[1]));	//人群信息
						bean.setStandInfo(isChinese(unitNameSplit[2]) ? unitNameSplit[2] : regexpStandInfo(unitNameSplit[2]));	//展位信息
						
						//人群属性为：全网用户、类目池用户、品牌池用户时对应的：人群分层(目标用户)、用户来源(非店铺自有用户)、所属推广计划(用户拓展)
						bean.setAllowdSpreadTheme(spreadScheduleSplit[0]);	//推广主题
						bean.setCrowdAttr(isChinese(spreadScheduleSplit[1]) ? spreadScheduleSplit[1] : regexpCrowdAttr(spreadScheduleSplit[1]));	//人群属性
						bean.setTimeInterval(spreadScheduleSplit[2]); //投放时段
						bean.setIsTest(isChinese(spreadScheduleSplit[3]) ? spreadScheduleSplit[3] : regexpIsTest(spreadScheduleSplit[3])); //是否测试
						
						//人群属性为：全网用户、类目池用户、品牌池用户时
						//人群分层为：目标用户	用户来源为：非店铺自有用户		所属推广计划为：用户拓展
						if(regexpCrowdInfo(spreadScheduleSplit[1].trim()).contains("全网用户") || regexpCrowdInfo(spreadScheduleSplit[1].trim()).contains("类目池用户") || regexpCrowdInfo(spreadScheduleSplit[1].trim()).contains("品牌池用户")){
							bean.setCrowdLayer("目标用户");
							bean.setUserOrigin("非店铺自有用户");
							bean.setAllowSpreadSchedule("用户拓展");
						}else{
							LOG.error("crowd attribute is not in pattern.");
							throw new NameSchemaException("品宣的人群属性命名不符合规范(unknown crowd attribute info.)");
						}
					}else{
						LOG.error("unitNameSplit split length is unknown");
						throw new NameSchemaException("单元/计划命名不符合规范(unknown length when split with _)");
					}
					
					//平台：
					//无线平台：展位信息包含“无线,app,wap，触摸版，触摸板”
					 //PC平台：不包含“无线,app,wap，触摸版，触摸板”
					if(bean.getStandInfo().trim().contains("无线") || bean.getStandInfo().trim().contains("app") || bean.getStandInfo().trim().contains("wap") || bean.getStandInfo().trim().contains("触摸版") || bean.getStandInfo().trim().contains("触摸板")){
						bean.setPaltform("无线");
					}
					if(!bean.getStandInfo().trim().contains("无线") && !bean.getStandInfo().trim().contains("app") && !bean.getStandInfo().trim().contains("wap") && !bean.getStandInfo().trim().contains("触摸版") && !bean.getStandInfo().trim().contains("触摸板")){
						bean.setPaltform("PC");
					}
					//表头数组(转换之后的)
					String[] titlesArray = convertTitileToAttr(sb.toString());
					//反射
					Class clazz = DirectSheetBean.class;
					Field[] fields = clazz.getDeclaredFields();
					Method[] methods = clazz.getDeclaredMethods();
					for(int h = 3; h < titlesArray.length; h++){
						System.out.println(cellVal[h]);
						String attrName = "set" + titlesArray[h].trim();
						for(Method method : methods){
							if(method.getName().toUpperCase().equals(attrName.toUpperCase())){
								for(Field field : fields){
									if(field.getName().equals(titlesArray[h].trim())){
										if(field.getType().getName().equals("java.lang.String")){
											method.invoke(bean, cellVal[h].trim());
											break;
										}
										if(field.getType().getName().equals("long")){
											method.invoke(bean, Long.parseLong(cellVal[h].trim()));
											break;
										}
										if(field.getType().getName().equals("double")){
											method.invoke(bean, Double.parseDouble(cellVal[h].trim()));
											break;
										}
										if(field.getType().getName().equals("int")){
											method.invoke(bean, Integer.parseInt(cellVal[h].trim()));
											break;
										}
										if(field.getType().getName().equals("java.util.Date")){
											method.invoke(bean, dateTimeSdf.parse(cellVal[h].trim()));
											break;
										}
									}
								}
								break;
							}
						}
					}
					/*bean.setDateTime(dateTimeSdf.parse(cellVal[3].trim()));
					bean.setReveal(Double.parseDouble(cellVal[4].trim()));
					bean.setClick(Double.parseDouble(cellVal[5].trim()));
					bean.setCTR(Double.parseDouble(cellVal[6].trim()));
					bean.setConsume(Double.parseDouble(cellVal[7].trim()));
					bean.setShowCostOf1000(Double.parseDouble(cellVal[8].trim()));
					bean.setUnitPriceOfClick(Double.parseDouble(cellVal[9].trim()));
					bean.setRateOfReturn_3(Double.parseDouble(cellVal[10].trim()));
					bean.setRateOfReturn_7(Double.parseDouble(cellVal[11].trim()));
					bean.setRateOfReturn_15(Double.parseDouble(cellVal[12].trim()));
					bean.setOutputOfClick_3(Double.parseDouble(cellVal[13].trim()));
					bean.setOutputOfClick_7(Double.parseDouble(cellVal[14].trim()));
					bean.setOutputOfClick_15(Double.parseDouble(cellVal[15].trim()));
					bean.setCustomerOrderNum_3(Long.parseLong(cellVal[16].trim()));
					bean.setCustomerOrderNum_7(Long.parseLong(cellVal[17].trim()));
					bean.setCustomerOrderNum_15(Long.parseLong(cellVal[18].trim()));
					bean.setShopCollectNum(Integer.parseInt(cellVal[19].trim()));
					bean.setGoodsCollectNum(Integer.parseInt(cellVal[20].trim()));
					bean.setVisitor(Long.parseLong(cellVal[21].trim()));
					bean.setGetUserCost(Double.parseDouble(cellVal[22].trim()));
					bean.setTouchOfUser(Long.parseLong(cellVal[23].trim()));
					bean.setTouchOfFrequency(Double.parseDouble(cellVal[24].trim()));
					bean.setCollectUser(Long.parseLong(cellVal[25].trim()));
					bean.setShowVisitor_3(Long.parseLong(cellVal[26].trim()));
					bean.setShowVisitor_7(Long.parseLong(cellVal[27].trim()));
					bean.setShowVisitor_15(Long.parseLong(cellVal[28].trim()));
					bean.setShowRateOfReturn_3(Double.parseDouble(cellVal[29].trim()));
					bean.setShowRateOfReturn_7(Double.parseDouble(cellVal[30].trim()));
					bean.setShowRateOfReturn_15(Double.parseDouble(cellVal[31].trim()));*/
					bean.setTimestamps(sdf.parse(sdf.format(new Date())));	//设置记录被读取后时间戳
					list.add(bean);
					//将数据入库mysql
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 
	 */
	public static String getFieldType(String header){
		String fieldType = "";
		//反射
		Class clazz = DirectSheetBean.class;
		Field[] fields = clazz.getDeclaredFields();
		for(int i =0;i < fields.length;i++ ){
			if(fields[i].getName().toUpperCase().equals(header.toUpperCase())){
				if(fields[i].getType().getName().equals("java.lang.String")){
					fieldType = "String";
				}
				if(fields[i].getType().getName().equals("long")){
					fieldType = "long";
				}
				if(fields[i].getType().getName().equals("double")){
					fieldType = "double";
				}
				if(fields[i].getType().getName().equals("int")){
					fieldType = "int";
				}
				if(fields[i].getType().getName().equals("java.util.Date")){
					fieldType = "Date";
				}
			}
		}
		return fieldType;
	}
	/**
	 * 解析创意报表
	 */
	/*public List<>*/
	
	/**
	 * 读取Excel表格表头的内容
	 * @param InputStream
	 * @return String 表头内容的数组
	 * @throws FileNotFoundException
	 */
	public String[] getTitles(InputStream is, Workbook wb, Sheet sheet) throws FileNotFoundException {
		LOG.info("get sheet titles array start.");
		// 表头第一行
		Row row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
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
	 * 
	 * @param 表头
	 */
	public String[] convertTitileToAttr(String titles){
		String[] titlesArray = titles.split(",");
		for(int i = 0;i < titlesArray.length;i++){
			if(titlesArray[i].trim().equals("所属推广主题")){
				titlesArray[i] = "allowdSpreadTheme";
			}
			if(titlesArray[i].trim().equals("投放目的")){
				titlesArray[i] = "putInPurpose";
			}
			if(titlesArray[i].trim().equals("人群信息")){
				titlesArray[i] = "crowdInfo";
			}
			if(titlesArray[i].trim().equals("用户来源")){
				titlesArray[i] = "userOrigin";
			}
			if(titlesArray[i].trim().equals("人群分层")){
				titlesArray[i] = "crowdLayer";
			}
			if(titlesArray[i].trim().equals("人群属性")){
				titlesArray[i] = "crowdAttr";
			}
			if(titlesArray[i].trim().equals("平台")){
				titlesArray[i] = "platform";
			}
			if(titlesArray[i].trim().equals("展位")){
				titlesArray[i] = "standInfo";
			}
			if(titlesArray[i].trim().equals("定向名称")){
				titlesArray[i] = "directoryName";
			}
			if(titlesArray[i].trim().equals("推广单元基本信息")){
				titlesArray[i] = "unitName";
			}
			if(titlesArray[i].trim().equals("所属推广计划")){
				titlesArray[i] = "spreadSchedule";
			}
			if(titlesArray[i].trim().equals("时间")){
				titlesArray[i] = "dateTimes";
			}
			if(titlesArray[i].trim().equals("展现")){
				titlesArray[i] = "reveal";
			}
			if(titlesArray[i].trim().equals("点击")){
				titlesArray[i] = "click";
			}
			if(titlesArray[i].trim().equals("点击率(%)")){
				titlesArray[i] = "CTR";
			}
			if(titlesArray[i].trim().equals("消耗")){
				titlesArray[i] = "consume";
			}
			if(titlesArray[i].trim().equals("千次展现成本(元)")){
				titlesArray[i] = "showCostOf1000";
			}
			if(titlesArray[i].trim().equals("点击单价")){
				titlesArray[i] = "unitPriceOfClick";
			}
			if(titlesArray[i].trim().equals("3天回报率")){
				titlesArray[i] = "rateOfReturn_3";
			}
			if(titlesArray[i].trim().equals("7天回报率")){
				titlesArray[i] = "rateOfReturn_7";
			}
			if(titlesArray[i].trim().equals("15天回报率")){
				titlesArray[i] = "rateOfReturn_15";
			}
			if(titlesArray[i].trim().equals("3天顾客订单数")){
				titlesArray[i] = "customerOrderNum_3";
			}
			if(titlesArray[i].trim().equals("7天顾客订单数")){
				titlesArray[i] = "customerOrderNum_7";
			}
			if(titlesArray[i].trim().equals("15天顾客订单数")){
				titlesArray[i] = "customerOrderNum_15";
			}
			if(titlesArray[i].trim().equals("店辅收藏数")){
				titlesArray[i] = "shopCollectNum";
			}
			if(titlesArray[i].trim().equals("宝贝收藏数")){
				titlesArray[i] = "goodsCollectNum";
			}
			if(titlesArray[i].trim().equals("访客")){
				titlesArray[i] = "visitor";
			}
			if(titlesArray[i].trim().equals("访客获取成本")){
				titlesArray[i] = "getUserCost";
			}
			if(titlesArray[i].trim().equals("触达用户")){
				titlesArray[i] = "touchOfUser";
			}
			if(titlesArray[i].trim().equals("触达频次")){
				titlesArray[i] = "touchOfFrequency";
			}
			if(titlesArray[i].trim().equals("收藏用户")){
				titlesArray[i] = "collectUser";
			}
			if(titlesArray[i].trim().equals("3天展示访客")){
				titlesArray[i] = "showVisitor_3";
			}
			if(titlesArray[i].trim().equals("7天展示访客")){
				titlesArray[i] = "showVisitor_7";
			}
			if(titlesArray[i].trim().equals("15天展示访客")){
				titlesArray[i] = "showVisitor_15";
			}
			if(titlesArray[i].trim().equals("3天展示回报率")){
				titlesArray[i] = "showRateOfReturn_3";
			}
			if(titlesArray[i].trim().equals("7天展示回报率")){
				titlesArray[i] = "showRateOfReturn_7";
			}
			if(titlesArray[i].trim().equals("15天展示回报率")){
				titlesArray[i] = "showRateOfReturn_15";
			}
			if(titlesArray[i].trim().equals("3天展示产出")){
				titlesArray[i] = "outputOfReveal_3";
			}
			if(titlesArray[i].trim().equals("7天展示产出")){
				titlesArray[i] = "outputOfReveal_7";
			}
			if(titlesArray[i].trim().equals("15天展示产出")){
				titlesArray[i] = "outputOfReveal_15";
			}
			if(titlesArray[i].trim().equals("7天订单金额")){
				titlesArray[i] = "orderSum_7";
			}
			if(titlesArray[i].trim().equals("15天订单金额")){
				titlesArray[i] = "orderSum_15";
			}
			if(titlesArray[i].trim().equals("15天下单转化率")){
				titlesArray[i] = "orderPercentConversion_15";
			}
		}
		return titlesArray;
	}
	
	
	/**
	 * 
	 * @param 表头
	 */
	public static String[] convertENToCN(String titles){
		String[] titlesArray = titles.split(",");
		for(int i = 0;i < titlesArray.length;i++){
			if(titlesArray[i].trim().equals("allowdSpreadTheme")){
				titlesArray[i] = "所属推广主题";
			}
			if(titlesArray[i].trim().equals("putInPurpose")){
				titlesArray[i] = "投放目的";
			}
			if(titlesArray[i].trim().equals("crowdInfo")){
				titlesArray[i] = "人群信息";
			}
			if(titlesArray[i].trim().equals("userOrigin")){
				titlesArray[i] = "用户来源";
			}
			if(titlesArray[i].trim().equals("crowdLayer")){
				titlesArray[i] = "人群分层";
			}
			if(titlesArray[i].trim().equals("crowdAttr")){
				titlesArray[i] = "人群属性";
			}
			if(titlesArray[i].trim().equals("platform")){
				titlesArray[i] = "平台";
			}
			if(titlesArray[i].trim().equals("standInfo")){
				titlesArray[i] = "展位";
			}
			if(titlesArray[i].trim().equals("directoryName")){
				titlesArray[i] = "定向名称";
			}
			if(titlesArray[i].trim().equals("unitName")){
				titlesArray[i] = "推广单元基本信息";
			}
			if(titlesArray[i].trim().equals("spreadSchedule")){
				titlesArray[i] = "所属推广计划";
			}
			if(titlesArray[i].trim().equals("dateTimes")){
				titlesArray[i] = "时间";
			}
			if(titlesArray[i].trim().equals("reveal")){
				titlesArray[i] = "展现";
			}
			if(titlesArray[i].trim().equals("click")){
				titlesArray[i] = "点击";
			}
			if(titlesArray[i].trim().equals("CTR")){
				titlesArray[i] = "点击率(%)";
			}
			if(titlesArray[i].trim().equals("consume")){
				titlesArray[i] = "消耗";
			}
			if(titlesArray[i].trim().equals("showCostOf1000")){
				titlesArray[i] = "千次展现成本(元)";
			}
			if(titlesArray[i].trim().equals("unitPriceOfClick")){
				titlesArray[i] = "点击单价";
			}
			if(titlesArray[i].trim().equals("rateOfReturn_3")){
				titlesArray[i] = "3天回报率";
			}
			if(titlesArray[i].trim().equals("rateOfReturn_7")){
				titlesArray[i] = "7天回报率";
			}
			if(titlesArray[i].trim().equals("rateOfReturn_15")){
				titlesArray[i] = "15天回报率";
			}
			if(titlesArray[i].trim().equals("customerOrderNum_3")){
				titlesArray[i] = "3天顾客订单数";
			}
			if(titlesArray[i].trim().equals("customerOrderNum_7")){
				titlesArray[i] = "7天顾客订单数";
			}
			if(titlesArray[i].trim().equals("customerOrderNum_15")){
				titlesArray[i] = "15天顾客订单数";
			}
			if(titlesArray[i].trim().equals("shopCollectNum")){
				titlesArray[i] = "店辅收藏数";
			}
			if(titlesArray[i].trim().equals("goodsCollectNum")){
				titlesArray[i] = "宝贝收藏数";
			}
			if(titlesArray[i].trim().equals("visitor")){
				titlesArray[i] = "访客";
			}
			if(titlesArray[i].trim().equals("getUserCost")){
				titlesArray[i] = "访客获取成本";
			}
			if(titlesArray[i].trim().equals("touchOfUser")){
				titlesArray[i] = "触达用户";
			}
			if(titlesArray[i].trim().equals("touchOfFrequency")){
				titlesArray[i] = "触达频次";
			}
			if(titlesArray[i].trim().equals("collectUser")){
				titlesArray[i] = "收藏用户";
			}
			if(titlesArray[i].trim().equals("showVisitor_3")){
				titlesArray[i] = "3天展示访客";
			}
			if(titlesArray[i].trim().equals("showVisitor_7")){
				titlesArray[i] = "7天展示访客";
			}
			if(titlesArray[i].trim().equals("showVisitor_15")){
				titlesArray[i] = "15天展示访客";
			}
			if(titlesArray[i].trim().equals("showRateOfReturn_3")){
				titlesArray[i] = "3天展示回报率";
			}
			if(titlesArray[i].trim().equals("showRateOfReturn_7")){
				titlesArray[i] = "7天展示回报率";
			}
			if(titlesArray[i].trim().equals("showRateOfReturn_15")){
				titlesArray[i] = "15天展示回报率";
			}
			if(titlesArray[i].trim().equals("outputOfReveal_3")){
				titlesArray[i] = "3天展示产出";
			}
			if(titlesArray[i].trim().equals("outputOfReveal_7")){
				titlesArray[i] = "7天展示产出";
			}
			if(titlesArray[i].trim().equals("outputOfReveal_15")){
				titlesArray[i] = "15天展示产出";
			}
			if(titlesArray[i].trim().equals("orderSum_7")){
				titlesArray[i] = "7天订单金额";
			}
			if(titlesArray[i].trim().equals("orderSum_15")){
				titlesArray[i] = "15天订单金额";
			}
			if(titlesArray[i].trim().equals("orderPercentConversion_15")){
				titlesArray[i] = "15天下单转化率";
			}
		}
		return titlesArray;
	}
	
	/**
	 * 
	 * 匹配人群分层
	 * @param args
	 * @throws SplitAbbreviationCompileException 
	 * 
	 */
	public static String regexpCrowdLayer(String crowdLayerAbbreviation) throws SplitAbbreviationCompileException {
		String crowdLayerFullName = "";
		for (CrowdLayerCompileSpec spec : CrowdLayerCompileSpec.values()) {
			if (spec.getCrowdLayerAbbreviation().equals(crowdLayerAbbreviation)) {
				crowdLayerFullName = spec.getCrowdLayerFullName();
				break;
			} else {
				continue;
			}
		}
		if (crowdLayerFullName.equals("")) {
			throw new SplitAbbreviationCompileException("人群分层未匹配(unknown):" + crowdLayerAbbreviation);
		}
		return crowdLayerFullName;
	}
	
	/**
	 * 
	 * 匹配人群信息
	 * @param args
	 * @throws SplitAbbreviationCompileException 
	 * 
	 */
	public static String regexpCrowdInfo(String crowdInfoAbbreviation) throws SplitAbbreviationCompileException {
		if(crowdInfoAbbreviation.contains("测试")){
			if(crowdInfoAbbreviation.indexOf("测试") > 0){
				crowdInfoAbbreviation = crowdInfoAbbreviation.substring(crowdInfoAbbreviation.indexOf("测试"), crowdInfoAbbreviation.length());
			}
		}else{
			if(crowdInfoAbbreviation.indexOf("人群") > 0){
				crowdInfoAbbreviation = crowdInfoAbbreviation.substring(crowdInfoAbbreviation.indexOf("人群"), crowdInfoAbbreviation.length());
			}
		}
		String crowdInfoFullName = "";
		for (CrowdInfoCompileSpec spec : CrowdInfoCompileSpec.values()) {
			if (spec.getCrowdInfoAbbreviation().contains(crowdInfoAbbreviation)) {
				crowdInfoFullName = spec.getCrowdInfoFullName();
				break;
			} else {
				continue;
			}
		}
		if (crowdInfoFullName.equals("")) {
			throw new SplitAbbreviationCompileException("人群信息未匹配(unknown):" + crowdInfoAbbreviation);
		}
		return crowdInfoFullName;
	}
	
	/**
	 * 
	 * 匹配所属类目
	 * @param args
	 * @throws SplitAbbreviationCompileException 
	 * 
	 */
	public static String regexpAllowCategory(String allowCategoryAbbreviation) throws SplitAbbreviationCompileException {
		String allowCategoryFullName = "";
		for (AllowCategoryCompileSpec spec : AllowCategoryCompileSpec.values()) {
			if (spec.getAllowCategoryAbbreviation().equals(allowCategoryAbbreviation)) {
				allowCategoryFullName = spec.getAllowCategoryFullName();
				break;
			} else {
				continue;
			}
		}
		if (allowCategoryFullName.equals("")) {
			throw new SplitAbbreviationCompileException("所属类目未匹配(unknown):" + allowCategoryAbbreviation);
		}
		return allowCategoryFullName;
	}
	
	/**
	 * 
	 * 匹配所属类目
	 * @param args
	 * @throws SplitAbbreviationCompileException 
	 * 
	 */
	public static String regexpIsTest(String isTestAbbreviation) throws SplitAbbreviationCompileException {
		String isTestFullName = "";
		for (IsTestCompileSpec spec : IsTestCompileSpec.values()) {
			if (spec.getIsTestAbbreviation().equals(isTestAbbreviation)) {
				isTestFullName = spec.getIsTestFullName();
				break;
			} else {
				continue;
			}
		}
		if (isTestFullName.equals("")) {
			throw new SplitAbbreviationCompileException("是否测试未匹配(unknown):" + isTestAbbreviation);
		}
		return isTestFullName;
	}
	
	/**
	 * 
	 * 匹配人群属性
	 * @param args
	 * @throws SplitAbbreviationCompileException 
	 * 
	 */
	public static String regexpCrowdAttr(String crowdAttrAbbreviation) throws SplitAbbreviationCompileException {
		String crowdFullName = "";
		for (CrowdAttrCompileSpec spec : CrowdAttrCompileSpec.values()) {
			if (spec.getCrowdAttrAbbreviation().equals(crowdAttrAbbreviation)) {
				crowdFullName = spec.getCrowdAttrFullName();
				break;
			} else {
				continue;
			}
		}
		if (crowdFullName.equals("")) {
			throw new SplitAbbreviationCompileException("人群属性未匹配(unknown):" + crowdAttrAbbreviation);
		}
		return crowdFullName;
	}
	
	/**
	 * 
	 * 匹配展位信息
	 * @param args
	 * @throws SplitAbbreviationCompileException 
	 * 
	 */
	public static String regexpStandInfo(String standInfoAbbreviation) throws SplitAbbreviationCompileException {
		String standInfoFullName = "";
		for (StandInfoCompileSpec spec : StandInfoCompileSpec.values()) {
			if (spec.getStandInfoAbbreviation().equals(standInfoAbbreviation)) {
				standInfoFullName = spec.getStandInfoFullName();
				break;
			} else {
				continue;
			}
		}
		if (standInfoFullName.equals("")) {
			throw new SplitAbbreviationCompileException("展位信息未匹配(unknown):" + standInfoAbbreviation);
		}
		return standInfoFullName;
	}
	
	// 判断一个字符是否是中文
	public static boolean isChinese(char c) {
	      return c >= 0x4E00 &&  c <= 0x9FA5;// 根据字节码判断
	}
	// 判断一个字符串是否含有中文
	public static boolean isChinese(String str) {
		if (str == null) return false;
		for (char c : str.toCharArray()) {
			if (isChinese(c)) return true;// 有一个中文字符就返回
		}
		return false;
	}
	
	public static void main(String[] args) throws ParseException {
		DirectSheetBean bean = new DirectSheetBean();
		Class clazz = DirectSheetBean.class;
		Field[] fields = clazz.getDeclaredFields();
		Method[] methods = clazz.getDeclaredMethods();
		/*for(Field field : fields){
			System.out.println(field.getName());
		}*/
		for(Method method : methods){
			if(method.getName().equals("setDateTimes")){
				for(Field field : fields){
					if(method.getName().replace("set", "").toUpperCase().equals(field.getName().toUpperCase())){
						System.out.println(field.getType().getName().equals("java.util.Date"));
					}
				}
				//System.out.println(method.getName());
				try {
					method.invoke(bean, new SimpleDateFormat("yyyy-MM-dd").parse("2015-10-05"));
					//System.out.println(bean.getDateTimes());
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//System.out.println(bean.getCollectUser());
		
		/*try {
			System.out.println(regexpIsTest("F"));
		} catch (SplitAbbreviationCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//System.out.println(isChinese("Abc"));
		/*String s = "凤凰色测试人群A";
		String ss = "测试人群A";
		System.out.println(s.indexOf("测试人群"));
		if(s.contains("测试人群")){
			System.out.println(s.substring(s.indexOf("测试人群"), s.length()));
		}*/
	}
}
