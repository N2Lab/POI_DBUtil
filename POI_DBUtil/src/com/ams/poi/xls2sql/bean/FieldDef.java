package com.ams.poi.xls2sql.bean;

import com.ams.poi.xls2sql.sqlfactory.DbmsType;
import com.ams.poi.xls2sql.sqltypes.JavaFieldTypes;
import com.ams.poi.xls2sql.util.NameUtil;

/**
 * <p>タイトル: FieldDef</p>
 * <p>説明: フィールド定義情報Bean</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class FieldDef {

  /** フィールド名 (論理名) */
	private String fieldNameLogic = "";

	/** フィールド名（物理名）*/
	private String fieldNamePhysics = "";

	/** 型（XLS記述）*/
	private String typeXls = "";

	//	/** 型（DBMS） XLS記述からDBMS別に変換 */
	//	private String typeDbms = "";

	/** Primary Key */
	private boolean primaryKey = false;

	/** Not Null フラグ */
	private boolean notNull = false;

	/** デフォルト値 */
	private String defaultValue = "";

  /** インデックスユニークフラグ */
  private boolean indexUnique = false;

	/** 単一フィールドINDEX名 */
	private String indexName = "";
  
  /** インデックス length */
  private int indexLength;

	/** 備考 */
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
	 * JDO用フィールド名(camel)を返す
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
		// sqlite3 の場合, "id" は "_id" とする
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
	 * 先頭大文字の fieldNamePysicsを返す
	 * @return
	 */
	public String getFieldNamePhysicsTopUpper() {
		
		return NameUtil.toTopUpper(fieldNamePhysics);
	}

}
