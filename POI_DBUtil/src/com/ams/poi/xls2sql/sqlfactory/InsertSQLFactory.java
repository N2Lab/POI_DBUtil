package com.ams.poi.xls2sql.sqlfactory;

/**
 * <p>�^�C�g��: InsertSQLFactory</p>
 * <p>����: InsertSQL�N���X�����A�擾�N���X</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/28</p>
 * @author ��c���F
 * @version 1.0
 */
public class InsertSQLFactory {


	public InsertSQL getInsertSQL() {
		
		// �f�t�H���g��Oracle�p�̂�
		return new ORACLEInsertSQL();
	}
	
	public InsertSQL getInsertSQL(DbmsType dbms) throws Exception {
		InsertSQL cs = null;
		
		if (dbms == null) {
			return getInsertSQL();
		}

        // DbmsType���琶��
        cs = (InsertSQL)Class.forName("com.ams.poi.xls2sql.sqlfactory." + 
        	        dbms.toString().toUpperCase() + "InsertSQL").newInstance();
			
		return cs;
	}
}
