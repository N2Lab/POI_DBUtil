package com.ams.poi.xls2sql.sqltypes;

import java.util.HashMap;

/**
 * <p>タイトル: JavaFieldTypes</p>
 * <p>説明: DB>JAVAの型定義</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class JavaFieldTypes {

	// DBMS > JAVA定義マッピング
	static HashMap type_map;
	static {
		type_map = new HashMap();
		String[][] maps = {
		        {"serial", "int"},
		        {"smallint", "short"},
		        {"Timestamp", "java.util.Date"},
		        {"date", "java.util.Date"},
		        {"varchar", "String"},
		        {"integer", "int"},
		        {"number", "int"},
		        {"int", "int"},
		        {"char", "String"},
		        {"raw", "byte[]"},
		        {"tinyint", "byte"},
		        {"smallint", "short"},
		        {"bigint", "long"},
		        {"float", "float"},
		        {"double", "double"},
		        {"DOUBLE PRECISION", "double"},
		        {"REAL", "float"}, // PGSQL は 4byte, MySQL は 8byte / JDBC 4byte に合わせる
		        {"set", "String"},
		        {"enum", "String"},
		        {"integer AUTO_INCREMENT", "int"},
		        {"integer  AUTO_INCREMENT", "int"},
		        {"integer   AUTO_INCREMENT", "int"},
		        {"integer    AUTO_INCREMENT", "int"},
		        {"integer     AUTO_INCREMENT", "int"},
		        {"BIGINT AUTO_INCREMENT", "long"},
		        
		        {"bit", "byte"},
		        {"bool", "boolean"},
		        {"boolean", "boolean"},
		        {"decimal", "java.math.BigDecimal"},
		        {"dec", "java.math.BigDecimal"},
		        {"numeric", "java.math.BigDecimal"},
		        {"fixed", "double"},
		        {"time", "java.util.Date"},
		        {"year", "String"}, // int でも可能
		        {"TINYBLOB", "java.sql.Blob"}, // byte[] から Blob に変更
		        {"TINYTEXT", "String"},
		        {"BLOB", "java.sql.Blob"}, // byte[] から Blob に変更
		        {"TEXT", "String"},
		        {"MEDIUMBLOB", "java.sql.Blob"}, // byte[] から Blob に変更
		        {"MEDIUMTEXT", "String"},
		        {"LONGBLOB", "java.sql.Blob"}, // byte[] から Blob に変更
		        {"LONGTEXT", "String"},
		        {"INTEGER GENERATED ALWAYS AS IDENTITY", ""},
		        {"INTEGER GENERATED ALWAYS", "int"},
		        {"INTEGER GENERATED", "int"},
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
		        {"interval", "int"}, // PostgreSQL
		        {"point", "org.postgresql.geometric.PGpoint"}, // PostgreSQL
		        {"line", "org.postgresql.geometric.PGline"}, // PostgreSQL
		        {"lseg", "org.postgresql.geometric.PGlseg"}, // PostgreSQL
		        {"box", "org.postgresql.geometric.PGbox"}, // PostgreSQL
		        {"path", "org.postgresql.geometric.PGpath"}, // PostgreSQL
		        {"polygon", "org.postgresql.geometric.PGpolygon"}, // PostgreSQL
		        {"circle", "org.postgresql.geometric.PGcircle"}, // PostgreSQL
		        {"cidr", "String"}, // PostgreSQL
		        {"inet", "String"}, // PostgreSQL
		        {"macaddr", "String"}, // PostgreSQL
		        {"MEDIUMINT", "int"}, // MySQL
		        {"DATETIME", "java.util.Date"}, // MySQL
		        {"VARCHAR2", "String"}, // Oracle
		        {"NVARCHAR2", "String"}, // Oracle
		        {"LONG", "String"}, // Oracle
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
	 * XLS定義TypeからJava用変数型を取得する
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

}
