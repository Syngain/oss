package com.guanhuodata.excel.dao;

import java.io.File;
import java.io.OutputStream;

public interface ISummarySheetDAO {

	boolean isOriginalitySheetStyle(File filePath,String sheetType);

	boolean readAndInsertToDB(File filePath);

	void summaryByCondition(String[] params, OutputStream os);

}
