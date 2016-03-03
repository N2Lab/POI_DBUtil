package com.ams.poi.xls2sql.util;

/**
 * <p>�v���W�F�N�g��: POI_DBUtil</p>
 * <p>�^�C�g��: EscapeUtil</p>
 * <p>����: �t�B�[���h�l����Escape����Util�N���X</p>
 * <p>Created on 2004/04/16</p>
 * @author ��c���F
 * @version $Revision: 69 $
 */
public final class EscapeUtil {

  /**
   * escapeMySQL<BR>
   * ' �Ȃǂ�MySQL�����񃊃e�����G�X�P�[�v�Ώە����� '' �̂悤�ɃG�X�P�[�v���ĕԂ��B<BR>
   * ���̃G�X�P�[�v�Ώە������BR>
   * <A href="http://dev.mysql.com/doc/mysql/ja/String_syntax.html" target="_blank">
   * MySQL ���t�@�����X http://dev.mysql.com/doc/mysql/ja/String_syntax.html</A>
   * 
   * @param val �G�X�P�[�v�Ώە�����
   * @return �G�X�P�[�v�㕶����
   */
  public static String escapeMySQL(String val) {

    if (val == null || val.length() == 0) {
      return val;
    }

    // ' �� '' �ɒu��
    // ' �̌オ '' ����Ȃ����m�F����������������
    // 
    return val.replaceAll("'", "''");
    
  }

}
