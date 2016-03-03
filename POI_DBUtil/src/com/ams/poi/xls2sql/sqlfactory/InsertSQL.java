package com.ams.poi.xls2sql.sqlfactory;

import java.util.ArrayList;
import java.util.HashMap;

import com.ams.poi.xls2sql.bean.TableValuesDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableValuesDefParseException;

/**
 * <p>�^�C�g��: InsertSQL</p>
 * <p>����: InsertSQL �t�@�C�������N���X�̃C���^�[�t�F�[�X</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/28</p>
 * @author ��c���F
 * @version 1.0
 */
public abstract class InsertSQL {
	
	/** DELETE FROM tableName ����ǉ����邩�ǂ����̃t���O, true �ǉ����Ȃ� */
	boolean add_only; 
    private HashMap options = new HashMap();

	/**
	 * getInsertSQL<BR>
	 * INSERT SQL �̂ݎ擾
	 * @param tvd �e�[�u����`
	 * @param rec_list �P���R�[�h�f�[�^
	 * @return INSERT SQL
	 * @throws TableValuesDefParseException
	 */
	public abstract String getInsertSQL(TableValuesDef tvd, ArrayList rec_list)  throws TableValuesDefParseException;

	/**
	 * getOutputFileString<BR>
	 * CreateSQL �t�@�C���o�͕�����擾
	 * @return �t�@�C���o�͕�����
	 */
	public abstract String getOutputFileString(TableValuesDef tvd) throws TableValuesDefParseException;

	/**
	 * @return Returns the is_add_only.
	 */
	public boolean isAdd_only() {
		return add_only;
	}

	/**
	 * @param is_add_only The is_add_only to set.
	 */
	public void setAdd_only(boolean is_add_only) {
		this.add_only = is_add_only;
	}

    /**
     * �I�v�V����HashMap���i�[
     * @param opt_map
     */
    public void setOptions(HashMap opt_map) {
        this.options = opt_map;
    }

    /**
     * @return Returns the options.
     */
    public HashMap getOptions() {
        return options;
    }
}
