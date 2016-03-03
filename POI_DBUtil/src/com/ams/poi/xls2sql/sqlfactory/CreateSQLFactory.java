package com.ams.poi.xls2sql.sqlfactory;

/**
 * <p>タイトル: CreateSQLFactor</p>
 * <p>説明: CreateSQLクラス生成、取得クラス</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class CreateSQLFactory {

	/**
	 * getCreateSQL<BR>
	 * DBMS毎のCreateSQL生成クラスを取得する
	 * @return SQL生成クラス
	 */
	public CreateSQL getCreateSQL() {
		// デフォルトはOracle専用で
		return new ORACLECreateSQL();
	}

	/**
	 * getCreateSQL<BR>
	 * DBMS毎のCreateSQL生成クラスを取得する
	 * @param dbms DBMS_TYPE
	 * @return SQL生成クラス
	 * @throws Exception
	 */
	public CreateSQL getCreateSQL(DbmsType dbms) throws Exception {
		
		CreateSQL cs = null;
		
		if (dbms == null) {
			return getCreateSQL();
		}
		
        // DbmsTypeから生成
        return (CreateSQL)Class.forName("com.ams.poi.xls2sql.sqlfactory." + 
        	        dbms.toString().toUpperCase() + "CreateSQL").newInstance();
			
	}
}
