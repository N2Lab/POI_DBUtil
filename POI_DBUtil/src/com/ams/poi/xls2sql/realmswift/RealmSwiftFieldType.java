package com.ams.poi.xls2sql.realmswift;

import java.util.HashMap;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.objectivec.ObjectiveCFieldType;

/**
 * <p>タイトル: RealmSwiftFieldType</p>
 * <p>説明: DB>JAVAの型定義</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class RealmSwiftFieldType {
	// DBMS > RealmSwift定義マッピング
	static HashMap type_map;
	static {
//		Realmでは、次に示すデータ型をサポートしています: 
		// Bool、Int8、Int16、Int32、Int64、Double、Float、String、NSDate（ミリ秒以下は切り捨てられます）、NSData。
//
//		CGFloat型はプラットフォーム（CPUアーキテクチャ）によって実際の定義が変わるため、使用しないようにしてください。
//
//		String、NSDateおよびNSData型のプロパティはOptionalとして指定できます。Object型のプロパティは必ずOptionalでなければなりません。数値型をOptionalとして扱うにはRealmOptionalで実際の値をラップします。

		type_map = new HashMap();
		String[][] maps = {
        {"Long", "Int64"},
        {"LONG", "Int64"},
		        {"smallint", "Int8"},
		        {"Timestamp", "NSDate"},
		        {"date", "NSDate"},
		        {"varchar", "String"},
		        {"integer", "Int32"},
		        {"number", "Int32"},
		        {"Int32", "Int32"},
		        {"INT", "Int64"},
		        {"char", "String"},
		        {"raw", "String"},
		        {"tinyint", "Int32"},
		        {"serial", "Int32"},
		        {"INTEGER", "Int32"},
		        {"smallint", "Int32"},
		        {"bigint", "Int32"},
		        {"float", "Double"},
		        {"Double", "Double"},
		        {"DOUBLE PRECISION", "Double"},
		        {"REAL", "Double"}, // PGSQL は 4byte, MySQL は 8byte / JDBC 4byte に合わせる
		        {"set", "String"},
		        {"enum", "String"},
		        {"integer AUTO_INCREMENT", "Int64"},
		        {"integer  AUTO_INCREMENT", "Int64"},
		        {"integer   AUTO_INCREMENT", "Int64"},
		        {"integer    AUTO_INCREMENT", "Int64"},
		        {"integer     AUTO_INCREMENT", "Int64"},
		        {"BIGINT AUTO_INCREMENT", "NSLong"},
		        {"bit", "Int32"},
		        {"bool", "Int32"},
		        {"boolean", "Int32"},
		        {"decimal", "Double"},
		        {"dec", "java.math.BigDecimal"},
		        {"numeric", "String"},
		        {"fixed", "Double"},
		        {"time", "NSDate"},
		        {"year", "String"}, // int でも可能
		        {"TINYBLOB", "NSData"}, // byte[] から Blob に変更
		        {"TINYTEXT", "String"},
		        {"BLOB", "NSData"}, // byte[] から Blob に変更
		        {"TEXT", "String"},
		        {"MEDIUMBLOB", "NSData"}, // byte[] から Blob に変更
		        {"MEDIUMTEXT", "String"},
		        {"LONGBLOB", "NSData"}, // byte[] から Blob に変更
		        {"LONGTEXT", "String"},
		        {"INTEGER GENERATED ALWAYS AS IDENTITY", ""},
		        {"INTEGER GENERATED ALWAYS", "Int32"},
		        {"INTEGER GENERATED", "Int32"},
		        {"CHARACTER VARYING", "String"},
		        {"CHAR VARYING", "String"},
		        {"LONG VARCHAR", "String"},
		        {"CLOB", "String"},
		        {"DBCLOB", "String"},
//		        {"BLOB", "java.sql.Blob"}, // byte[] から Blob に変更
		        {"GRAPHIC", "String"},
		        {"VARGRAPHIC", "String"},
		        {"LONG VARGRAPHIC", "String"},
		        {"DATALINK", "String"},
		        {"DATALINK LINKTYPE URL", "String"},
		        {"CHARACTER", "String"},
		        {"bigserial", "long"}, // PostgreSQL
		        {"bytea", "byte[]"}, // PostgreSQL
		        {"timestamp with time zone", "java.util.Date"}, // PostgreSQL
		        {"timestamp  with time zone", "java.util.Date"}, // PostgreSQL
		        {"timestamp   with time zone", "java.util.Date"}, // PostgreSQL
		        {"timestamp    with time zone", "java.util.Date"}, // PostgreSQL
		        {"time with time zone", "java.util.Date"}, // PostgreSQL
		        {"time  with time zone", "java.util.Date"}, // PostgreSQL
		        {"time   with time zone", "java.util.Date"}, // PostgreSQL
		        {"time    with time zone", "java.util.Date"}, // PostgreSQL
		        {"interval", "Int32"}, // PostgreSQL
		        {"point", "String"}, // PostgreSQL
		        {"geometry", "String"}, // PostgreSQL
		        {"line", "org.postgresql.geometric.PGline"}, // PostgreSQL
		        {"lseg", "org.postgresql.geometric.PGlseg"}, // PostgreSQL
		        {"box", "org.postgresql.geometric.PGbox"}, // PostgreSQL
		        {"path", "org.postgresql.geometric.PGpath"}, // PostgreSQL
		        {"polygon", "org.postgresql.geometric.PGpolygon"}, // PostgreSQL
		        {"circle", "org.postgresql.geometric.PGcircle"}, // PostgreSQL
		        {"cidr", "String"}, // PostgreSQL
		        {"inet", "String"}, // PostgreSQL
		        {"macaddr", "String"}, // PostgreSQL
		        {"MEDIUMINT", "Int32"}, // MySQL
		        {"DATETIME", "String"}, // MySQL
		        {"VARCHAR2", "String"}, // Oracle
		        {"NVARCHAR2", "String"}, // Oracle
//		        {"LONG", "String"}, // Oracle
		        {"ROWID", "String"}, // Oracle
		        {"UROWID", "String"}, // Oracle
		        {"NCHAR", "String"}, // Oracle
		        {"NCLOB", "String"}, // Oracle
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
		final String ctype = ObjectiveCFieldType.getTypeByXLS(fd.getTypeXls());
		
		if ("Int32".equals(ctype)) {
			return "sqlite3_column_int(statement, c++)";
		} else if ("NSLong".equals(ctype)) {
				return "sqlite3_column_int(statement, c++)";
		} else if ("String".equals(ctype)) {
			return "!sqlite3_column_text(statement, c) ? nil : [[String stringWithUTF8String:(char*)sqlite3_column_text(statement, c)] copy]; c++";
		} else if ("Double".equals(ctype)) {
			return "sqlite3_column_double(statement, c++)";
		} else if ("float".equals(ctype)) {
			return "(float)sqlite3_column_double(statement, c++)";
		} else if ("NSData".equals(ctype)) {
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
		final String ctype = ObjectiveCFieldType.getTypeByXLS(fd.getTypeXls());
		
		if ("Int32".equals(ctype)) {
			return "sqlite3_bind_int(statement, c++, (int)m."+fd.getFieldNamePhysics()+")";
			
		}	if ("NSLong".equals(ctype)) {
			return "sqlite3_bind_int64(statement, c++, (sqlite3_int64)m."+fd.getFieldNamePhysics()+")";
			
		} else if ("String".equals(ctype)) {
			return "sqlite3_bind_text(statement, c++, [m."+fd.getFieldNamePhysics()+" UTF8String],-1, SQLITE_TRANSIENT)";
			
		} else if ("Double".equals(ctype)) {
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
