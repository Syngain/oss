package com.guanhuodata.photo.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.guanhuodata.framework.core.Action;

public class PhotoMaterialAction implements Action {

	private static final Logger LOG = Logger.getLogger(PhotoMaterialAction.class);
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String type = request.getParameter("type");
		LOG.info("according type judge the page to forward start.");
		if("getImgList".equals(type)){
			getImgList(request,response);
		}else if("".equals(type)){
			
		}
	}

	private void getImgList(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
}
