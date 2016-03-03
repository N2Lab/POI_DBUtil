package com.ams.poi.xls2sql.sqlfactory;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableDefParseException;
import com.ams.poi.xls2sql.sqltypes.OracleFieldTypes;

/**
 * <p>�^�C�g��: ORACLECreateSQL</p>
 * <p>����: Oracle CreateSQL �t�@�C�������N���X</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author ��c���F
 * @version 1.0
 */
public class ORACLECreateSQL implements CreateSQL {

	/** �f�t�H���g�C���f�b�N�X���ړ��� */
	private static final String INDEX_PREFIX_DEF = "p_";
	
	/** �g���L�[�C���f�b�N�X���ړ��� */
//	private static final String INDEX_EXT_PREFIX_DEF = "IDX_";

	/** oracle max field name */
//	private static final int MAX_FIELD_NAME = 30;

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.CreateSQLFactory#getCreateSQL(com.ams.poi.xls2sql.bean.TableDef)
	 */
	public String getCreateSQL(TableDef td) throws TableDefParseException {
		StringBuffer out = new StringBuffer();
		String pkey_name = INDEX_PREFIX_DEF + td.getTableNamePhysics();
		int max_field = getMaxFieldLen(td);

		// CreateTable
		out.append("CREATE TABLE " + td.getTableNamePhysics() + " (\n");

		// CreateField
		Iterator it = td.getFieldList().iterator();
		while (it.hasNext()) {
			FieldDef fd = (FieldDef) it.next();

			// field name
			out.append(" " + fd.getFieldNamePhysics());
			out.append(
				StringUtils.repeat(
					" ",
					max_field - fd.getFieldNamePhysics().length() + 1));

			// type
			String oracle_type = OracleFieldTypes.getTypeByXLS(fd.getTypeXls());
			if (oracle_type == null) {
                oracle_type = fd.getTypeXls();
//			    throw new RuntimeException("oracle_type is null, type=" + fd.getTypeXls());
			}
			out.append(oracle_type);
			//			out.append(StringUtils.repeat(" ", max_type - oracle_type.length() + 1));

			// Default
			String defval = fd.getDefaultValue();
			if (!(fd.getDefaultValue() == null
				|| fd.getDefaultValue().equals(""))) {
				// NUMBER(����)�̏ꍇ�͐����l�ɕϊ�
				if (oracle_type.startsWith("NUMBER")) {
					if (oracle_type.indexOf(",") != -1) {
						// �����w��L�� .. �f�t�H���g�̂܂�
					} else {
						// �����w�� .. int ����
						defval = "" + (int)Double.parseDouble(defval);
					}
				}
					
				out.append("   DEFAULT " + defval);
			}

			// NOT NULL
			if (fd.isNotNull()) {
				out.append(" NOT NULL");
			}

			out.append(",\n");

		}

		// pkey
		if (td.getFieldList().size() > 0) {
			FieldDef fd = (FieldDef) td.getFieldList().get(0);
			out.append(
				"CONSTRAINT "
					+ pkey_name
					+ " PRIMARY KEY ("
					+ fd.getFieldNamePhysics()
					+ ")   );\n\n");

		}

		return out.toString();
	}

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.CreateSQLFactory#getOutputFileString(com.ams.poi.xls2sql.bean.TableDef)
	 */
	public String getOutputFileString(TableDef td, boolean dropSql)
		throws TableDefParseException {

		StringBuffer out = new StringBuffer("");
		String pkey_name = INDEX_PREFIX_DEF + td.getTableNamePhysics();
		int max_field = getMaxFieldLen(td);

    if (dropSql) {
  		// DropTable
  		if (td.isDropTableEnable()) {
  			out.append("DROP TABLE " + td.getTableNamePhysics() + ";\n");
  		}
  
  		// DropIndex
  		if (td.isDropIndexEnable()) {
  			out.append("DROP INDEX " + pkey_name + ";\n\n");
  		}
    }

		// add CreateSQL
		out.append(getCreateSQL(td));

		int max_comment =
			"COMMENT ON COLUMN . ".length()
				+ td.getTableNamePhysics().length()
				+ max_field;

		// table comment
		if (td.getTableNameLogic() != null
			&& td.getTableNameLogic().length() > 0) {
			out.append("COMMENT ON TABLE  " + td.getTableNamePhysics());
			out.append(
				StringUtils.repeat(
					" ",
					max_comment - td.getTableNamePhysics().length() + 1 - "COMMENT ON TABLE  ".length()));
			out.append(" IS '" + td.getTableNameLogic() + "';\n");
		}

		// fields comment
		{
			Iterator it = td.getFieldList().iterator();
			while (it.hasNext()) {
				FieldDef fd = (FieldDef) it.next();
				String col_name =
					td.getTableNamePhysics() + "." + fd.getFieldNamePhysics();
				out.append("COMMENT ON COLUMN " + col_name);
				out.append(
					StringUtils.repeat(
						" ",
						max_comment - col_name.length() + 1 - "COMMENT ON COLUMN ".length()));
				out.append(" IS '" + fd.getFieldNameLogic() + "';\n");
			}
		}

		// add external INDEX
		out.append(getIndex(td));

		// else sql ....

		return out.toString();
	}

	/**
	 * getIndex<BR>
	 * �L�[�ȊO�̃C���f�b�N�X������Βǉ�����
	 * @return �C���f�b�N�XSQL
	 */
	private String getIndex(TableDef td) {
		StringBuffer buf = new StringBuffer("");
		HashMap index_map = new HashMap();
		
		// �C���f�b�N�X�̃}�b�v���쐬���ăC���f�b�N�X�̐�����
		// CREATE INDEX ���o��
		for(Iterator it = td.getFieldList().iterator(); it.hasNext();) {
			FieldDef fd = (FieldDef) it.next();
			String index = fd.getIndexName();
			
			if (index != null && index.length() > 0) {
				index_map.put(index, index);
			}
		}
		
		// �e�C���f�b�N�X�����
		for(Iterator it = index_map.keySet().iterator(); it.hasNext();) {
			String index = (String)it.next();
			
			buf.append("CREATE UNIQUE INDEX " +
				index + " ON " + td.getTableNamePhysics() + " (\n\t");
			
			// �Ή�����t�B�[���h��T���Ēǉ�
			int c = 0;
			for(Iterator i2 = td.getFieldList().iterator(); i2.hasNext();) {
				FieldDef fd = (FieldDef)i2.next();
				if (fd.getIndexName() != null &&
					fd.getIndexName().equals(index)) {
					if (c++ > 0) {
						buf.append(",");	
					}
					buf.append(fd.getFieldNamePhysics());
											
				}
			}
			buf.append("\n);\n");
		}
		
		return buf.toString();
	}

	/**
	 * getMaxFieldLen<BR>
	 * �t�B�[���h�ő咷���擾
	 * @param td �e�[�u����`
	 * @return �t�B�[���h�ő咷
	 */
	private int getMaxFieldLen(TableDef td) {
		int max_field = 0;
		Iterator it = td.getFieldList().iterator();
		while (it.hasNext()) {
			FieldDef fd = (FieldDef) it.next();
			max_field = Math.max(max_field, fd.getFieldNamePhysics().length());
		}

		return max_field;
	}

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.CreateSQL#getCreateTableHeader()
	 */
	public String getCreateTableHeader() {
		return "";
	}
	
	public String getCreateTableFooter() {
		return "";
	}


}
