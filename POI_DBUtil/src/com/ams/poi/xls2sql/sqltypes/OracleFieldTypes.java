package com.ams.poi.xls2sql.sqltypes;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

/**
 * <p>�^�C�g��: OracleFieldTypes</p>
 * <p>����: �I���N���̌^��`</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author ��c���F
 * @version 1.0
 */
public class OracleFieldTypes {

	// XLS > Oracle��`�}�b�s���O
	static HashMap type_map;
	static {
		type_map = new HashMap();
		// type_map.put(XLS type, Oracle type)
		type_map.put("SERIAL", "INTEGER");
		type_map.put("Timestamp", "DATE");
        type_map.put("Date", "DATE");
        type_map.put("DATE", "DATE");
		type_map.put("date", "DATE");
        type_map.put("DATETIME", "DATE"); // oracle�ɂ�DATETIME������
        type_map.put("TIMESTAMP", "DATE"); // oracle�ɂ�TIMESTAMP������
        type_map.put("TIME", "DATE"); // oracle�ɂ�TIME������
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
        // Oracle�ȊO��DBMS�t�B�[���h�Ή�
        type_map.put("BIT", "CHAR(1)");
        type_map.put("bit", "CHAR(1)");
        type_map.put("BOOL", "CHAR(5)");
        type_map.put("bool", "CHAR(5)");
        type_map.put("BOOLEAN", "CHAR(5)");
        type_map.put("boolean", "CHAR(5)");
        type_map.put("SMALLINT", "NUMBER(3)");
        type_map.put("MEDIUMINT", "NUMBER(6)");
        type_map.put("FLOAT", "NUMBER(10,10)"); // ����̓G���[�ɂȂ�Ȃ��悤�ɂ��Ă邾����
        // ���ۂ͖���
        type_map.put("FIXED", "NUMBER(10,10)"); // ����̓G���[�ɂȂ�Ȃ��悤�ɂ��Ă邾����
        // ���ۂ͖���
        type_map.put("DOUBLE", "NUMBER(20,10)"); // ����̓G���[�ɂȂ�Ȃ��悤�ɂ��Ă邾����
        // ���ۂ͖���
        type_map.put("DOUBLE PRECISION", "NUMBER(20,10)"); // ����̓G���[�ɂȂ�Ȃ��悤�ɂ��Ă邾����
        // ���ۂ͖���
        type_map.put("REAL", "NUMBER(20,10)"); // ����̓G���[�ɂȂ�Ȃ��悤�ɂ��Ă邾����
        // ���ۂ͖���
        type_map.put("DECIMAL", "NUMBER"); // ����̓G���[�ɂȂ�Ȃ��悤�ɂ��Ă邾����
        // ���ۂ͖���
        type_map.put("DEC", "NUMBER"); // ����̓G���[�ɂȂ�Ȃ��悤�ɂ��Ă邾����
        // ���ۂ͖���
        type_map.put("NUMERIC", "NUMBER"); // ����̓G���[�ɂȂ�Ȃ��悤�ɂ��Ă邾����
        // ���ۂ͖���
        type_map.put("YEAR", "CHAR(4)");
	}


	/**
	 * getTypeByXLS<BR>
	 * XLS��`Type����Oracle�pFieldType���擾����
	 * @param xls_type XLS��`�^�C�v
	 * @return Oracle�pFieldType
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
		
		if (n1 == -1 || n2 == -1 || n1 > n2) {
			// �T�C�Y����type
			return (String)type_map.get(xls_type);
		}
		
		// �T�C�Y�L��type
		String type_only = xls_type.substring(0, n1);
		
		return (String)type_map.get(type_only) + xls_type.substring(n1, n2 + 1);
		
	}

	// Oracle > JavaBean��`�}�b�s���O
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

	// XLS > Oracle insert �t�B�[���h�p
	// Oracle �̒萔�ȂǏo�͏�������ʂɒ�`�������
	static HashMap insert_type_map;
	static {
		insert_type_map = new HashMap();
		insert_type_map.put("SYSDATE", "SYSDATE");
		insert_type_map.put("NULL", "NULL");
		insert_type_map.put("null", "null");
	}

	/**
	 * getInsertTypeDependOracle<BR>
	 * XLS�ɋL�q�����l��SQL�ɋL�q�\�ȏ����ɕϊ����ĕԂ��B
	 * @param val ���ׂ�l
	 * @return �o�͕�����
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
