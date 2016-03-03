/**
 * 
 */
package com.ams.poi.xls2sql.jspmaker;

import com.ams.poi.xls2sql.bean.TableValuesDef;
import com.ams.poi.xls2sql.util.NameUtil;

/**
 * slim3 用セレクトボックス生成用
 * @author Akihiko MONDEN
 *
 * @version $Id: JspFormSelectMaker.java 961 2010-10-12 05:12:42Z amonden $
 */
public class JspFormSelectMaker {
	
	public String getSource(TableValuesDef tvd) {
		StringBuilder source = new StringBuilder();
		
		// import
		source.append(getDirective());
		
		// add select tag source
		source.append(getSelectTag(tvd));
		
		// add error msg tag
		source.append(getErrorMsgTag(tvd));
		
		return source.toString();
	}

/**
	 * @return
	 */
	private String getDirective() {
		return 
		"<%@page pageEncoding=\"UTF-8\" isELIgnored=\"false\"%>\r\n" + 
		"<%@taglib prefix=\"c\" uri=\"http://java.sun.com/jsp/jstl/core\"%>\r\n" +
		"<%@taglib prefix=\"fmt\" uri=\"http://java.sun.com/jsp/jstl/fmt\"%>\r\n" +
		"<%@taglib prefix=\"f\" uri=\"http://www.slim3.org/functions\"%>\r\n" + 
		"<%@taglib prefix=\"poif\" uri=\"/WEB-INF/poif.tld\"%>\r\n\r\n";
	}

/**
	 * @param tvd 
 * @return
	 */
	private String getSelectTag(TableValuesDef td) {
		
		// 例
		// ${est:selectByEnum("BuildType", "fbuildId", "buildName", "選択")}
	// <span class="err_msg">${f:h(errors.fbuildId)}</span>

//		final String table_name = td.getTableNamePhysics();
		final String class_name = td.getTableNamePhysics() + "Type";
		final String field_name = NameUtil.toTopLower(td.getTableNamePhysics()) + "Id";
		final String label_property = NameUtil.toTopLower(td.getTableNamePhysics()) + "Name";
		
		return 
		String.format("${poif:selectByEnum(\"%s\", \"%s\", \"%s\", \"選択\")}\r\n" + 
		"",
		class_name, field_name, label_property
		);
		
//    * enum_simple_name のEnum型でセレクトボックスを作成する.
//    * デフォルト値はリクエストパラメータから取得する
//    * @param enum_simple_name
//    * @param item_name
//    * @param label_field 
//    * @return
//    */
//   public static String getSelectBoxByEnum(String enum_simple_name, String item_name, String label_field, String top_label) {
	}

/**
	 * @param tvd 
 * @return
	 */
	private String getErrorMsgTag(TableValuesDef td) {

		final String field_name = NameUtil.toTopLower(td.getTableNamePhysics()) + "Id";

		return String.format(
				"<span class=\"err_msg\">${f:h(errors.%s)}</span>",field_name);
	}

}
