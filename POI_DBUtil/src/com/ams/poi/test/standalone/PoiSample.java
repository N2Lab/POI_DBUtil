package com.ams.poi.test.standalone;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * <p>タイトル: PoiSample</p>
 * <p>説明: POIサンプルクラス</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class PoiSample {

	private static final String XLS_FILE =
		"E:\\Develop\\JAVA\\study\\Eclipse\\POI_DBUtil\\doc\\テーブル_カスタマイズメニュー.xls";

	public static void main(String[] args) {
		PoiSample ps = new PoiSample();
		try {
			ps.outputSheetInfo();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void outputSheetInfo() throws FileNotFoundException, IOException {

		// Create POIFS
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(XLS_FILE));

		// Create WorkBook
		HSSFWorkbook workbook = new HSSFWorkbook(fs);
		
		// get sheet
		HSSFSheet sheet = workbook.getSheet("Sheet1"); // 全角はだめ
		
		// get Row
		HSSFRow row = sheet.getRow(4);
		
		// get Col
		HSSFCell cell = row.getCell((short)1); // B
		
		// get value
		String val = cell.getStringCellValue();
		
		System.out.println("4:1 = " + val);
	}
}
