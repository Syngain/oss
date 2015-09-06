package com.guanhuodata.framework.log;

import com.guanhuodata.framework.log.loggerTool.CTOMSLogDAO;

public class CtomsLoger {
	private int isActionLogger;
	private CTOMSLogDAO ctomsLogDao;

	public void addLogger(CtomsLoggerBean clb) {
		if (isActionLogger == 1) {
			ctomsLogDao.addLog(clb);
		}
	}

	public void setIsActionLogger(int isActionLogger) {
		this.isActionLogger = isActionLogger;
	}

	public void setCtomsLogDao(CTOMSLogDAO ctomsLogDao) {
		this.ctomsLogDao = ctomsLogDao;
	}
}
