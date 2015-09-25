package com.guanhuodata.photo.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.guanhuodata.photo.bean.MaterialChartSplitBean;

public class ExportExcel<T> {

	public void exportExcel(Collection<T> dataset, OutputStream out) {
		exportExcel("测试POI导出EXCEL文档", null, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out) {
		exportExcel("测试POI导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
		exportExcel("测试POI导出EXCEL文档", headers, dataset, out, pattern);
	}

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 *
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	@SuppressWarnings("deprecation")
	public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
	    sheet.setDefaultColumnWidth(50);
		//sheet.setColumnWidth(0, 100*256);
	    //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
	    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();     
	    // 创建一个居中格式  
	    HSSFCellStyle style = workbook.createCellStyle();
	    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //水平居左
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		//垂直居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		// 创建一个居中格式  
	    HSSFCellStyle cellStyle = workbook.createCellStyle();
	    cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	    cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	    cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
	    cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //水平居左
	    cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		//垂直居中
	    cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
        //产生Excel报表标题行，创建单元格，并设置值表头 设置表头居中 
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
	        HSSFCell cell = row.createCell(i);
	        cell.setCellStyle(style);
	        HSSFRichTextString text = new HSSFRichTextString(headers[i]);
	        cell.setCellValue(text);
		}
		
		/**
		 * public HSSFClientAnchor(int dx1,int dy1,int dx2,int dy2,short col1,int row1,short col2,int row2);
		 * Creates a new client anchor and sets the top-left and bottom-right coordinates of the anchor.
		 * Parameters:
		 * dx1 - the x coordinate within the first cell.
		 * dy1 - the y coordinate within the first cell.
		 * dx2 - the x coordinate within the second cell.
		 * dy2 - the y coordinate within the second cell.
		 * col1 - the column (0 based); of the first cell.
		 * row1 - the row (0 based); of the first cell.
		 * col2 - the column (0 based); of the second cell.
		 * row2 - the row (0 based); of the second cell.
		 * col1 图片的左上角放在第几个列cell， 
		 * row1 图片的左上角放在第几个行cell， 
		 * col2 图片的右下角放在第几个列cell， 
		 * row2 图片的右下角放在第几个行cell，
		 */
		String path = "d://img//";
		Iterator<T> it = dataset.iterator();
       int index = 0;
	   while (it.hasNext()) {
		   index++;
		   row = sheet.createRow(index);
		   row.setHeight((short) 2000);
		   MaterialChartSplitBean bean = (MaterialChartSplitBean)it.next();
		   for(int j = 0;j <= headers.length; j++){
			   HSSFCell cell = row.createCell(j);
			   switch(j){
			   		case 0:
			   			BufferedImage bufferImg = null;     
			   	       //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
			   	        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			   	        try{
			   	            bufferImg = ImageIO.read(new File(path + bean.getOriginalityName() + ".jpg"));     
			   	            ImageIO.write(bufferImg, "jpg", byteArrayOut);  
			   	        }catch(IOException e){
			   	        	e.printStackTrace();
			   	        }
			   	        /*finally{
			   	        	try {
								byteArrayOut.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			   	        }*/
			   			//插入图片    
			   			//anchor主要用于设置图片的属性  
			   			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255,(short) 0, index, (short) 0, index);     
			   			anchor.setAnchorType(3);     
			   			patriarch.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
			   			break;
			   		case 1:
			   			cell.setCellStyle(style);
			   			cell.setCellValue(bean.getOriginalityName());	//创意名称
			   			break;
			   		case 2:
			   			cell.setCellStyle(style);
			   			cell.setCellValue(bean.getSpreadUnitBasicInfo());	//推广单元基本信息
			   			break;
			   		case 3:
			   			cell.setCellStyle(style);
			   			cell.setCellValue(bean.getAllowSpreadSchedule());//所属推广计划
			   			break;
			   		case 4:
			   			cell.setCellStyle(style);
			   			cell.setCellValue(bean.getDateTime());	//时间
			   			break;
			   		case 5:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getReveal());	//展现
			   			break;
			   		case 6:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getClick());	//点击
			   			break;
			   		case 7:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getCTR());	//点击率
			   			break;
			   		case 8:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getConsume());	//消耗
			   			break;
			   		case 9:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getShowCostOf1000());//千次展现成本
			   			break;
			   		case 10:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getVisitor());	//访客
			   			break;
			   		case 11:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getVisitor()/bean.getConsume());	//访客获取成本
			   			break;
			   		case 12:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getUnitPriceOfClick());	//点击单价(元)
			   			break;
			   		case 13:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getRateOfReturn_15());	//15天回报率
			   			break;
			   		case 14:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getShowRateOfReturn_15());	//15天展示回报率
			   			break;
			   		case 15:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getClickSum_15());	//15天点击产出
			   			break;
			   		case 16:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getShowSum_15());	//15天展示产出
			   			break;
			   		case 17:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getRateOfReturn_3());	//3天回报率
			   			break;
			   		case 18:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getRateOfReturn_7());	//7天回报率
			   			break;
			   		case 19:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getCustomerOrderNum_3());	//3天顾客订单数
			   			break;
			   		case 20:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getCustomerOrderNum_7());	//7天顾客订单数
			   			break;
			   		case 21:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getCustomerOrderNum_15());	//15天顾客订单数
			   			break;
			   		case 22:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getShopCollectNum());	//店铺收藏数
			   			break;
			   		case 23:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getGoodsCollectNum());	//宝贝收藏数
			   			break;
			   		case 24:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getTouchOfUser());	//触达用户
			   			break;
			   		case 25:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getTouchOfFrequency());	//触达频次
			   			break;
			   		case 26:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getCollectUser());	//收藏用户
			   			break;
			   		case 27:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getShowVisitor_3());	//3天展示访客
			   			break;
			   		case 28:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getShowVisitor_7());	//7天展示访客
			   			break;
			   		case 29:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getShowVisitor_15());	//15天展示访客
			   			break;
			   		case 30:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getShowRateOfReturn_3());	//3天展示回报率
			   			break;
			   		case 31:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getShowRateOfReturn_7());	//7天展示回报率
			   			break;
			   		case 32:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getOrderSum_7());	//7天订单金额
			   			break;
			   		case 33:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(bean.getOrderSum_15());	//15天订单金额
			   			break;
			   		default:
			   			cell.setCellStyle(cellStyle);
			   			cell.setCellValue(0);
			   			break;
			   }
		   }
	   }
	   try{
		   //FileOutputStream fout = new FileOutputStream("d://materialchart.xls");  
	       workbook.write(out);  
	   }catch(FileNotFoundException e){
		   e.printStackTrace();
	   }catch(IOException e){
		   e.printStackTrace();
	   }finally{
		   try{
			   out.close();
			   workbook.close();
		   }catch(IOException e){
			   e.printStackTrace();
		   }
	   }
	}
	
	// 自定义的方法,插入某个图片到指定索引的位置
	private static void insertImage(HSSFWorkbook wb, HSSFPatriarch pa, byte[] data, int row, int column, int index) {
		int x1 = index * 250;
		int y1 = 0;
		int x2 = x1 + 255;
		int y2 = 255;
		HSSFClientAnchor anchor = new HSSFClientAnchor(x1, y1, x2, y2, (short) column, row, (short) column, row);
		anchor.setAnchorType(2);
		pa.createPicture(anchor, wb.addPicture(data, HSSFWorkbook.PICTURE_TYPE_JPEG));
	}
	
	public static void main(String[] args) {
		List<MaterialChartSplitBean> dataset = new ArrayList<MaterialChartSplitBean>();
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
		dataset.add(bean3);
		String[] headersCustomer = {"创意图","创意名称","推广单元基本信息","所属推广计划","时间","展现","点击","点击率(%)","消耗","千次展现成本(元)","访客","访客获取成本","点击单价(元)","15天回报率","15天展示回报率","15天点击产出","15天展示产出","3天回报率","7天回报率","3天顾客订单数","7天顾客订单数","15天顾客订单数","店辅收藏数","宝贝收藏数","触达用户","触达频次","收藏用户","3天展示访客","7天展示访客","15天展示访客","3天展示回报率","7天展示回报率","7天订单金额","15天订单金额"};
		String[] headersSelf = {"创意图","创意名称","推广单元基本信息","所属推广计划","时间","展现","点击","点击率(%)","消耗","千次展现成本(元)","访客","访客获取成本","点击单价(元)","15天回报率","15天展示回报率","15天点击产出","15天展示产出","3天回报率","7天回报率","3天顾客订单数","7天顾客订单数","15天顾客订单数","店辅收藏数","宝贝收藏数","触达用户","触达频次","收藏用户","3天展示访客","7天展示访客","15天展示访客","3天展示回报率","7天展示回报率","7天订单金额","15天订单金额"};
		try {
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
		}
	}
}
