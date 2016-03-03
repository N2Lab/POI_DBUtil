package com.ams.poi.xls2sql.objectivec;

import java.util.HashMap;

import com.ams.poi.xls2sql.bean.FieldDef;

/**
 * <p>�^�C�g��: ObjectiveCFieldType</p>
 * <p>����: DB>JAVA�̌^��`</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author ��c���F
 * @version 1.0
 */
public class ObjectiveCFieldType {

	// DBMS > Objective-C��`�}�b�s���O
	static HashMap type_map;
	static {
		type_map = new HashMap();
		String[][] maps = {
        {"Long", "NSLong"},
        {"LONG", "NSLong"},
		        {"smallint", "NSInteger"},
		        {"Timestamp", "NSString*"},
		        {"date", "NSString*"},
		        {"varchar", "NSString*"},
		        {"integer", "NSInteger"},
		        {"number", "NSInteger"},
		        {"int", "NSInteger"},
		        {"char", "NSString*"},
		        {"raw", "NSString*"},
		        {"tinyint", "NSInteger"},
		        {"serial", "NSInteger"},
		        {"INTEGER", "NSInteger"},
		        {"smallint", "NSInteger"},
		        {"bigint", "NSInteger"},
		        {"float", "double"},
		        {"double", "double"},
		        {"DOUBLE PRECISION", "double"},
		        {"REAL", "double"}, // PGSQL �� 4byte, MySQL �� 8byte / JDBC 4byte �ɍ��킹��
		        {"set", "NSString*"},
		        {"enum", "NSString*"},
		        {"integer AUTO_INCREMENT", "NSInteger"},
		        {"integer  AUTO_INCREMENT", "NSInteger"},
		        {"integer   AUTO_INCREMENT", "NSInteger"},
		        {"integer    AUTO_INCREMENT", "NSInteger"},
		        {"integer     AUTO_INCREMENT", "NSInteger"},
		        {"BIGINT AUTO_INCREMENT", "NSLong"},
		        {"bit", "NSInteger"},
		        {"bool", "NSInteger"},
		        {"boolean", "NSInteger"},
		        {"decimal", "double"},
		        {"dec", "java.math.BigDecimal"},
		        {"numeric", "NSString*"},
		        {"fixed", "double"},
		        {"time", "NSString*"},
		        {"year", "String"}, // int �ł��\
		        {"TINYBLOB", "NSData *"}, // byte[] ���� Blob �ɕύX
		        {"TINYTEXT", "String"},
		        {"BLOB", "NSData *"}, // byte[] ���� Blob �ɕύX
		        {"TEXT", "NSString*"},
		        {"MEDIUMBLOB", "NSData *"}, // byte[] ���� Blob �ɕύX
		        {"MEDIUMTEXT", "String"},
		        {"LONGBLOB", "NSData *"}, // byte[] ���� Blob �ɕύX
		        {"LONGTEXT", "NSString*"},
		        {"INTEGER GENERATED ALWAYS AS IDENTITY", ""},
		        {"INTEGER GENERATED ALWAYS", "int"},
		        {"INTEGER GENERATED", "int"},
		        {"CHARACTER VARYING", "String"},
		        {"CHAR VARYING", "String"},
		        {"LONG VARCHAR", "String"},
		        {"CLOB", "String"},
		        {"DBCLOB", "String"},
//		        {"BLOB", "java.sql.Blob"}, // byte[] ���� Blob �ɕύX
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
		        {"point", "NSString*"}, // PostgreSQL
		        {"geometry", "NSString*"}, // PostgreSQL
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
		        {"DATETIME", "NSString*"}, // MySQL
		        {"VARCHAR2", "String"}, // Oracle
		        {"NVARCHAR2", "String"}, // Oracle
//		        {"LONG", "String"}, // Oracle
		        {"ROWID", "String"}, // Oracle
		        {"UROWID", "String"}, // Oracle
		        {"NCHAR", "String"}, // Oracle
		        {"NCLOB", "String"}, // Oracle
		};
		for(int i = 0 ; i < maps.length; i++) {
		    // 3�p�^�[���Ŋi�[ (int, INT, Int)
		    String low_name = maps[i][0].toLowerCase();
            type_map.put(low_name, maps[i][1]);
		    type_map.put(maps[i][0].toUpperCase(), maps[i][1]);
		    type_map.put(low_name.substring(0,1).toUpperCase() + 
		            low_name.substring(1), maps[i][1]);
		}
	}


	/**
	 * getTypeByXLS<BR>
	 * XLS��`Type����Objective-C�p�ϐ��^���擾����
	 * @param xls_type XLS��`�^�C�v
	 * @return Java�p�^
	 */
	public static String getTypeByXLS(String xls_type) {
		
		// ���ʂ�T��
		// �S�p�ɂ��Ή����Ă����
		int n1 = xls_type.indexOf('(');
		if (n1 == -1 ) {
			n1 = xls_type.indexOf('�i');
		}
		int n2 = xls_type.indexOf(')', n1);
		if (n2 == -1) {
			n2 = xls_type.indexOf('�j', n1);
		}
		
		String result;
		if (n1 == -1 || n2 == -1 || n1 > n2) {
			// �T�C�Y����type
			result = (String)type_map.get(xls_type.toUpperCase());
		} else {
		
			// �T�C�Y�L��type
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
	 * �������ʎ擾�R�[�h��Ԃ�
	 * @param fd
	 * @return
	 */
	public static String getFetchCall(FieldDef fd) {
		final String ctype = ObjectiveCFieldType.getTypeByXLS(fd.getTypeXls());
		
		if ("NSInteger".equals(ctype)) {
			return "sqlite3_column_int(statement, c++)";
		} else if ("NSLong".equals(ctype)) {
				return "sqlite3_column_int(statement, c++)";
		} else if ("NSString*".equals(ctype)) {
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
	 * sqlite3_bind_xxxx �R�[�h��Ԃ�
	 * @param fd
	 * @return
	 */
	public static String getBindValueCall(FieldDef fd) {
		final String ctype = ObjectiveCFieldType.getTypeByXLS(fd.getTypeXls());
		
		if ("NSInteger".equals(ctype)) {
			return "sqlite3_bind_int(statement, c++, (int)m."+fd.getFieldNamePhysics()+")";
			
		}	if ("NSLong".equals(ctype)) {
			return "sqlite3_bind_int64(statement, c++, (sqlite3_int64)m."+fd.getFieldNamePhysics()+")";
			
		} else if ("NSString*".equals(ctype)) {
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
