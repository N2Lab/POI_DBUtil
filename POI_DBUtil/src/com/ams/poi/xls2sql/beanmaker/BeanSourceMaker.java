package com.ams.poi.xls2sql.beanmaker;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.sqltypes.JavaFieldTypes;

/**
 * <p>�v���W�F�N�g��: POI_DBUtil</p>
 * <p>�^�C�g��: EnumSourceMaker</p>
 * <p>����: TableDef ���� Java Bean �\�[�X�R�[�h�𐶐�����N���X�B</p>
 * <p>Created on 2003/12/15</p>
 * @author ��c���F
 * @version $Revision: 69 $
 */
public class BeanSourceMaker {

  /**
   * getBeanSource<BR>
   * Bean�\�[�X�R�[�h���o�́B<BR>
   * import, package ���͋L�q���Ȃ�(Eclipse���̃��t�@�N�^�����O�ɔC����j
   * @param td
   * @return �\�[�X�R�[�h
   */
  public String getBeanSource(TableDef td) {
    
    StringBuffer source = new StringBuffer();
    
    // �t�@�C���擪�R�����g
    source.append(getHeaderComment(td));
    
    // �N���X��`
    source.append(getClassSource(td));
    
    return source.toString();
  }

  /**
   * getHeaderComment<BR>
   * �\�[�X�w�b�_�R�����g�擾
   * @param td
   * @return
   */
  private String getHeaderComment(TableDef td) {
    String s = "/**\n" + 
     " * <p>�^�C�g��: " + td.getTableNameLogic() + "�e�[�u���pBean</p>\n" +
     " * <p>����: " + td.getTableNameLogic() + "(" + td.getTableNamePhysics() + ")�e�[�u���ƑΉ�����Bean</p>\n" +
     " * @author \n" +
     " * @version $Id: BeanSourceMaker.java 69 2009-11-22 05:08:18Z amonden $\n" +
     " */\n";
     
    return s;
  }

  /**
   * getClassSource<BR>
   * �\�[�X�R�[�h�擾
   * @param td
   * @return
   */
  private String getClassSource(TableDef td) {
    StringBuffer sb = new StringBuffer();
    
    // �N���X��` start
    sb.append("public class " + td.getTableNamePhysics() + " implements java.io.Serializable {\n\n");
   
    // �N���X�ϐ���`
    for(Iterator it = td.getFieldList().iterator(); it.hasNext();) {
      sb.append(getClassVar((FieldDef)it.next()));
    }
    
    // setter, getter��`
    for(Iterator it = td.getFieldList().iterator(); it.hasNext();) {
      FieldDef fd = (FieldDef)it.next();
      sb.append(getSetterMethod(fd));
      sb.append(getGetterMethod(fd));
    }
    
    // �N���X��` end
    sb.append("}"); 
    
    return sb.toString();
  }

  /**
   * getSetterMethod<BR>
   * setter ���\�b�h�L�q���擾
   * @param fd
   * @return
   */
  private String getSetterMethod(FieldDef fd) {
    
    return 
      "\t/**\n" +
      "\t * " + fd.getFieldNameLogic() + "�ݒ�<BR>\n" +
      "\t * @param " + fd.getFieldNamePhysics() + " " + fd.getFieldNameLogic() + "\n" +
      "\t */\n" +
      "\tpublic void set" + StringUtils.capitalise(fd.getFieldNamePhysics()) + "(" + JavaFieldTypes.getTypeByXLS(fd.getTypeXls()) + " " + fd.getFieldNamePhysics() + ") {\n" +
      "\t  " + "this." + fd.getFieldNamePhysics() + " = " + fd.getFieldNamePhysics() + ";\n" +
      "\t}\n\n";
  }

  /**
   * getGetterMethod<BR>
   * getter ���\�b�h�L�q���擾
   * @param fd
   * @return
   */
  private String getGetterMethod(FieldDef fd) {
    
    return 
      "\t/**\n" +
      "\t * " + fd.getFieldNameLogic() + "�擾<BR>\n" +
      "\t * @return " + fd.getFieldNameLogic() + "\n" +
      "\t */\n" +
      "\tpublic " + JavaFieldTypes.getTypeByXLS(fd.getTypeXls()) + " get" + StringUtils.capitalise(fd.getFieldNamePhysics()) + "() {\n" +
      "\t  " + "return this." + fd.getFieldNamePhysics() + ";\n" +
      "\t}\n\n";
  }

  /**
   * getClassVar<BR>
   * �N���X�ϐ��L�q���擾
   * @param fd
   * @return
   */
  private String getClassVar(FieldDef fd) {
    StringBuffer sb = new StringBuffer();
    
    // �R�����g
    sb.append("\t/** " + fd.getFieldNameLogic() + " */\n");
    
    // �ϐ���`
    sb.append("\tprivate " + JavaFieldTypes.getTypeByXLS(fd.getTypeXls()) + " " + fd.getFieldNamePhysics() + ";\n\n");
    
    return sb.toString();
  }

}
