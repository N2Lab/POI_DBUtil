package com.ams.poi.xls2sql.realmswiftmaker;

import java.util.HashMap;

import com.ams.poi.xls2sql.bean.FieldDef;

/**
 * <p>タイトル: RealmswiftFieldType</p>
 * <p>説明: DB>Realmswift(swift)の型定義</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class RealmswiftFieldType {

	// DBMS > Swift定義マッピング
	static HashMap type_map;
	static {
		type_map = new HashMap();
		String[][] maps = {
        {"Long", "RealmOptional<Int>()"},
        {"LONG", "RealmOptional<Int>()"},
		        {"smallint", "RealmOptional<Int>()"},
		        {"Timestamp", "\"\""},
		        {"date", "\"\""},
		        {"varchar", "\"\""},
		        {"integer", "RealmOptional<Int>()"},
		        {"number", "RealmOptional<Int>()"},
		        {"int", "RealmOptional<Int>()"},
		        {"char", "\"\""},
		        {"raw", "\"\""},
		        {"tinyint", "RealmOptional<Int>()"},
		        {"serial", "RealmOptional<Int>()"},
		        {"INTEGER", "RealmOptional<Int>()"},
		        {"smallint", "RealmOptional<Int>()"},
		        {"bigint", "RealmOptional<Int>()"},
		        {"float", "RealmOptional<Float>()"},
		        {"double", "RealmOptional<Double>()"},
		        {"DOUBLE PRECISION", "RealmOptional<Double>()"},
		        {"REAL", "RealmOptional<Double>()"}, // PGSQL は 4byte, MySQL は 8byte / JDBC 4byte に合わせる
		        {"set", "\"\""},
		        {"enum", "\"\""},
		        {"integer AUTO_INCREMENT", "RealmOptional<Int>()"},
		        {"integer  AUTO_INCREMENT", "RealmOptional<Int>()"},
		        {"integer   AUTO_INCREMENT", "RealmOptional<Int>()"},
		        {"integer    AUTO_INCREMENT", "RealmOptional<Int>()"},
		        {"integer     AUTO_INCREMENT", "RealmOptional<Int>()"},
		        {"BIGINT AUTO_INCREMENT", "NSLong"},
		        {"bit", "RealmOptional<Int>()"},
		        {"bool", "RealmOptional<Bool>()"},
		        {"boolean", "RealmOptional<Bool>()"},
		        {"decimal", "double"},
		        {"dec", "java.math.BigDecimal"},
		        {"numeric", "\"\""},
		        {"fixed", "double"},
		        {"time", "\"\""},
		        {"year", "\"\""}, // int でも可能
		        {"TINYBLOB", "NSData *"}, // byte[] から Blob に変更
		        {"TINYTEXT", "\"\""},
		        {"BLOB", "NSData *"}, // byte[] から Blob に変更
		        {"TEXT", "\"\""},
		        {"MEDIUMBLOB", "NSData *"}, // byte[] から Blob に変更
		        {"MEDIUMTEXT", "\"\""},
		        {"LONGBLOB", "NSData *"}, // byte[] から Blob に変更
		        {"LONGTEXT", "\"\""},
		        {"INTEGER GENERATED ALWAYS AS IDENTITY", ""},
		        {"INTEGER GENERATED ALWAYS", "RealmOptional<Int>()"},
		        {"INTEGER GENERATED", "RealmOptional<Int>()"},
		        {"CHARACTER VARYING", "\"\""},
		        {"CHAR VARYING", "\"\""},
		        {"LONG VARCHAR", "\"\""},
		        {"CLOB", "\"\""},
		        {"DBCLOB", "\"\""},
//		        {"BLOB", "java.sql.Blob"}, // byte[] から Blob に変更
		        {"GRAPHIC", "\"\""},
		        {"VARGRAPHIC", "\"\""},
		        {"LONG VARGRAPHIC", "\"\""},
		        {"DATALINK", "\"\""},
		        {"DATALINK LINKTYPE URL", "\"\""},
		        {"CHARACTER", "\"\""},
		        {"bigserial", "RealmOptional<Int>()"}, // PostgreSQL
		        {"bytea", "byte[]"}, // PostgreSQL
		        {"timestamp with time zone", "java.util.Date"}, // PostgreSQL
		        {"timestamp  with time zone", "java.util.Date"}, // PostgreSQL
		        {"timestamp   with time zone", "java.util.Date"}, // PostgreSQL
		        {"timestamp    with time zone", "java.util.Date"}, // PostgreSQL
		        {"time with time zone", "java.util.Date"}, // PostgreSQL
		        {"time  with time zone", "java.util.Date"}, // PostgreSQL
		        {"time   with time zone", "java.util.Date"}, // PostgreSQL
		        {"time    with time zone", "java.util.Date"}, // PostgreSQL
		        {"interval", "RealmOptional<Int>()"}, // PostgreSQL
		        {"point", "\"\""}, // PostgreSQL
		        {"geometry", "\"\""}, // PostgreSQL
		        {"line", "org.postgresql.geometric.PGline"}, // PostgreSQL
		        {"lseg", "org.postgresql.geometric.PGlseg"}, // PostgreSQL
		        {"box", "org.postgresql.geometric.PGbox"}, // PostgreSQL
		        {"path", "org.postgresql.geometric.PGpath"}, // PostgreSQL
		        {"polygon", "org.postgresql.geometric.PGpolygon"}, // PostgreSQL
		        {"circle", "org.postgresql.geometric.PGcircle"}, // PostgreSQL
		        {"cidr", "\"\""}, // PostgreSQL
		        {"inet", "\"\""}, // PostgreSQL
		        {"macaddr", "\"\""}, // PostgreSQL
		        {"MEDIUMINT", "int"}, // MySQL
		        {"DATETIME", "\"\""}, // MySQL
		        {"VARCHAR2", "\"\""}, // Oracle
		        {"NVARCHAR2", "\"\""}, // Oracle
//		        {"LONG", "\"\""}, // Oracle
		        {"ROWID", "\"\""}, // Oracle
		        {"UROWID", "\"\""}, // Oracle
		        {"NCHAR", "\"\""}, // Oracle
		        {"NCLOB", "\"\""}, // Oracle
		};
		for(int i = 0 ; i < maps.length; i++) {
		    // 3パターンで格納 (int, INT, Int)
		    String low_name = maps[i][0].toLowerCase();
            type_map.put(low_name, maps[i][1]);
		    type_map.put(maps[i][0].toUpperCase(), maps[i][1]);
		    type_map.put(low_name.substring(0,1).toUpperCase() + 
		            low_name.substring(1), maps[i][1]);
		}
	}


	/**
	 * getTypeByXLS<BR>
	 * XLS定義TypeからObjective-C用変数型を取得する
	 * @param xls_type XLS定義タイプ
	 * @return Java用型
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
		
		String result;
		if (n1 == -1 || n2 == -1 || n1 > n2) {
			// サイズ無しtype
			result = (String)type_map.get(xls_type.toUpperCase());
		} else {
		
			// サイズ有りtype
			String type_only = xls_type.substring(0, n1);		
			result = (String)type_map.get(type_only.toUpperCase());
//      result = (String)type_map.get(type_only) + xls_type.substring(n1, n2 + 1);
		}
		if (result == null) {
			result = xls_type;
		    System.out.println("[getTypeByXLS] xls_type is org:" + result);
		}
		return result;
	}


	/**
	 * 検索結果取得コードを返す
	 * @param fd
	 * @return
	 */
	public static String getFetchCall(FieldDef fd) {
		final String ctype = RealmswiftFieldType.getTypeByXLS(fd.getTypeXls());
		
		if ("NSInteger".equals(ctype)) {
			return "sqlite3_column_int(statement, c++)";
		} else if ("NSLong".equals(ctype)) {
				return "sqlite3_column_int(statement, c++)";
		} else if ("\"\"".equals(ctype)) {
			return "!sqlite3_column_text(statement, c) ? nil : [[NSString stringWithUTF8String:(char*)sqlite3_column_text(statement, c)] copy]; c++";
		} else if ("double".equals(ctype)) {
			return "sqlite3_column_double(statement, c++)";
		} else if ("float".equals(ctype)) {
			return "(float)sqlite3_column_double(statement, c++)";
		} else if ("NSData *".equals(ctype)) {
			return "[[NSData alloc] initWithBytes:sqlite3_column_blob(statement, c++) length:sqlite3_column_bytes(statement, c-1)]";
		}
		
		System.err.println("invalid getFetchCall : fd.getTypeXls()=" + fd.getTypeXls());
				
		return "";
	}


	/**
	 * sqlite3_bind_xxxx コードを返す
	 * @param fd
	 * @return
	 */
	public static String getBindValueCall(FieldDef fd) {
		final String ctype = RealmswiftFieldType.getTypeByXLS(fd.getTypeXls());
		
		if ("NSInteger".equals(ctype)) {
			return "sqlite3_bind_int(statement, c++, (int)m."+fd.getFieldNamePhysics()+")";
			
		}	if ("NSLong".equals(ctype)) {
			return "sqlite3_bind_int64(statement, c++, (sqlite3_int64)m."+fd.getFieldNamePhysics()+")";
			
		} else if ("\"\"".equals(ctype)) {
			return "sqlite3_bind_text(statement, c++, [m."+fd.getFieldNamePhysics()+" UTF8String],-1, SQLITE_TRANSIENT)";
			
		} else if ("double".equals(ctype)) {
			return "sqlite3_bind_double(statement, c++, m."+fd.getFieldNamePhysics()+")";
			
		} else if ("float".equals(ctype)) {
			return "sqlite3_bind_double(statement, c++, m."+fd.getFieldNamePhysics()+")";
		}
		
		System.err.println("invalid getFetchCall : fd.getTypeXls()=" + fd.getTypeXls());
				
		return "";
//		+ "		sqlite3_bind_text(statement, c++, [m.url UTF8String],-1, SQLITE_TRANSIENT);\n"
//		+ "		sqlite3_bind_text(statement, c++, [m.title UTF8String],-1, SQLITE_TRANSIENT);\n"
//		+ "		sqlite3_bind_text(statement, c++, [m.contents UTF8String],-1, SQLITE_TRANSIENT);\n"
//		+ "		sqlite3_bind_int(statement, c++, m.disp_order);\n"
//		+ "		sqlite3_bind_text(statement, c++, [m.registDate UTF8String],-1, SQLITE_TRANSIENT);\n"
	}
}
