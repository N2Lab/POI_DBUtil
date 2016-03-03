package com.ams.poi.xls2sql.sqlfactory;

import java.util.ArrayList;
import java.util.Iterator;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.bean.TableDefCache;
import com.ams.poi.xls2sql.bean.TableValuesDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableValuesDefParseException;
import com.ams.poi.xls2sql.sqltypes.SQLITE3FieldTypes;

/**
 * <p>�^�C�g��: SQLITE3InsertSQL</p>
 * <p>����: </p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/10/27</p>
 * @author ��c���F
 * @version $Revision: 69 $
 */
public class SQLITE3InsertSQL extends InsertSQL {

	/**
	 * 
	 */
	public SQLITE3InsertSQL() {
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
		
		// VALUES���''�����邩�ǂ������f���邽��
		// �e�[�u����`���𗘗p
		TableDef td = TableDefCache.get(table_name);
		ArrayList flist = td.getFieldList();
	
		Iterator it = rec_list.iterator();
		int c = 0;
		while(it.hasNext()) {
			Object val = it.next();
			
			if (c > 0) {
				out.append(",");
			}
			out.append(SQLITE3FieldTypes.getInsertType(val, (FieldDef)flist.get(c)));
			
			c++;
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
		
//		// �o�͕����R�[�h���ݒ肳��Ă���Ίi�[
//		String set_char_set = (String)getOptions().get("set_char_set");
//		if (set_char_set != null && set_char_set.length() > 0) {
//			out.append("SET CHARACTER SET " + set_char_set + ";\n\n");
//		}
		
		Iterator ri = tvd.getRecordDataList().iterator();
		while(ri.hasNext()) {
			// 1���R�[�h�f�[�^
			ArrayList recordData = (ArrayList)ri.next();
			out.append(getInsertSQL(tvd, recordData));	
		}
		
		return out.toString();
	}

}
