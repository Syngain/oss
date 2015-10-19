package com.guanhuodata.excel.service;

import java.io.File;
import java.io.OutputStream;

public interface ISummarySheetService {

	boolean isOriginalitySheetStyle(File filePath,String sheetType);

	boolean  readAndInsertToDB(File filePath);

	void summaryByCondition(String[] params, OutputStream os);

}
