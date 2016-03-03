package com.ams.poi.xls2sql.sqltypes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.util.EscapeUtil;

/**
 * <p>タイトル: DB2FieldTypes</p>
 * <p>説明: DB2の型定義</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class DB2FieldTypes {

	// XLS > DB2定義マッピング
	static HashMap type_map;
	static {
		type_map = new HashMap();
		// type_map.put(XLS type, DB2 type)
		type_map.put("SERIAL", "INTEGER");
		type_map.put("Timestamp", "TIMESTAMP");
		type_map.put("Date", "DATE");
		type_map.put("date", "DATE");
		type_map.put("Time", "TIME");
		type_map.put("time", "TIME");
		type_map.put("varchar", "VARCHAR");
		type_map.put("long varchar", "TEXT");
		type_map.put("integer", "INTEGER");
		type_map.put("Integer", "INTEGER");
		type_map.put("NUMBER", "INTEGER");
		type_map.put("Number", "INTEGER");
		type_map.put("int", "INTEGER");
		type_map.put("Int", "INTEGER");
		type_map.put("CHAR", "CHAR");
		type_map.put("Char", "CHAR");
		type_map.put("char", "CHAR");
		type_map.put("RAW", "RAW");
		type_map.put("Raw", "RAW");
		type_map.put("raw", "RAW");

		type_map.put("TINYINT", "SMALLINT"); // not exist in DB2
		type_map.put("SMALLINT", "SMALLINT");
		type_map.put("MEDIUMINT", "INTEGER"); // not exist in DB2
		type_map.put("BIGINT", "BIGINT");
		type_map.put("FLOAT", "FLOAT");
		type_map.put("DOUBLE", "DOUBLE");
		//type_map.put("DOUBLE PRECISION", "DOUBLE PRECISION"); not exist in DB2
		type_map.put("REAL", "REAL");
		type_map.put("DECIMAL", "DECIMAL");

		//type_map.put("SET", "SET");
		//type_map.put("ENUM", "ENUM");
		type_map.put("CLOB", "CLOB");
		type_map.put("BLOB", "BLOB");
    
	    type_map.put("TEXT", "TEXT");
	    type_map.put("Text", "TEXT");
	    type_map.put("text", "TEXT");
    
    	// auto number
		type_map.put("integer  AUTO_INCREMENT", "INTEGER GENERATED ALWAYS AS IDENTITY");
		type_map.put("integer AUTO_INCREMENT", "INTEGER GENERATED ALWAYS AS IDENTITY");
    
//		type_map.put("System", "");
	}


	/**
	 * getTypeByXLS<BR>
	 * XLS定義TypeからDB2用FieldTypeを取得する
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
		
		String result = null;
		if (n1 == -1 || n2 == -1 || n1 > n2) {
			// サイズ無しtype
			result = (String)type_map.get(xls_type);
		} else {
		
			// サイズ有りtype
			String type_only = xls_type.substring(0, n1);
			if (type_map.containsKey(type_only)) {
				result = (String)type_map.get(type_only) + xls_type.substring(n1, n2 + 1);
			}
		}
		if (result == null) {
			result = xls_type;
		}
		return result;
	}


	// XLS > DB2 insert フィールド用
	// MySQL の定数など出力書式を特別に定義するもの( '' でくくらないもの)
	static HashMap insert_type_map;
	static {
		insert_type_map = new HashMap();
		// DB2 は CURRENT TIMESTAMP で現在のタイムスタンプを表す
		insert_type_map.put("SYSDATE", "CURRENT TIMESTAMP");
		insert_type_map.put("NOW()", "CURRENT TIMESTAMP");
		insert_type_map.put("NOW", "CURRENT TIMESTAMP");
	}
	
	// XLS > DB2 insert 値作成時に　値を日付として変換するXLSの型
    private static Set datetime_type;
    static {
        datetime_type = new HashSet(10);
        datetime_type.add("TIMESTAMP");
        datetime_type.add("DATE");
        datetime_type.add("TIME");
    }

    // XLS > DB2 insert 値作成時に 値を '' で囲むXLSの型(文字列の型)
    private static Set add_sq_type;
    static {
        add_sq_type = new HashSet(10);
        add_sq_type.add("CHAR");
        add_sq_type.add("VARCHAR");
        add_sq_type.add("TEXT");
        add_sq_type.add("TIMESTAMP");
        add_sq_type.add("DATE");
        add_sq_type.add("TIME");
    }

//	/**
//	 * getInsertTypeDependOracle<BR>
//	 * XLSに記述した値をSQLに記述可能な書式に変換して返す。
//	 * @param val 調べる値
//	 * @return 出力文字列
//	 */
//	public static String getInsertType(Object val) {
//		String res = "";
//		
//		if (val instanceof String) {
//			String val_t = StringUtils.trim((String)val);
//			if (insert_type_map.containsKey(val_t)) {
//				res = (String)insert_type_map.get(val_t);				
//			} else {
//			    // 日付値の場合はできればここで 'yyyy/mm/dd' > 'yyyy-mm-dd' 変換する
//			    if (val_t.matches("^[0-9]+/[0-9]+/[0-9]+$")) {
//			        val_t = val_t.replace('/', '-');
//			    }
//				res = "'" + EscapeUtil.escapeMySQL(val_t) + "'";
//			}
//										
//		} else if (val instanceof Double) {
//			res = "" + (Double)val;
//
//		} else if (val instanceof Integer) {
//			res = "" + (Integer)val;
//
//		}
//		return res;		
//	}

    /**
	 * getInsertTypeDependOracle<BR>
	 * XLSに記述した値をSQLに記述可能な書式に変換して返す。
	 * @param val 調べる値
     * @param def フィールド定義
	 * @return 出力文字列
     */
    public static String getInsertType(Object val, FieldDef def) {
		String res = "";
		
		if (val instanceof String) {
			String val_t = StringUtils.trim((String)val);
			if (insert_type_map.containsKey(val_t)) {
				res = (String)insert_type_map.get(val_t);				
			} else {
			    if (datetime_type.contains(def.getTypeXls().toUpperCase())) {
				    // 日付値の場合はここで 'yyyy/mm/dd' > 'yyyy-mm-dd' 変換する
			        val_t = val_t.replace('/', '-');
			    }
		        
			    // 値を取得
				res = EscapeUtil.escapeMySQL(val_t);
				
			    // フィールドの型によって'をつけるかどうか判断する
			    for(Iterator it = add_sq_type.iterator(); it.hasNext(); ) {
			        String add_sq_type_name = (String)it.next();
			        if (def.getTypeXls().toUpperCase().startsWith(add_sq_type_name)) {
			            // 先頭が一致したので ' をつける
						res = "'" + res + "'";
			            break;
			        }
			    }
			}
										
		} else if (val instanceof Double) {
			res = "" + (Double)val;

		} else if (val instanceof Integer) {
			res = "" + (Integer)val;

		}
		return res;
    }
}
