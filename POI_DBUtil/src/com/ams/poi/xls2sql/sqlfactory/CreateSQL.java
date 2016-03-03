package com.ams.poi.xls2sql.sqlfactory;

import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableDefParseException;

/**
 * <p>タイトル: CreateSQL</p>
 * <p>説明: CreateSQL ファイル生成クラスのインターフェース</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public interface CreateSQL {

	/**
	 * getCreateSQL<BR>
	 * CRATE SQL 文のみ取得 
	 * @return CREATE SQL 文
	 */
	public String getCreateSQL(TableDef td) throws TableDefParseException;
	
	/**
	 * getOutputFileString<BR>
	 * CreateSQL ファイル出力文字列取得
	 * @return ファイル出力文字列
	 */
	public String getOutputFileString(TableDef td, boolean dropSql) throws TableDefParseException;

	/**
	 * create_sql ファイルへの共通文字列（ヘッダ）を返す
	 * @return 共通文字列
	 */
	public String getCreateTableHeader();

	/**
	 * create_sql ファイルへの共通文字列（フッタ）を返す
	 * @return
	 */
	public String getCreateTableFooter();
	
}
