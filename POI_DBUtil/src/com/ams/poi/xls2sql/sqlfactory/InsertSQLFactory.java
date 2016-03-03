package com.ams.poi.xls2sql.sqlfactory;

/**
 * <p>タイトル: InsertSQLFactory</p>
 * <p>説明: InsertSQLクラス生成、取得クラス</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/28</p>
 * @author 門田明彦
 * @version 1.0
 */
public class InsertSQLFactory {


	public InsertSQL getInsertSQL() {
		
		// デフォルトはOracle用のみ
		return new ORACLEInsertSQL();
	}
	
	public InsertSQL getInsertSQL(DbmsType dbms) throws Exception {
		InsertSQL cs = null;
		
		if (dbms == null) {
			return getInsertSQL();
		}

        // DbmsTypeから生成
        cs = (InsertSQL)Class.forName("com.ams.poi.xls2sql.sqlfactory." + 
        	        dbms.toString().toUpperCase() + "InsertSQL").newInstance();
			
		return cs;
	}
}
