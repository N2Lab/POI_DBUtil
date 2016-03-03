/**
 * 
 */
package com.ams.poi.xls2sql.cakephpmaker;

import java.util.ArrayList;
import java.util.List;

import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.jerseymaker.WebAPIMethod;
import com.ams.poi.xls2sql.util.NameUtil;

/**
 * CakePHPModelSourceMaker
 *
 * @author amonden
 *
 */
public class CakePHPModelSourceMaker {
//	private String app_cd_name;

	/**
	 * @param m
	 * @param td 
	 * @return
	 */
	public String getSource(WebAPIMethod m, TableDef td) {
		StringBuilder source = new StringBuilder();

		// import
		source.append(getImport());
		
		// ファイル先頭コメント
		source.append(getHeaderComment(m));

		// クラス定義
		source.append(getClassSource(m, td));

		return source.toString();
	}

	private String getImport() {
		return
		"<?php\r\nApp::uses('AppModel', 'Model');\r\n\r\n";
	}

	/**
	 * getHeaderComment<BR>
	 * ソースヘッダコメント取得
	 * 
	 * @param td
	 * @return
	 */
	private String getHeaderComment(WebAPIMethod m) {
		String s = "/**\n"
				+ " * <p>タイトル: "
				+ m.getTableNameLogic()
				+ "CakePHP2.x Model</p>\n"
				+ " * <p>説明: "
				+ m.getTableNameLogic()
				+ "("
				+ m.getTableNamePhysics()
				+ ")テーブルと対応するModel</p>\n"
				+ " * @author \n"
				+ " * @version $Id: CakePHPModelSourceMaker.java 100 2009-11-27 08:54:08Z amonden $\n"
				+ " */\n";

		return s;
	}

	/**
	 * getClassSource<BR>
	 * ソースコード取得
	 * @param td 
	 * 
	 * @param td
	 * @return
	 */
	private String getClassSource(WebAPIMethod m, TableDef td) {
		StringBuffer sb = new StringBuffer();
		final String table_name = m.getTableNamePhysics();
		final String class_name = NameUtil.toCakePHPModel(m.getTableNamePhysics());
		final String bean_class = NameUtil.toCakePHPModel(m.getTableNamePhysics());
		final String api_path = m.getTableNamePhysics().toLowerCase();
		List<String> def_list = new ArrayList<String>();

		// 1. クラス定義 start
		sb.append("class "+class_name+" extends AppModel {\r\n");
		sb.append("\t/**\r\n");
		sb.append("\t * Primary key field\r\n");
		sb.append("\t *\r\n");
		sb.append("\t * @var string\r\n");
		sb.append("\t */\r\n");
		sb.append("\tpublic $primaryKey = '"+td.getId().getFieldNamePhysics()+"';\r\n");
		sb.append("}\r\n");

		
		// クラス定義 end

		return sb.toString();
	}
}
