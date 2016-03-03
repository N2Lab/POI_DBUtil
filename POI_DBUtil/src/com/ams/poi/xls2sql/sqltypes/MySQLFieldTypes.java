package com.ams.poi.xls2sql.sqltypes;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.util.EscapeUtil;

/**
 * <p>�^�C�g��: MySQLFieldTypes</p>
 * <p>����: MySQL�̌^��`</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author ��c���F
 * @version 1.0
 */
public class MySQLFieldTypes {

	// XLS > MySQL��`�}�b�s���O
	static HashMap type_map;
	static {
		type_map = new HashMap();
		// type_map.put(XLS type, Oracle type)
		type_map.put("SERIAL", "INTEGER");
		type_map.put("Timestamp", "TIMESTAMP");
		type_map.put("Date", "DATE");
		type_map.put("date", "DATE");
		type_map.put("varchar", "VARCHAR");
		type_map.put("VARCHAR", "VARCHAR");
		type_map.put("Varchar", "VARCHAR");
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

		type_map.put("TINYINT", "TINYINT");
		type_map.put("SMALLINT", "SMALLINT");
		type_map.put("MEDIUMINT", "MEDIUMINT");
		type_map.put("BIGINT", "BIGINT");
		type_map.put("FLOAT", "FLOAT");
		type_map.put("DOUBLE", "DOUBLE");
		type_map.put("DOUBLE PRECISION", "DOUBLE PRECISION");
		type_map.put("REAL", "REAL");

		type_map.put("SET", "SET");
		type_map.put("ENUM", "ENUM");
    
    type_map.put("TEXT", "TEXT");
    type_map.put("Text", "TEXT");
    type_map.put("text", "TEXT");
    
	type_map.put("serial", "integer  AUTO_INCREMENT");
	type_map.put("Serial", "integer  AUTO_INCREMENT");
	type_map.put("SERIAL", "integer  AUTO_INCREMENT");
	type_map.put("SERIAL", "integer  AUTO_INCREMENT");
	type_map.put("SERIAL", "BIGINT AUTO_INCREMENT");

    type_map.put("DECIMAL", "NUMERIC"); // ����̓G���[�ɂȂ�Ȃ��悤�ɂ��Ă邾����
    type_map.put("NUMERIC", "NUMERIC"); // ����̓G���[�ɂȂ�Ȃ��悤�ɂ��Ă邾����

    
    type_map.put("BLOB", "BLOB");
    type_map.put("Blob", "BLOB");
    type_map.put("blob", "BLOB");
    
    type_map.put("double", "DOUBLE");
    type_map.put("DOUBLE", "DOUBLE");

//		type_map.put("System", "");
	}


	/**
	 * getTypeByXLS<BR>
	 * XLS��`Type����MySQL�pFieldType���擾����
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
		
		String result;
		if (n1 == -1 || n2 == -1 || n1 > n2) {
			// �T�C�Y����type
			result = (String)type_map.get(xls_type);
		} else {
		
			// �T�C�Y�L��type
			String type_only = xls_type.substring(0, n1);		
			result = (String)type_map.get(type_only) + xls_type.substring(n1, n2 + 1);
		}
		if (result == null) {
			result = xls_type;
		}
		return result;
	}


	// XLS > MySQL insert �t�B�[���h�p
	// MySQL �̒萔�ȂǏo�͏�������ʂɒ�`�������( '' �ł�����Ȃ�����)
	static HashMap insert_type_map;
	static {
		insert_type_map = new HashMap();
		insert_type_map.put("NULL", "NULL");
		insert_type_map.put("null", "NULL");
		insert_type_map.put("SYSDATE", "NOW()");
		insert_type_map.put("NOW()", "NOW()");
		insert_type_map.put("NOW", "NOW()");
		insert_type_map.put("CURRENT_TIMESTAMP", "CURRENT_TIMESTAMP");
		insert_type_map.put("current_timestamp", "current_timestamp");
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
			} else if (val_t.startsWith("GeomFromText")) {// �֐������̂܂�
				res = val_t;
			} else {
				res = "'" + EscapeUtil.escapeMySQL(val_t) + "'";
			}
										
		} else if (val instanceof Double) {
			res = "" + (Double)val;

		} else if (val instanceof Integer) {
			res = "" + (Integer)val;

		} else {
			res = "NULL";
		}
		return res;		
	}
}
