package com.ams.poi.xls2sql.sqlfactory;

import java.util.ArrayList;
import java.util.Iterator;

import com.ams.poi.xls2sql.bean.TableValuesDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableValuesDefParseException;
import com.ams.poi.xls2sql.sqltypes.MySQLFieldTypes;

/**
 * <p>�^�C�g��: MYSQLInsertSQL</p>
 * <p>����: </p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/10/27</p>
 * @author ��c���F
 * @version $Revision: 3255 $
 */
public class MYSQLInsertSQL extends InsertSQL {

	/**
	 * 
	 */
	public MYSQLInsertSQL() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.InsertSQL#getInsertSQL(java.util.ArrayList)
	 */
	public String getInsertSQL(TableValuesDef tvd, ArrayList rec_list)
		throws TableValuesDefParseException {
	
	    String table_name = tvd.getTableNamePhysics();
		// �P���R�[�h����INSERT���o��
		StringBuffer out = new StringBuffer("INSERT INTO " + table_name + " VALUES(");
	
		Iterator it = rec_list.iterator();
		int c = 0;
		while(it.hasNext()) {
			Object val = it.next();
			
			if (c++ > 0) {
				out.append(",");
			}
			out.append(MySQLFieldTypes.getInsertType(val));
			
		}
		out.append(");\n");
			
		return out.toString();
	}

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.InsertSQL#getOutputFileString(com.ams.poi.xls2sql.bean.TableValuesDef)
	 */
	public String getOutputFileString(TableValuesDef tvd)
		throws TableValuesDefParseException {
		StringBuffer out = new StringBuffer("");
		
		// DELETE from TABLEname
		if (!isAdd_only()) {
			out.append("DELETE FROM " + tvd.getTableNamePhysics() + ";\n\n");
		}
		
		// �o�͕����R�[�h���ݒ肳��Ă���Ίi�[
		String set_char_set = (String)getOptions().get("set_char_set");
		if (set_char_set != null && set_char_set.length() > 0) {
			if (System.getProperty("MYSQL_UTF8MB4_DISABLE") != null && "utf8mb4".equals(set_char_set)) {
				// MB4 ������ ���� utf8mb4 �w��̏ꍇ�� utf8 �ɂ���
				set_char_set = "utf8";
			}
			
			out.append("SET CHARACTER SET " + set_char_set + ";\n\n");
		}
		
		Iterator ri = tvd.getRecordDataList().iterator();
		while(ri.hasNext()) {
			// 1���R�[�h�f�[�^
			ArrayList recordData = (ArrayList)ri.next();
			out.append(getInsertSQL(tvd, recordData));	
		}
		
		return out.toString();
	}

}
