package com.guanhuodata.excel.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import com.guanhuodata.excel.dao.IChartDAO;

public class IChartDAOImpl implements IChartDAO{

	@Override
	public void readChart(String path) {
		// TODO Auto-generated method stub
		File file = new File(path);
		try {
			InputStream is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
