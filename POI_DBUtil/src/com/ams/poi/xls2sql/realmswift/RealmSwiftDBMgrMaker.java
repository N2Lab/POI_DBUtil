/**
 * 
 */
package com.ams.poi.xls2sql.realmswift;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;

/**
 * RealmSwift �́@�����f�[�^�쐬�������쐬(�A�v���������s�p)
 * 
 * @author Akihiko MONDEN
 *
 */
public class RealmSwiftDBMgrMaker {

	private StringBuilder headerImport;
	private StringBuilder headerInterface;
	private StringBuilder impl;

	/**
	 * 
	 */
	public RealmSwiftDBMgrMaker() {
		super();
		this.headerImport = new StringBuilder();
		this.headerInterface = new StringBuilder();
		this.impl = new StringBuilder();
		
		addHeaderHead();
		addHeaderImpl();
	}

	/**
	 * �w�b�_�t�@�C���̃w�b�_���o��
	 */
	private void addHeaderHead() {
		// 1. header �� import�܂�
		{
			StringBuilder s = new StringBuilder();
			s.append(getFileComment("DBMgr.h"));
			headerImport.append(s.toString());
			headerImport.append("#import \"AmsDBMgr.h\"\n\n");
		}
		// 2. header �� interface
		{
			StringBuilder s = new StringBuilder();
			s.append(""
					+ "@interface DBMgr : AmsDBMgr {\n"
					+ "}\n"
					+ "\n"
					);
			headerInterface.append(s.toString());
		}
	}

	/**
	 * �����t�@�C���̏����������o��
	 */
	private void addHeaderImpl() {
		
		StringBuilder s = new StringBuilder(); 
		s.append("// addHeaderImpl");
//		s.append("" 
//				+ getFileComment("DBMgr.m")
//				+ "\n"
//				+ "#import \"DBMgr.h\"\n"
//				+ "\n"
//				+ "@implementation DBMgr\n"
//				+ "\n");
		impl.append(s.toString());
	}

	/**
	 * �t�@�C���R�����g��Ԃ�
	 * @param td
	 * @return
	 */
	private String getFileComment(String filename) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		String ymd = df.format(new Date());
		return
		"//\n"
		+ "//  "+filename + "\n"
		+ "//\n"
		+  "//  Created by akihiko monden on " + ymd + ".\n"
		+ "//  Copyright 2009 __MyCompanyName__. All rights reserved.\n"
		+ "//" + "\n"
		+ "\n";
//		+ "import RealmSwift" + "\n";
	}

	/**
	 * Table td �̒�`�Ɋ�Â��Ċe���\�b�h�̃\�[�X������ɏo��
	 * @param td
	 */
	public void addTableDef(TableDef td) {
		// 1. Header
		// 1.1 import
		{
			this.headerImport.append("import RealmSwift" + "\n");
		}
		
		// 1.2 ���\�b�h��`
//		{
//			StringBuilder s = new StringBuilder();
//			
//			// TODO xxxById, findXxxAll, findXxx (2�ڂ̍���,�R�����g�o��), create, insert, update
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��ID�Ŏ擾\n");
//			s.append(getMethodIf_xxxById(td) + ";\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��S���擾\n");
//			s.append(getMethodIf_findXxxAll(td) + ";\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") �������w��擾\n");
//			s.append(getMethodIf_findXxxByParam(td) + ";\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��ǉ�\n");
//			s.append(getMethodIf_insertXxx(td) + ";\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ���X�V\n");
//			s.append(getMethodIf_updateXxx(td) + ";\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��ǉ�or�X�V\n");
//			s.append(getMethodIf_updateOrInsertXxx(td) + ";\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��ID�ō폜\n");
//			s.append(getMethodIf_deleteXxxById(td) + ";\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��S���폜\n");
//			s.append(getMethodIf_deleteXxxAll(td) + ";\n");
//			s.append("\n");
//			
//			this.headerInterface.append(s.toString());
//		}
		
		// 2. Impl (���\�b�h����)
//		{
//			StringBuilder s = new StringBuilder();
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��ID�Ŏ擾\n");
//			s.append(getMethodIf_xxxById(td) + " {\n");
//			s.append(getMethodImpl_xxxById(td));
//			s.append("}\n");
//			s.append("\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��S���擾\n");
//			s.append(getMethodIf_findXxxAll(td) + " {\n");
//			s.append(getMethodImpl_findXxxAll(td));
//			s.append("}\n");
//			s.append("\n");
//
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") �������w��擾\n");
//			s.append(getMethodIf_findXxxByParam(td) + " {\n");
//			s.append("/* " + getMethodImpl_findXxxByParam(td));
//			s.append("} */\n");
//			s.append("\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��ǉ�\n");
//			s.append(getMethodIf_insertXxx(td) + " {\n");
//			s.append(getMethodImpl_insertXxx(td));
//			s.append("}\n");
//			s.append("\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ���X�V\n");
//			s.append(getMethodIf_updateXxx(td) + " {\n");
//			s.append(getMethodImpl_updateXxx(td));
//			s.append("}\n");
//			s.append("\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��ǉ� or �X�V\n");
//			s.append(getMethodIf_updateOrInsertXxx(td) + " {\n");
//			s.append(getMethodImpl_updateOrInsertXxx(td));
//			s.append("}\n");
//			s.append("\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��ID�ō폜\n");
//			s.append(getMethodIf_deleteXxxById(td) + " {\n");
//			s.append(getMethodImpl_deleteXxxById(td));
//			s.append("}\n");
//			s.append("\n");
//			
//			s.append("// " + td.getTableNameLogic() + "(" + td.getTableNamePhysicsTopUpper() + ") ��S���폜\n");
//			s.append(getMethodIf_deleteXxxAll(td) + " {\n");
//			s.append(getMethodImpl_deleteXxxAll(td));
//			s.append("}\n");
//			s.append("\n");
//			
//			this.impl.append(s.toString());
//		}
	}



	/**
	 * ���\�b�h��` xxxById ���擾
	 * @param td
	 * @return
	 */
	private String getMethodIf_xxxById(TableDef td) {
		return "- ("+td.getTableNamePhysicsTopUpper()+"*)"  + td.getTableNamePhysics() + "ById:(NSInteger)_id";
	}

	/**
	 * ���\�b�h���� xxxById ���擾
	 * @param td
	 * @return
	 */
	private String getMethodImpl_xxxById(TableDef td) {
		final String table = td.getTableNamePhysics();
		
		return "	NSString* sql = @\"SELECT "+getAllColumnForSelect(td)+" from "+table+" \";\n"
		+ "	NSString* sql_where_base = [NSString stringWithFormat:@\" where 1=1 \"];\n"
		+ "	NSString* sql_where = @\"\";\n"
		+ "	\n"
		+ "	{\n"
		+ "		NSString* str = [NSString stringWithFormat:@\" AND "+ td.getId().getFieldNamePhysics() +"=%d \", (int)_id];\n"
		+ "		sql_where = [sql_where stringByAppendingString:str];\n"
		+ "	}\n"
		+ "	\n"
		+ "	if ([sql_where length] == 0) {\n"
		+ "		return nil;\n"
		+ "	}\n"
		+ "	sql_where_base = [sql_where_base stringByAppendingString:sql_where];\n"
		+ "	sql = [sql stringByAppendingString:sql_where_base];\n"
		+ "	sql = [sql stringByAppendingString:@\" order by "+td.getId().getFieldNamePhysics()+" \"];\n"
		+ "	\n"
		+ "	NSLog(sql,nil);\n"
		+ "	const char *exe_sql = [sql UTF8String];\n"
		+ "	sqlite3_stmt *statement;\n"
		+ "	\n"
		+ "	if (sqlite3_prepare_v2(database, exe_sql, -1, &statement, NULL) == SQLITE_OK) {\n"
		+ "		\n"
		+ "		while (sqlite3_step(statement) == SQLITE_ROW) {\n"
		+ "			"+td.getTableNamePhysicsTopUpper()	+" * m = ["+td.getTableNamePhysicsTopUpper()+" alloc];\n"
		+ "			\n"
		+ "			int c = 0;\n"
		+ "			\n"
		+ getResultSetFetchAll(td)
//		+ "			m._id = sqlite3_column_int(statement, c++);\n"
//		+ "			m.area_code = sqlite3_column_int(statement, c++);\n"
//		+ "			m.code = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, c++)] copy];\n"
		+ "	        sqlite3_finalize(statement);\n"
		+ "			return m;\n"
		+ "		}\n"
		+ "	} else {\n"
		+ "		NSLog(@\"SQL Error. %@\", sql);\n"
		+ "		NSLog(@\"%s\", sqlite3_errmsg(database));\n"
		+ "	}\n"
		+ "	sqlite3_finalize(statement);\n"
		+ "	\n"
		+ "	return nil;\n";
	}
	
	/**
	 * �������ʂ�model�Ɋi�[���鏈����Ԃ�
	 * @param td
	 * @return
	 */
	private String getResultSetFetchAll(TableDef td) {
//		+ "			m._id = sqlite3_column_int(statement, c++);\n"
//		+ "			m.area_code = sqlite3_column_int(statement, c++);\n"
//		+ "			m.code = [[NSString stringWithUTF8String:(char *)sqlite3_column_text(statement, c++)] copy];\n"
		
		StringBuilder s = new StringBuilder();
		
		for (FieldDef fd : td.getFieldList()) {
			s.append("			{m."+fd.getFieldNamePhysics()+" = "+ RealmSwiftFieldType.getFetchCall(fd) +";}\n");
		}
		
		return s.toString();
	}

	/**
	 * select �p�S�J�����̃J���}��؂��Ԃ�
	 * @param td
	 * @return
	 */
	private String getAllColumnForSelect(TableDef td) {
		StringBuilder s = new StringBuilder();
		
		// id,area_code,code,input_date,hospital_name,kamoku,service,sisetu,kiki,zip,pref,addr,tel,keiei,normal_beds,rest_beds,rest_care_beds,rest_med_beds,mental_beds,tuber_beds,infect_beds,fax,name_kana,name_hira,open_ym,notice,url,health_worker_sum,doctor_sum,dentist_sum,disorder,standerd,special,inpatient,outpatient,inmate_days,lat,lng,exti1,exti2,exts1,exts2
		int c = 0;
		for (FieldDef fd : td.getFieldList()) {
			if (c ++ > 0) {
				s.append(",");
			}
			s.append(fd.getFieldNamePhysics());
		}
		return s.toString();
	}

	/**
	 * pk�ȊO�̑S�J�����̃J���}��؂��Ԃ�
	 * @param td
	 * @return
	 */
	private String getNotIdColumns(TableDef td) {
		StringBuilder s = new StringBuilder();
		
		// area_code,code,input_date,hospital_name,kamoku,service,sisetu,kiki,zip,pref,addr,tel,keiei,normal_beds,rest_beds,rest_care_beds,rest_med_beds,mental_beds,tuber_beds,infect_beds,fax,name_kana,name_hira,open_ym,notice,url,health_worker_sum,doctor_sum,dentist_sum,disorder,standerd,special,inpatient,outpatient,inmate_days,lat,lng,exti1,exti2,exts1,exts2
		int c = 0;
		for (FieldDef fd : td.getFieldList()) {
			if (fd.isPrimaryKey()) {
				continue;
			}
			if (c ++ > 0) {
				s.append(",");
			}
			s.append(fd.getFieldNamePhysics());
		}
		return s.toString();
	}
	
	/**
	 * pk�ȊO�̃J�����ƈ�v���� ? �ϐ��L����,�A���������Ԃ�
	 * @param td
	 * @return
	 */
	private String getVarMarkByNotIdColumnCount(TableDef td) {
		StringBuilder s = new StringBuilder();
		
		// area_code,code,input_date,hospital_name,kamoku,service,sisetu,kiki,zip,pref,addr,tel,keiei,normal_beds,rest_beds,rest_care_beds,rest_med_beds,mental_beds,tuber_beds,infect_beds,fax,name_kana,name_hira,open_ym,notice,url,health_worker_sum,doctor_sum,dentist_sum,disorder,standerd,special,inpatient,outpatient,inmate_days,lat,lng,exti1,exti2,exts1,exts2
		int c = 0;
		for (FieldDef fd : td.getFieldList()) {
			if (fd.isPrimaryKey()) {
				continue;
			}
			if (c ++ > 0) {
				s.append(",");
			}
			s.append("?");
		}
		return s.toString();
	}


	/**
	 * ���\�b�h��` findXxxAll ��ǉ�
	 * @param td
	 * @return
	 */
	private String getMethodIf_findXxxAll(TableDef td) {
		return "- (NSMutableArray*)" + "find" + td.getTableNamePhysicsTopUpper() + "All";
	}

	/**
	 * ���\�b�h���� findXxxAll ���擾
	 * @param td
	 * @return
	 */
	private String getMethodImpl_findXxxAll(TableDef td) {
		final String table = td.getTableNamePhysics();
		
		return "	NSMutableArray* results = [NSMutableArray array];\n" +
				"	NSString* sql = @\"SELECT "+getAllColumnForSelect(td)+" from "+table+" \";\n"
		+ "	NSString* sql_where_base = [NSString stringWithFormat:@\" where 1=1 \"];\n"
		+ "	NSString* sql_where = @\"\";\n"
		+ "	\n"
		+ "	// {\n"
		+ "	// 	NSString* str = [NSString stringWithFormat:@\" AND "+ td.getId().getFieldNamePhysics() +"=%d \", (int)_id];\n"
		+ "	// 	sql_where = [sql_where stringByAppendingString:str];\n"
		+ "	// }\n"
		+ "	\n"
		+ "	// if ([sql_where length] == 0) {\n"
		+ "	// 	return nil;\n"
		+ "	// }\n"
		+ "	sql_where_base = [sql_where_base stringByAppendingString:sql_where];\n"
		+ "	sql = [sql stringByAppendingString:sql_where_base];\n"
		+ "	sql = [sql stringByAppendingString:@\" order by "+td.getId().getFieldNamePhysics()+" \"];\n"
		+ "	\n"
		+ "	NSLog(sql,nil);\n"
		+ "	const char *exe_sql = [sql UTF8String];\n"
		+ "	sqlite3_stmt *statement;\n"
		+ "	\n"
		+ "	if (sqlite3_prepare_v2(database, exe_sql, -1, &statement, NULL) == SQLITE_OK) {\n"
		+ "		\n"
		+ "		while (sqlite3_step(statement) == SQLITE_ROW) {\n"
		+ "			"+td.getTableNamePhysicsTopUpper()	+" * m = ["+td.getTableNamePhysicsTopUpper()+" alloc];\n"
		+ "			\n"
		+ "			int c = 0;\n"
		+ "			\n"
		+ getResultSetFetchAll(td)
		+ "			[results addObject:m];\n"
		+ "		}\n"
		+ "	} else {\n"
		+ "		NSLog(@\"SQL Error. %@\", sql);\n"
		+ "		NSLog(@\"%s\", sqlite3_errmsg(database));\n"
		+ "	}\n"
		+ "	sqlite3_finalize(statement);\n"
		+ "	\n"
		+ "	return results;\n";
	}
	/**
	 * @param td
	 * @return
	 */
	private String getMethodIf_findXxxByParam(TableDef td) {
		return "// - (NSMutableArray*)" + "find" + td.getTableNamePhysicsTopUpper() + "ByParam:(NSString*)name param2:(NSString*)name2";
	}
	
	/**
	 * ���\�b�h���� findXxxByParam ���擾
	 * @param td
	 * @return
	 */
	private String getMethodImpl_findXxxByParam(TableDef td) {
		final String table = td.getTableNamePhysics();
		
		return "	NSMutableArray* results = [NSMutableArray array];\n" +
				"	NSString* sql = @\"SELECT "+getAllColumnForSelect(td)+" from "+table+" \";\n"
		+ "	NSString* sql_where_base = [NSString stringWithFormat:@\" where 1=1 \"];\n"
		+ "	NSString* sql_where = @\"\";\n"
		+ "	\n"
		+ "	{\n"
		+ "		NSString* str = [NSString stringWithFormat:@\" AND "+ td.getId().getFieldNamePhysics() +"=%d \", (int)_id];\n"
		+ "		sql_where = [sql_where stringByAppendingString:str];\n"
		+ "	}\n"
		+ "	\n"
		+ "	// 1. nameKeyword\n"
		+ "	if ([nameKeyword length] > 0)  {\n"
		+ "		NSString* str = [NSString stringWithFormat:@\" AND (hospital_name Like '%%%@%%' OR name_kana Like '%%%@%%' OR name_hira Like '%%%@%%') \", nameKeyword, nameKeyword, nameKeyword];\n"
		+ "		sql_where = [sql_where stringByAppendingString:str];\n"
		+ "	}\n"
		+ "	\n"
		+ "	// 2. sinryouKamoku\n"
		+ "	if ([sinryouKamoku length] > 0)  {\n"
		+ "		NSString* str = [NSString stringWithFormat:@\" AND (kamoku Like '%%%@%%') \", sinryouKamoku];\n"
		+ "		sql_where = [sql_where stringByAppendingString:str];\n"
		+ "	}\n"
		+ "	\n"
		+ "	// 3. iryouService\n"
		+ "	if ([iryouService length] > 0)  {\n"
		+ "		NSString* str = [NSString stringWithFormat:@\" AND (service Like '%%%@%%') \", iryouService];\n"
		+ "		sql_where = [sql_where stringByAppendingString:str];\n"
		+ "	}\n"
		+ "	\n"
		+ "	// 4. prefName\n"
		+ "	if ([prefName length] > 0)  {\n"
		+ "		NSString* str = [NSString stringWithFormat:@\" AND pref = '%@' \", prefName];\n"
		+ "		sql_where = [sql_where stringByAppendingString:str];\n"
		+ "	}\n"
		+ "	\n"
		+ "	// 5. cityName\n"
		+ "	if ([cityName length] > 0)  {\n"
		+ "		NSString* str = [NSString stringWithFormat:@\" AND (addr Like '%@%%') \", cityName];\n"
		+ "		sql_where = [sql_where stringByAppendingString:str];\n"
		+ "	}\n"
		+ "	\n"
		+ "	NSLog(sql,nil);"
		+ "	if ([sql_where length] == 0) {\n"
		+ "		return nil;\n"
		+ "	}\n"
		+ "	sql_where_base = [sql_where_base stringByAppendingString:sql_where];\n"
		+ "	sql = [sql stringByAppendingString:sql_where_base];\n"
		+ "	sql = [sql stringByAppendingString:@\" order by "+td.getId().getFieldNamePhysics()+" \"];\n"
		+ "	\n"
		+ "	const char *exe_sql = [sql UTF8String];\n"
		+ "	sqlite3_stmt *statement;\n"
		+ "	\n"
		+ "	if (sqlite3_prepare_v2(database, exe_sql, -1, &statement, NULL) == SQLITE_OK) {\n"
		+ "		\n"
		+ "		while (sqlite3_step(statement) == SQLITE_ROW) {\n"
		+ "			"+td.getTableNamePhysicsTopUpper()	+" * m = ["+td.getTableNamePhysicsTopUpper()+" alloc];\n"
		+ "			\n"
		+ "			int c = 0;\n"
		+ "			\n"
		+ getResultSetFetchAll(td)
		+ "			[results addObject:m];\n"
		+ "		}\n"
		+ "	} else {\n"
		+ "		NSLog(@\"SQL Error. %@\", sql);\n"
		+ "		NSLog(@\"%s\", sqlite3_errmsg(database));\n"
		+ "	}\n"
		+ "	sqlite3_finalize(statement);\n"
		+ "	\n"
		+ "	return results;\n";
	}
	
	/**
	 * ���\�b�h��` insertXxx ��ǉ�
	 * @param td
	 * @return
	 */
	private String getMethodIf_insertXxx(TableDef td) {
		return "- ("+td.getTableNamePhysicsTopUpper()+"*)" + "insert" + td.getTableNamePhysicsTopUpper() + ":(" + td.getTableNamePhysicsTopUpper() + "*) obj";
	}
	
	/**
	 * ���\�b�h���� insertXxx ���擾
	 * @param td
	 * @return
	 */
	private String getMethodImpl_insertXxx(TableDef td) {
		final String table = td.getTableNamePhysics();
		
		return "	const char *sql = \"INSERT INTO "+table+" ("+getNotIdColumns(td)+")VALUES("+getVarMarkByNotIdColumnCount(td)+");\";\n"
		+ "	sqlite3_stmt *statement;\n"
		+ "	"+td.getTableNamePhysicsTopUpper()+"* m = obj;\n"
		+ "	if (sqlite3_prepare_v2(database, sql, -1, &statement, NULL) == SQLITE_OK) {\n"
		+ "		\n"
		+ "		int c = 1;\n"
		+ getBindValuesExceptId(td)
//		+ "		sqlite3_bind_text(statement, c++, [m.contents UTF8String],-1, SQLITE_TRANSIENT);\n"
//		+ "		sqlite3_bind_int(statement, c++, m.disp_order);\n"
		+ "		\n"
		+ "		int result = sqlite3_step(statement);\n"
		+ "		if (result != SQLITE_DONE) {\n"
		+ "			NSLog(@\"SQL INSERT ERROR\");\n"
		+ "			NSLog(@\"Error: failed to insert into the database with message '%s'.\", sqlite3_errmsg(database));\n"
		+ "		} else {\n"
		+ "			m."+td.getId().getFieldNamePhysics()+" = sqlite3_last_insert_rowid(database);\n"
		+ "			NSLog(@\"SQL INSERT NEW OK. id=%d\", m."+td.getId().getFieldNamePhysics()+");\n"
		+ "		}\n"
		+ "		sqlite3_reset(statement);\n"
		+ "		\n"
		+ "	} else {\n"
		+ "		NSLog(@\"prepare error. %s\",  sqlite3_errmsg(database));\n"
		+ "	}\n"
		+ "	// \"Finalize\" the statement - releases the resources associated with the statement.\n"
		+ "	sqlite3_finalize(statement);\n"
		+ "	\n"
		+ "	return m;\n";
	}

	/**
	 * ID�ȊO�̒l�� sqlite3_bind_xxxx �R�[�h��Ԃ�
	 * @param td
	 * @return
	 */
	private String getBindValuesExceptId(TableDef td) {
		StringBuilder s = new StringBuilder();
		
		for (FieldDef fd : td.getFieldList()) {
			if (fd.isPrimaryKey()) {
				continue;
			}
			s.append("\t\t" + RealmSwiftFieldType.getBindValueCall(fd) + ";\n");
		}
		
		return s.toString();
	}

	/**
	 * ���\�b�h��` updateXxx ��ǉ�
	 * @param td
	 * @return
	 */
	private String getMethodIf_updateXxx(TableDef td) {
		return "- ("+td.getTableNamePhysicsTopUpper()+"*)" + "update" + td.getTableNamePhysicsTopUpper() + ":(" + td.getTableNamePhysicsTopUpper() + "*) obj";
	}

	/**
	 * ���\�b�h���� updateXxx 
	 * @param td
	 * @return
	 */
	private String getMethodImpl_updateXxx(TableDef td) {
		final String table = td.getTableNamePhysics();
		
		return "	const char *sql = \"UPDATE  "+table+" SET "+getUpdateColumns(td)+" WHERE "+td.getId().getFieldNamePhysics()+" = ? ;\";\n"
		+ "	sqlite3_stmt *statement;\n"
		+ "	"+td.getTableNamePhysicsTopUpper()+"* m = obj;\n"
		+ "	if (sqlite3_prepare_v2(database, sql, -1, &statement, NULL) == SQLITE_OK) {\n"
		+ "		\n"
		+ "		int c = 1;\n"
		+ getBindValuesExceptId(td)
//		+ "		sqlite3_bind_text(statement, c++, [m.contents UTF8String],-1, SQLITE_TRANSIENT);\n"
//		+ "		sqlite3_bind_int(statement, c++, m.disp_order);\n"
		+ "		sqlite3_bind_int(statement, c++, m."+td.getId().getFieldNamePhysics()+");\n"
		+ "		\n"
		+ "		int result = sqlite3_step(statement);\n"
		+ "		if (result == SQLITE_ERROR) {\n"
		+ "			NSLog(@\"SQL INSERT ERROR\");\n"
		+ "			NSLog(@\"Error: failed to update with message '%s'.\", sqlite3_errmsg(database));\n"
		+ "		} else {\n"
		+ "			NSLog(@\"SQL UPDATE OK. id=%d\", m."+td.getId().getFieldNamePhysics()+");\n"
		+ "		}\n"
		+ "		sqlite3_reset(statement);\n"
		+ "		\n"
		+ "	} else {\n"
		+ "		NSLog(@\"prepare error. %s\",  sqlite3_errmsg(database));\n"
		+ "	}\n"
		+ "	// \"Finalize\" the statement - releases the resources associated with the statement.\n"
		+ "	sqlite3_finalize(statement);\n"
		+ "	\n"
		+ "	return m;\n";
	}

	/**
	 * UPDATE SET ����𐶐�����Ԃ�
	 * @param td
	 * @return
	 */
	private String getUpdateColumns(TableDef td) {
		StringBuilder s = new StringBuilder();
		
		int c = 0;
		for (FieldDef fd : td.getFieldList()) {
			if (fd.isPrimaryKey()) {
				continue;
			}
			if (c++ > 0) {
				s.append(",");
			}
			s.append(fd.getFieldNamePhysics()  + "=?");
		}
		
		return s.toString();
	}

	/**
	 * ���\�b�h��` updateOrInsertXxx ��ǉ�
	 * @param td
	 * @return
	 */
	private String getMethodIf_updateOrInsertXxx(TableDef td) {
		return "- ("+td.getTableNamePhysicsTopUpper()+"*)" + "updateOrInsert" + td.getTableNamePhysicsTopUpper() + ":(" + td.getTableNamePhysicsTopUpper() + "*) obj";
	}
	
	/**
	 * ���\�b�h���� updateOrInsertXxx 
	 * @param td
	 * @return
	 */
	private String getMethodImpl_updateOrInsertXxx(TableDef td) {
		final String table = td.getTableNamePhysics();
		final String id = td.getId().getFieldNamePhysics();
		
		// ���݂���� update
		// ���݂��Ȃ���� insert
		return "	" +
				td.getTableNamePhysicsTopUpper() + "* chkobj = [self " + td.getTableNamePhysics() + "ById:obj." + id + "];\n"  +
				"	if (chkobj && chkobj." + id + " == obj." + id + ") {\n" +
				"		return [self update"+td.getTableNamePhysicsTopUpper()+":obj];\n" +
				"	} else {\n" +
				"		return [self insert"+td.getTableNamePhysicsTopUpper()+":obj];\n" +
				"	}\n" +
				"";
	}

	/**
	 * ���\�b�h��` deleteXxxById ��ǉ�
	 * @param td
	 * @return
	 */
	private String getMethodIf_deleteXxxById(TableDef td) {
		return "- (int)" + "delete" + td.getTableNamePhysicsTopUpper() + "ById:(NSInteger)_id";
	}
	
	/**
	 * ���\�b�h���� deleteXxxById ��ǉ�
	 * @param td
	 * @return
	 */
	private String getMethodImpl_deleteXxxById(TableDef td) {
		final String table = td.getTableNamePhysics();
		
		return 
		"	int num = 0;\n"
		+ "	const char *sql = \"DELETE FROM "+table+" where "+td.getId().getFieldNamePhysics()+" = ?;\";\n"
		+ "	sqlite3_stmt *statement;\n"
		+ "	if (sqlite3_prepare_v2(database, sql, -1, &statement, NULL) == SQLITE_OK) {\n"
		+ "		\n"
		+ "		sqlite3_bind_int(statement, 1, _id);\n"
		+ "		int result = sqlite3_step(statement);\n"
		+ "		if (result == SQLITE_ERROR) {\n"
		+ "			NSLog(@\"SQL INSERT ERROR\");\n"
		+ "			NSLog(@\"Error: failed to delete with message '%s'.\", sqlite3_errmsg(database));\n"
		+ "			\n"
		+ "		} else {\n"
		+ "			NSLog(@\"SQL DELETE OK\");\n"
		+ "			num = sqlite3_changes(database);\n"
		+ "		}\n"
		+ "		sqlite3_reset(statement);\n"
		+ "		\n"
		+ "	} else {\n"
		+ "		NSLog(@\"prepare error. %s\",  sqlite3_errmsg(database));\n"
		+ "	}\n"
		+ "	// \"Finalize\" the statement - releases the resources associated with the statement.\n"
		+ "	sqlite3_finalize(statement);\n"
		+ "	\n"
		+ "	return num;";
	}

	/**
	 * ���\�b�h��` deleteXxxAll ��ǉ�
	 * @param td
	 * @return
	 */
	private String getMethodIf_deleteXxxAll(TableDef td) {
		return "- (int)" + "delete" + td.getTableNamePhysicsTopUpper() + "All";
	}

	/**
	 * ���\�b�h���� deleteXxxAll
	 * @param td
	 * @return
	 */
	private String getMethodImpl_deleteXxxAll(TableDef td) {
		final String table = td.getTableNamePhysics();
		
		return 
		"	const char *sql = \"DELETE FROM "+table+";\";\n"
		+ "	sqlite3_stmt *statement;\n"
		+ "	int result = 0;\n"
		+ "	if (sqlite3_prepare_v2(database, sql, -1, &statement, NULL) == SQLITE_OK) {\n"
		+ "		\n"
		+ "		result = sqlite3_step(statement);\n"
		+ "		if (result == SQLITE_ERROR) {\n"
		+ "			NSLog(@\"SQL INSERT ERROR\");\n"
		+ "			NSLog(@\"Error: failed to insert into the database with message '%s'.\", sqlite3_errmsg(database));\n"
		+ "				\n"
		+ "		} else {\n"
		+ "			result = sqlite3_changes(database);\n"
		+ "			NSLog(@\"SQL DELETE OK %d\", result);\n"
		+ "		}\n"
		+ "		sqlite3_reset(statement);\n"
		+ "		\n"
		+ "	} else {\n"
		+ "		NSLog(@\"prepare error. %s\",  sqlite3_errmsg(database));\n"
		+ "	}\n"
		+ "	// \"Finalize\" the statement - releases the resources associated with the statement.\n"
		+ "	sqlite3_finalize(statement);\n"
		+ "	\n"
		+ "	return result;\n";
	}

	/**
	 * �t�@�C�������̏o��
	 */
	public void onEndFile() {
		this.headerInterface.append("@end\n");
		this.impl.append("@end\n");
	}

	/**
	 * �w�b�_�[�t�@�C���\�[�X���擾
	 * @return
	 */
	public String getHeaderSource() {
		return this.headerImport.toString() + this.headerInterface.toString();
	}

	/**
	 * �N���X�t�@�C���\�[�X���擾
	 * @return
	 */
	public String getClassSource() {
		return this.impl.toString();
	}

}
