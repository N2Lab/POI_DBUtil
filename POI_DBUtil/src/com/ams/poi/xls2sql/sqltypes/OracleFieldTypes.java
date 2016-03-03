package com.ams.poi.xls2sql.sqltypes;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

/**
 * <p>タイトル: OracleFieldTypes</p>
 * <p>説明: オラクルの型定義</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class OracleFieldTypes {

	// XLS > Oracle定義マッピング
	static HashMap type_map;
	static {
		type_map = new HashMap();
		// type_map.put(XLS type, Oracle type)
		type_map.put("SERIAL", "INTEGER");
		type_map.put("Timestamp", "DATE");
        type_map.put("Date", "DATE");
        type_map.put("DATE", "DATE");
		type_map.put("date", "DATE");
        type_map.put("DATETIME", "DATE"); // oracleにはDATETIMEが無い
        type_map.put("TIMESTAMP", "DATE"); // oracleにはTIMESTAMPが無い
        type_map.put("TIME", "DATE"); // oracleにはTIMEが無い
        type_map.put("varchar", "VARCHAR2");
        type_map.put("varchar2", "VARCHAR2");
        type_map.put("VARCHAR2", "VARCHAR2");
        type_map.put("NVARCHAR2", "NVARCHAR2");
        type_map.put("nvarchar2", "NVARCHAR2");
		type_map.put("integer", "NUMBER");
		type_map.put("Integer", "NUMBER");
		type_map.put("NUMBER", "NUMBER");
		type_map.put("Number", "NUMBER");
		type_map.put("int", "NUMBER");
		type_map.put("Int", "NUMBER");
        type_map.put("INT", "NUMBER");
        type_map.put("INTEGER", "NUMBER");
        type_map.put("BIGINT", "NUMBER");
        type_map.put("CHAR", "CHAR");
        type_map.put("Char", "CHAR");
        type_map.put("char", "CHAR");
        type_map.put("NCHAR", "NCHAR");
        type_map.put("Nchar", "NCHAR");
        type_map.put("nchar", "NCHAR");
		type_map.put("RAW", "RAW");
		type_map.put("Raw", "RAW");
		type_map.put("raw", "RAW");

		type_map.put("TINYINT", "NUMBER");
		type_map.put("tinyint", "NUMBER");
		
		type_map.put("integer AUTO_INCREMENT", "INTEGER");
		type_map.put("integer  AUTO_INCREMENT", "INTEGER");
		type_map.put("integer   AUTO_INCREMENT", "INTEGER");
		type_map.put("integer    AUTO_INCREMENT", "INTEGER");

//		type_map.put("System", "");
        // Oracle以外のDBMSフィールド対応
        type_map.put("BIT", "CHAR(1)");
        type_map.put("bit", "CHAR(1)");
        type_map.put("BOOL", "CHAR(5)");
        type_map.put("bool", "CHAR(5)");
        type_map.put("BOOLEAN", "CHAR(5)");
        type_map.put("boolean", "CHAR(5)");
        type_map.put("SMALLINT", "NUMBER(3)");
        type_map.put("MEDIUMINT", "NUMBER(6)");
        type_map.put("FLOAT", "NUMBER(10,10)"); // これはエラーにならないようにしてるだけで
        // 実際は無効
        type_map.put("FIXED", "NUMBER(10,10)"); // これはエラーにならないようにしてるだけで
        // 実際は無効
        type_map.put("DOUBLE", "NUMBER(20,10)"); // これはエラーにならないようにしてるだけで
        // 実際は無効
        type_map.put("DOUBLE PRECISION", "NUMBER(20,10)"); // これはエラーにならないようにしてるだけで
        // 実際は無効
        type_map.put("REAL", "NUMBER(20,10)"); // これはエラーにならないようにしてるだけで
        // 実際は無効
        type_map.put("DECIMAL", "NUMBER"); // これはエラーにならないようにしてるだけで
        // 実際は無効
        type_map.put("DEC", "NUMBER"); // これはエラーにならないようにしてるだけで
        // 実際は無効
        type_map.put("NUMERIC", "NUMBER"); // これはエラーにならないようにしてるだけで
        // 実際は無効
        type_map.put("YEAR", "CHAR(4)");
	}


	/**
	 * getTypeByXLS<BR>
	 * XLS定義TypeからOracle用FieldTypeを取得する
	 * @param xls_type XLS定義タイプ
	 * @return Oracle用FieldType
	 */
	public static String getTypeByXLS(String xls_type) {
		
		// 括弧を探す
		// 全角にも対応してやった
		int n1 = xls_type.indexOf('(');
		if (n1 == -1 ) {
			n1 = xls_type.indexOf('（');
		}
		int n2 = xls_type.indexOf(')', n1);
		if (n2 == -1) {
			n2 = xls_type.indexOf('）', n1);
		}
		
		if (n1 == -1 || n2 == -1 || n1 > n2) {
			// サイズ無しtype
			return (String)type_map.get(xls_type);
		}
		
		// サイズ有りtype
		String type_only = xls_type.substring(0, n1);
		
		return (String)type_map.get(type_only) + xls_type.substring(n1, n2 + 1);
		
	}

	// Oracle > JavaBean定義マッピング
//	static HashMap type_map_java;
//	static {
//		type_map_java = new HashMap();
//		type_map_java.put("INTEGER", "int");
//		type_map_java.put("DATE", "Timestamp");
//		type_map_java.put("VARCHAR2", "String");
//		type_map_java.put("NUMBER", "int");
//		type_map_java.put("CHAR", "String");
//		type_map_java.put("RAW", "Object");
//	}

	// XLS > Oracle insert フィールド用
	// Oracle の定数など出力書式を特別に定義するもの
	static HashMap insert_type_map;
	static {
		insert_type_map = new HashMap();
		insert_type_map.put("SYSDATE", "SYSDATE");
		insert_type_map.put("NULL", "NULL");
		insert_type_map.put("null", "null");
	}

	/**
	 * getInsertTypeDependOracle<BR>
	 * XLSに記述した値をSQLに記述可能な書式に変換して返す。
	 * @param val 調べる値
	 * @return 出力文字列
	 */
	public static String getInsertType(Object val) {
		String res = "";
		
		if (val instanceof String) {
			String val_t = StringUtils.trim((String)val);
			if (insert_type_map.containsKey(val_t)) {
				res = (String)insert_type_map.get(val_t);				
			} else {
				res = "'" + val_t + "'";
			}
										
		} else if (val instanceof Double) {
			res = "" + (Double)val;

		} else if (val instanceof Integer) {
			res = "" + (Integer)val;

		}
		return res;		
	}
}
