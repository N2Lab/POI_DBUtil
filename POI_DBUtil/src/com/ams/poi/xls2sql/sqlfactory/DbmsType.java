package com.ams.poi.xls2sql.sqlfactory;

/**
 * <p>�^�C�g��: DbmsType</p>
 * <p>����: DBMS �̃^�C�v���`����N���X</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/10/27</p>
 * @author ��c���F
 * @version $Revision: 69 $
 */
public enum DbmsType {

	ORACLE("oracle"), MYSQL("mysql"), DB2("db2"), SQLITE3("sqlite3");
//	/** Oracle */
//	
//	public static final DbmsType ORACLE = new DbmsType("oracle");
//	
//	/** MySQL */
//	public static final String MYSQL = "mysql";
//
//	/** DB2 */
//    public static final Object DB2 = "db2";

	private String typename; 


	/**
	 * �R���X�g���N�^
	 * @param typename �^�C�v��(�N���I�v�V�����w�薼)
	 */
	private DbmsType(String typename) {
		this.typename = typename;
	}


	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return typename;
	}
}
