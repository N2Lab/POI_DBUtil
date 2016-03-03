package com.ams.poi.xls2sql.jdomaker;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.sqltypes.JdoFieldTypes;

/**
 * JDOクラスを生成するファイル
 * @author Akihiko MONDEN
 *
 * @version $Id: JDOSourceMaker.java 79 2009-11-23 16:17:41Z amonden $
 */
public class JDOSourceMaker {

	/**
	 * JDOモデルのソースを返す
	 * @param td
	 * @return
	 */
	public String getBeanSource(TableDef td) {
	    StringBuilder source = new StringBuilder();
	    
	    // ファイル先頭コメント
	    source.append(getHeaderComment(td));
	    
	    // クラス定義
	    source.append(getClassSource(td));
	    
	    return source.toString();
	}
	
	  /**
	   * getHeaderComment<BR>
	   * ソースヘッダコメント取得
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
	     " * <p>タイトル: %s Dto</p>\n" +
	     " * <p>説明: %s を保持するJDO Entity, Slim3/LL API 対応</p>\n" +
	     " * @author \n" +
	     " * @version $Id: JDOSourceMaker.java 79 2009-11-23 16:17:41Z amonden $\n" +
	     " */\n",
	     	td.getJdoName(),
	     	td.getTableNameLogic());
	  }

	  /**
	   * getClassSource<BR>
	   * ソースコード取得
	   * @param td
	   * @return
	   */
	  private String getClassSource(TableDef td) {
	    StringBuffer sb = new StringBuffer();
	    
	    // クラス定義 start
	    sb.append("@PersistenceCapable(identityType = IdentityType.APPLICATION)\n");
	    sb.append("@Model\n"); // for LL
	    sb.append("public class " + td.getJdoName() + " implements java.io.Serializable {\n\n");
	   
	    // クラス変数定義
	    for(Iterator it = td.getFieldList().iterator(); it.hasNext();) {
	      sb.append(getClassVar((FieldDef)it.next()));
	    }
	    
	    // setter, getter定義
	    for(Iterator it = td.getFieldList().iterator(); it.hasNext();) {
	      FieldDef fd = (FieldDef)it.next();
	      sb.append(getSetterMethod(fd));
	      sb.append(getGetterMethod(fd));
	    }
	    
	    // クラス定義 end
	    sb.append("}"); 
	    
	    return sb.toString();
	  }

	  /**
	   * getSetterMethod<BR>
	   * setter メソッド記述を取得
	   * @param fd
	   * @return
	   */
	  private String getSetterMethod(FieldDef fd) {
	    
		    // TODO for LL
		 final String type = 
			 fd.isPrimaryKey() ? "Key" :JdoFieldTypes.getTypeByXLS(fd.getTypeXls()); 

	    return 
	      "\t/**\n" +
	      "\t * " + fd.getJdoField() + "設定<BR>\n" +
	      "\t * @param " + fd.getJdoField() + " " + fd.getFieldNameLogic() + "\n" +
	      "\t */\n" +
	      "\tpublic void set" + StringUtils.capitalise(fd.getJdoField()) + "(" + type + " " + fd.getJdoField() + ") {\n" +
	      "\t  " + "this." + fd.getJdoField() + " = " + fd.getJdoField() + ";\n" +
	      "\t}\n\n";
	  }

	  /**
	   * getGetterMethod<BR>
	   * getter メソッド記述を取得
	   * @param fd
	   * @return
	   */
	  private String getGetterMethod(FieldDef fd) {
		    // TODO for LL
			 final String type = 
				 fd.isPrimaryKey() ? "Key" :JdoFieldTypes.getTypeByXLS(fd.getTypeXls()); 
	    
	    return 
	      "\t/**\n" +
	      "\t * " + fd.getJdoField() + "取得<BR>\n" +
	      "\t * @return " + fd.getFieldNameLogic() + "\n" +
	      "\t */\n" +
	      "\tpublic " + type + " get" + StringUtils.capitalise(fd.getJdoField()) + "() {\n" +
	      "\t  " + "return this." + fd.getJdoField() + ";\n" +
	      "\t}\n\n";
	  }

	  /**
	   * getClassVar<BR>
	   * クラス変数記述を取得
	   * @param fd
	   * @return
	   */
	  private String getClassVar(FieldDef fd) {
	    StringBuffer sb = new StringBuffer();
	    
	    // コメント
	    sb.append("\t/** " + fd.getFieldNameLogic() + " */\n");
	    
	    if (fd.isPrimaryKey()) {
	    	sb.append("\t@PrimaryKey\n");
	    	sb.append("\t@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)\n");
	    	sb.append("\t@Attribute(primaryKey = true)\n");	    // TODO for LL
	    } else {
	    	sb.append("\t@Persistent\n");
	    }
	    
	    // 変数定義
	    // TODO for LL
	    final String type = fd.isPrimaryKey() ? "Key" : JdoFieldTypes.getTypeByXLS(fd.getTypeXls());
	    sb.append("\tprivate " + type + " " + fd.getJdoField() + ";\n\n");
//	    sb.append("\tprivate Key " + fd.getJdoField() + ";\n\n");
	    // for old JDO
//	    sb.append("\tprivate " + JdoFieldTypes.getTypeByXLS(fd.getTypeXls()) + " " + fd.getJdoField() + ";\n\n");
	    
	    return sb.toString();
	  }

}
