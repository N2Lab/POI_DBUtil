package com.ams.poi.xls2sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.bean.TableDefCache;
import com.ams.poi.xls2sql.beanmaker.BeanSourceMaker;
import com.ams.poi.xls2sql.jdomaker.JDOSourceMaker;
import com.ams.poi.xls2sql.objectivec.ObjectiveCDBMgrMaker;
import com.ams.poi.xls2sql.objectivec.ObjectiveCModelMaker;
import com.ams.poi.xls2sql.sqlfactory.CreateSQL;
import com.ams.poi.xls2sql.sqlfactory.CreateSQLFactory;
import com.ams.poi.xls2sql.sqlfactory.DbmsType;
import com.ams.poi.xls2sql.util.FileUtil;

/**
 * <p>�^�C�g��: GenerateCreateSQLManager</p>
 * <p>����: 
 * XLS�t�@�C��DB�e�[�u����`����ǂݎ��
 * Create SQL �t�@�C�����o�͂���B<BR>
 * ��Ńt�H�[�}�b�g��properties�t�@�C���Ŏw��ł���悤�ɂ���B
 * </p>
 * <p>���쌠: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author ��c���F
 * @version 1.0
 */
public class GenerateCreateSQLManager {

  // Create POIFS
  private POIFSFileSystem fs;

	// Create WorkBook
	private HSSFWorkbook workbook;

	// get sheet
	//	private HSSFSheet sheet;

	// output dir
	private String outDir = "";
  
  // all create sql (with drop)
  private StringBuffer allCreateSQL = new StringBuffer();
  
  // all create sql (no drop)
  private StringBuffer allCreateSQL_nodrop = new StringBuffer();

	// -- static final

	// �e�[�u����`SheetName (Table##)
	private static final String TABLE_DEF_SHEETNAME = "Table";
  
  // �SCreateSQL�o�̓t�@�C���� (DROP, CREATE)
  public static final String CREATE_ALL_SQL_FILE = "create_all.sql";

  // �SCreateSQL�o�̓t�@�C���� (DROP �Ȃ�, CREATE�̂�)
  public static final String CREATE_ALL_NODROP_SQL_FILE = "create_all_first.sql";

  // JAVA �t�@�C���o�̓f�B���N�g����
  private static final String JAVA_DIR = File.separator + "java";

  // Objective-C �t�@�C���o�̓f�B���N�g����
private static final String OBJECTIVE_C_DIR = File.separator + "objective-c";

// JAVA(JDO) �t�@�C���o�̓f�B���N�g����
private static final String JAVA_JDO_DIR = File.separator + "java_jdo";

	/**
	 * �R���X�g���N�^
	 * @param xls_file XLS�t�@�C��
	 * @param out_dir �o�̓f�B���N�g��
	 */
	public GenerateCreateSQLManager(String xls_file, String out_dir)
		throws FileNotFoundException, IOException {

		// Create POIFS
		fs = new POIFSFileSystem(new FileInputStream(xls_file));

		// Create WorkBook
		workbook = new HSSFWorkbook(fs);

		// get sheet
		//		HSSFSheet sheet = workbook.getSheet("Sheet1"); // �S�p�͂���

		outDir = out_dir;
	}

	/**
	 * �R���X�g���N�^
	 */
	private GenerateCreateSQLManager() {
		super();
	}

////	/**
////	 * generateCreateSQL<BR>
////	 * @param xls_file �ǂݍ���XLS�t�@�C��
////	 * @param out_dir �o�͂���f�B���N�g��
////	 * @return ��������SQL�t�@�C����
////	 */
////	public int generateCreateSQL() {
////        return generateCreateSQL(DbmsType.ORACLE);
////   	}


    /**
     * @param string
     * @throws Exception
     * @returns
     */
    public int generateCreateSQL(DbmsType dbms, String output_sql_encode, String output_java_encode) throws Exception {
      int create_sheet_num = 0;
      int crate_sql_num = 0;

      // �S�V�[�g�ɑ΂��ăV�[�g���� 'TableXxx' �ł����
      // ��͂���CreateSQL ���o�͂���B

      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

        String sheet_name = workbook.getSheetName(i);

        if (sheet_name.startsWith(TABLE_DEF_SHEETNAME)) {
          crate_sql_num += sheet2CreateSQLs(workbook.getSheetAt(i), dbms, output_sql_encode);
          create_sheet_num++;
        }
      }

      // �SSQL���o��(drop�L)
      System.out.print("Export to " + CREATE_ALL_SQL_FILE + "...");
      String output_file = outDir + File.separator + CREATE_ALL_SQL_FILE;

      try {
        FileUtil.writeFile(output_file, getAllCreateSQL().toString(), output_sql_encode);
        System.out.println(" ok.");

      } catch (IOException e) {
        System.out.println(" fail.");
      } finally {
      }
      
      // �SSQL���o��(drop��)
      System.out.print("Export to " + CREATE_ALL_NODROP_SQL_FILE + "...");
      String output_file_nodrop = outDir + File.separator + CREATE_ALL_NODROP_SQL_FILE;

      try {
        FileUtil.writeFile(output_file_nodrop, getAllCreateSQL_nodrop().toString(), output_sql_encode);
        System.out.println(" ok.");

      } catch (IOException e) {
        System.out.println(" fail.");
      } finally {
      }
      
      return crate_sql_num;
    }
	/**
	 * sheet2CreateSQL<BR>
	 * �����̃V�[�g����e�[�u�����擾��CreateSQL�t�@�C�����쐬�B<BR>
	 * 1�V�[�g�ɕ����̃e�[�u����`���L�q����Ă���Ƃ���B
	 * @param sheet �e�[�u����`���L�q����Ă���V�[�g
     * @param dbms DBMS 
	 * @return �쐬����SQL�t�@�C����
	 * @throws Exception
	 */
	private int sheet2CreateSQLs(HSSFSheet sheet, DbmsType dbms, String output_sql_encode) throws Exception {

// short koumoku_col = 1;
		short table_col = 1;
		// final String KOUMOKU_COLUMN = "����";
		final String TABLE_START_MARK = "(";

		CreateSQLFactory factory = new CreateSQLFactory();
		CreateSQL create_sql = factory.getCreateSQL(dbms);
		
		// Objective C DBMgr manager 
		ObjectiveCDBMgrMaker oc_dbmgr = new ObjectiveCDBMgrMaker();
		
		// create_all.sql prefix ��ǉ�
		allCreateSQL.append(create_sql.getCreateTableHeader());
		allCreateSQL_nodrop.append(create_sql.getCreateTableHeader());

		// �e�[�u����� --
		Iterator rowit = sheet.rowIterator();
		int create_sql_num = 0;
		int r = 1;
		HSSFCell before_cell = null;
		while (rowit.hasNext()) {
			HSSFRow row = (HSSFRow) rowit.next();
			TableDef td = null;
			int table_row_top, table_row_bottom;

			// TODO �Q�s�A���� null��������break���邩
			if (before_cell == null && row.getCell(table_col) == null && row.getRowNum() > 0) {
				break;
			}
			before_cell = row.getCell(table_col);
			if (row.getCell(table_col) == null) {
				continue;
			}

			String cell_value = row.getCell(table_col).getStringCellValue();
			if (cell_value.startsWith(TABLE_START_MARK)) {

				// "(" �̍s ���e�[�u���擪�s�Ƃ��Ď擾
				table_row_top = row.getRowNum();
				table_row_bottom = table_row_top;

				// �e�[�u����`�ŏI�s��T��
				while (table_row_bottom + 1 <= sheet.getLastRowNum()
						&& sheet.getRow(table_row_bottom + 1) != null
						&& sheet.getRow(table_row_bottom + 1)
								.getCell(table_col) != null
						&& !sheet.getRow(table_row_bottom + 1).getCell(
								table_col).getStringCellValue().startsWith(
								TABLE_START_MARK)) {
					table_row_bottom++;
				}

				td = getTableDefFromSheet(sheet, table_row_top,
						table_row_bottom);

				// �L���b�V���ɒǉ�
				TableDefCache.add(td);

				System.out.print("Export to " + td.getTableNamePhysics()
						+ ".sql ...");

				// �e�[�u����`�I�v�V����
				setTableDefOptions(td);

				// �e�[�u����`��sql�t�@�C���ɏo��(Drop�L)
				String file_out = create_sql.getOutputFileString(td, true);
				String output_file = outDir + File.separator
						+ td.getTableNamePhysics() + ".sql";

				try {
					FileUtil
							.writeFile(output_file, file_out, output_sql_encode);
					System.out.println(" ok.");
					create_sql_num++;
					// �S�t�@�C���o�b�t�@(Drop�L)�ɒǉ�
					allCreateSQL.append(file_out);

				} catch (IOException e) {
					System.out.println(" fail.");
				} finally {
				}

				////// �o��1 create sql ///////////////
				// �e�[�u����`��sql�t�@�C���ɏo��(Drop��)
				String file_out_nodrop = create_sql.getOutputFileString(td,
						false);
				// �S�t�@�C���o�b�t�@(Drop����)�ɒǉ�
				allCreateSQL_nodrop.append(file_out_nodrop);

				////// �o��2 Java Model  ///////////////
				outputForJava(output_sql_encode, td);
				
				////// �o��3 Objective-C  ///////////////
				outputForObjectiveC(output_sql_encode, td, oc_dbmgr);
				
				////// �o��4 JDO (for Google App Engine) ///////////
				outputForJdo(output_sql_encode, td);
				
			}
			r++;
		}
		
		// Objective-C DBMgr���o��
		oc_dbmgr.onEndFile();
		try {
			String header = oc_dbmgr.getHeaderSource();
			String hfile = outDir + OBJECTIVE_C_DIR +  File.separator
					+ "DBMgr.h";
			String source = oc_dbmgr.getClassSource();
			String sfile = outDir + OBJECTIVE_C_DIR +  File.separator
					+ "DBMgr.m";
			
			FileUtil.writeFile(sfile, source, output_sql_encode);
			FileUtil.writeFile(hfile, header, output_sql_encode);
			System.out.println(" Objective-C DBMgr output ok.");

		} catch (IOException e) {
			System.out.println(" Objective-C DBMgr output fail.");
		} finally {
		}

		allCreateSQL.append(create_sql.getCreateTableFooter());
		allCreateSQL_nodrop.append(create_sql.getCreateTableFooter());

		return create_sql_num;
	}

	/**
	 * JDO���f����ǉ�
	 * @param output_sql_encode
	 * @param td
	 */
	private void outputForJdo(String output_sql_encode, TableDef td) {
		JDOSourceMaker bs = new JDOSourceMaker();
		String source = bs.getBeanSource(td);
		String sfile = outDir + JAVA_JDO_DIR +  File.separator
				+ td.getTableNamePhysicsTopUpper() + ".java";
		try {
			FileUtil.writeFile(sfile, source, output_sql_encode);
			System.out.println(" java bean output ok.");

		} catch (IOException e) {
			System.out.println(" java bean output fail.");
		} finally {
		}
	}

	/**
	 * @param output_sql_encode
	 * @param td
	 * @param oc_dbmgr 
	 */
	private void outputForObjectiveC(String output_sql_encode, TableDef td, ObjectiveCDBMgrMaker oc_dbmgr) {
		// 3.1 Objective-C Model
		{
		ObjectiveCModelMaker bs = new ObjectiveCModelMaker();
		
		String source = bs.getModelClassSource(td);
		String sfile = outDir + OBJECTIVE_C_DIR +  File.separator
				+ td.getTableNamePhysicsTopUpper() + ".m";
		
		String header = bs.getModelClassHearder(td);
		String hfile = outDir + OBJECTIVE_C_DIR +  File.separator
				+ td.getTableNamePhysicsTopUpper() + ".h";
		
		// 3.2 Objective-C DBMgr
		// TODO xxxById, findXxxAll, findXxx (2�ڂ̍���,�R�����g�o��), create, insert, update
		// Table td �̒�`�Ɋ�Â��Ċe���\�b�h�̃\�[�X������ɏo��
		oc_dbmgr.addTableDef(td);
		
		try {
			FileUtil.writeFile(sfile, source, output_sql_encode);
			FileUtil.writeFile(hfile, header, output_sql_encode);
			System.out.println(" Objective-C output ok.");

		} catch (IOException e) {
			System.out.println(" Objective-C output fail.");
		} finally {
		}
		}
	}

	/**
	 * @param output_sql_encode
	 * @param td
	 */
	private void outputForJava(String output_sql_encode, TableDef td) {
		// Java Bean �����o��
		{
		BeanSourceMaker bs = new BeanSourceMaker();
		String source = bs.getBeanSource(td);
		String sfile = outDir + JAVA_DIR +  File.separator
				+ td.getTableNamePhysicsTopUpper() + ".java";
		try {
			FileUtil.writeFile(sfile, source, output_sql_encode);
			System.out.println(" java bean output ok.");

		} catch (IOException e) {
			System.out.println(" java bean output fail.");
		} finally {
		}
		}
	}

	/**
	 * setTableDefOptions<BR>
	 * �e�[�u����`�I�v�V�����ݒ�
	 * @param td �e�[�u����`
	 */
	private void setTableDefOptions(TableDef td) {

		td.setDropTableEnable(true);
		td.setDropIndexEnable(false);
	}

	/**
	 * getTableDefFromSheet<BR>
	 * �V�[�g�̎w��s����e�[�u����`���擾�B<BR>
	 * ���Ȃ�V�[�g�̎d�l���Ɉˑ����Ă���
	 * 
	 * @param sheet �V�[�g
	 * @param table_row_top �擪�s(�e�[�u�����s)
	 * @param table_row_bottom �ŏI�s
	 * @return �e�[�u����`
	 */
	private TableDef getTableDefFromSheet(
		HSSFSheet sheet,
		int table_row_top,
		int table_row_bottom) {
		TableDef td = new TableDef();

		short table_all_col = 1;
		short strage_engine_col = 2; // MySQL
		short character_set_col = 3; // MySQL
		short row_format_col = 4; // MySQL
		
		short koumoku = 1;
		short field_name = 2;
		short type = 3;
		short is_pkey = 4;
		short is_null = 5;
		short default_val = 6;
    short index_unique = 7;
		short index_name = 8;
    short index_length = 9;
		short note = 10;

//		String null_on = "�~";
		String default_system = "System";

		{
			// �e�[�u���� (XX)���j���[���ރ}�X�^(MstMenuBunrui)
			//              ^n1               ^n2           ^n3
			HSSFRow row = sheet.getRow(table_row_top);
			String org_text = row.getCell(table_all_col).getStringCellValue();
			int n1 = org_text.indexOf(')');
			int n2 = org_text.indexOf('(', n1);
			int n3 = org_text.indexOf(')', n2);
			if (n1 == -1 || n2 == -1) {
				// ��͕s��
				return td;
			}
			td.setTableNameLogic(org_text.substring(n1 + 1, n2));
			td.setTableNamePhysics(org_text.substring(n2 + 1, n3));
			
			// �X�g�[���W�G���W����(for MySQL)
			if (row.getCell(strage_engine_col) != null) {
			    td.setStrageEngineName(row.getCell(strage_engine_col).getStringCellValue());
			}
			
			// �L�����N�^�Z�b�g(for MySQL)
			if (row.getCell(character_set_col) != null) {
				td.setCharacterSetName(row.getCell(character_set_col).getStringCellValue());
			}

			// �f�[�^���k(for MySQL)
			if (row.getCell(row_format_col) != null) {
				td.setRowFormatName(row.getCell(row_format_col).getStringCellValue());
			}
		}

		// �e�t�B�[���h�������
		for (int r = table_row_top + 2; r <= table_row_bottom; r++) {
			FieldDef fd = new FieldDef();
			HSSFRow row = sheet.getRow(r);
			
			fd.setFieldNameLogic(row.getCell(koumoku).getStringCellValue());
			
			// �_������null��""�̏ꍇ�͓ǂݍ��ݒ��~
			if (fd.getFieldNameLogic() == null || "".equals(fd.getFieldNameLogic())) {
			    break;
			}


			String field_name_txt = StringUtils.trim(row.getCell(field_name).getStringCellValue());
			fd.setFieldNamePhysics(field_name_txt);
			fd.setTypeXls(
				StringUtils.trim(row.getCell(type).getStringCellValue()));
				
			String pkey =
				StringUtils.trim(row.getCell(is_pkey).getStringCellValue());
			fd.setPrimaryKey(!(pkey == null || pkey.length() == 0));
			
			String not_null =
				StringUtils.trim(row.getCell(is_null).getStringCellValue());
			fd.setNotNull(!(not_null == null || not_null.length() == 0));

			// default�l �� ������ or ���l
			switch (row.getCell(default_val).getCellType()) {
				case HSSFCell.CELL_TYPE_STRING :
					String def_string =
						StringUtils.trim(
							row.getCell(default_val).getStringCellValue());
					if (def_string == null
						|| def_string.equals("")
						|| StringUtils.trim(def_string).equals(default_system)) {
						fd.setDefaultValue("");
					} else {
						fd.setDefaultValue(StringUtils.trim(def_string));
					}
					break;

				case HSSFCell.CELL_TYPE_NUMERIC :
					double def_num = row.getCell(default_val).getNumericCellValue();
					fd.setDefaultValue(Double.toString(def_num));			
					
					break;

				default :
				    // ����ȊO�̏ꍇ�̓f�t�H���g�l�Ȃ�
//		          System.out.println("* row " + r + ", unsupport type " + row.getCell(default_val).getCellType());
		          // continue;
			}

      // index
			if (row.getCell(index_unique) == null) {
	      fd.setIndexUnique(false);
			} else {
	      String isindex_unique =
	        StringUtils.trim(row.getCell(index_unique).getStringCellValue());
	      fd.setIndexUnique(!(isindex_unique == null || isindex_unique.length() == 0));
			}

			if (row.getCell(index_name) != null) {
				// �C���f�b�N�X���ݒ�͖���
				try {
					fd.setIndexName(StringUtils.trim(row.getCell(index_name).getStringCellValue()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	      
	      if (row.getCell(index_length).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
	        fd.setIndexLength((int)row.getCell(index_length).getNumericCellValue());
	      }
			}
      
      // note (���l)
      {
      	HSSFCell note_cell = row.getCell(note);
      	if (note_cell != null && note_cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
      		fd.setNote(
      				StringUtils.trim(row.getCell(note).getStringCellValue()));
      		
      	}
      }

      td.getFieldList().add(fd);
	  }

		return td;
	}

  /**
   * getAllCreateSQL<BR>
   * @return
   */
  public StringBuffer getAllCreateSQL() {
    return allCreateSQL;
  }

  /**
   * setAllCreateSQL<BR>
   * @param buffer
   */
  public void setAllCreateSQL(StringBuffer buffer) {
    allCreateSQL = buffer;
  }

  /**
   * initAllCreateSQL<BR>
   * @param buffer
   */
  public void initAllCreateSQL() {
    allCreateSQL = new StringBuffer();
  }

	/**
	 * @return
	 */
	public StringBuffer getAllCreateSQL_nodrop() {
		return allCreateSQL_nodrop;
	}

	/**
	 * @param buffer
	 */
	public void setAllCreateSQL_nodrop(StringBuffer buffer) {
		allCreateSQL_nodrop = buffer;
	}

}
