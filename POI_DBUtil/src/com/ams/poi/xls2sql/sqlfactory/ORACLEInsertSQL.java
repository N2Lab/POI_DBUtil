package com.ams.poi.xls2sql.sqlfactory;

import java.util.ArrayList;
import java.util.Iterator;

import com.ams.poi.xls2sql.bean.TableValuesDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableValuesDefParseException;
import com.ams.poi.xls2sql.sqltypes.OracleFieldTypes;

/**
 * <p>�^�C�g��: ORACLEInsertSQL</p>
 * <p>����: Oracle�pINSERTSQL�����N���X</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/28</p>
 * @author ��c���F
 * @version 1.0
 */
public class ORACLEInsertSQL extends InsertSQL {

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
			out.append(OracleFieldTypes.getInsertType(val));
			
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
		
		Iterator ri = tvd.getRecordDataList().iterator();
		while(ri.hasNext()) {
			// 1���R�[�h�f�[�^
			ArrayList recordData = (ArrayList)ri.next();
			out.append(getInsertSQL(tvd, recordData));	
		}
		
		return out.toString();
	}

}
