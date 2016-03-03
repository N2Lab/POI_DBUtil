package com.ams.poi.xls2sql.bean;

import com.ams.poi.xls2sql.sqlfactory.DbmsType;
import com.ams.poi.xls2sql.sqltypes.JavaFieldTypes;
import com.ams.poi.xls2sql.util.NameUtil;

/**
 * <p>�^�C�g��: FieldDef</p>
 * <p>����: �t�B�[���h��`���Bean</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author ��c���F
 * @version 1.0
 */
public class FieldDef {

  /** �t�B�[���h�� (�_����) */
	private String fieldNameLogic = "";

	/** �t�B�[���h���i�������j*/
	private String fieldNamePhysics = "";

	/** �^�iXLS�L�q�j*/
	private String typeXls = "";

	//	/** �^�iDBMS�j XLS�L�q����DBMS�ʂɕϊ� */
	//	private String typeDbms = "";

	/** Primary Key */
	private boolean primaryKey = false;

	/** Not Null �t���O */
	private boolean notNull = false;

	/** �f�t�H���g�l */
	private String defaultValue = "";

  /** �C���f�b�N�X���j�[�N�t���O */
  private boolean indexUnique = false;

	/** �P��t�B�[���hINDEX�� */
	private String indexName = "";
  
  /** �C���f�b�N�X length */
  private int indexLength;

	/** ���l */
	private String note = "";

	/**
	 * getDefaultValue<BR>
	 * @return
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * getFieldNameLogic<BR>
	 * @return
	 */
	public String getFieldNameLogic() {
		return fieldNameLogic;
	}

	/**
	 * getFieldNamePhysics<BR>
	 * @return
	 */
	public String getFieldNamePhysics() {
		return fieldNamePhysics;
	}
	
	/**
	 * JDO�p�t�B�[���h��(camel)��Ԃ�
	 * @return
	 */
	public String getJdoField() {
		return NameUtil.toCamel(fieldNamePhysics);
	}

	/**
	 * getIndexName<BR>
	 * @return
	 */
	public String getIndexName() {
		return indexName;
	}

	/**
	 * isSetNull<BR>
	 * @return
	 */
	public boolean isNotNull() {
		return notNull;
	}

	/**
	 * getTypeXls<BR>
	 * @return
	 */
	public String getTypeXls() {
		return typeXls;
	}
	
	public String getTypeJava() {
		return JavaFieldTypes.getTypeByXLS(typeXls);
	}

	/**
	 * setDefaultValue<BR>
	 * @param string
	 */
	public void setDefaultValue(String string) {
		defaultValue = string;
	}

	/**
	 * setFieldNameLogic<BR>
	 * @param string
	 */
	public void setFieldNameLogic(String string) {
		fieldNameLogic = string;
	}

	/**
	 * setFieldNamePhysics<BR>
	 * @param string
	 */
	public void setFieldNamePhysics(String string) {
		// sqlite3 �̏ꍇ, "id" �� "_id" �Ƃ���
		DbmsType dbms = DbmsType.valueOf(System.getProperty("dbms"));
		
		if (dbms == DbmsType.SQLITE3 && "id".equalsIgnoreCase(string)) {
			string = "_id";
		}
		fieldNamePhysics = string;
	}

	/**
	 * setIndexName<BR>
	 * @param string
	 */
	public void setIndexName(String string) {
		indexName = string;
	}

	/**
	 * setSetNull<BR>
	 * @param b
	 */
	public void setNotNull(boolean b) {
		notNull = b;
	}

	/**
	 * setTypeXls<BR>
	 * @param string
	 */
	public void setTypeXls(String string) {
		typeXls = string;
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
	 * isPkey<BR>
	 * @return
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/**
	 * setPkey<BR>
	 * @param b
	 */
	public void setPrimaryKey(boolean b) {
		primaryKey = b;
	}

  /**
   * setIndexUnique<BR>
   * @param b
   */
  public void setIndexUnique(boolean b) {
    indexUnique = b;
  }

  /**
   * isIndexUnique<BR>
   * @return
   */
  public boolean isIndexUnique() {
    return indexUnique;
  }


	/**
	 * @return
	 */
	public int getIndexLength() {
		return indexLength;
	}

	/**
	 * @param i
	 */
	public void setIndexLength(int i) {
		indexLength = i;
	}

	/**
	 * �擪�啶���� fieldNamePysics��Ԃ�
	 * @return
	 */
	public String getFieldNamePhysicsTopUpper() {
		
		return NameUtil.toTopUpper(fieldNamePhysics);
	}

}
