package com.ams.poi.xls2sql.sqlfactory;

/**
 * <p>�^�C�g��: CreateSQLFactor</p>
 * <p>����: CreateSQL�N���X�����A�擾�N���X</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author ��c���F
 * @version 1.0
 */
public class CreateSQLFactory {

	/**
	 * getCreateSQL<BR>
	 * DBMS����CreateSQL�����N���X���擾����
	 * @return SQL�����N���X
	 */
	public CreateSQL getCreateSQL() {
		// �f�t�H���g��Oracle��p��
		return new ORACLECreateSQL();
	}

	/**
	 * getCreateSQL<BR>
	 * DBMS����CreateSQL�����N���X���擾����
	 * @param dbms DBMS_TYPE
	 * @return SQL�����N���X
	 * @throws Exception
	 */
	public CreateSQL getCreateSQL(DbmsType dbms) throws Exception {
		
		CreateSQL cs = null;
		
		if (dbms == null) {
			return getCreateSQL();
		}
		
        // DbmsType���琶��
        return (CreateSQL)Class.forName("com.ams.poi.xls2sql.sqlfactory." + 
        	        dbms.toString().toUpperCase() + "CreateSQL").newInstance();
			
	}
}
