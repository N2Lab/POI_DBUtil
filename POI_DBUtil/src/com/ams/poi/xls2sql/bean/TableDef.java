package com.ams.poi.xls2sql.bean;

import java.util.ArrayList;

import com.ams.poi.xls2sql.util.NameUtil;

/**
 * <p>�^�C�g��: TableDef</p>
 * <p>����: �e�[�u����`���Bean</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author ��c���F
 * @version 1.0
 */
public class TableDef {

	/** �e�[�u���� (�_����) */
	private String tableNameLogic = "";

	/** �e�[�u�����i�������j*/
	private String tableNamePhysics = "";

	/** �t�B�[���h���X�g */
	private ArrayList<FieldDef> fieldList = new ArrayList<FieldDef>();

	/** DROP TABLE �L�q�L���t���O */
	private boolean dropTableEnable = false;

	/** DROP INDEX �L�q�L���t���O */
	private boolean dropIndexEnable = false;

	/** ���l */
	private String note = "";

	/** �X�g���[�W�G���W����, MySQL �ŗ��p */
	private String strageEngineName;

	/** �e�[�u���L�����N�^�Z�b�g MySQL �ŗ��p */
	private String characterSetName;

	/** �f�[�^���k MySQL �ŗ��p */
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
	 * JDO�N���X����ԋp
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
	 * PK�̃J������Ԃ�.�����̏ꍇ�͖��l��
	 * @param td
	 * @return PK�̃J����.
	 */
	public FieldDef getId() {
		for (FieldDef fd : this.fieldList) {
			if (fd.isPrimaryKey()) {
				return fd;
			}
		}
		// �ʏ킠�肦�Ȃ�
		return null;
	}

	/**
	 * �e�[�u���̃L�����N�^�Z�b�g���擾
	 * @return
	 */
	public String getCharacterSetName() {

		if (System.getProperty("MYSQL_UTF8MB4_DISABLE") != null && "utf8mb4".equals(this.characterSetName)) {
			// MB4 ������ ���� utf8mb4 �w��̏ꍇ�� utf8 �ɂ���
			return "utf8";
		}

		return characterSetName;
	}

	/**
	 * �e�[�u���̃L�����N�^�Z�b�g��ݒ�
	 * @param name
	 */
	public void setCharacterSetName(String name) {
		characterSetName = name;
	}

	/**
	 * �f�[�^���k�������擾
	 * @return
	 */
	public String getRowFormatName() {
		return rowFormatName;
	}

	/**
	 * �f�[�^���k������ݒ�
	 * @param name
	 */
	public void setRowFormatName(String name) {
		this.rowFormatName = name;
	}

}
