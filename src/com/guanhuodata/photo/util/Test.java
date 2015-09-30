package com.guanhuodata.photo.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.guanhuodata.photo.bean.MaterialChartSplitBean;

public class Test {

	public static void main(String[] args) throws IOException, SQLException {
		//materialRepository
				/*File s = new File("d:\\img\\14.jpg");
				File t = new File("d:\\eg.jpg");
				InputStream in = null;
				OutputStream out = null;
				try{
					in = new BufferedInputStream(new FileInputStream(s));
					out = new BufferedOutputStream(new FileOutputStream(t));
					byte[] buf = new byte[2048];
					int len = 0;
					while((len = in.read(buf)) != -1){
						out.write(buf, 0, len);
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try{
						in.close();
						out.close();
					}catch(IOException e){
						e.printStackTrace();
					}
				}*/
		/*File file = new File("D:\\tomcat7\\apache-tomcat-7.0.62\\webapps\\statement-analysis");
		List<File> fileList = Arrays.asList(file.listFiles());
		if(fileList.contains("materialRepository")){
			for(File files:fileList){
				System.out.println(files.getName());
			}
		}else{
			for(File files:fileList){
				System.out.println(files.getName());
			}
			File material = new File("D:\\tomcat7\\apache-tomcat-7.0.62\\webapps\\statement-analysis\\materialRepository");
			boolean is = material.mkdir();
			File filen = new File("d:\\aaa");
			filen.mkdir();
			System.out.println(is);
		}*/
		/*String path = "d:" +File.separator + "img";
		File filen = new File(path);
		List<FileBean> fileBeanList = new ArrayList<FileBean>();
		for(File fileName:filen.listFiles()){
			System.out.println(fileName.getPath());
			FileBean fileBean = new FileBean();
			fileBean.setFileName(fileName.getName());
			fileBeanList.add(fileBean);
		}
		String ret = JsonUtil.makeListJson(fileBeanList);
		System.out.println(ret);*/
		/*System.out.println(filen.exists());
		
		filen.mkdir();
		System.out.println(filen.exists());*/
		/*String name = "01.jpg";
		String nameSubString = name.substring(0, name.lastIndexOf("."));
		System.out.println(nameSubString);*/
		/*QImageInfoBean qBean = new QImageInfoBean();
		qBean.setShop("施华蔻");
		qBean.setCTR(5.5);
		qBean.setPutInCrowd("浏览用户");
		qBean.setTitle("日常活动");
		qBean.setROI(8.8);
		qBean.setImageSize("520*360");
		String s = JsonUtil.makeJson(qBean);
		System.out.println(s);*/
		/*int size = 13;
		int pageSize = 7;
		System.out.println((size + 1) % pageSize);*/
		//System.out.println(size % pageSize == 0 ? size/pageSize : size/pageSize + 1);
		/*List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("a");
		list.add("a");
		list.add("a");
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("c");
		list.add("c");
		list.add("d");
		list.add("d");
		list.add("d");
		list.add("d");
		list.add("e");
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
		for(String ss : disList){
			System.out.println(ss);
		}*/
		
		/*FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		// 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
		try {
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			bufferImg = ImageIO.read(new File("d://img//美妆_99大促预热_单品页_淘宝首页2屏右_R12_jpg.jpg"));
			ImageIO.write(bufferImg, "jpg", byteArrayOut);

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet1 = wb.createSheet("test picture");
			// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
			// anchor主要用于设置图片的属性
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255, (short) 1, 1, (short) 5, 8);
			anchor.setAnchorType(3);
			// 插入图片
			patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
			fileOut = new FileOutputStream("D://测试Excel.xls");
			// 写入excel文件
			wb.write(fileOut);
			JOptionPane.showMessageDialog(null, "导出成功!");
			System.out.println("excel导出成功！");
			System.out.println("----Excle文件已生成------");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}*/
		
		/*HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");

        HSSFRow row = sheet.createRow(1);
        HSSFCell cell = row.createCell(1);
        cell.setCellValue("This is a test of merging");

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("d://workbook.xls");
        wb.write(fileOut);
        fileOut.close();*/
		/*List<MaterialChartSplitBean> list = new ArrayList<MaterialChartSplitBean>();
		String[] headersCustomer = {"创意图","创意名称","推广单元基本信息","所属推广计划","时间","展现","点击","点击率(%)","消耗","千次展现成本(元)","访客","访客获取成本","点击单价(元)","15天回报率","15天展示回报率","15天点击产出","15天展示产出","3天回报率","7天回报率","3天顾客订单数","7天顾客订单数","15天顾客订单数","店辅收藏数","宝贝收藏数","触达用户","触达频次","收藏用户","3天展示访客","7天展示访客","15天展示访客","3天展示回报率","7天展示回报率","7天订单金额","15天订单金额"};
		String[] headersSelf = {"创意图","创意名称","推广单元基本信息","所属推广计划","时间","展现","点击","点击率(%)","消耗","千次展现成本(元)","访客","访客获取成本","点击单价(元)","15天回报率","15天展示回报率","15天点击产出","15天展示产出","3天回报率","7天回报率","3天顾客订单数","7天顾客订单数","15天顾客订单数","店辅收藏数","宝贝收藏数","触达用户","触达频次","收藏用户","3天展示访客","7天展示访客","15天展示访客","3天展示回报率","7天展示回报率","7天订单金额","15天订单金额"};
		Connection conn =  MaterialChartUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
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
		Map<String,List<MaterialChartSplitBean>> map = MaterialChartUtil.partition_1(list);*/
		/*List<MaterialChartSplitBean> dataset = new ArrayList<MaterialChartSplitBean>();
		MaterialChartSplitBean bean1 = new MaterialChartSplitBean();
		bean1.setOriginalityName("美妆_99大促预热_单品页_淘宝首页2屏右_R12_jpg");
		bean1.setAllowSpreadSchedule("美妆_99大促预热_单品页_淘宝首页2屏右_R12_jpg");
		bean1.setSpreadUnitBasicInfo("美妆_99大促预热_单品页_淘宝首页2屏右_R12_jpg");
		bean1.setClick(1);
		bean1.setClickSum_15(1.1);
		bean1.setCollectUser(1);
		bean1.setConsume(1.1);
		bean1.setCTR(1.1);
		bean1.setCustomerOrderNum_15(1);
		bean1.setCustomerOrderNum_3(1);
		bean1.setCustomerOrderNum_7(1);
		bean1.setDateTime("2015-09-21");
		bean1.setGoodsCollectNum(1);
		bean1.setId(1);
		bean1.setMaterialContinuePage("http://www.mail.sina.com");
		bean1.setMaterialCrowd("关注用户");
		bean1.setMaterialStandAbbreviation("无线640_1");
		bean1.setMaterialTheme("爆发");
		bean1.setOperateTime("2015-09-21");
		bean1.setVisitor(1);
		bean1.setTouchOfUser(1);
		bean1.setTouchOfFrequency(1);
		bean1.setShowVisitor_7(1);
		bean1.setShowVisitor_3(1);
		bean1.setShowVisitor_15(1);
		bean1.setShowSum_15(1.1);
		bean1.setShowRateOfReturn_7(1.1);
		bean1.setShowRateOfReturn_3(1.1);
		bean1.setShowRateOfReturn_15(1.1);
		bean1.setShowCostOf1000(1.1);
		bean1.setReveal(1);
		bean1.setOrderSum_7(1.1);
		bean1.setOrderSum_15(1.1);
		dataset.add(bean1);
		MaterialChartSplitBean bean2 = new MaterialChartSplitBean();
		bean2.setOriginalityName("自有非交易_99大促预热_单品页_天猫首焦_Z30_jpg");
		bean2.setAllowSpreadSchedule("自有非交易_99大促预热_单品页_天猫首焦_Z30_jpg");
		bean2.setSpreadUnitBasicInfo("自有非交易_99大促预热_单品页_天猫首焦_Z30_jpg");
		bean2.setClick(1);
		bean2.setClickSum_15(2.2);
		bean2.setCollectUser(2);
		bean2.setConsume(2.2);
		bean2.setCTR(2.2);
		bean2.setCustomerOrderNum_15(2);
		bean2.setCustomerOrderNum_3(2);
		bean2.setCustomerOrderNum_7(2);
		bean2.setDateTime("2015-09-21");
		bean2.setGoodsCollectNum(1);
		bean2.setId(1);
		bean2.setMaterialContinuePage("http://www.mail.sina.com");
		bean2.setMaterialCrowd("交易用户");
		bean2.setMaterialStandAbbreviation("无线640_1");
		bean2.setMaterialTheme("预热");
		bean2.setOperateTime("2015-09-21");
		bean2.setVisitor(2);
		bean2.setTouchOfUser(2);
		bean2.setTouchOfFrequency(2);
		bean2.setShowVisitor_7(2);
		bean2.setShowVisitor_3(2);
		bean2.setShowVisitor_15(2);
		bean2.setShowSum_15(2.2);
		bean2.setShowRateOfReturn_7(2.2);
		bean2.setShowRateOfReturn_3(2.2);
		bean2.setShowRateOfReturn_15(2.2);
		bean2.setShowCostOf1000(2.2);
		bean2.setReveal(2);
		bean2.setOrderSum_7(2.2);
		bean2.setOrderSum_15(2.2);
		dataset.add(bean2);
		MaterialChartSplitBean bean3 = new MaterialChartSplitBean();
		bean3.setOriginalityName("美妆_99大促预热_单品页_淘宝首页2屏右_R12_jpg");
		bean3.setAllowSpreadSchedule("美妆_99大促预热_单品页_淘宝首页2屏右_R12_jpg");
		bean3.setSpreadUnitBasicInfo("美妆_99大促预热_单品页_淘宝首页2屏右_R12_jpg");
		bean3.setClick(3);
		bean3.setClickSum_15(3.3);
		bean3.setCollectUser(3);
		bean3.setConsume(3.3);
		bean3.setCTR(3.3);
		bean3.setCustomerOrderNum_15(3);
		bean3.setCustomerOrderNum_3(3);
		bean3.setCustomerOrderNum_7(3);
		bean3.setDateTime("2015-09-22");
		bean3.setGoodsCollectNum(3);
		bean3.setId(3);
		bean3.setMaterialContinuePage("http://www.mail.sina.com");
		bean3.setMaterialCrowd("关注用户");
		bean3.setMaterialStandAbbreviation("无线640_1");
		bean3.setMaterialTheme("爆发");
		bean3.setOperateTime("2015-09-21");
		bean3.setVisitor(3);
		bean3.setTouchOfUser(3);
		bean3.setTouchOfFrequency(3);
		bean3.setShowVisitor_7(3);
		bean3.setShowVisitor_3(3);
		bean3.setShowVisitor_15(3);
		bean3.setShowSum_15(3.3);
		bean3.setShowRateOfReturn_7(3.3);
		bean3.setShowRateOfReturn_3(3.3);
		bean3.setShowRateOfReturn_15(3.3);
		bean3.setShowCostOf1000(3.3);
		bean3.setReveal(3);
		bean3.setOrderSum_7(3.3);
		bean3.setOrderSum_15(3.3);
		dataset.add(bean3);*/
		/*try {
			OutputStream out = new FileOutputStream("d://materialchart.xls");
			ExportExcel<MaterialChartSplitBean> ex = new ExportExcel<MaterialChartSplitBean>();
			ex.exportExcel(headersCustomer, dataset, out);
			out.close();
			JOptionPane.showMessageDialog(null, "导出成功!");
	         System.out.println("excel导出成功！");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}*/
		/*Iterator<MaterialChartSplitBean> it = dataset.iterator();
		Iterator<MaterialChartSplitBean> its = dataset.iterator();
		Map<String,List<MaterialChartSplitBean>> map = new HashMap<String,List<MaterialChartSplitBean>>();
		while(it.hasNext()){
			List<MaterialChartSplitBean> choosed = new ArrayList<MaterialChartSplitBean>();
			MaterialChartSplitBean obean = it.next();
			while(its.hasNext()){
				MaterialChartSplitBean ibean = its.next();
				if(ibean.getMaterialTheme().equals(obean.getMaterialTheme()) && ibean.getOriginalityName().equals(obean.getOriginalityName())){
					choosed.add(ibean);
				}
			}
			map.put(obean.getOriginalityName(), choosed);
		}*/
		/*Map<String,List<MaterialChartSplitBean>> map = new HashMap<String,List<MaterialChartSplitBean>>();
		for(int i = 0; i < dataset.size();i++){
			List<MaterialChartSplitBean> choose = new ArrayList<MaterialChartSplitBean>();
			MaterialChartSplitBean obean = dataset.get(i);
			for(int j = 0;j < dataset.size();j++){
				MaterialChartSplitBean ibean = dataset.get(j);
				//if(ibean.getMaterialTheme().equals(obean.getMaterialTheme()) && ibean.getOriginalityName().equals(obean.getOriginalityName())){
				if(ibean.getMaterialTheme().equals(obean.getMaterialTheme())){
					choose.add(ibean);
				}
			}
			map.put(obean.getOriginalityName(), choose);
		}
		System.out.println(map.size());
		Map<String,List<MaterialChartSplitBean>> chooseMap = new HashMap<String,List<MaterialChartSplitBean>>();
		for(String key : map.keySet()){
			List<MaterialChartSplitBean> dateRegexpList = map.get(key);
			System.out.println(key + " : " + map.get(key).size());
			for(int k = 0;k < dateRegexpList.size();k++){
				System.out.println(dateRegexpList.get(k).getOriginalityName() + " *** " + "materialTheme: " + dateRegexpList.get(k).getMaterialTheme() + " *** " + "materialCrowd: " +dateRegexpList.get(k).getMaterialCrowd() + " *** " + "dateTime: " + dateRegexpList.get(k).getDateTime());
			}
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
		}*/
		/*XSSFWorkbook wb = new XSSFWorkbook();
        // 创建工作表对象
        XSSFSheet sheet = wb.createSheet("我的工作表");
        // 创建绘图对象
        XSSFDrawing p = sheet.createDrawingPatriarch();
        // 创建单元格对象,批注插入到4行,1列,B5单元格
        XSSFCell cell = sheet.createRow(4).createCell(1);
        // 插入单元格内容
        cell.setCellValue(new XSSFRichTextString("批注"));
        // 获取批注对象
        // (int dx1, int dy1, int dx2, int dy2, short col1, int row1, short
        // col2, int row2)
        // 前四个参数是坐标点,后四个参数是编辑和显示批注时的大小.
        XSSFComment comment = p.createCellComment(new XSSFClientAnchor(0, 0, 0,
                0, (short) 3, 3, (short) 5, 6));
        // 输入批注信息
        comment.setString(new XSSFRichTextString("这是批注内容!"));
        // 添加作者,选中B5单元格,看状态栏
        comment.setAuthor("toad");
        
        // 将批注添加到单元格对象中
        cell.setCellComment(comment);
        // 创建输出流
        FileOutputStream out = new FileOutputStream("d:/writerPostil.xlsx");
 
        wb.write(out);
        // 关闭流对象
        out.close();*/
		/*String oname = "竞品品牌_99大促爆发_首页_无线640_L63_jpg汇总";
		String name = "";
		if((oname.substring(oname.length() - 2, oname.length())).equals("汇总")){
			name = oname.substring(0, oname.length() - 2);
		}else{
			name = oname;
		}
		System.out.println(name);*/
		String s = "aa明星店铺1111";
		if(s.contains("明星店铺")){
			System.out.println("contains");
		}else{
			System.out.println("not");
		}
	}
}