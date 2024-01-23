package com.ams.poi.xls2sql.bean;

import java.util.ArrayList;

import com.ams.poi.xls2sql.util.NameUtil;

/**
 * <p>タイトル: TableDef</p>
 * <p>説明: テーブル定義情報Bean</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class TableDef {

	/** テーブル名 (論理名) */
	private String tableNameLogic = "";

	/** テーブル名（物理名）*/
	private String tableNamePhysics = "";

	/** フィールドリスト */
	private ArrayList<FieldDef> fieldList = new ArrayList<FieldDef>();

	/** DROP TABLE 記述有無フラグ */
	private boolean dropTableEnable = false;

	/** DROP INDEX 記述有無フラグ */
	private boolean dropIndexEnable = false;

	/** 備考 */
	private String note = "";

	/** ストレージエンジン名, MySQL で利用 */
	private String strageEngineName;

	/** テーブルキャラクタセット MySQL で利用 */
	private String characterSetName;

	/** データ圧縮 MySQL で利用 */
	private String rowFormatName;

	/**
	 * isDropIndexEnable<BR>
	 * @return
	 */
	public boolean isDropIndexEnable() {
		return dropIndexEnable;
	}

	/**
	 * isDropTableEnable<BR>
	 * @return
	 */
	public boolean isDropTableEnable() {
		return dropTableEnable;
	}

	/**
	 * getFieldList<BR>
	 * @return
	 */
	public ArrayList<FieldDef> getFieldList() {
		return fieldList;
	}

	/**
	 * getTableNameLogic<BR>
	 * @return
	 */
	public String getTableNameLogic() {
		return tableNameLogic;
	}

	/**
	 * getTableNamePhysics<BR>
	 * @return
	 */
	public String getTableNamePhysics() {
		return tableNamePhysics;
	}

	/**
	 * getTableNamePhysics with Backquote<BR>
	 * @return
	 */
	public String getTableNamePhysicsBq() {
		return "`" + tableNamePhysics + "`";
	}

	/**
	 * getTableNamePhysics<BR>
	 * @return
	 */
	public String getTableNamePhysicsTopUpper() {

		return NameUtil.toTopUpper(tableNamePhysics);
	}

	/**
	 * JDOクラス名を返却
	 * @return
	 */
	public String getJdoName() {
		return getTableNamePhysicsTopUpper();
	}

	/**
	 * setDropIndexEnable<BR>
	 * @param b
	 */
	public void setDropIndexEnable(boolean b) {
		dropIndexEnable = b;
	}

	/**
	 * setDropTableEnable<BR>
	 * @param b
	 */
	public void setDropTableEnable(boolean b) {
		dropTableEnable = b;
	}

	/**
	 * setFieldList<BR>
	 * @param list
	 */
	public void setFieldList(ArrayList<FieldDef> list) {
		fieldList = list;
	}

	/**
	 * setTableNameLogic<BR>
	 * @param string
	 */
	public void setTableNameLogic(String string) {
		tableNameLogic = string;
	}

	/**
	 * setTableNamePhysics<BR>
	 * @param string
	 */
	public void setTableNamePhysics(String string) {
		tableNamePhysics = string;
	}

	/**
	 * getNote<BR>
	 * @return
	 */
	public String getNote() {
		return note;
	}

	/**
	 * setNote<BR>
	 * @param string
	 */
	public void setNote(String string) {
		note = string;
	}

    /**
     * @return Returns the strageEngineName.
     */
    public String getStrageEngineName() {
        return strageEngineName;
    }
    /**
     * @param strageEngineName The strageEngineName to set.
     */
    public void setStrageEngineName(String strageEngineName) {
        this.strageEngineName = strageEngineName;
    }

	/**
	 * PKのカラムを返す.複数の場合は未考慮
	 * @param td
	 * @return PKのカラム.
	 */
	public FieldDef getId() {
		for (FieldDef fd : this.fieldList) {
			if (fd.isPrimaryKey()) {
				return fd;
			}
		}
		// 通常ありえない
		return null;
	}

	/**
	 * テーブルのキャラクタセットを取得
	 * @return
	 */
	public String getCharacterSetName() {

		if (System.getProperty("MYSQL_UTF8MB4_DISABLE") != null && "utf8mb4".equals(this.characterSetName)) {
			// MB4 が無効 かつ utf8mb4 指定の場合は utf8 にする
			return "utf8";
		}

		return characterSetName;
	}

	/**
	 * テーブルのキャラクタセットを設定
	 * @param name
	 */
	public void setCharacterSetName(String name) {
		characterSetName = name;
	}

	/**
	 * データ圧縮方式を取得
	 * @return
	 */
	public String getRowFormatName() {
		return rowFormatName;
	}

	/**
	 * データ圧縮方式を設定
	 * @param name
	 */
	public void setRowFormatName(String name) {
		this.rowFormatName = name;
	}

}
