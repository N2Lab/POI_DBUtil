package com.ams.poi.xls2sql.enummaker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.bean.TableDefCache;
import com.ams.poi.xls2sql.bean.TableValuesDef;
import com.ams.poi.xls2sql.sqltypes.JavaFieldTypes;
import com.ams.poi.xls2sql.util.NameUtil;

/**
 * <p>
 * �v���W�F�N�g��: POI_DBUtil
 * </p>
 * <p>
 * �^�C�g��: EnumSourceMaker..
 * </p>
 * <p>
 * ����: TableDef ���� Java Enum �\�[�X�R�[�h�𐶐�����N���X�B
 * </p>
 * <p>
 * Created on 2003/12/15
 * </p>
 * 
 * @author ��c���F
 * @version $Revision: 100 $
 */
public class EnumSourceMaker {

	// /**
	// * getEnumSource<BR>
	// * Bean�\�[�X�R�[�h���o�́B<BR>
	// * import, package ���͋L�q���Ȃ�(Eclipse���̃��t�@�N�^�����O�ɔC����j
	// * @param td
	// * @return �\�[�X�R�[�h
	// */
	// public String getEnumSource(TableDef td) {
	//    
	// StringBuffer source = new StringBuffer();
	//    
	// // �t�@�C���擪�R�����g
	// source.append(getHeaderComment(td));
	//    
	// // �N���X��`
	// source.append(getClassSource(td));
	//    
	// return source.toString();
	// }

	private String app_cd_name;

	/**
	 * @param tvd
	 * @return
	 */
	public String getEnumSource(TableValuesDef tvd) {
		StringBuilder source = new StringBuilder();

		// import
		source.append(getImport());
		
		// �t�@�C���擪�R�����g
		source.append(getHeaderComment(tvd));

		// �N���X��`
		source.append(getClassSource(tvd));

		return source.toString();
	}

	private String getImport() {
		return "import java.util.ArrayList;\r\n"+
		"import java.util.Collection;\r\n"+
		"import java.util.Collections;\r\n"+
		"import java.util.LinkedHashMap;\r\n"+
		"import java.util.List;\r\n\r\n";
	}

	/**
	 * getHeaderComment<BR>
	 * �\�[�X�w�b�_�R�����g�擾
	 * 
	 * @param td
	 * @return
	 */
	private String getHeaderComment(TableValuesDef td) {
		String s = "/**\n"
				+ " * <p>�^�C�g��: "
				+ td.getTableNameLogic()
				+ "�e�[�u���pEnum</p>\n"
				+ " * <p>����: "
				+ td.getTableNameLogic()
				+ "("
				+ td.getTableNamePhysics()
				+ ")�e�[�u���ƑΉ�����Enum</p>\n"
				+ " * @author \n"
				+ " * @version $Id: EnumSourceMaker.java 100 2009-11-27 08:54:08Z amonden $\n"
				+ " */\n";

		return s;
	}

	/**
	 * getClassSource<BR>
	 * �\�[�X�R�[�h�擾
	 * 
	 * @param td
	 * @return
	 */
	private String getClassSource(TableValuesDef td) {
		StringBuffer sb = new StringBuffer();
		final String table_name = td.getTableNamePhysics();
		final String class_name = td.getTableNamePhysics() + "Type";
		List<String> def_list = new ArrayList<String>();

		// 1. �N���X��` start
		sb.append("public enum " + class_name + " {\n\n");

		// 2. Enum Data��`
		def_list = addDataDefines(td, sb);

		// 2. �N���X�ϐ���`
		for (Iterator it = td.getFieldNameList().iterator(); it.hasNext();) {
			final String field_name = (String) it.next();
			sb.append(getEnumVar(table_name, field_name.trim()));
		}

		// 3. �R���X�g���N�^
		sb.append(getConstructor(table_name, td, class_name));

		// setter, getter��` > public final �Œ��ڎQ�Ƃ�����
		// sb.append(getSetterGetterMethod(table_name, td, class_name));
		// for(Iterator it = td.getFieldList().iterator(); it.hasNext();) {
		// FieldDef fd = (FieldDef)it.next();
		// sb.append(getSetterMethod(fd));
		// sb.append(getGetterMethod(fd));
		// }

		// 4. �SList�擾���b�\�b�h
		sb.append(getAllList(table_name, td, class_name, def_list));
		sb.append(getAllLinkedList(table_name, td, class_name, def_list));

		// �N���X��` end
		sb.append("}");

		return sb.toString();
	}

	private String getAllList(String table_name, TableValuesDef td,
			String class_name, List<String> def_list) {
		// private static final List<RailType> allList;
		// static {
		// allList = new ArrayList<RailType>();
		// allList.add(CD_00002);
		// }
		//    
		// /**
		// * get all list
		// * @return
		// */
		// public static final List<RailType> getAllList() {
		// return allList;
		// }

		StringBuilder sb = new StringBuilder();

		sb.append("\tprivate static final List<" + class_name
				+ "> allList;\r\n");
		sb.append("\tstatic {\r\n");
		sb.append("\t\tallList = new ArrayList<" + class_name + ">();\r\n");
		for (String def : def_list) {
			sb.append("\t\tallList.add(" + def + ");\r\n");
		}
		// allList.add(CD_00002);
		sb.append("\t\t}\r\n\r\n");

		sb.append("\t/**\r\n");
		sb.append("\t * �S�f�[�^List���`���Ɏ擾 \r\n");
		sb.append("\t * @return �S���X�g\r\n");
		sb.append("\t */\r\n");
		sb.append("\tpublic static final Collection<"+class_name+"> getAllList() {\r\n");
		sb.append("\t\treturn Collections.unmodifiableCollection(allList);\r\n");
		sb.append("\t}\r\n\r\n");

		return sb.toString();
	}

	private String getAllLinkedList(String table_name, TableValuesDef td,
			String class_name, List<String> def_list) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\tprivate static final LinkedHashMap<String, "+class_name+"> allLinkedHashMap;\r\n");
		sb.append("\tstatic {\r\n");
		sb.append("\t\tallLinkedHashMap = new LinkedHashMap<String, "+class_name+">();\r\n");
//		sb.append("\t\tallLinkedHashMap.put(CD_00002.rail_cd, CD_00002);\r\n");
		for (String def : def_list) {
			sb.append("\t\tallLinkedHashMap.put(" + def + "."+ NameUtil.toCamel(app_cd_name) +", " + def + ");\r\n");
		}

//		sb.append("\t\t// TODO sort ? \r\n");
		sb.append("\t}\r\n");
		sb.append(" \r\n");
		sb.append("\t/**\r\n");
		sb.append("\t * �S�f�[�^Map���擾 \r\n");
		sb.append("\t * @return �SMap\r\n");
		sb.append("\t */\r\n");
		sb.append("\tpublic static final LinkedHashMap<String, "+class_name+"> getAllLinkedHashMap() {\r\n");
		sb.append("\t\treturn allLinkedHashMap;\r\n");
		sb.append("\t}\r\n");
		sb.append("\t\r\n");
		sb.append("\t/**\r\n");
		sb.append("\t * Enum by "+app_cd_name+"\r\n");
		sb.append("\t * @param "+app_cd_name+"\r\n");
		sb.append("\t * @return\r\n");
		sb.append("\t */\r\n");
		String method = String.format("getBy%s", NameUtil.toCamel(app_cd_name));
		sb.append("\tpublic static final "+class_name+" "+method+"(String "+app_cd_name+") {\r\n");
		sb.append("\t\treturn allLinkedHashMap.get("+app_cd_name+");\r\n");
		sb.append("\t}\r\n");
		sb.append("\r\n");
		

//		sb.append("\tprivate static final List<" + class_name
//				+ "> allList;\r\n");
//		sb.append("\tstatic {\r\n");
//		sb.append("\t\tallList = new ArrayList<" + class_name + ">();\r\n");
//		for (String def : def_list) {
//			sb.append("\t\tallList.add(" + def + ");\r\n");
//		}
		// allList.add(CD_00002);
//		sb.append("\t\t}\r\n\r\n");
//
//		sb.append("\t/**\r\n");
//		sb.append("\t * �S�f�[�^���`���Ɏ擾 \r\n");
//		sb.append("\t * @return �S���X�g\r\n");
//		sb.append("\t */\r\n");
//		sb.append("\tpublic static final Collection<RailType> getAllList() {\r\n");
//		sb.append("\t\treturn Collections.unmodifiableCollection(allList);\r\n");
//		sb.append("\t}\r\n\r\n");

		return sb.toString();
	}

	private List<String> addDataDefines(TableValuesDef td, StringBuffer sb) {
		List<String> def_list = new ArrayList<String>();
		final String table_name = td.getTableNamePhysics();
		TableDef tdef = TableDefCache.get(table_name);
		// CD_00001("00001","JR CHUUOUSEN","00001"),
		// CD_00002("00001","JR CHUUOUSEN","00001"); // �����
		// cd_name = CD_00001 �̕���

		boolean first1 = true;
		final String cd_name_def = 
			(String) (td.getFieldNameList().size() >= 2 ? td.getFieldNameList().get(1) :
			td.getFieldNameList().size() >= 1 ? td.getFieldNameList().get(0) : "ID");
		app_cd_name = cd_name_def;

		for (Iterator it2 = td.getRecordDataList().iterator(); it2.hasNext();) {
			List val_list = (List) it2.next();
			boolean first2 = true;

			if (first1) {
			} else {
				sb.append("\r\n");
			}

			sb.append("\t");
			if (first1) {
				first1 = false;
			} else {
				sb.append(",");
			}

			int i = 0;
			String cd_name = cd_name_def;
			StringBuilder sb2 = new StringBuilder();
			for (Iterator it3 = val_list.iterator(); it3.hasNext();i++) {
				String data = (String) it3.next();
				
				if (i == 1) {
					// 2��ڂ��`���Ƃ���
					cd_name = data;
				}
				// find field_type
				FieldDef fd = tdef.getFieldList().get(i);
//				for (Iterator it = tdef.getFieldList().iterator(); it.hasNext();) {
//					FieldDef fdtmp = (FieldDef) it.next();
//					if (fdtmp.getFieldNamePhysics().equalsIgnoreCase(field_name)) {
//						fd = fdtmp;
//						break;
//					}
//				}
//				if (fd == null) {
//					return "";
//				}

				String java_type = JavaFieldTypes.getTypeByXLS(fd.getTypeXls());
				String sdata = data;
				if (java_type.equals("String")) {
					sdata = "\"" + data + "\"";
				} else if (java_type.endsWith("Type")) {
					sdata = fd.getTypeXls() + "." + data;
				}
				
//					// �ϐ���`
//					sb.append("\tpublic final "
//							+ JavaFieldTypes.getTypeByXLS(fd.getTypeXls()) + " "
//							+ NameUtil.toCamel(fd.getFieldNamePhysics()) + ";\n\n");
					

				if (first2) {
//					String def = String.format("%s_%s", cd_name, data); �Â����@  id_pk
//					sb.append(String.format("%s(", def));
					sb2.append("(");
					sb2.append(String.format("%s", sdata));
				} else {
					sb2.append(String.format(",%s", sdata));
				}
				if (first2) {
					first2 = false;
				}
			}
			sb2.append(")");
			sb2.insert(0, cd_name); // �擪�ɒ�`����ǉ�
			def_list.add(cd_name);
			
			sb.append(sb2);
		}
		// }
		sb.append(";\r\n\r\n");
		return def_list;
	}

	private String getConstructor(String table_name, TableValuesDef tvd,
			String class_name) {

		StringBuffer sb = new StringBuffer();
		TableDef td = TableDefCache.get(table_name);

		// /**
		// * �R���X�g���N�^
		// * @param rail_cd
		// * @param rail_name
		// * @param disp_order
		// */

		sb.append("\t/**\n" + "\t * �R���X�g���N�^<BR>\n");
		for (Iterator it = tvd.getFieldNameList().iterator(); it.hasNext();) {
			String field_name = (String) it.next();
			sb.append("\t * @param " + field_name + "\n");
		}
		sb.append("\t */\n\n");

		// private RailType(String rail_cd, String rail_name, String disp_order)
		// {
		sb.append("\tprivate " + class_name + "(");

		boolean first = true;
		for (Iterator it = td.getFieldList().iterator(); it.hasNext();) {
			FieldDef fd = (FieldDef) it.next();
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append(String.format("%s %s", JavaFieldTypes.getTypeByXLS(fd
					.getTypeXls()), fd.getFieldNamePhysics()));
		}
		sb.append(") {\n");

		for (Iterator it = td.getFieldList().iterator(); it.hasNext();) {
			FieldDef fd = (FieldDef) it.next();
			sb.append(String.format("\t\tthis.%s = %s;\n", 
					NameUtil.toCamel(fd
					.getFieldNamePhysics()), fd.getFieldNamePhysics()));
		}
		sb.append("\t}\n");

		return sb.toString();
	}

	private String getSetterGetterMethod(String table_name, TableValuesDef tvd,
			String class_name) {

		StringBuffer sb = new StringBuffer();
		TableDef td = TableDefCache.get(table_name);

		for (Iterator it = td.getFieldList().iterator(); it.hasNext();) {
			FieldDef fd = (FieldDef) it.next();
			// sb.append(getSetterMethod(fd));
			sb.append(getGetterMethod(fd));
		}
		return sb.toString();
	}

	private String getEnumVar(String table_name, String field_name) {

		StringBuffer sb = new StringBuffer();

		TableDef td = TableDefCache.get(table_name);
		FieldDef fd = null;
		for (Iterator it = td.getFieldList().iterator(); it.hasNext();) {
			FieldDef fdtmp = (FieldDef) it.next();
			if (fdtmp.getFieldNamePhysics().equalsIgnoreCase(field_name)) {
				fd = fdtmp;
				break;
			}
		}
		if (fd == null) {
			return "";
		}

		// �R�����g
		sb.append("\t/** " + fd.getFieldNameLogic() + " */\n");

		// �ϐ���`
		sb.append("\tpublic final "
				+ JavaFieldTypes.getTypeByXLS(fd.getTypeXls()) + " "
				+ NameUtil.toCamel(fd.getFieldNamePhysics()) + ";\n\n");

		return sb.toString();
	}

	/**
	 * getSetterMethod<BR>
	 * setter ���\�b�h�L�q���擾
	 * 
	 * @param fd
	 * @return
	 */
	private String getSetterMethod(FieldDef fd) {

		return "\t/**\n" + "\t * " + fd.getFieldNameLogic() + "�ݒ�<BR>\n"
				+ "\t * @param " + fd.getFieldNamePhysics() + " "
				+ fd.getFieldNameLogic() + "\n" + "\t */\n"
				+ "\tpublic void set"
				+ StringUtils.capitalise(fd.getFieldNamePhysics()) + "("
				+ JavaFieldTypes.getTypeByXLS(fd.getTypeXls()) + " "
				+ fd.getFieldNamePhysics() + ") {\n" + "\t  " + "this."
				+ fd.getFieldNamePhysics() + " = " + fd.getFieldNamePhysics()
				+ ";\n" + "\t}\n\n";
	}

	/**
	 * getGetterMethod<BR>
	 * getter ���\�b�h�L�q���擾
	 * 
	 * @param fd
	 * @return
	 */
	private String getGetterMethod(FieldDef fd) {

		return "\t/**\n" + "\t * " + fd.getFieldNameLogic() + "�擾<BR>\n"
				+ "\t * @return " + fd.getFieldNameLogic() + "\n" + "\t */\n"
				+ "\tpublic " + JavaFieldTypes.getTypeByXLS(fd.getTypeXls())
				+ " get" + StringUtils.capitalise(fd.getFieldNamePhysics())
				+ "() {\n" + "\t  " + "return this." + fd.getFieldNamePhysics()
				+ ";\n" + "\t}\n\n";
	}

	/**
	 * getClassVar<BR>
	 * �N���X�ϐ��L�q���擾
	 * 
	 * @param fd
	 * @return
	 */
	private String getClassVar(FieldDef fd) {
		StringBuffer sb = new StringBuffer();

		// �R�����g
		sb.append("\t/** " + fd.getFieldNameLogic() + " */\n");

		// �ϐ���`
		sb.append("\tprivate " + JavaFieldTypes.getTypeByXLS(fd.getTypeXls())
				+ " " + fd.getFieldNamePhysics() + ";\n\n");

		return sb.toString();
	}

}
