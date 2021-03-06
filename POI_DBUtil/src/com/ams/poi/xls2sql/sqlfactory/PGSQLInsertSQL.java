package com.ams.poi.xls2sql.sqlfactory;

import java.util.ArrayList;
import java.util.Iterator;

import com.ams.poi.xls2sql.bean.TableValuesDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableValuesDefParseException;
import com.ams.poi.xls2sql.sqltypes.PgSQLFieldTypes;

/**
 * <p>タイトル: PGSQLInsertSQL</p>
 * <p>説明: </p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/10/27</p>
 * @author 門田明彦
 * @version $Revision: 69 $
 */
public class PGSQLInsertSQL extends InsertSQL {

	/**
	 * 
	 */
	public PGSQLInsertSQL() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.InsertSQL#getInsertSQL(java.util.ArrayList)
	 */
	public String getInsertSQL(TableValuesDef tvd, ArrayList rec_list)
		throws TableValuesDefParseException {
	
	    String table_name = tvd.getTableNamePhysics();
		// １レコード分のINSERTを出力
		StringBuffer out = new StringBuffer("INSERT INTO " + table_name + " VALUES(");
	
		Iterator it = rec_list.iterator();
		int c = 0;
		while(it.hasNext()) {
			Object val = it.next();
			
			if (c++ > 0) {
				out.append(",");
			}
			out.append(PgSQLFieldTypes.getInsertType(val));
			
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
		
//		// 出力文字コードが設定されていれば格納
//		String set_char_set = (String)getOptions().get("set_char_set");
//		if (set_char_set != null && set_char_set.length() > 0) {
//			out.append("SET CHARACTER SET " + set_char_set + ";\n\n");
//		}
		
		Iterator ri = tvd.getRecordDataList().iterator();
		while(ri.hasNext()) {
			// 1レコードデータ
			ArrayList recordData = (ArrayList)ri.next();
			out.append(getInsertSQL(tvd, recordData));	
		}
		
		return out.toString();
	}

}
