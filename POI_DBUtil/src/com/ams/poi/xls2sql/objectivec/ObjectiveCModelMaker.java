/**
 * 
 */
package com.ams.poi.xls2sql.objectivec;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.util.NameUtil;

/**
 * Objective-C 関連メーカー
 * @author Akihiko MONDEN
 *
 */
public class ObjectiveCModelMaker {

	/**
	 * モデルクラスヘッダを出力
	 * @param td
	 * @return
	 */
	public String getModelClassHearder(TableDef td) {
		StringBuilder source = new StringBuilder();
	    
	    // ファイル先頭コメント
	    source.append(getFileComment(td, td.getTableNamePhysicsTopUpper()+".h"));
	    
	    // クラス定義
	    source.append(getHearderBody(td));
	    
	    return source.toString();
	}


	/**
	 * モデルクラスソースを出力
	 * @param td
	 * @return
	 */
	public String getModelClassSource(TableDef td) {
		StringBuilder source = new StringBuilder();
	    
	    // ファイル先頭コメント
	    source.append(getFileComment(td, td.getTableNamePhysicsTopUpper()+".m"));
	    
	    // クラス定義
	    source.append(getSourceBody(td));
	    
	    return source.toString();
	}


	/**
	 * ファイルコメントを返す
	 * @param td
	 * @return
	 */
	private String getFileComment(TableDef td, String filename) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String ymd = df.format(new Date());
		return
		"//\n"
		+ "//  "+filename + "\n"
		+ "//\n"
		+  "//  Created by akihiko mondeni on " + ymd + ".\n"
		+ "//  Copyright 2009 __MyCompanyName__. All rights reserved.\n"
		+ "//" + "\n";
	}



	/**
	 * ヘッダファイル本文を返す
	 * @param td
	 * @return
	 */
	private String getHearderBody(TableDef td) {
		StringBuilder s = new StringBuilder();
		
		s.append("#import <UIKit/UIKit.h>\n");
		s.append("\n");
		s.append("@interface "+td.getTableNamePhysicsTopUpper()+" : NSObject {\n");

	    // クラス変数定義
		for (Iterator it = td.getFieldList().iterator(); it.hasNext();) {
			FieldDef fd = (FieldDef) it.next();

			// field name
			// "id" は "_id" に変換 (Objective-C対策)
			String field_name = fd.getFieldNamePhysics(); 
			if  (field_name.equals("id")) {
				field_name = "_" + field_name;
			}

			
			s.append("\n");
			s.append("// " + fd.getFieldNameLogic() + "\n");
			s.append(" " + ObjectiveCFieldType.getTypeByXLS(fd.getTypeXls()) + " " + field_name + ";\n");
		}

		s.append("\n");
		s.append("}\n");
		s.append("\n");

		//  @property 属性指定
		for (Iterator it = td.getFieldList().iterator(); it.hasNext();) {
			FieldDef fd = (FieldDef) it.next();
			// field name
			// "id" は "_id" に変換 (Objective-C対策)
			String field_name = fd.getFieldNamePhysics(); 
			if  (field_name.equals("id")) {
				field_name = "_" + field_name;
			}
			s.append("\n");
			s.append("// " + fd.getFieldNameLogic() + "\n");
			s.append(" @property (assign, nonatomic) " + ObjectiveCFieldType.getTypeByXLS(fd.getTypeXls()) + " " + field_name + ";\n");
		}

		s.append("@end\n");

		return s.toString();
	}


	/**
	 * クラスファイル本文を探す
	 * @param td 
	 * @return
	 */
	private String getSourceBody(TableDef td) {
		StringBuilder s = new StringBuilder();
		
		s.append("#import \""+td.getTableNamePhysicsTopUpper()+".h\"");
		s.append("\n");
		s.append("@implementation "+td.getTableNamePhysicsTopUpper()+"\n");

	    // クラス変数定義
		for (Iterator it = td.getFieldList().iterator(); it.hasNext();) {
			FieldDef fd = (FieldDef) it.next();
			final String type = ObjectiveCFieldType.getTypeByXLS(fd.getTypeXls());
			
			// field name
			// "id" は "_id" に変換 (Objective-C対策)
			String field_name = fd.getFieldNamePhysics(); 
			if  (field_name.equals("id")) {
				field_name = "_" + field_name;
			}


			
			s.append("\n");
			s.append("// "+fd.getFieldNameLogic()+" 取得 \n");
			s.append("- ("+type+")"+field_name+" { \n");
			s.append("    return "+field_name+"; \n");
			s.append("}	 \n");
						
			s.append("\n");
			s.append("// "+fd.getFieldNameLogic()+" 設定 \n");
			s.append("- (void)set"+ NameUtil.toTopUpper(field_name)+":("+type+")val {\n");
			s.append("    "+field_name+" = val;\n");
			s.append("}\n");
		}

		s.append("\n");
		s.append("@end\n");
		
		return s.toString();
	}

}
