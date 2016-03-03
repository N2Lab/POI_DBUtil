package com.ams.poi.xls2sql.bean;

import java.util.ArrayList;

/**
 * <p>�^�C�g��: TableValuesDef</p>
 * <p>����: �e�[�u���f�[�^�i�f�t�H���g�l�j��`���Bean</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/28</p>
 * @author ��c���F
 * @version 1.0
 */
public class TableValuesDef {
	
	/** �e�[�u���� (�_����) */
	private String tableNameLogic = "";
	
	/** �e�[�u�����i�������j*/
	private String tableNamePhysics = "";

	/** �t�B�[���h�����X�g */
	private ArrayList fieldNameList;
	
	/** �e�s�̃��R�[�h�f�[�^(Object �̃��X�g�̃��X�g) */
	private ArrayList recordDataList;
	
	/**
	 * �R���X�g���N�^
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
