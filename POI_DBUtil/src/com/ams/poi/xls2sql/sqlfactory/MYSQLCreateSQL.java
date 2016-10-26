package com.ams.poi.xls2sql.sqlfactory;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableDefParseException;
import com.ams.poi.xls2sql.sqltypes.MySQLFieldTypes;

/**
 * <p>�^�C�g��: MYSQLCreateSQL</p>
 * <p>����: </p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/10/27</p>
 * @author ��c���F
 * @version $Revision: 3255 $
 */
public class MYSQLCreateSQL implements CreateSQL {

	/** �f�t�H���g�C���f�b�N�X���ړ��� */
	private static final String INDEX_PREFIX_DEF = "p_";

	/**
	 * �R���X�g���N�^
	 */
	public MYSQLCreateSQL() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.CreateSQL#getCreateSQL(com.ams.poi.xls2sql.bean.TableDef)
	 */
	public String getCreateSQL(TableDef td) throws TableDefParseException {
		StringBuffer out = new StringBuffer();
		//String pkey_name = INDEX_PREFIX_DEF + td.getTableNamePhysics();
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
			String type = MySQLFieldTypes.getTypeByXLS(fd.getTypeXls());
			out.append(type);
			//			out.append(StringUtils.repeat(" ", max_type - oracle_type.length() + 1));

			// Default
			String defval = fd.getDefaultValue();
			if (!(fd.getDefaultValue() == null
				|| fd.getDefaultValue().equals(""))) {
				// NUMBER(����)�̏ꍇ�͐����l�ɕϊ�
				if (type.startsWith("NUMBER")) {
					if (type.indexOf(",") != -1) {
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
			out.append(
				" PRIMARY KEY (");
			int fdc = 0;
			for(Iterator itf = td.getFieldList().iterator(); itf.hasNext();) {
				FieldDef fd = (FieldDef)itf.next();
				if (fd.isPrimaryKey()) {
					out.append(fdc++ > 0 ? "," : "");
					out.append(fd.getFieldNamePhysics());
				}
			}
			out.append(") ) ");
			String cm = "";
			if (td.getStrageEngineName() != null && td.getStrageEngineName().length() > 0) {
	      		// MROONGA_DISABLE=true �̏ꍇ��InnoDB��
				String engine = td.getStrageEngineName();
	      		if (System.getProperty("MROONGA_DISABLE") != null && !"MyISAM".equalsIgnoreCase(engine)) {
	      			engine = "InnoDB";
	      		}
				
			    out.append(" ENGINE = " + engine);
			    cm = ",";
			}
			// table character set   ex)			, CHARACTER SET utf8mb4
			if (td.getCharacterSetName() != null && td.getCharacterSetName().length() > 0) {
			    out.append(cm + " CHARACTER SET " + td.getCharacterSetName());
			}
			// row format set   ex)			, ROW_FORMAT=DYNAMIC
			if (td.getRowFormatName() != null && td.getRowFormatName().length() > 0) {
			    out.append(cm + " ROW_FORMAT=" + td.getRowFormatName());
			}
			out.append(";\n\n");
		}

		return out.toString();
	}

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.CreateSQL#getOutputFileString(com.ams.poi.xls2sql.bean.TableDef)
	 */
	public String getOutputFileString(TableDef td, boolean dropSql)
		throws TableDefParseException {
			StringBuffer out = new StringBuffer("");
			String pkey_name = INDEX_PREFIX_DEF + td.getTableNamePhysics();
//			int max_field = getMaxFieldLen(td);

      if (dropSql) {
  			// DropTable
  			if (td.isDropTableEnable()) {
                out.append("DROP TABLE IF EXISTS " + td.getTableNamePhysics() + ";\n");
  			}
  
  			// DropIndex
  			if (td.isDropIndexEnable()) {
  				out.append("DROP INDEX " + pkey_name + ";\n\n");
  			}
      }

			// add CreateSQL
			out.append(getCreateSQL(td));

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
				// �J���}��؂肵�Ċi�[
				String[] indexs = index.split(",");
				for (String index_item : indexs) {
					index_map.put(index_item, fd);
				}
//        index_map.put(index, fd);
//        index_map.put(index, index);
			}
		}
		
		// �e�C���f�b�N�X�����
		for(Iterator it = index_map.keySet().iterator(); it.hasNext();) {
			String index = (String)it.next();
      FieldDef itfd = (FieldDef)index_map.get(index);
//      String index = itfd.getIndexName();
      String unique = itfd.isIndexUnique() ? "UNIQUE" : 
    	  index.startsWith("FULLTEXT") ? "FULLTEXT" : 
    	  index.startsWith("SPATIAL") ? "SPATIAL" : 
    	  "";
      
      		// FULLTEXT ���� MROONGA_DISABLE=true �̏ꍇ�͖���
      		if ("FULLTEXT".equals(unique) && System.getProperty("MROONGA_DISABLE") != null) {
      			continue;
      		}
      
			buf.append("CREATE " + unique +  " INDEX " +
				index + " ON " + td.getTableNamePhysics() + " (\n\t");
			
			// �Ή�����t�B�[���h��T���Ēǉ�
			int c = 0;
			for(Iterator i2 = td.getFieldList().iterator(); i2.hasNext();) {
				FieldDef fd = (FieldDef)i2.next();
				if (fd.getIndexName() != null &&
					fd.getIndexName().contains(index)) {
					if (c++ > 0) {
						buf.append(",");	
					}
					buf.append(fd.getFieldNamePhysics());
          
          if (fd.getIndexLength() > 0) {
            buf.append("(" + fd.getIndexLength() + ")");
          }
											
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
