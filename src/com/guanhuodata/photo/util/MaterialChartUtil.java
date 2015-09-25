package com.guanhuodata.photo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.guanhuodata.photo.bean.MaterialChartSplitBean;
import com.guanhuodata.photo.constant.StandAbbreviationSpec;

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
	public static List<MaterialChartSplitBean> readMaterialExcel(File file) {
		LOG.info("MaterialChartUtil readMaterialExcel fileName:" + file.getName() + " start.");
		Workbook wb = null;
		InputStream is = null;
		List<MaterialChartSplitBean> list = new ArrayList<MaterialChartSplitBean>();
		//判断报表版本：.xls/.xlsx
		String fileName = file.getName();
		String suffix = fileName.substring(fileName.lastIndexOf("."),fileName.length());
		//判断店铺名称
		//String shopName = getShopName(fileName);
		String shopName = "雪肌精";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
							value_1 =cell_1.getStringCellValue() + ",";
							break;
						default:
							value_1 = "0";
						break;
					}
				}
				sb.append(value_1);
			}
			/**
			 * 判断表头中包含的列数量
			 */
			System.out.println("********************************");
			System.out.println(sb.toString());
			int colNum = sb.toString().split(",").length;
			System.out.println(colNum);
			System.out.println("********************************");
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
					String[] cellVal = colsValue.split(",");
					MaterialChartSplitBean materialChartSplitBean = new MaterialChartSplitBean();
					materialChartSplitBean.setOriginalityName(cellVal[0]);
					materialChartSplitBean.setSpreadUnitBasicInfo(cellVal[1]);
					materialChartSplitBean.setAllowSpreadSchedule(cellVal[2]);
					materialChartSplitBean.setDateTime(cellVal[3]);
					//在读取该cell时取出的数据为double类型的，需要转换为long
					materialChartSplitBean.setReveal((long)Double.parseDouble(cellVal[4]));
					materialChartSplitBean.setClick((long)Double.parseDouble(cellVal[5]));
					materialChartSplitBean.setCTR(Double.parseDouble(cellVal[6]));
					materialChartSplitBean.setConsume(Double.parseDouble(cellVal[7]));
					materialChartSplitBean.setShowCostOf1000(Double.parseDouble(cellVal[8]));
					materialChartSplitBean.setUnitPriceOfClick(Double.parseDouble(cellVal[9]));
					materialChartSplitBean.setRateOfReturn_3(Double.parseDouble(cellVal[10]));
					materialChartSplitBean.setRateOfReturn_7(Double.parseDouble(cellVal[11]));
					materialChartSplitBean.setRateOfReturn_15(Double.parseDouble(cellVal[12]));
					//在读取该cell时取出的数据为double类型的，需要转换为long/int
					materialChartSplitBean.setCustomerOrderNum_3((long)Double.parseDouble(cellVal[13]));
					materialChartSplitBean.setCustomerOrderNum_7((long)Double.parseDouble(cellVal[14]));
					materialChartSplitBean.setCustomerOrderNum_15((long)Double.parseDouble(cellVal[15]));
					materialChartSplitBean.setShopCollectNum((int)Double.parseDouble(cellVal[16]));
					materialChartSplitBean.setGoodsCollectNum((int)Double.parseDouble(cellVal[17]));
					materialChartSplitBean.setVisitor((long)Double.parseDouble(cellVal[18]));
					materialChartSplitBean.setTouchOfUser((long)Double.parseDouble(cellVal[19]));
					materialChartSplitBean.setTouchOfFrequency(Double.parseDouble(cellVal[20]));
					materialChartSplitBean.setCollectUser((long)Double.parseDouble(cellVal[21]));
					materialChartSplitBean.setShowVisitor_3((long)Double.parseDouble(cellVal[22]));
					materialChartSplitBean.setShowVisitor_7((long)Double.parseDouble(cellVal[23]));
					materialChartSplitBean.setShowVisitor_15((long)Double.parseDouble(cellVal[24]));
					materialChartSplitBean.setShowRateOfReturn_3(Double.parseDouble(cellVal[25]));
					materialChartSplitBean.setShowRateOfReturn_7(Double.parseDouble(cellVal[26]));
					materialChartSplitBean.setRateOfReturn_15(Double.parseDouble(cellVal[27]));
					materialChartSplitBean.setOrderSum_7(Double.parseDouble(cellVal[28]));
					materialChartSplitBean.setOrderSum_15(Double.parseDouble(cellVal[29]));
					if(colNum > 30 && colNum <= 32){
						materialChartSplitBean.setClickSum_15(Double.parseDouble(cellVal[30]));
						materialChartSplitBean.setShowSum_15(Double.parseDouble(cellVal[31]));
					}else if(colNum <= 30){
						materialChartSplitBean.setClickSum_15(0);
						materialChartSplitBean.setShowSum_15(0);
					}else{
						LOG.error("material chart col num is not regex.");
						throw new Exception("素材报表的列数量不在匹配范围处理失败.");
					}
					/**
					 * 素材命名：人群_主题_承接页_展位简称_设计简写+编号
					 * 计划命名：活动主题_投放目的_人群分层_展位类别
					 * 单元命名：人群_具体展位
					 *	带material：素材命名
					 *	带schedule：计划命名
					 *	带unit：单元命名 
					 *	clo[0]：素材名称
					 *	col[1]：推广单元基本信息
					 *	col[2]：推广计划
					 */
					//素材拆分
					String[] materialSplit = cellVal[0].split("_");
					if(materialSplit.length == 6){
						LOG.info("materialSplit array's length：6" );
						materialChartSplitBean.setMaterialCrowd(materialSplit[0]);
						materialChartSplitBean.setMaterialTheme(materialSplit[1]);
						materialChartSplitBean.setMaterialContinuePage(materialSplit[2]);
						materialChartSplitBean.setMaterialStandAbbreviation(materialSplit[3]);
						materialChartSplitBean.setMaterialDesignAbbreviationAndSerialNumber(materialSplit[4]);
					}else if(materialSplit.length == 5){
						LOG.info("materialSplit array's length：5" );
						//对于没有人群列的素材素材名称命名为：通用
						String materialCrowd = "通用";
						materialChartSplitBean.setMaterialCrowd(materialCrowd);
						materialChartSplitBean.setMaterialTheme(materialSplit[0]);
						materialChartSplitBean.setMaterialContinuePage(materialSplit[1]);
						materialChartSplitBean.setMaterialStandAbbreviation(materialSplit[2]);
						materialChartSplitBean.setMaterialDesignAbbreviationAndSerialNumber(materialSplit[3]);
					}else{
						LOG.error("materialSplit array's length：unknow.");
						throw new Exception("素材名称拆分出现未知错误.");
					}
					//推广单元拆分
					String[] unitSplit = cellVal[1].split("_");
					materialChartSplitBean.setUnitCrowd(unitSplit[0]);
					materialChartSplitBean.setUnitSpecificStand(unitSplit[1]);
					//推广计划拆分
					String[] scheduleSplit = cellVal[2].split("_");
					materialChartSplitBean.setScheduleActivityTheme(scheduleSplit[0]);
					materialChartSplitBean.setSchedulePutInPurpose(scheduleSplit[1]);
					materialChartSplitBean.setScheduleCrowdLayer(scheduleSplit[2]);
					materialChartSplitBean.setScheduleStandCategory(scheduleSplit[3]);
					//按照店铺名称来分
					materialChartSplitBean.setShopName(shopName);
					//扩展字段:operateTime
					materialChartSplitBean.setOperateTime(sdf.format(new Date()));
					list.add(materialChartSplitBean);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 根据文件名来判断报表所属店铺
	 */
	public static String getShopName(String fileName){
		String shopName = fileName.split("_")[3];
		return shopName;
	}
	
	/**
	 * 
	 * 匹配展位尺寸
	 * @param args
	 * 
	 */
	public static String regexpStandSize(String standAbbreviation){
		String standSize = "";
		for(StandAbbreviationSpec spec : StandAbbreviationSpec.values()){
			if(spec.getStandAbbreviation().equals(standAbbreviation)){
				standSize = spec.getStandSize();
				break;
			}else{
				continue;
			}
		}
		if(standSize.equals("")){
			standSize = "未匹配";
		}
		return standSize;
	}
	
	/**
	 * 
	 * 匹配展位简称
	 * @param args
	 * 
	 */
	public static String regexpStandAbbreviation(String standSize){
		String standAbbreviation = "";
		for(StandAbbreviationSpec spec : StandAbbreviationSpec.values()){
			if(spec.getStandSize().equals(standSize)){
				standAbbreviation = spec.getStandAbbreviation();
				break;
			}else{
				continue;
			}
		}
		if(standAbbreviation.equals("")){
			standAbbreviation = "未匹配";
		}
		return standAbbreviation;
	}
	
	//List去重
	public static List<String> dupRepeat(List<String> list){
		List<String> disList = new ArrayList<String>();
		String n = "";
		for(String s : list){
			if(Collections.frequency(list, s) <= 1){
				disList.add(s);
			}else{
				if(n.equals("")){
					disList.add(s);
					n=s;
				}else{
					if(n.equals(s)){
						continue;
					}else{
						disList.add(s);
						n=s;
					}
				}
			}
		}
		return disList;
	}
	
	//JDBC MYSQL
	public static Connection getConnection(){
		LOG.info("JDBC connect to localhost mysql db.");
		Connection conn = null;
		String url = "jdbc:mysql://127.0.0.1:3306/guanhuodata?user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
		} catch (ClassNotFoundException e) {
			LOG.error("ClassNotFoundException(com.mysql.jdbc.Driver): " + e.getLocalizedMessage());
			e.printStackTrace();
		}catch(SQLException e){
			LOG.error("SQLException for url:jdbc:mysql://127.0.0.1:3306/guanhuodata?user=root&password=123456&useUnicode=true&characterEncoding=UTF8");
			e.printStackTrace();
		}
		return conn;
	}
	
	//close
	public static void close(Connection conn,PreparedStatement pstmt,ResultSet rs){
		LOG.info("close connection,preparestatement,resultset.");
		try{
			if(rs != null){
				rs.close();
			}
			if(pstmt != null){
				pstmt.close();
			}
			if(conn != null){
				conn.close();
			}
		}catch(SQLException e){
			LOG.error("close connection,preparestatement,resultset has SQLException.");
			e.printStackTrace();
		}
	}
	
	/**
	 * 数据库查询操作后，后台为导出excel报表维护一个Map<String,List<MaterialChartSplitBean>>(先按照主题划分,然后再根据人群细分)
	 * @param dataset
	 * @return
	 */
	public static Map<String,List<MaterialChartSplitBean>> partition(List<MaterialChartSplitBean> dataset){
		//按照主题划分
		Map<String,List<MaterialChartSplitBean>> map = new HashMap<String,List<MaterialChartSplitBean>>();
		Map<String,List<MaterialChartSplitBean>> chooseMap = new HashMap<String,List<MaterialChartSplitBean>>();
		for(int i = 0; i < dataset.size();i++){
			List<MaterialChartSplitBean> choose = new ArrayList<MaterialChartSplitBean>();
			MaterialChartSplitBean obean = dataset.get(i);
			for(int j = 0;j < dataset.size();j++){
				MaterialChartSplitBean ibean = dataset.get(j);
				if(ibean.getMaterialTheme().equals(obean.getMaterialTheme()) && ibean.getOriginalityName().equals(obean.getOriginalityName())){
				//if(ibean.getMaterialTheme().equals(obean.getMaterialTheme())){
					choose.add(ibean);
				}
			}
			map.put(obean.getOriginalityName(), choose);
		}
		System.out.println(map.size());
		for(String key : map.keySet()){
			List<MaterialChartSplitBean> dateRegexpList = map.get(key);
			System.out.println(key + " : " + map.get(key).size());
			/*for(int k = 0;k < dateRegexpList.size();k++){
				System.out.println(dateRegexpList.get(k).getOriginalityName() + " *** " + "materialTheme: " + dateRegexpList.get(k).getMaterialTheme() + " *** " + "materialCrowd: " +dateRegexpList.get(k).getMaterialCrowd() + " *** " + "dateTime: " + dateRegexpList.get(k).getDateTime());
			}*/
			for(int m = 0;m < dateRegexpList.size();m++){
				List<MaterialChartSplitBean> choosed = new ArrayList<MaterialChartSplitBean>();
				MaterialChartSplitBean obeans = dateRegexpList.get(m);
				for(int n = 0;n < dateRegexpList.size();n++){
					MaterialChartSplitBean ibeans = dateRegexpList.get(n);
					if(ibeans.getMaterialCrowd().equals(obeans.getMaterialCrowd())){
						choosed.add(ibeans);
					}
				}
				chooseMap.put(obeans.getMaterialCrowd(), choosed);
			}
		}
		System.out.println("细分:*****************************************************");
		for(String keys : chooseMap.keySet()){
			System.out.println(keys + " : " + chooseMap.get(keys).size());
			List<MaterialChartSplitBean> dateRegexpList = chooseMap.get(keys);
			for(int k = 0;k < dateRegexpList.size();k++){
				System.out.println(dateRegexpList.get(k).getOriginalityName() + " *** " + "materialTheme: " + dateRegexpList.get(k).getMaterialTheme() + " *** " + "materialCrowd: " +dateRegexpList.get(k).getMaterialCrowd() + " *** " + "dateTime: " + dateRegexpList.get(k).getDateTime());
			}
		}
		System.out.println(chooseMap.size());
		return chooseMap;
	}
	
	/**
	 * 数据库查询操作后，后台为导出excel报表维护一个Map<String,List<MaterialChartSplitBean>>(先按照主题划分,然后再根据人群细分)
	 * @param dataset
	 * @return
	 * 自用报表
	 */
	public static Map<String,List<MaterialChartSplitBean>> partitionSelf(List<MaterialChartSplitBean> dataset){
		//按照主题划分
		Map<String,List<MaterialChartSplitBean>> map = new HashMap<String,List<MaterialChartSplitBean>>();
		for(int i = 0; i < dataset.size();i++){
			List<MaterialChartSplitBean> choose = new ArrayList<MaterialChartSplitBean>();
			MaterialChartSplitBean obean = dataset.get(i);
			MaterialChartSplitBean extraBean = new MaterialChartSplitBean();
			for(int j = 0;j < dataset.size();j++){
				MaterialChartSplitBean ibean = dataset.get(j);
				if(ibean.getMaterialTheme().equals(obean.getMaterialTheme()) && ibean.getOriginalityName().equals(obean.getOriginalityName())){
				//if(ibean.getMaterialTheme().equals(obean.getMaterialTheme())){
					extraBean.setOriginalityName(ibean.getOriginalityName());
					extraBean.setMaterialTheme(ibean.getMaterialTheme());
					extraBean.setMaterialCrowd(ibean.getMaterialCrowd() + "汇总");
					extraBean.setReveal(ibean.getReveal() + extraBean.getReveal());
					extraBean.setClick(ibean.getClick() + extraBean.getClick());
					extraBean.setTouchOfUser(ibean.getTouchOfUser() + extraBean.getTouchOfUser());
					extraBean.setTouchOfFrequency(ibean.getTouchOfFrequency() + extraBean.getTouchOfFrequency());
					extraBean.setConsume(ibean.getConsume() + extraBean.getConsume());
					extraBean.setClickSum_15(ibean.getClickSum_15() + extraBean.getClickSum_15());
					extraBean.setShowSum_15(ibean.getShowSum_15() + extraBean.getShowSum_15());
					extraBean.setCTR(ibean.getCTR() + extraBean.getCTR());
					extraBean.setShowCostOf1000(ibean.getShowCostOf1000() + extraBean.getShowCostOf1000());
					extraBean.setUnitPriceOfClick(ibean.getUnitPriceOfClick() + extraBean.getUnitPriceOfClick());
					extraBean.setRateOfReturn_15(ibean.getRateOfReturn_15() + extraBean.getRateOfReturn_15());
					extraBean.setShowRateOfReturn_15(ibean.getShowRateOfReturn_15() + extraBean.getShowRateOfReturn_15());
					extraBean.setCustomerOrderNum_15(ibean.getCustomerOrderNum_15() + extraBean.getCustomerOrderNum_15());
					extraBean.setShopCollectNum(ibean.getShopCollectNum() + extraBean.getShopCollectNum());
					extraBean.setGoodsCollectNum(ibean.getGoodsCollectNum() + extraBean.getGoodsCollectNum());
					extraBean.setVisitor(ibean.getVisitor() + extraBean.getVisitor());
					extraBean.setShowVisitor_15(ibean.getShowVisitor_15() + extraBean.getShowVisitor_15());
					extraBean.setOrderSum_15(ibean.getOrderSum_15() + extraBean.getOrderSum_15());
					choose.add(ibean);
				}
			}
			choose.add(extraBean);
			extraBean.setOriginalityName(extraBean.getOriginalityName() + "汇总");
			choose.add(extraBean);
			map.put(obean.getOriginalityName(), choose);
		}
		System.out.println(map.size());
		for(String key : map.keySet()){
			List<MaterialChartSplitBean> dateRegexpList = map.get(key);
			System.out.println(key + " : " + map.get(key).size());
			for(int k = 0;k < dateRegexpList.size();k++){
				System.out.println(dateRegexpList.get(k).getOriginalityName() + " *** " + "materialTheme: " + dateRegexpList.get(k).getMaterialTheme() + " *** " + "materialCrowd: " +dateRegexpList.get(k).getMaterialCrowd() + " *** " + "dateTime: " + dateRegexpList.get(k).getDateTime());
			}
		}
		return map;
	}
	
	/**
	 * 数据库查询操作后，后台为导出excel报表维护一个Map<String,List<MaterialChartSplitBean>>(先按照主题划分,然后再根据人群细分)
	 * @param dataset
	 * @return
	 * 自用报表
	 */
	public static Map<String,List<MaterialChartSplitBean>> partitionCustomer(List<MaterialChartSplitBean> dataset){
		//按照主题划分
		Map<String,List<MaterialChartSplitBean>> map = new HashMap<String,List<MaterialChartSplitBean>>();
		for(int i = 0; i < dataset.size();i++){
			List<MaterialChartSplitBean> choose = new ArrayList<MaterialChartSplitBean>();
			MaterialChartSplitBean obean = dataset.get(i);
			MaterialChartSplitBean extraBean = new MaterialChartSplitBean();
			for(int j = 0;j < dataset.size();j++){
				MaterialChartSplitBean ibean = dataset.get(j);
				if(ibean.getMaterialTheme().equals(obean.getMaterialTheme()) && ibean.getOriginalityName().equals(obean.getOriginalityName())){
				//if(ibean.getMaterialTheme().equals(obean.getMaterialTheme())){
					extraBean.setOriginalityName(ibean.getOriginalityName());
					extraBean.setMaterialTheme(ibean.getMaterialTheme());
					extraBean.setMaterialCrowd(ibean.getMaterialCrowd() + "汇总");
					extraBean.setReveal(ibean.getReveal() + extraBean.getReveal());
					extraBean.setClick(ibean.getClick() + extraBean.getClick());
					extraBean.setTouchOfUser(ibean.getTouchOfUser() + extraBean.getTouchOfUser());
					extraBean.setTouchOfFrequency(ibean.getTouchOfFrequency() + extraBean.getTouchOfFrequency());
					extraBean.setConsume(ibean.getConsume() + extraBean.getConsume());
					extraBean.setClickSum_15(ibean.getClickSum_15() + extraBean.getClickSum_15());
					extraBean.setShowSum_15(ibean.getShowSum_15() + extraBean.getShowSum_15());
					extraBean.setCTR(ibean.getCTR() + extraBean.getCTR());
					extraBean.setShowCostOf1000(ibean.getShowCostOf1000() + extraBean.getShowCostOf1000());
					extraBean.setUnitPriceOfClick(ibean.getUnitPriceOfClick() + extraBean.getUnitPriceOfClick());
					extraBean.setRateOfReturn_15(ibean.getRateOfReturn_15() + extraBean.getRateOfReturn_15());
					extraBean.setShowRateOfReturn_15(ibean.getShowRateOfReturn_15() + extraBean.getShowRateOfReturn_15());
					extraBean.setCustomerOrderNum_15(ibean.getCustomerOrderNum_15() + extraBean.getCustomerOrderNum_15());
					extraBean.setShopCollectNum(ibean.getShopCollectNum() + extraBean.getShopCollectNum());
					extraBean.setGoodsCollectNum(ibean.getGoodsCollectNum() + extraBean.getGoodsCollectNum());
					extraBean.setVisitor(ibean.getVisitor() + extraBean.getVisitor());
					extraBean.setShowVisitor_15(ibean.getShowVisitor_15() + extraBean.getShowVisitor_15());
					extraBean.setOrderSum_15(ibean.getOrderSum_15() + extraBean.getOrderSum_15());
					choose.add(ibean);
				}
			}
			extraBean.setOriginalityName(extraBean.getOriginalityName() + "汇总");
			choose.add(extraBean);
			map.put(obean.getOriginalityName(), choose);
		}
		System.out.println(map.size());
		for(String key : map.keySet()){
			List<MaterialChartSplitBean> dateRegexpList = map.get(key);
			System.out.println(key + " : " + map.get(key).size());
			for(int k = 0;k < dateRegexpList.size();k++){
				System.out.println(dateRegexpList.get(k).getOriginalityName() + " *** " + "materialTheme: " + dateRegexpList.get(k).getMaterialTheme() + " *** " + "materialCrowd: " +dateRegexpList.get(k).getMaterialCrowd() + " *** " + "dateTime: " + dateRegexpList.get(k).getDateTime());
			}
		}
		return map;
	}
	
	//重新构造一个bean用于封装汇总信息
	public static MaterialChartSplitBean getExtraBean(MaterialChartSplitBean bean){
		MaterialChartSplitBean extraBean = new MaterialChartSplitBean();
		
		return extraBean;
	}
	
	public static List<MaterialChartSplitBean> loadData() throws SQLException{
		List<MaterialChartSplitBean> list = new ArrayList<MaterialChartSplitBean>();
		//String[] headersCustomer = {"创意图","创意名称","推广单元基本信息","所属推广计划","时间","展现","点击","点击率(%)","消耗","千次展现成本(元)","访客","访客获取成本","点击单价(元)","15天回报率","15天展示回报率","15天点击产出","15天展示产出","3天回报率","7天回报率","3天顾客订单数","7天顾客订单数","15天顾客订单数","店辅收藏数","宝贝收藏数","触达用户","触达频次","收藏用户","3天展示访客","7天展示访客","15天展示访客","3天展示回报率","7天展示回报率","7天订单金额","15天订单金额"};
		//String[] headersSelf = {"创意图","创意名称","推广单元基本信息","所属推广计划","时间","展现","点击","点击率(%)","消耗","千次展现成本(元)","访客","访客获取成本","点击单价(元)","15天回报率","15天展示回报率","15天点击产出","15天展示产出","3天回报率","7天回报率","3天顾客订单数","7天顾客订单数","15天顾客订单数","店辅收藏数","宝贝收藏数","触达用户","触达频次","收藏用户","3天展示访客","7天展示访客","15天展示访客","3天展示回报率","7天展示回报率","7天订单金额","15天订单金额"};
		Connection conn =  getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
//		String sql = "select * from materialchart limit 20";
		String sql = "select * from materialchart";
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		while(rs.next()){
			MaterialChartSplitBean bean1 = new MaterialChartSplitBean();
			bean1.setOriginalityName(rs.getString("originalityName"));
			bean1.setAllowSpreadSchedule(rs.getString("allowSpreadSchedule"));
			bean1.setSpreadUnitBasicInfo(rs.getString("spreadUnitBasicInfo"));
			bean1.setClick(rs.getLong("click"));
			bean1.setClickSum_15(rs.getDouble("clickSum_15"));
			bean1.setCollectUser(rs.getLong("collectUser"));
			bean1.setConsume(rs.getDouble("consume"));
			bean1.setCTR(rs.getDouble("CTR"));
			bean1.setCustomerOrderNum_15(rs.getLong("customerOrderNum_15"));
			bean1.setCustomerOrderNum_3(rs.getLong("customerOrderNum_3"));
			bean1.setCustomerOrderNum_7(rs.getLong("customerOrderNum_7"));
			bean1.setDateTime(rs.getString("dateTime"));
			bean1.setGoodsCollectNum(rs.getInt("goodsCollectNum"));
			bean1.setId(rs.getLong("id"));
			bean1.setMaterialContinuePage(rs.getString("materialContinuePage"));
			bean1.setMaterialCrowd(rs.getString("materialCrowd"));
			bean1.setMaterialStandAbbreviation(rs.getString("materialStandAbbreviation"));
			bean1.setMaterialTheme(rs.getString("materialTheme"));
			bean1.setOperateTime(rs.getString("operateTime"));
			bean1.setVisitor(rs.getLong("visitor"));
			bean1.setTouchOfUser(rs.getLong("touchOfUser"));
			bean1.setTouchOfFrequency(rs.getDouble("touchOfFrequency"));
			bean1.setShowVisitor_7(rs.getLong("showVisitor_7"));
			bean1.setShowVisitor_3(rs.getLong("showVisitor_3"));
			bean1.setShowVisitor_15(rs.getLong("showVisitor_15"));
			bean1.setShowSum_15(rs.getDouble("showSum_15"));
			bean1.setShowRateOfReturn_7(rs.getDouble("showRateOfReturn_7"));
			bean1.setShowRateOfReturn_3(rs.getDouble("showRateOfReturn_3"));
			bean1.setShowRateOfReturn_15(rs.getDouble("showRateOfReturn_15"));
			bean1.setShowCostOf1000(rs.getDouble("showCostOf1000"));
			bean1.setReveal(rs.getLong("reveal"));
			bean1.setOrderSum_7(rs.getDouble("orderSum_7"));
			bean1.setOrderSum_15(rs.getDouble("orderSum_15"));
			list.add(bean1);
		}
		MaterialChartUtil.close(conn, pstmt, rs);
		System.out.println(list.size());
		return list;
	}
	
	public static void main(String[] args) {
		String path = "d:" + File.separator + "materialRepo" + File.separator + "雪肌精表_20150914195405.xlsx";
		File file = new File(path);
		List<MaterialChartSplitBean> list = MaterialChartUtil.readMaterialExcel(file);
		System.out.println(list.size());
		System.out.println(MaterialChartUtil.regexpStandSize("3屏通栏大b"));
		String s = "创意名称|推广单元基本信息|所属推广计划|时间|展现|点击|点击率(%)|消耗|千次展现成本(元)|点击单价(元)|3天回报率|7天回报率|15天回报率|3天顾客订单数|7天顾客订单数|15天顾客订单数|店辅收藏数|宝贝收藏数|访客|触达用户|触达频次|收藏用户|3天展示访客|7天展示访客|15天展示访客|3天展示回报率|7天展示回报率|15天展示回报率|7天订单金额|15天订单金额|";
		String[] arr = s.split("|");
		for(String ss : arr){
			System.out.println(ss);
		}
		System.out.println(arr.length);
	}
}
