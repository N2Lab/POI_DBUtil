package com.ams.poi.xls2sql.beanmaker;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.sqltypes.JavaFieldTypes;

/**
 * <p>プロジェクト名: POI_DBUtil</p>
 * <p>タイトル: EnumSourceMaker</p>
 * <p>説明: TableDef から Java Bean ソースコードを生成するクラス。</p>
 * <p>Created on 2003/12/15</p>
 * @author 門田明彦
 * @version $Revision: 69 $
 */
public class BeanSourceMaker {

  /**
   * getBeanSource<BR>
   * Beanソースコードを出力。<BR>
   * import, package 文は記述しない(Eclipse等のリファクタリングに任せる）
   * @param td
   * @return ソースコード
   */
  public String getBeanSource(TableDef td) {
    
    StringBuffer source = new StringBuffer();
    
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
    String s = "/**\n" + 
     " * <p>タイトル: " + td.getTableNameLogic() + "テーブル用Bean</p>\n" +
     " * <p>説明: " + td.getTableNameLogic() + "(" + td.getTableNamePhysics() + ")テーブルと対応するBean</p>\n" +
     " * @author \n" +
     " * @version $Id: BeanSourceMaker.java 69 2009-11-22 05:08:18Z amonden $\n" +
     " */\n";
     
    return s;
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
    sb.append("public class " + td.getTableNamePhysics() + " implements java.io.Serializable {\n\n");
   
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
    
    return 
      "\t/**\n" +
      "\t * " + fd.getFieldNameLogic() + "設定<BR>\n" +
      "\t * @param " + fd.getFieldNamePhysics() + " " + fd.getFieldNameLogic() + "\n" +
      "\t */\n" +
      "\tpublic void set" + StringUtils.capitalise(fd.getFieldNamePhysics()) + "(" + JavaFieldTypes.getTypeByXLS(fd.getTypeXls()) + " " + fd.getFieldNamePhysics() + ") {\n" +
      "\t  " + "this." + fd.getFieldNamePhysics() + " = " + fd.getFieldNamePhysics() + ";\n" +
      "\t}\n\n";
  }

  /**
   * getGetterMethod<BR>
   * getter メソッド記述を取得
   * @param fd
   * @return
   */
  private String getGetterMethod(FieldDef fd) {
    
    return 
      "\t/**\n" +
      "\t * " + fd.getFieldNameLogic() + "取得<BR>\n" +
      "\t * @return " + fd.getFieldNameLogic() + "\n" +
      "\t */\n" +
      "\tpublic " + JavaFieldTypes.getTypeByXLS(fd.getTypeXls()) + " get" + StringUtils.capitalise(fd.getFieldNamePhysics()) + "() {\n" +
      "\t  " + "return this." + fd.getFieldNamePhysics() + ";\n" +
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
    
    // 変数定義
    sb.append("\tprivate " + JavaFieldTypes.getTypeByXLS(fd.getTypeXls()) + " " + fd.getFieldNamePhysics() + ";\n\n");
    
    return sb.toString();
  }

}
