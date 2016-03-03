package com.ams.poi.xls2sql.bean;

import java.util.ArrayList;

/**
 * <p>タイトル: TableValuesDef</p>
 * <p>説明: テーブルデータ（デフォルト値）定義情報Bean</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/28</p>
 * @author 門田明彦
 * @version 1.0
 */
public class TableValuesDef {
	
	/** テーブル名 (論理名) */
	private String tableNameLogic = "";
	
	/** テーブル名（物理名）*/
	private String tableNamePhysics = "";

	/** フィールド名リスト */
	private ArrayList fieldNameList;
	
	/** 各行のレコードデータ(Object のリストのリスト) */
	private ArrayList recordDataList;
	
	/**
	 * コンストラクタ
	 */
	public TableValuesDef() {
		super();
		
		fieldNameList = new ArrayList();
		recordDataList = new ArrayList();
	}
	

	/**
	 * getFieldNameList<BR>
	 * @return
	 */
	public ArrayList getFieldNameList() {
		return fieldNameList;
	}

	/**
	 * getRecordData<BR>
	 * @return
	 */
	public ArrayList getRecordDataList() {
		return recordDataList;
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
	 * setFieldNameList<BR>
	 * @param list
	 */
	public void setFieldNameList(ArrayList list) {
		fieldNameList = list;
	}

	/**
	 * setRecordData<BR>
	 * @param list
	 */
	public void setRecordDataList(ArrayList list) {
		recordDataList = list;
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

}
