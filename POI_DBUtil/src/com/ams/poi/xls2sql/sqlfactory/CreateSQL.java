package com.ams.poi.xls2sql.sqlfactory;

import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableDefParseException;

/**
 * <p>�^�C�g��: CreateSQL</p>
 * <p>����: CreateSQL �t�@�C�������N���X�̃C���^�[�t�F�[�X</p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author ��c���F
 * @version 1.0
 */
public interface CreateSQL {

	/**
	 * getCreateSQL<BR>
	 * CRATE SQL ���̂ݎ擾 
	 * @return CREATE SQL ��
	 */
	public String getCreateSQL(TableDef td) throws TableDefParseException;
	
	/**
	 * getOutputFileString<BR>
	 * CreateSQL �t�@�C���o�͕�����擾
	 * @return �t�@�C���o�͕�����
	 */
	public String getOutputFileString(TableDef td, boolean dropSql) throws TableDefParseException;

	/**
	 * create_sql �t�@�C���ւ̋��ʕ�����i�w�b�_�j��Ԃ�
	 * @return ���ʕ�����
	 */
	public String getCreateTableHeader();

	/**
	 * create_sql �t�@�C���ւ̋��ʕ�����i�t�b�^�j��Ԃ�
	 * @return
	 */
	public String getCreateTableFooter();
	
}
