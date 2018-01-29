package com.ams.poi.xls2sql.jerseymaker;

import java.util.ArrayList;
import java.util.List;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.util.NameUtil;

/**
 * <p>
 * �v���W�F�N�g��: POI_DBUtil
 * </p>
 * <p>
 * �^�C�g��: EnumSourceMaker
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
public class JerseySourceMaker {

	private String app_cd_name;

	/**
	 * @param m
	 * @param td 
	 * @return
	 */
	public String getSource(WebAPIMethod m, TableDef td) {
		StringBuilder source = new StringBuilder();

		// import
		source.append(getImport());
		
		// �t�@�C���擪�R�����g
		source.append(getHeaderComment(m));

		// �N���X��`
		source.append(getClassSource(m, td));

		return source.toString();
	}

	private String getImport() {
		return
		"import javax.ws.rs.*;\r\n"+
		"import javax.ws.rs.core.*;\r\n"+
		"import javax.xml.bind.annotation.XmlRootElement;\r\n"+
		"\r\n"+
		"import jp.co.powerbeans.powerql.POQLManager;\r\n"+
		"import jp.co.powerbeans.powerql.dao.POQLDAO;\r\n"+
		"import POQLManagerFactory;\r\n\r\n";
	}

	/**
	 * getHeaderComment<BR>
	 * �\�[�X�w�b�_�R�����g�擾
	 * 
	 * @param td
	 * @return
	 */
	private String getHeaderComment(WebAPIMethod m) {
		String s = "/**\n"
				+ " * <p>�^�C�g��: "
				+ m.getTableNameLogic()
				+ "Jersey WEB API�e�[�u���pEnum</p>\n"
				+ " * <p>����: "
				+ m.getTableNameLogic()
				+ "("
				+ m.getTableNamePhysics()
				+ ")�e�[�u���ƑΉ�����WebAPI</p>\n"
				+ " * @author \n"
				+ " * @version $Id: JerseySourceMaker.java 100 2009-11-27 08:54:08Z amonden $\n"
				+ " */\n";

		return s;
	}

	/**
	 * getClassSource<BR>
	 * �\�[�X�R�[�h�擾
	 * @param td 
	 * 
	 * @param td
	 * @return
	 */
	private String getClassSource(WebAPIMethod m, TableDef td) {
		StringBuffer sb = new StringBuffer();
		final String table_name = m.getTableNamePhysics();
		final String class_name = NameUtil.toCamel(m.getTableNamePhysics() + "Resource");
		final String bean_class = NameUtil.toCamel(m.getTableNamePhysics());
		final String api_path = m.getTableNamePhysics().toLowerCase();
		List<String> def_list = new ArrayList<String>();

		// 1. �N���X��` start
		sb.append("@Path(\"/"+ api_path +"\")\n");
		sb.append("public class " + class_name + " {\n");
		sb.append("private static final String PRODUCE = MediaType.APPLICATION_XML+\";charset=UTF-8\";\n\n");
		
		// 2. Get 
		if (m.isMethodGet()) {
			sb.append("	@GET\r\n");
			sb.append("	@Produces({PRODUCE})\r\n");
			sb.append(String.format("	public %s get(\r\n", bean_class));
			sb.append("			@DefaultValue(\"\") @QueryParam(\"id\") String id\r\n");
			sb.append("			) {\r\n");
			sb.append("		POQLManager poqlManager = POQLManagerFactory.getManager();\r\n");
			sb.append(String.format("		POQLDAO dao = poqlManager.createDAO(%s.class);\r\n", bean_class));
			sb.append(String.format("		return (%s)dao.findByPrimaryKey(id);\r\n", bean_class));
			sb.append("	}\r\n\r\n");
		}

		// 3. POST
		if (m.isMethodPost()) {
			sb.append("	@POST // for CREATE\r\n");
			sb.append("	@Produces({PRODUCE})\r\n");
			sb.append("	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)\r\n");
			sb.append("	public Result craete(\r\n");
			
			String com = "";
			for (FieldDef f : td.getFieldList()) {
				if (f.isPrimaryKey()) {continue;}
				sb.append(String.format("			%s@DefaultValue(\"\") @FormParam(\"%s\") %s %s\r\n",
						com, NameUtil.toCamel(f.getFieldNamePhysics()), f.getTypeJava(), NameUtil.toCamel(f.getFieldNamePhysics())));
				if (com.length() == 0) com = ",";
			}
			sb.append("			) {\r\n");
			sb.append("		POQLManager poqlManager = POQLManagerFactory.getManager();\r\n");
			sb.append(String.format("		POQLDAO dao = poqlManager.createDAO(%s.class);\r\n", bean_class));
			sb.append(String.format("		%s obj = new %s();\r\n", bean_class, bean_class));
			
			for (FieldDef f : td.getFieldList()) {
				if (f.isPrimaryKey()) continue; // pk �͖���
				sb.append(String.format("		obj.set%s(%s);\r\n", NameUtil.toCamel(f.getFieldNamePhysicsTopUpper()),
						NameUtil.toCamel(f.getFieldNamePhysics())  ));
			}
			
			sb.append("		dao.create(obj);\r\n");
			sb.append("		return obj;\r\n");
			sb.append("	}\r\n\r\n");
		}
	
		// 4. PUT (update)
		if (m.isMethodPut()) {
			sb.append("	@PUT // for UPDATE\r\n");
			sb.append("	@Produces({PRODUCE})\r\n");
			sb.append("	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)\r\n");
			sb.append("	public Result update(\r\n");
			String com = "";
			for (FieldDef f : td.getFieldList()) {
				if ("updateDate".equals(NameUtil.toCamel(f.getFieldNamePhysics()))) {continue;}
				sb.append(String.format("			%s@DefaultValue(\"\") @FormParam(\"%s\") %s %s\r\n",
						com, NameUtil.toCamel(f.getFieldNamePhysics()), f.getTypeXls(), f.getTypeJava(), NameUtil.toCamel(f.getFieldNamePhysics())));
				if (com.length() == 0) com = ",";
			}
			sb.append("			) {\r\n");
			sb.append(String.format("		if (%s == 0) {\r\n", NameUtil.toCamel(td.getId().getFieldNamePhysics())) );
			sb.append("			return getResultNum(0);\r\n");
			sb.append("	}\r\n");
			sb.append("		POQLManager poqlManager = POQLManagerFactory.getManager();\r\n");
			sb.append(String.format("		POQLDAO dao = poqlManager.createDAO(%s.class);\r\n", bean_class));
			sb.append(String.format("		%s obj = new %s();\r\n", bean_class, bean_class));
			
			for (FieldDef f : td.getFieldList()) {
				if ("updateDate".equals(NameUtil.toCamel(f.getFieldNamePhysics()))) {continue;}
				sb.append(String.format("		obj.set%s(%s);\r\n", NameUtil.toCamel(f.getFieldNamePhysicsTopUpper()),
				NameUtil.toCamel(f.getFieldNamePhysics())  ));
			}
			
			sb.append("		dao.update(obj);\r\n");
			sb.append("		return obj;\r\n");
			sb.append("	}\r\n\r\n");
		}

		// 5. Delete 
		if (m.isMethodDelete()) {
			sb.append("	@DELETE\r\n");
			sb.append("	@Produces({PRODUCE})\r\n");
			sb.append("	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)\r\n");
			sb.append("	public Result delete(\r\n");
			sb.append("			@DefaultValue(\"\") @QueryParam(\"id\") String id\r\n");
			sb.append("			) {\r\n");
			sb.append("		POQLManager poqlManager = POQLManagerFactory.getManager();\r\n");
			sb.append(String.format("		POQLDAO dao = poqlManager.createDAO(%s.class);\r\n", bean_class));
			sb.append("		return getResultNum(dao.removeByPrimaryKey(id));\r\n");
			sb.append("	}\r\n\r\n");
		}
		
		// 6. Inner class
		
		sb.append("	/**\r\n");
		sb.append("	 * �����ԋp�pObject�𐶐����ĕԂ�\r\n");
		sb.append("	 * @param num ����\r\n");
		sb.append("	 * @return ����Object\r\n");
		sb.append("	 */\r\n");
		sb.append("	private Result getResultNum(int num) {\r\n");
		sb.append("		Result r = new Result();\r\n");
		sb.append("		r.count = num;\r\n");
		sb.append("		return r;\r\n");
		sb.append("	}\r\n");
		sb.append("	\r\n");
		sb.append("	/**\r\n");
		sb.append("	 * ����Object\r\n");
		sb.append("	 */\r\n");
		sb.append("	@XmlRootElement\r\n");
		sb.append("	public static class Result {\r\n");
		sb.append("		public int count;\r\n");
		sb.append("	}\r\n");
		
		// �N���X��` end
		sb.append("}");

		return sb.toString();
	}
}
