package com.ams.poi.xls2sql.jdomaker;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.sqltypes.JdoFieldTypes;

/**
 * JDO�N���X�𐶐�����t�@�C��
 * @author Akihiko MONDEN
 *
 * @version $Id: JDOSourceMaker.java 79 2009-11-23 16:17:41Z amonden $
 */
public class JDOSourceMaker {

	/**
	 * JDO���f���̃\�[�X��Ԃ�
	 * @param td
	 * @return
	 */
	public String getBeanSource(TableDef td) {
	    StringBuilder source = new StringBuilder();
	    
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
		  return 
		  String.format(
		    "import javax.jdo.annotations.IdGeneratorStrategy;\n" +
		    "import javax.jdo.annotations.IdentityType;\n" +
		    "import javax.jdo.annotations.PersistenceCapable;\n" +
		    "import javax.jdo.annotations.Persistent;\n" +
		    "import javax.jdo.annotations.PrimaryKey;\n" +

		    "import org.slim3.datastore.Attribute;\n" +
		    "import org.slim3.datastore.Model;\n" +

		    "import com.google.appengine.api.datastore.Key;\n" +
	    "\n" +
	    "/**\n" + 
	     " * <p>�^�C�g��: %s Dto</p>\n" +
	     " * <p>����: %s ��ێ�����JDO Entity, Slim3/LL API �Ή�</p>\n" +
	     " * @author \n" +
	     " * @version $Id: JDOSourceMaker.java 79 2009-11-23 16:17:41Z amonden $\n" +
	     " */\n",
	     	td.getJdoName(),
	     	td.getTableNameLogic());
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
	    sb.append("@PersistenceCapable(identityType = IdentityType.APPLICATION)\n");
	    sb.append("@Model\n"); // for LL
	    sb.append("public class " + td.getJdoName() + " implements java.io.Serializable {\n\n");
	   
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
	    
		    // TODO for LL
		 final String type = 
			 fd.isPrimaryKey() ? "Key" :JdoFieldTypes.getTypeByXLS(fd.getTypeXls()); 

	    return 
	      "\t/**\n" +
	      "\t * " + fd.getJdoField() + "�ݒ�<BR>\n" +
	      "\t * @param " + fd.getJdoField() + " " + fd.getFieldNameLogic() + "\n" +
	      "\t */\n" +
	      "\tpublic void set" + StringUtils.capitalise(fd.getJdoField()) + "(" + type + " " + fd.getJdoField() + ") {\n" +
	      "\t  " + "this." + fd.getJdoField() + " = " + fd.getJdoField() + ";\n" +
	      "\t}\n\n";
	  }

	  /**
	   * getGetterMethod<BR>
	   * getter ���\�b�h�L�q���擾
	   * @param fd
	   * @return
	   */
	  private String getGetterMethod(FieldDef fd) {
		    // TODO for LL
			 final String type = 
				 fd.isPrimaryKey() ? "Key" :JdoFieldTypes.getTypeByXLS(fd.getTypeXls()); 
	    
	    return 
	      "\t/**\n" +
	      "\t * " + fd.getJdoField() + "�擾<BR>\n" +
	      "\t * @return " + fd.getFieldNameLogic() + "\n" +
	      "\t */\n" +
	      "\tpublic " + type + " get" + StringUtils.capitalise(fd.getJdoField()) + "() {\n" +
	      "\t  " + "return this." + fd.getJdoField() + ";\n" +
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
	    
	    if (fd.isPrimaryKey()) {
	    	sb.append("\t@PrimaryKey\n");
	    	sb.append("\t@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)\n");
	    	sb.append("\t@Attribute(primaryKey = true)\n");	    // TODO for LL
	    } else {
	    	sb.append("\t@Persistent\n");
	    }
	    
	    // �ϐ���`
	    // TODO for LL
	    final String type = fd.isPrimaryKey() ? "Key" : JdoFieldTypes.getTypeByXLS(fd.getTypeXls());
	    sb.append("\tprivate " + type + " " + fd.getJdoField() + ";\n\n");
//	    sb.append("\tprivate Key " + fd.getJdoField() + ";\n\n");
	    // for old JDO
//	    sb.append("\tprivate " + JdoFieldTypes.getTypeByXLS(fd.getTypeXls()) + " " + fd.getJdoField() + ";\n\n");
	    
	    return sb.toString();
	  }

}
