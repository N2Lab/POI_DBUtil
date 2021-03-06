package com.ams.poi.xls2sql.util;

/**
 * 名称関連Util
 * @author Akihiko MONDEN
 *
 */
public final class NameUtil {

	/**
	 * str の先頭を大文字にした文字列を返す
	 * @param str
	 * @return str の先頭を大文字にした文字列
	 */
	public static String toTopUpper(String str) {
		if (str == null) {
			return null;
		} else if (str.length() == 0) {
			return "";
		} else if (str.length() == 1) {
			return str.toUpperCase();
		}
		
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * str の先頭を小文字にした文字列を返す
	 * @param str
	 * @return str の先頭を小文字にした文字列
	 */
	public static String toTopLower(String str) {
		if (str == null) {
			return null;
		} else if (str.length() == 0) {
			return "";
		} else if (str.length() == 1) {
			return str.toLowerCase();
		}
		
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	/**
	 * _ 区切りを camel 文字にして返す
	 * @param str
	 * @return
	 */
	public static String toCamel(String str) {
		if (str == null) {
			return str;
		}
		StringBuilder s = new StringBuilder();
		boolean before_uber = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '_') {
				before_uber = true;
				continue;
			}
			s.append(before_uber ? Character.toUpperCase(c) : c);
			before_uber = false;
		}
		
		return s.toString();
	}
	/**
	 * _ 区切りを cakephp model 形式にして返す
	 * @param str
	 * @return
	 */
	public static String toCakePHPModel(String str) {
		if (str == null) {
			return str;
		}
		str = toTopUpper(toCamel(str));
		
		// 末尾のiesをyに
		String class_name = str.replaceAll("ies$", "y");
		// 末尾のsを""に
		class_name = class_name.replaceAll("s$", "");
		
		return class_name;
	}
	
	/**
	 * _ 区切りのまま利用 末尾の sをとる
	 * @param str
	 * @return
	 */
	public static String toRailsModel(String str) {
		if (str == null) {
			return str;
		}
//		str = toTopUpper(toCamel(str));
		
		// 末尾のiesをyに
		String class_name = str.replaceAll("ies$", "y");
		// 末尾のsを""に
		class_name = class_name.replaceAll("s$", "");
		
		return class_name;
	}
}
