package com.ams.poi.xls2sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.bean.TableValuesDef;
import com.ams.poi.xls2sql.jerseymaker.JerseySourceMaker;
import com.ams.poi.xls2sql.jerseymaker.WebAPIMethod;
import com.ams.poi.xls2sql.sqlfactory.DbmsType;
import com.ams.poi.xls2sql.util.FileUtil;
import com.ams.poi.xls2sql.util.ParseUtil;
import com.ams.poi.xls2sql.util.Path;

/**
 * <p>
 * �^�C�g��: GenerateJavaEnumManager
 * </p>
 * <p>
 * ����: XLS�t�@�C��DB�e�[�u����`����ǂݎ�� Java Jersey Resource(WEB API)�N���X�𐶐�����<BR>
 * </p>
 * <p>
 * ���쌠: Copyright (c) 2009 PowerBEANS Inc
 * </p>
 * <p>
 * PowerBEANS.Inc
 * </p>
 * <p>
 * Created on 2003/07/25
 * </p>
 * 
 * @author ��c���F
 * @version 1.0
 */
public class GenerateJavaJerseyResourceManager {

	// Create POIFS
	private POIFSFileSystem fs;

	// Create WorkBook
	private HSSFWorkbook workbook;

	// get sheet
	// private HSSFSheet sheet;

	// output dir
	private String outDir = "";

	// all insert sql
	private StringBuffer allInsertSQL = new StringBuffer();

	// -- static final

	// �e�[�u����`SheetName (Table##)
	// private static final String SHEETNAME_TABLE_PREFIX = "Table";
	// �e�[�u����`SheetName (Insert##)
	private static final String TARGET_SHEETNAME_PREFIX = "Jersey";

	// �SINSERTSQL�t�@�C����
	// public static final String INSERT_ALL_SQL_FILE = "insert_all.sql";

	// �I�v�V����Hash
	private HashMap opt_map = new HashMap();

	/**
	 * �R���X�g���N�^
	 * 
	 * @param xls_file
	 *            XLS�t�@�C��
	 * @param out_dir
	 *            �o�̓f�B���N�g��
	 * @param encode
	 *            �o�͕����R�[�h
	 */
	public GenerateJavaJerseyResourceManager(String xls_file, String out_dir)
			throws FileNotFoundException, IOException {

		// Create POIFS
		fs = new POIFSFileSystem(new FileInputStream(xls_file));

		// Create WorkBook
		workbook = new HSSFWorkbook(fs);

		outDir = out_dir;
	}

	/**
	 * �R���X�g���N�^
	 */
	private GenerateJavaJerseyResourceManager() {
		super();
	}

	/**
	 * �o��
	 * 
	 * @param dbms
	 * @param output_sql_encode
	 * @param output_java_encode
	 * @param output_jsp_encode
	 * @return
	 * @throws Exception
	 */
	public int generate(DbmsType dbms, String output_sql_encode,
			String output_java_encode, String output_jsp_encode)
			throws Exception {

		int sheet_num = 0;
		int num = 0;

		// �S�V�[�g�ɑ΂��ăV�[�g���� 'JerseyXxx' �ł����
		// ��͂��ďo�͂���B

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

			String sheet_name = workbook.getSheetName(i);

			if (sheet_name.startsWith(TARGET_SHEETNAME_PREFIX)) {
				num += sheet2JavaJersey(workbook.getSheetAt(i), dbms,
						output_java_encode, output_jsp_encode);
				sheet_num++;
			}
		}

		return num;
	}

	/**
	 * sheet2JavaJersey<BR>
	 * �����̃V�[�g����e�[�u�����擾��JavaJerseyResource�t�@�C�����쐬�B<BR>
	 * 1�V�[�g�ɕ����̃e�[�u����`���L�q����Ă���Ƃ���B
	 * 
	 * @param sheet
	 *            �e�[�u����`���L�q����Ă���V�[�g
	 * @param dbms
	 *            DBMS type
	 * @param output_jsp_encode
	 * @return �쐬����SQL�t�@�C����
	 * @throws Exception
	 */
	private int sheet2JavaJersey(HSSFSheet sheet, DbmsType dbms, String output_java_encode, String output_jsp_encode) throws Exception {

		// short koumoku_col = 1;
		short table_col = 1;
		// final String KOUMOKU_COLUMN = "����";
		final String TABLE_START_MARK = "(";

		// Jersey API��`��� --
		Iterator rowit = sheet.rowIterator();
		int sql_num = 0;
		int r = 1;
		while (rowit.hasNext()) {
			HSSFRow row = (HSSFRow) rowit.next();
			// TableDef td = null;
			TableValuesDef tvd = null;
			int table_row_top, table_row_bottom;

			HSSFCell mark_cell;
			if ((mark_cell = row.getCell(table_col)) == null
					|| mark_cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
				continue;
			}

			if (mark_cell.getStringCellValue().substring(0, 1).equals(
					TABLE_START_MARK)) {

				// "(" �̍s ���e�[�u���擪�s�Ƃ��Ď擾
				table_row_top = row.getRowNum();
				table_row_bottom = table_row_top + 1; // �J�����s�̕���ǉ�

				// ����(�̍s�̍Ōオ "add" �̏ꍇ�� DELETE ����ǉ����Ȃ�
				boolean is_add_only = mark_cell.getStringCellValue().endsWith(
						"add");

				// �e�[�u���f�[�^��`�ŏI�s��T��
				// AMS table_row_bottom + 1 �� table_row_bottom �ɕύX
				rb_loop: while (table_row_bottom + 1 <= sheet.getLastRowNum()
				// rb_loop:while(table_row_bottom + 1 <= sheet.getLastRowNum()
						&& sheet.getRow(table_row_bottom + 1) != null) {

					HSSFCell cell = sheet.getRow(table_row_bottom + 1).getCell(
							table_col);
					if (cell == null) {
						break;
					}

					switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_STRING:
						if (cell.getStringCellValue().startsWith(
								TABLE_START_MARK)) {
							break rb_loop;
						}
						break;

					case HSSFCell.CELL_TYPE_NUMERIC:
						break;

					default:
						break rb_loop;
					}

					table_row_bottom++;
				}

				WebAPIMethod m = getTableValuesDefFromSheet(sheet,
						table_row_top, table_row_bottom);
				
				// �e�[�u����`�擾. ����"Table"�̂ݑΉ�
				TableDef td = ParseUtil.getTableDef(workbook.getSheet("Table"), mark_cell.getStringCellValue());

				{ // 2. Resource ���o��
					final String filename = m.getTableNamePhysics()
							+ "Resource.java";
					System.out.print("Export Jersey WebAPI Resource to "
							+ filename + " ...");
					JerseySourceMaker maker = new JerseySourceMaker();
					String source = maker.getSource(m, td);
					String sfile = outDir + File.separator
							+ Path.JAVA_API_RESOURCE + File.separator + filename;
					try {
						FileUtil.writeFile(sfile, source, output_java_encode);
						System.out.println("ok." + sfile);
						sql_num++;
					} catch (Exception e) {
						System.out.println(" java Jersey output fail."
								+ e.getMessage());
					}

				}
				// �Œ�t�@�C�����o��

			}
			r++;
		}
		return sql_num;
	}

	/**
	 * @param source
	 * @return
	 */
	private String toTagSource(String source) {
		return source.replaceAll("<%@page", "<%@tag");
	}

	/**
	 * setTableDefOptions<BR>
	 * �e�[�u����`�I�v�V�����ݒ�
	 * 
	 * @param td
	 *            �e�[�u����`
	 */
	private void setTableDefOptions(TableDef td) {

		td.setDropTableEnable(true);
		td.setDropIndexEnable(true);
	}

	/**
	 * getTableValuesDefFromSheet<BR>
	 * �V�[�g�̎w��s����P�e�[�u���ɑ΂���Jersey�ݒ�ꗗ���擾�B<BR>
	 * ���Ȃ�V�[�g�̎d�l���Ɉˑ����Ă���B
	 * 
	 * @param sheet
	 *            �V�[�g
	 * @param table_row_top
	 *            �擪�s(�e�[�u�����s)
	 * @param table_row_bottom
	 *            �ŏI�s
	 * @return �e�[�u��INSERT�f�[�^��`
	 */
	private WebAPIMethod getTableValuesDefFromSheet(HSSFSheet sheet,
			int table_row_top, int table_row_bottom) {

		WebAPIMethod m = new WebAPIMethod();
		// TableValuesDef tvd = new TableValuesDef();
		short table_all_col = 1;
		{
			// �e�[�u���� (XX)���j���[���ރ}�X�^(MstMenuBunrui)
			// ^n1 ^n2 ^n3
			HSSFRow row = sheet.getRow(table_row_top);
			String org_text = row.getCell(table_all_col).getStringCellValue();
			int n1 = org_text.indexOf(')');
			int n2 = org_text.indexOf('(', n1);
			int n3 = org_text.indexOf(')', n2);
			if (n1 == -1 || n2 == -1) { // ��͕s��
				return m;
			}
			// tvd.setTableNameLogic(org_text.substring(n1 + 1, n2));
			// tvd.setTableNamePhysics(org_text.substring(n2 + 1, n3));
			m.setTableNameLogic(org_text.substring(n1 + 1, n2));
			m.setTableNamePhysics(org_text.substring(n2 + 1, n3));
			
		}

		// �eAPI�o�͗L�����擾
		for (int i = 0; i < 4; i++) {
			HSSFRow row = sheet.getRow(table_row_top + 2);
			String org_text = row.getCell((short) (table_all_col + i))
					.getStringCellValue();
			if (org_text == null || org_text.trim().length() == 0) {
				break;
			}
			switch (i) {
			case 0:
				m.setMethodGet(true);
				break;
			case 1:
				m.setMethodPost(true);
				break;
			case 2:
				m.setMethodPut(true);
				break;
			case 3:
				m.setMethodDelete(true);
				break;
			}
		}

		return m;

		// short cell_left = 1;
		// short cell_right = 1;
		// {
		// HSSFRow row = sheet.getRow(table_row_top + 2);
		// while (cell_right + 1 < row.getLastCellNum() &&
		// row.getCell((short) (cell_right + 1)) != null &&
		// StringUtils.isNotEmpty(row.getCell((short) (cell_right +
		// 1)).getStringCellValue())) {
		// // while (cell_right + 1 < row.getLastCellNum() &&
		// row.getCell((short) (cell_right + 1)) != null) {
		// cell_right++;
		// }
		// }
		// // �e�t�B�[���h�������
		// {
		// ArrayList fieldData = new ArrayList();
		// HSSFRow row = sheet.getRow(table_row_top + 2);
		// for(short c = cell_left; c <= cell_right; c++) {
		// HSSFCell cell = row.getCell(c);
		// String val = cell == null ? "" :
		// cell.getCellType() == HSSFCell.CELL_TYPE_STRING ?
		// cell.getStringCellValue() : "";
		// fieldData.add(val);
		// }
		// tvd.setFieldNameList(fieldData);
		// }
		//		
		// // �e�t�B�[���h�������
		// for (int r = table_row_top + 3; r <= table_row_bottom; r++) {
		// ArrayList recordData = new ArrayList();
		// HSSFRow row = sheet.getRow(r);
		// for (short c = cell_left; c <= cell_right; c++) {
		// // �e�Z���̒l��Double�^��String�^�Ŋi�[
		// HSSFCell cell = row.getCell(c);
		// if (cell == null) {
		// // ��Z���̏ꍇ��NULL������
		// recordData.add("NULL");
		// continue;
		// }
		// switch (cell.getCellType()) {
		// case HSSFCell.CELL_TYPE_STRING :
		// recordData.add(cell.getStringCellValue());
		// break;
		//
		// case HSSFCell.CELL_TYPE_NUMERIC :
		// // recordData.add(new Double(cell.getNumericCellValue()));
		// // ��������������ݒ�l���画�f
		// double org_val = cell.getNumericCellValue();
		// if (org_val == (int) org_val) { // ����
		// recordData.add(new Integer((int) org_val));
		// } else {
		// // ����
		// recordData.add(
		// new Double(cell.getNumericCellValue()));
		// }
		// break;
		//
		// case HSSFCell.CELL_TYPE_BLANK :
		// recordData.add("");
		// break;
		//
		// default :
		// System.err.println(
		// " Not correspond type "
		// + cell.getCellType()
		// + " ("
		// + r
		// + ","
		// + c
		// + ")");
		// break;
		// }
		// }
		//
		// tvd.getRecordDataList().add(recordData);
		// }
		//
		// return tvd;
	}

	/**
	 * getTableDefFromSheet<BR>
	 * �V�[�g�̎w��s����e�[�u����`���擾�B<BR>
	 * ���Ȃ�V�[�g�̎d�l���Ɉˑ����Ă���
	 * 
	 * @param sheet
	 *            �V�[�g
	 * @param table_row_top
	 *            �擪�s(�e�[�u�����s)
	 * @param table_row_bottom
	 *            �ŏI�s
	 * @return �e�[�u����`
	 */
	private TableDef getTableDefFromSheet(HSSFSheet sheet, int table_row_top,
			int table_row_bottom) {
		TableDef td = new TableDef();
		short table_all_col = 1;
		short koumoku = 1;
		short field_name = 2;
		short type = 3;
		short is_null = 4;
		short default_val = 5;
		short index_name = 6;
		short note = 7;
		// String null_on = "�~";
		String default_system = "System";
		{
			// �e�[�u���� (XX)���j���[���ރ}�X�^(MstMenuBunrui)
			// ^n1 ^n2 ^n3
			HSSFRow row = sheet.getRow(table_row_top);
			String org_text = row.getCell(table_all_col).getStringCellValue();
			int n1 = org_text.indexOf(')');
			int n2 = org_text.indexOf('(', n1);
			int n3 = org_text.indexOf(')', n2);
			if (n1 == -1 || n2 == -1) { // ��͕s��
				return td;
			}
			td.setTableNameLogic(org_text.substring(n1 + 1, n2));
			td.setTableNamePhysics(org_text.substring(n2 + 1, n3));
		} // �e�t�B�[���h�������
		for (int r = table_row_top + 2; r <= table_row_bottom; r++) {
			FieldDef fd = new FieldDef();
			HSSFRow row = sheet.getRow(r);
			fd.setFieldNameLogic(row.getCell(koumoku).getStringCellValue());
			fd.setFieldNamePhysics(StringUtils.trim(row.getCell(field_name)
					.getStringCellValue()));
			fd.setTypeXls(StringUtils.trim(row.getCell(type)
					.getStringCellValue()));
			String not_null = StringUtils.trim(row.getCell(is_null)
					.getStringCellValue());
			fd.setNotNull(!(not_null == null || not_null.length() == 0));
			// default �� ������ or ���l
			switch (row.getCell(default_val).getCellType()) {
			case HSSFCell.CELL_TYPE_STRING:
				String def_string = StringUtils.trim(row.getCell(default_val)
						.getStringCellValue());
				if (def_string == null || def_string.equals("")
						|| StringUtils.trim(def_string).equals(default_system)) {
					fd.setDefaultValue("");
				} else {
					fd.setDefaultValue(StringUtils.trim(def_string));
				}
				break;
			case HSSFCell.CELL_TYPE_NUMERIC:
				double def_num = row.getCell(default_val).getNumericCellValue();
				fd.setDefaultValue(Double.toString(def_num));
				break;
			default: // ��͕s��
			}

			fd.setIndexName(row.getCell(index_name).getStringCellValue());
			fd
					.setNote(StringUtils.trim(row.getCell(note)
							.getStringCellValue()));
			td.getFieldList().add(fd);
		}

		return td;
	}

	/**
	 * getAllInsertSQL<BR>
	 * 
	 * @return
	 */
	public StringBuffer getAllInsertSQL() {
		return allInsertSQL;
	}

	/**
	 * setAllInsertSQL<BR>
	 * 
	 * @param buffer
	 */
	public void setAllInsertSQL(StringBuffer buffer) {
		allInsertSQL = buffer;
	}

	/**
	 * initllInsertSQL<BR>
	 * 
	 * @param buffer
	 */
	public void initAllInsertSQL() {
		allInsertSQL = new StringBuffer();
	}

	/**
	 * �I�v�V�����l���i�[
	 * 
	 * @param key
	 * @param val
	 */
	public void setOption(String key, String val) {
		this.opt_map.put(key, val);
	}
}
