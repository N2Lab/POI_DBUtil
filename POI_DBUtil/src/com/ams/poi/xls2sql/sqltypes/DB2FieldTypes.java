package com.ams.poi.xls2sql.sqltypes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.util.EscapeUtil;

/**
 * <p>�^�C�g��: DB2FieldTypes</p>
 * <p>����: DB2�̌^��`</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author ��c���F
 * @version 1.0
 */
public class DB2FieldTypes {

	// XLS > DB2��`�}�b�s���O
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
	 * XLS��`Type����DB2�pFieldType���擾����
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
		
		String result = null;
		if (n1 == -1 || n2 == -1 || n1 > n2) {
			// �T�C�Y����type
			result = (String)type_map.get(xls_type);
		} else {
		
			// �T�C�Y�L��type
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


	// XLS > DB2 insert �t�B�[���h�p
	// MySQL �̒萔�ȂǏo�͏�������ʂɒ�`�������( '' �ł�����Ȃ�����)
	static HashMap insert_type_map;
	static {
		insert_type_map = new HashMap();
		// DB2 �� CURRENT TIMESTAMP �Ō��݂̃^�C���X�^���v��\��
		insert_type_map.put("SYSDATE", "CURRENT TIMESTAMP");
		insert_type_map.put("NOW()", "CURRENT TIMESTAMP");
		insert_type_map.put("NOW", "CURRENT TIMESTAMP");
	}
	
	// XLS > DB2 insert �l�쐬���Ɂ@�l����t�Ƃ��ĕϊ�����XLS�̌^
    private static Set datetime_type;
    static {
        datetime_type = new HashSet(10);
        datetime_type.add("TIMESTAMP");
        datetime_type.add("DATE");
        datetime_type.add("TIME");
    }

    // XLS > DB2 insert �l�쐬���� �l�� '' �ň͂�XLS�̌^(������̌^)
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
//	 * XLS�ɋL�q�����l��SQL�ɋL�q�\�ȏ����ɕϊ����ĕԂ��B
//	 * @param val ���ׂ�l
//	 * @return �o�͕�����
//	 */
//	public static String getInsertType(Object val) {
//		String res = "";
//		
//		if (val instanceof String) {
//			String val_t = StringUtils.trim((String)val);
//			if (insert_type_map.containsKey(val_t)) {
//				res = (String)insert_type_map.get(val_t);				
//			} else {
//			    // ���t�l�̏ꍇ�͂ł���΂����� 'yyyy/mm/dd' > 'yyyy-mm-dd' �ϊ�����
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
	 * XLS�ɋL�q�����l��SQL�ɋL�q�\�ȏ����ɕϊ����ĕԂ��B
	 * @param val ���ׂ�l
     * @param def �t�B�[���h��`
	 * @return �o�͕�����
     */
    public static String getInsertType(Object val, FieldDef def) {
		String res = "";
		
		if (val instanceof String) {
			String val_t = StringUtils.trim((String)val);
			if (insert_type_map.containsKey(val_t)) {
				res = (String)insert_type_map.get(val_t);				
			} else {
			    if (datetime_type.contains(def.getTypeXls().toUpperCase())) {
				    // ���t�l�̏ꍇ�͂����� 'yyyy/mm/dd' > 'yyyy-mm-dd' �ϊ�����
			        val_t = val_t.replace('/', '-');
			    }
		        
			    // �l���擾
				res = EscapeUtil.escapeMySQL(val_t);
				
			    // �t�B�[���h�̌^�ɂ����'�����邩�ǂ������f����
			    for(Iterator it = add_sq_type.iterator(); it.hasNext(); ) {
			        String add_sq_type_name = (String)it.next();
			        if (def.getTypeXls().toUpperCase().startsWith(add_sq_type_name)) {
			            // �擪����v�����̂� ' ������
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
