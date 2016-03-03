package com.ams.poi.xls2sql.util;

/**
 * ���̊֘AUtil
 * @author Akihiko MONDEN
 *
 */
public final class NameUtil {

	/**
	 * str �̐擪��啶���ɂ����������Ԃ�
	 * @param str
	 * @return str �̐擪��啶���ɂ���������
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
	 * str �̐擪���������ɂ����������Ԃ�
	 * @param str
	 * @return str �̐擪���������ɂ���������
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
	 * _ ��؂�� camel �����ɂ��ĕԂ�
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
	 * _ ��؂�� cakephp model �`���ɂ��ĕԂ�
	 * @param str
	 * @return
	 */
	public static String toCakePHPModel(String str) {
		if (str == null) {
			return str;
		}
		str = toTopUpper(toCamel(str));
		
		// ������ies��y��
		String class_name = str.replaceAll("ies$", "y");
		// ������s��""��
		class_name = class_name.replaceAll("s$", "");
		
		return class_name;
	}
	
	/**
	 * _ ��؂�̂܂ܗ��p ������ s���Ƃ�
	 * @param str
	 * @return
	 */
	public static String toRailsModel(String str) {
		if (str == null) {
			return str;
		}
//		str = toTopUpper(toCamel(str));
		
		// ������ies��y��
		String class_name = str.replaceAll("ies$", "y");
		// ������s��""��
		class_name = class_name.replaceAll("s$", "");
		
		return class_name;
	}
}
