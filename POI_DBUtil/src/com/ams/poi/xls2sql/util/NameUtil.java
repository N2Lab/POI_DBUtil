package com.ams.poi.xls2sql.util;

/**
 * –¼ÌŠÖ˜AUtil
 * @author Akihiko MONDEN
 *
 */
public final class NameUtil {

	/**
	 * str ‚Ìæ“ª‚ğ‘å•¶š‚É‚µ‚½•¶š—ñ‚ğ•Ô‚·
	 * @param str
	 * @return str ‚Ìæ“ª‚ğ‘å•¶š‚É‚µ‚½•¶š—ñ
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
	 * str ‚Ìæ“ª‚ğ¬•¶š‚É‚µ‚½•¶š—ñ‚ğ•Ô‚·
	 * @param str
	 * @return str ‚Ìæ“ª‚ğ¬•¶š‚É‚µ‚½•¶š—ñ
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
	 * _ ‹æØ‚è‚ğ camel •¶š‚É‚µ‚Ä•Ô‚·
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
	 * _ ‹æØ‚è‚ğ cakephp model Œ`®‚É‚µ‚Ä•Ô‚·
	 * @param str
	 * @return
	 */
	public static String toCakePHPModel(String str) {
		if (str == null) {
			return str;
		}
		str = toTopUpper(toCamel(str));
		
		// ––”ö‚Ìies‚ğy‚É
		String class_name = str.replaceAll("ies$", "y");
		// ––”ö‚Ìs‚ğ""‚É
		class_name = class_name.replaceAll("s$", "");
		
		return class_name;
	}
	
	/**
	 * _ ‹æØ‚è‚Ì‚Ü‚Ü—˜—p ––”ö‚Ì s‚ğ‚Æ‚é
	 * @param str
	 * @return
	 */
	public static String toRailsModel(String str) {
		if (str == null) {
			return str;
		}
//		str = toTopUpper(toCamel(str));
		
		// ––”ö‚Ìies‚ğy‚É
		String class_name = str.replaceAll("ies$", "y");
		// ––”ö‚Ìs‚ğ""‚É
		class_name = class_name.replaceAll("s$", "");
		
		return class_name;
	}
}
