/**
 * 
 */
package com.ams.poi.xls2sql.realmswiftmaker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.jerseymaker.WebAPIMethod;
import com.ams.poi.xls2sql.util.NameUtil;

/**
 * RealmswiftModelSourceMaker
 *
 * @author amonden
 *
 */
public class RealmswiftModelSourceMaker {
//	private String app_cd_name;

	/**
	 * @param m
	 * @param td 
	 * @return
	 */
	public String getSource(WebAPIMethod m, TableDef td) {
		StringBuilder source = new StringBuilder();

		// import
		source.append(getImport());
		
		// ファイル先頭コメント
		source.append(getHeaderComment(m));

		// クラス定義
		source.append(getClassSource(m, td));

		return source.toString();
	}

	private String getImport() {
		return
		"";
	}

	/**
	 * getHeaderComment<BR>
	 * ソースヘッダコメント取得
	 * 
	 * @param td
	 * @return
	 */
	private String getHeaderComment(WebAPIMethod m) {
		String s = "//\n"
				+ "// "
				+ m.getTableNameLogic()
				+ " Realm swift Model\n"
				+ "// 説明: "
				+ m.getTableNameLogic()
				+ "("
				+ m.getTableNamePhysics()
				+ ")テーブルと対応するRealm swift model</p>\n\n"
				+ "// @author \n"
				+ "// @version $Id: RealmswiftModelSourceMaker.java 100 2009-11-27 08:54:08Z amonden $\n"
				+ "//\n";

		return s;
	}

	/**
	 * getClassSource<BR>
	 * ソースコード取得
	 * @param td 
	 * 
	 * @param td
	 * @return
	 */
	private String getClassSource(WebAPIMethod m, TableDef td) {
		StringBuffer sb = new StringBuffer();
		StringBuffer s = sb;
		final String table_name = m.getTableNamePhysics();
		final String class_name = NameUtil.toRealmswiftModel(m.getTableNamePhysics());
		final String bean_class = NameUtil.toRealmswiftModel(m.getTableNamePhysics());
		final String api_path = m.getTableNamePhysics().toLowerCase();
		List<String> def_list = new ArrayList<String>();

		// 1. クラス定義 start
//		import RealmSwift
//
//		// Dog model
//		class Dog: Object {
//		    dynamic var name = ""
//		    dynamic var owner: Person? // Properties can be optional
//		}
		// import
		sb.append("import RealmSwift\r\n\r\n");
		// class def
		sb.append("class "+class_name+": Object {\r\n");

		// fields
	    // クラス変数定義
		for (Iterator it = td.getFieldList().iterator(); it.hasNext();) {
			FieldDef fd = (FieldDef) it.next();
			final String type = RealmswiftFieldType.getTypeByXLS(fd.getTypeXls());
//			final String type = ObjectiveCFieldType.getTypeByXLS(fd.getTypeXls());
			
			// field name
			// "id" は "_id" に変換 (Objective-C対策)
			String field_name = fd.getFieldNamePhysics(); 
			String field_name_l = fd.getFieldNameLogic();
//			if  (field_name.equals("id")) {
//				field_name = "_" + field_name;
//			}
			
			s.append("\r\n");
			s.append("\t// " + field_name_l + "\r\n");
			
			String dynamic = type.indexOf("RealmOptional") == -1 ? "dynamic " : "";
			s.append("\t"+dynamic+"var "+field_name+" = "+type+"\r\n"); // type というより初期値(右辺)を指定する
		}

		// class end
		sb.append("}\r\n");

//		StringBuilder s = new StringBuilder();
//		
//		s.append("#import \""+td.getTableNamePhysicsTopUpper()+".h\"");
//		s.append("\n");
//		s.append("@implementation "+td.getTableNamePhysicsTopUpper()+"\n");
//
//	    // クラス変数定義
//		for (Iterator it = td.getFieldList().iterator(); it.hasNext();) {
//			FieldDef fd = (FieldDef) it.next();
//			final String type = ObjectiveCFieldType.getTypeByXLS(fd.getTypeXls());
//			
//			// field name
//			// "id" は "_id" に変換 (Objective-C対策)
//			String field_name = fd.getFieldNamePhysics(); 
//			if  (field_name.equals("id")) {
//				field_name = "_" + field_name;
//			}
//
//
//			
//			s.append("\n");
//			s.append("// "+fd.getFieldNameLogic()+" 取得 \n");
//			s.append("- ("+type+")"+field_name+" { \n");
//			s.append("    return "+field_name+"; \n");
//			s.append("}	 \n");
//						
//			s.append("\n");
//			s.append("// "+fd.getFieldNameLogic()+" 設定 \n");
//			s.append("- (void)set"+ NameUtil.toTopUpper(field_name)+":("+type+")val {\n");
//			s.append("    "+field_name+" = val;\n");
//			s.append("}\n");
//		}
//
//		s.append("\n");
//		s.append("@end\n");
//		
//		return s.toString();		
		
		// クラス定義 end

		return sb.toString();
	}
}
