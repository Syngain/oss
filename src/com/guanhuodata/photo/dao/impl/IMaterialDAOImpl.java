package com.guanhuodata.photo.dao.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.guanhuodata.framework.util.JsonUtil;
import com.guanhuodata.photo.bean.InitConditions;
import com.guanhuodata.photo.bean.MaterialChartSplitBean;
import com.guanhuodata.photo.bean.QImageInfoBean;
import com.guanhuodata.photo.bean.QueryCondition;
import com.guanhuodata.photo.dao.IMaterialDAO;
import com.guanhuodata.photo.util.MaterialChartUtil;
import com.guanhuodata.photo.util.Page;

/**
 * 
 * @author fudk
 * 素材数据访问层
 *
 */
public class IMaterialDAOImpl implements IMaterialDAO{
	
	private SessionFactory sessionFactory;

	@Override
	public List<MaterialChartSplitBean> getListByCondition(QueryCondition queryCondition) {
		Session session = getSession();
		//使用占位符
		StringBuffer hql = new StringBuffer("from MaterialChartSplitBean as m where 1=1");
		if(!queryCondition.getShopName().equals("all")){
			hql.append(" and m.shopName='").append(queryCondition.getShopName()).append("'");
		}
		if(!queryCondition.getStandSize().equals("all")){
			hql.append(" and m.materialStandAbbreviation='").append(MaterialChartUtil.regexpStandAbbreviation(queryCondition.getStandSize())).append("'");
		}
		if(!queryCondition.getActivityName().equals("all")){
			hql.append(" and m.materialTheme='").append(queryCondition.getActivityName()).append("'");
		}
		if(!queryCondition.getPutInCrowd().equals("all")){
			hql.append(" and m.materialCrowd='").append(queryCondition.getPutInCrowd()).append("'");
		}
		if(!queryCondition.getPutInDateTime().equals("")){
			hql.append(" and m.dateTime='").append(queryCondition.getPutInDateTime()).append("'");
		}
		if(!queryCondition.getCTR().equals("")){
			hql.append(" order by ").append(queryCondition.getCTR()).append(" ").append(queryCondition.getCTROrder());
		}
		if(!queryCondition.getReveal().equals("")){
			hql.append(" order by ").append(queryCondition.getReveal()).append(" ").append(queryCondition.getRevealOrder());
		}
		if(!queryCondition.getConsume().equals("")){
			hql.append(" order by ").append(queryCondition.getConsume()).append(" ").append(queryCondition.getConsumeOrder());
		}
		if(!queryCondition.getShowROI().equals("")){
			hql.append(" order by ").append(queryCondition.getShowROI()).append(" ").append(queryCondition.getShowROIOrder());
		}
		if(!queryCondition.getClickOutROI().equals("")){
			hql.append(" order by ").append(queryCondition.getClickOutROI()).append(" ").append(queryCondition.getClickOutROIOrder());
		}
		if(!queryCondition.getCPC().equals("")){
			hql.append(" order by ").append(queryCondition.getCPC()).append(" ").append(queryCondition.getCPCOrder());
		}
		//hql.append(" limit ").append(page.getTotal()).append(",").append(page.getPageSize());
		Query query = session.createQuery(hql.toString());
		List<MaterialChartSplitBean> list = query.list();
		session.close();
		return list;
	}
	
	@Override
	public Page getPaginationInfoByName(Page page,String originalityName) {
		Session session = getSession();
		//使用占位符
		String hql = "from MaterialChartSplitBean m where m.originalityName like:originalityName";
		Query query = session.createQuery(hql);
		query.setParameter("originalityName", "%" + originalityName + "%");
		List<MaterialChartSplitBean> list = query.list();
		page.setTotalRecords(list.size());
		int totalPages = list.size() % page.getPageSize() == 0 ? list.size()/page.getPageSize() : list.size()/page.getPageSize() + 1;
		page.setTotalPages(totalPages);
		session.close();
		return page;
	}
	
	@Override
	public List<MaterialChartSplitBean> findByName(Page page,String originalityName) {
		Session session = getSession();
		String hql = "from MaterialChartSplitBean m where m.originalityName like:originalityName";
		Query query = session.createQuery(hql);
		query.setParameter("originalityName", "%" + originalityName + "%");
		query.setFirstResult((page.getPageNumber() - 1) * page.getPageSize());
		query.setMaxResults(page.getPageSize());
		List<MaterialChartSplitBean> list = query.list();
		session.close();
		return list;
	}
	
	@Override
	public InitConditions getInitConditions() {
		//String hql = "select distinct shopName,materialCrowd,materialTheme,materialStandAbbreviation from MaterialChartSplitBean where (shopName,materialCrowd,materialTheme,materialStandAbbreviation) in (select a.shopName,a.materialCrowd,a.materialTheme,a.materialStandAbbreviation from MaterialChartSplitBean a inner join MaterialChartSplitBean b on a.shopName=b.shopName and a.materialCrowd=b.materialCrowd and a.materialTheme=b.materialTheme and a.materialStandAbbreviation=b.materialStandAbbreviation)";
		List<String> shopNameList = new ArrayList<String>();
		List<String> materialStandAbbreviationList = new ArrayList<String>();
		List<String> materialThemeList = new ArrayList<String>();
		List<String> materialCrowdList = new ArrayList<String>();
		InitConditions initConditions = new InitConditions();
		String distinctShopNameSQL = "select distinct shopName from materialchart";
		String distinctMaterialCrowdSQL = "select distinct materialCrowd from materialchart";
		String distinctMaterialThemeSQL = "select distinct materialTheme from materialchart";
		String distinctMaterialStandAbbreviationSQL = "select distinct materialStandAbbreviation from materialchart";
		Connection conn = MaterialChartUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(distinctShopNameSQL);
			rs = pstmt.executeQuery();
			while(rs.next()){
				shopNameList.add(rs.getString("shopName"));
			}
			System.out.println("shopName end*************************");
			pstmt = conn.prepareStatement(distinctMaterialCrowdSQL);
			rs = pstmt.executeQuery();
			while(rs.next()){
				materialCrowdList.add(rs.getString("materialCrowd"));
			}
			System.out.println("materialCrowd end*************************");
			pstmt = conn.prepareStatement(distinctMaterialThemeSQL);
			rs = pstmt.executeQuery();
			while(rs.next()){
				materialThemeList.add(rs.getString("materialTheme"));
			}
			System.out.println("materialTheme end*************************");
			pstmt = conn.prepareStatement(distinctMaterialStandAbbreviationSQL);
			rs = pstmt.executeQuery();
			while(rs.next()){
				materialStandAbbreviationList.add(MaterialChartUtil.regexpStandSize(rs.getString("materialStandAbbreviation")));
			}
			System.out.println("materialStandAbbreviation end*************************");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MaterialChartUtil.close(conn,pstmt,rs);
		initConditions.setMaterialCrowdList(materialCrowdList);
		initConditions.setMaterialStandAbbreviationList(materialStandAbbreviationList);
		initConditions.setMaterialThemeList(materialThemeList);
		initConditions.setShopNameList(shopNameList);
		return initConditions;
	}
	
	@Override
	public Page getPaginationInfoByCondition(Page page, QueryCondition queryCondition) {
		Session session = getSession();
		//使用占位符
		StringBuffer hql = new StringBuffer("from MaterialChartSplitBean as m where 1=1");
		if(!queryCondition.getShopName().equals("all")){
			hql.append(" and m.shopName='").append(queryCondition.getShopName()).append("'");
		}
		if(!queryCondition.getStandSize().equals("all")){
			hql.append(" and m.materialStandAbbreviation='").append(MaterialChartUtil.regexpStandAbbreviation(queryCondition.getStandSize())).append("'");
		}
		if(!queryCondition.getActivityName().equals("all")){
			hql.append(" and m.materialTheme='").append(queryCondition.getActivityName()).append("'");
		}
		if(!queryCondition.getPutInCrowd().equals("all")){
			hql.append(" and m.materialCrowd='").append(queryCondition.getPutInCrowd()).append("'");
		}
		if(!queryCondition.getPutInDateTime().equals("")){
			hql.append(" and m.dateTime='").append(queryCondition.getPutInDateTime()).append("'");
		}
		if(!queryCondition.getCTR().equals("")){
			hql.append(" order by ").append(queryCondition.getCTR()).append(" ").append(queryCondition.getCTROrder());
		}
		if(!queryCondition.getReveal().equals("")){
			hql.append(" order by ").append(queryCondition.getReveal()).append(" ").append(queryCondition.getRevealOrder());
		}
		if(!queryCondition.getConsume().equals("")){
			hql.append(" order by ").append(queryCondition.getConsume()).append(" ").append(queryCondition.getConsumeOrder());
		}
		//15天展示回报率
		if(!queryCondition.getShowROI().equals("")){
			hql.append(" order by ").append(queryCondition.getShowROI()).append(" ").append(queryCondition.getShowROIOrder());
		}
		//15天点击回报率
		if(!queryCondition.getClickOutROI().equals("")){
			hql.append(" order by ").append(queryCondition.getClickOutROI()).append(" ").append(queryCondition.getClickOutROIOrder());
		}
		if(!queryCondition.getCPC().equals("")){
			hql.append(" order by ").append(queryCondition.getCPC()).append(" ").append(queryCondition.getCPCOrder());
		}
		//hql.append(" limit ").append(page.getTotal()).append(",").append(page.getPageSize());
		Query query = session.createQuery(hql.toString());
		List<MaterialChartSplitBean> list = query.list();
		page.setTotalRecords(list.size());
		int totalPages = list.size() % page.getPageSize() == 0 ? list.size()/page.getPageSize() : list.size()/page.getPageSize() + 1;
		page.setTotalPages(totalPages);
		session.close();
		return page;
	}
	
	@Override
	public Page getPaginationInfo(Page page) {
		Session session = getSession();
		String hql = "from MaterialChartSplitBean";
		Query query = session.createQuery(hql);
		List<MaterialChartSplitBean> list = query.list();
		page.setTotalRecords(list.size());
		int totalPages = list.size() % page.getPageSize() == 0 ? list.size()/page.getPageSize() : list.size()/page.getPageSize() + 1;
		page.setTotalPages(totalPages);
		session.close();
		return page;
	}
	
	@Override
	public String getOriginalityNameById(long imageId) {
		Session session = getSession();
		MaterialChartSplitBean bean = (MaterialChartSplitBean)session.get(MaterialChartSplitBean.class,imageId);
		session.close();
		return bean.getOriginalityName();
	}
	
	//处理页面的条件查询List<MaterialChartSplitBean>
	@Override
	public List<MaterialChartSplitBean> getPagedMaterialInfos(Page page, QueryCondition queryCondition) {
		
		Session session = getSession();
		//使用占位符
		StringBuffer hql = new StringBuffer("from MaterialChartSplitBean as m where 1=1");
		if(!queryCondition.getShopName().equals("all")){
			hql.append(" and m.shopName='").append(queryCondition.getShopName()).append("'");
		}
		if(!queryCondition.getStandSize().equals("all")){
			hql.append(" and m.materialStandAbbreviation='").append(MaterialChartUtil.regexpStandAbbreviation(queryCondition.getStandSize())).append("'");
		}
		if(!queryCondition.getActivityName().equals("all")){
			hql.append(" and m.materialTheme='").append(queryCondition.getActivityName()).append("'");
		}
		if(!queryCondition.getPutInCrowd().equals("all")){
			hql.append(" and m.materialCrowd='").append(queryCondition.getPutInCrowd()).append("'");
		}
		if(!queryCondition.getPutInDateTime().equals("")){
			hql.append(" and m.dateTime='").append(queryCondition.getPutInDateTime()).append("'");
		}
		if(!queryCondition.getCTR().equals("")){
			hql.append(" order by ").append(queryCondition.getCTR()).append(" ").append(queryCondition.getCTROrder());
		}
		if(!queryCondition.getReveal().equals("")){
			hql.append(" order by ").append(queryCondition.getReveal()).append(" ").append(queryCondition.getRevealOrder());
		}
		if(!queryCondition.getConsume().equals("")){
			hql.append(" order by ").append(queryCondition.getConsume()).append(" ").append(queryCondition.getConsumeOrder());
		}
		if(!queryCondition.getShowROI().equals("")){
			hql.append(" order by ").append(queryCondition.getShowROI()).append(" ").append(queryCondition.getShowROIOrder());
		}
		if(!queryCondition.getClickOutROI().equals("")){
			hql.append(" order by ").append(queryCondition.getClickOutROI()).append(" ").append(queryCondition.getClickOutROIOrder());
		}
		if(!queryCondition.getCPC().equals("")){
			hql.append(" order by ").append(queryCondition.getCPC()).append(" ").append(queryCondition.getCPCOrder());
		}
		//hql.append(" limit ").append(page.getTotal()).append(",").append(page.getPageSize());
		Query query = session.createQuery(hql.toString());
		query.setFirstResult((page.getPageNumber() - 1) * page.getPageSize());
		query.setMaxResults(page.getPageSize());
		List<MaterialChartSplitBean> list = query.list();
		session.close();
		return list;
	}
	
	
	@Override
	public String getQImageInfoByQImageId(long qImgId) {
		Session session = getSession();
		MaterialChartSplitBean bean = (MaterialChartSplitBean)session.get(MaterialChartSplitBean.class, qImgId);
		QImageInfoBean qBean = new QImageInfoBean();
		qBean.setOriginalityName(bean.getOriginalityName());
		qBean.setShop(bean.getShopName());
		qBean.setCTR(bean.getCTR());
		qBean.setPutInCrowd(bean.getUnitCrowd());
		qBean.setTitle(bean.getScheduleActivityTheme());
		qBean.setROI(bean.getShowRateOfReturn_15());
		//枚举获得展位尺寸
		qBean.setImageSize(MaterialChartUtil.regexpStandSize(bean.getMaterialStandAbbreviation()));
		qBean.setPutInTime(bean.getDateTime());
		qBean.setLinkAddress(bean.getMaterialContinuePage());
		String ret = JsonUtil.makeJson(qBean);
		session.close();
		return ret;
	}
	
	@Override
	public boolean readMaterialExcelInsertToDB(File file) {
		boolean flag = false;
		List<MaterialChartSplitBean> list = MaterialChartUtil.readMaterialExcel(file);
		if(list.size() > 0){
			for(MaterialChartSplitBean bean : list){
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
