package com.ams.poi.xls2sql.util;

public class CommonUtil {

	public static boolean doCreateSqlFileOnly() {
		return System.getProperty("ONLY_SQL_FILE") != null;
	}
}
