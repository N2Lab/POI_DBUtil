package com.ams.poi.xls2sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
import com.ams.poi.xls2sql.enummaker.EnumSourceMaker;
import com.ams.poi.xls2sql.jspmaker.JspFormCheckMaker;
import com.ams.poi.xls2sql.jspmaker.JspFormRadioMaker;
import com.ams.poi.xls2sql.jspmaker.JspFormSelectMaker;
import com.ams.poi.xls2sql.sqlfactory.DbmsType;
import com.ams.poi.xls2sql.util.FileUtil;

/**
 * <p>タイトル: GenerateJavaEnumManager</p>
 * <p>説明: 
 * XLSファイルDBテーブル定義書を読み取り
 * EnumシートからEnumクラスを作成する<BR>
 * </p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class GenerateJavaEnumManager {

  // Create POIFS
	private POIFSFileSystem fs;

	// Create WorkBook
	private HSSFWorkbook workbook;

	// get sheet
	//	private HSSFSheet sheet;

	// output dir
	private String outDir = "";

  // all insert sql
  private StringBuffer allInsertSQL = new StringBuffer();

	// -- static final

	// テーブル定義SheetName (Table##)
	//	private static final String SHEETNAME_TABLE_PREFIX = "Table";
	// テーブル定義SheetName (Insert##)
	private static final String SHEETNAME_INSERT_PREFIX = "Enum";

  // 全INSERTSQLファイル名
//  public static final String INSERT_ALL_SQL_FILE = "insert_all.sql";

  // オプションHash
  private HashMap opt_map = new HashMap(); 


	/**
	 * コンストラクタ
	 * @param xls_file XLSファイル
	 * @param out_dir 出力ディレクトリ
   * @param encode 出力文字コード
	 */
	public GenerateJavaEnumManager(String xls_file, String out_dir)
		throws FileNotFoundException, IOException {

		// Create POIFS
		fs = new POIFSFileSystem(new FileInputStream(xls_file));

		// Create WorkBook
		workbook = new HSSFWorkbook(fs);

		outDir = out_dir;
	}

	/**
	 * コンストラクタ
	 */
	private GenerateJavaEnumManager() {
		super();
	}
  
	/**
	 * generateInsertSQL<BR>
	 * @param xls_file 読み込むXLSファイル
	 * @param out_dir 出力するディレクトリ
	 * @return 生成したSQLファイル数
	 * @throws Exception
	 */
	public int generateEnums(DbmsType dbms, String output_sql_encode, String output_java_encode, String output_jsp_encode) throws Exception {

		int sheet_num = 0;
		int sql_num = 0;

		// 全シートに対してシート名が 'EnumXxx' であれば
		// 解析して出力する。

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

			String sheet_name = workbook.getSheetName(i);

			if (sheet_name.startsWith(SHEETNAME_INSERT_PREFIX)) {
				sql_num += sheet2JavaEnum(workbook.getSheetAt(i), dbms, output_java_encode, output_jsp_encode);
				sheet_num++;
			}
		}

    // 1. 全SQLを出力
//		{
//			System.out.print("Export to " + INSERT_ALL_SQL_FILE + "...");
//			String output_file = outDir + File.separator + INSERT_ALL_SQL_FILE;
//
//			try {
//				FileUtil.writeFile(output_file, getAllInsertSQL().toString(), output_sql_encode);
//				System.out.println(" ok.");
//
//			} catch (IOException e) {
//				System.out.println(" fail.");
//			} finally {
//			}
//		}

		return sql_num;
	}

	/**
	 * sheet2InsertSQLs<BR>
	 * 引数のシートからテーブルを取得しInsertSQLファイルを作成。<BR>
	 * 1シートに複数のテーブル定義が記述されているとする。
	 * @param sheet テーブル定義が記述されているシート
     * @param dbms DBMS type
	 * @param output_jsp_encode 
	 * @return 作成したSQLファイル数
	 * @throws Exception
	 */
	private int sheet2JavaEnum(HSSFSheet sheet, DbmsType dbms, String output_java_encode, String output_jsp_encode) throws Exception {

		//		short koumoku_col = 1;
		short table_col = 1;
		//		final String KOUMOKU_COLUMN = "項目";
		final String TABLE_START_MARK = "(";

		// テーブル解析 --
		Iterator rowit = sheet.rowIterator();
		int sql_num = 0;
		int r = 1;
		while (rowit.hasNext()) {
			HSSFRow row = (HSSFRow) rowit.next();
			//			TableDef td = null;
			TableValuesDef tvd = null;
			int table_row_top, table_row_bottom;

			HSSFCell mark_cell;
			if ((mark_cell = row.getCell(table_col)) == null
				|| mark_cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
				continue;
			}

			if (mark_cell
				.getStringCellValue()
				.substring(0, 1)
				.equals(TABLE_START_MARK)) {

				// "(" の行  をテーブル先頭行として取得
				table_row_top = row.getRowNum();
				table_row_bottom = table_row_top + 1; // カラム行の分を追加
				
				// もし(の行の最後が "add" の場合は DELETE 文を追加しない
				boolean is_add_only = 
					mark_cell.getStringCellValue().endsWith("add");

				// テーブルデータ定義最終行を探す
				// AMS table_row_bottom + 1 を table_row_bottom に変更
				rb_loop:while(table_row_bottom + 1 <= sheet.getLastRowNum() 
				// rb_loop:while(table_row_bottom + 1 <= sheet.getLastRowNum() 
								&& sheet.getRow(table_row_bottom + 1) != null) {
            
          HSSFCell cell = sheet.getRow(table_row_bottom + 1).getCell(table_col);
          if (cell == null) {
            break;
          }
          
          switch(cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
              if (cell.getStringCellValue().startsWith(TABLE_START_MARK)) {
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
        

				tvd =
					getTableValuesDefFromSheet(
						sheet,
						table_row_top,
						table_row_bottom);

				{
					// 2. JAVA Enum を出力
					final String filename = tvd.getTableNamePhysics() + "Type.java";
					System.out.print(
						"Export JAVA Enum to "
							+ filename + " ...");
					EnumSourceMaker bs = new EnumSourceMaker();
					String source = bs.getEnumSource(tvd);
					String sfile = outDir + File.separator+ "java_jdo" +  File.separator
					+ filename;
				try {
					FileUtil.writeFile(sfile, source, output_java_encode);
						System.out.println(" java Enum output ok."  + sfile);

					} catch (IOException e) {
						System.out.println(" java Enum output fail.");
					} finally {
					}
				}
				{
					// 3.1 JSP form parts (select) を出力
					final String filename = "select" + tvd.getTableNamePhysics() + ".jsp";
					final String filename_tag = tvd.getTableNamePhysics() + "_select.tag";
					System.out.print(
						"Export JSP form parts(select) to "
							+ filename + " ...");
					JspFormSelectMaker maker = new JspFormSelectMaker();
					String source = maker.getSource(tvd);
					String sfile = outDir + File.separator+ "jsp" +  File.separator
					+ filename;
					String sfile_tag = outDir + File.separator+ "tags" +  File.separator
					+ filename_tag;
					try {
						FileUtil.writeFile(sfile, source, output_jsp_encode);
						FileUtil.writeFile(sfile_tag, toTagSource(source), output_jsp_encode);
						System.out.println(" JSP form parts(select) output ok."  + sfile);

					} catch (IOException e) {
						System.out.println(" JSP form parts(select) output fail.");
					} finally {
					}
				}
				{
					// 3.2 JSP form parts (checkbox) を出力
					final String filename = "checkbox" + tvd.getTableNamePhysics() + ".jsp";
					final String filename_tag = tvd.getTableNamePhysics() + "_checkbox.tag";
					System.out.print(
						"Export JSP form parts(checkbox) to "
							+ filename + " ...");
					JspFormCheckMaker maker = new JspFormCheckMaker();
					String source = maker.getSource(tvd);
					String sfile = outDir + File.separator+ "jsp" +  File.separator
					+ filename;
					String sfile_tag = outDir + File.separator+ "tags" +  File.separator
					+ filename_tag;
					try {
						FileUtil.writeFile(sfile, source, output_jsp_encode);
						FileUtil.writeFile(sfile_tag, toTagSource(source), output_jsp_encode);
						System.out.println(" JSP form parts(checkbox) output ok."  + sfile);

					} catch (IOException e) {
						System.out.println(" JSP form parts(checkbox) output fail.");
					} finally {
					}
				}
				{
					// 3.3 JSP form parts (radiobutton) を出力
					final String filename = "radio" + tvd.getTableNamePhysics() + ".jsp";
					final String filename_tag = tvd.getTableNamePhysics() + "_radio.tag";
					System.out.print(
						"Export JSP form parts(radio) to "
							+ filename + " ...");
					JspFormRadioMaker maker = new JspFormRadioMaker();
					String source = maker.getSource(tvd);
					String sfile = outDir + File.separator+ "jsp" +  File.separator
							+ filename;
					String sfile_tag = outDir + File.separator+ "tags" +  File.separator
					+ filename_tag;
					try {
						FileUtil.writeFile(sfile, source, output_jsp_encode);
						FileUtil.writeFile(sfile_tag, toTagSource(source), output_jsp_encode);
						System.out.println(" JSP form parts(radio) output ok."  + sfile);

					} catch (IOException e) {
						System.out.println(" JSP form parts(radio) output fail.");
					} finally {
					}
				}
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
	* テーブル定義オプション設定
	* @param td テーブル定義
	*/
	private void setTableDefOptions(TableDef td) {

		td.setDropTableEnable(true);
		td.setDropIndexEnable(true);
	}

	/**
	  * getTableValuesDefFromSheet<BR>
	  * シートの指定行から１テーブルに対するINSERTデータの一覧を取得。<BR>
	  * かなりシートの仕様書に依存している。
	  * 
	  * @param sheet シート
	  * @param table_row_top 先頭行(テーブル名行)
	  * @param table_row_bottom 最終行
	  * @return テーブルINSERTデータ定義
	  */
	private TableValuesDef getTableValuesDefFromSheet(
		HSSFSheet sheet,
		int table_row_top,
		int table_row_bottom) {

		TableValuesDef tvd = new TableValuesDef();
		short table_all_col = 1;
		{
			// テーブル名 (XX)メニュー分類マスタ(MstMenuBunrui)
			//              ^n1               ^n2           ^n3
			HSSFRow row = sheet.getRow(table_row_top);
			String org_text = row.getCell(table_all_col).getStringCellValue();
			int n1 = org_text.indexOf(')');
			int n2 = org_text.indexOf('(', n1);
			int n3 = org_text.indexOf(')', n2);
			if (n1 == -1 || n2 == -1) { // 解析不可
				return tvd;
			}
			tvd.setTableNameLogic(org_text.substring(n1 + 1, n2));
			tvd.setTableNamePhysics(org_text.substring(n2 + 1, n3));
		} // データ定義最終列を探す
		short cell_left = 1;
		short cell_right = 1;
		{
			HSSFRow row = sheet.getRow(table_row_top + 2);
      while (cell_right + 1 < row.getLastCellNum() &&
        row.getCell((short) (cell_right + 1)) != null &&
        StringUtils.isNotEmpty(row.getCell((short) (cell_right + 1)).getStringCellValue())) {
//        while (cell_right + 1 < row.getLastCellNum() && row.getCell((short) (cell_right + 1)) != null) {
				cell_right++;
			}
		}
		// 各フィールド名を解析
		{
			ArrayList fieldData = new ArrayList();
			HSSFRow row = sheet.getRow(table_row_top + 2);
			for(short c = cell_left; c <= cell_right; c++) {
				HSSFCell cell = row.getCell(c);
				String val = cell == null ? "" :
					cell.getCellType() == HSSFCell.CELL_TYPE_STRING ? 
							cell.getStringCellValue() : "";
				fieldData.add(val);
			}
			tvd.setFieldNameList(fieldData);
		}
		
		// 各フィールド情報を解析
		for (int r = table_row_top + 3; r <= table_row_bottom; r++) {
			ArrayList recordData = new ArrayList();
			HSSFRow row = sheet.getRow(r);
			for (short c = cell_left; c <= cell_right; c++) {
				// 各セルの値をDouble型かString型で格納
				HSSFCell cell = row.getCell(c);
				if (cell == null) {
					// 空セルの場合はNULLを入れる
					recordData.add("NULL");
					continue;
				}
				switch (cell.getCellType()) {
					case HSSFCell.CELL_TYPE_STRING :
						recordData.add(cell.getStringCellValue());
						break;

					case HSSFCell.CELL_TYPE_NUMERIC :
						//						recordData.add(new Double(cell.getNumericCellValue()));
						// 整数か少数かを設定値から判断
						double org_val = cell.getNumericCellValue();
						if (org_val == (int) org_val) { // 整数
							recordData.add(new Integer((int) org_val));
						} else {
							// 小数
							recordData.add(
								new Double(cell.getNumericCellValue()));
						}
						break;

					case HSSFCell.CELL_TYPE_BLANK :
						recordData.add("");
						break;

					default :
						System.err.println(
							" Not correspond type "
								+ cell.getCellType()
								+ " ("
								+ r
								+ ","
								+ c
								+ ")");
						break;
				}
			}

			tvd.getRecordDataList().add(recordData);
		}

		return tvd;
	}

	/**
	 * getTableDefFromSheet<BR>
	 * シートの指定行からテーブル定義を取得。<BR>
	 * かなりシートの仕様書に依存している
	 * 
	 * @param sheet シート
	 * @param table_row_top 先頭行(テーブル名行)
	 * @param table_row_bottom 最終行
	 * @return テーブル定義
	 */
	private TableDef getTableDefFromSheet(
		HSSFSheet sheet,
		int table_row_top,
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
//		String null_on = "×";
		String default_system = "System";
		{
			// テーブル名 (XX)メニュー分類マスタ(MstMenuBunrui)
			//              ^n1               ^n2           ^n3
			HSSFRow row = sheet.getRow(table_row_top);
			String org_text = row.getCell(table_all_col).getStringCellValue();
			int n1 = org_text.indexOf(')');
			int n2 = org_text.indexOf('(', n1);
			int n3 = org_text.indexOf(')', n2);
			if (n1 == -1 || n2 == -1) { // 解析不可
				return td;
			}
			td.setTableNameLogic(org_text.substring(n1 + 1, n2));
			td.setTableNamePhysics(org_text.substring(n2 + 1, n3));
		} // 各フィールド情報を解析
		for (int r = table_row_top + 2; r <= table_row_bottom; r++) {
			FieldDef fd = new FieldDef();
			HSSFRow row = sheet.getRow(r);
			fd.setFieldNameLogic(row.getCell(koumoku).getStringCellValue());
			fd.setFieldNamePhysics(
				StringUtils.trim(row.getCell(field_name).getStringCellValue()));
			fd.setTypeXls(
				StringUtils.trim(row.getCell(type).getStringCellValue()));
			String not_null =
				StringUtils.trim(row.getCell(is_null).getStringCellValue());
			fd.setNotNull(!(not_null == null || not_null.length() == 0));
			// default は 文字列 or 数値
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
					double def_num =
						row.getCell(default_val).getNumericCellValue();
					fd.setDefaultValue(Double.toString(def_num));
					break;
				default : // 解析不可
			}

			fd.setIndexName(row.getCell(index_name).getStringCellValue());
			fd.setNote(
				StringUtils.trim(row.getCell(note).getStringCellValue()));
			td.getFieldList().add(fd);
		}

		return td;
	}

  /**
   * getAllInsertSQL<BR>
   * @return
   */
  public StringBuffer getAllInsertSQL() {
    return allInsertSQL;
  }

  /**
   * setAllInsertSQL<BR>
   * @param buffer
   */
  public void setAllInsertSQL(StringBuffer buffer) {
    allInsertSQL = buffer;
  }

  /**
   * initllInsertSQL<BR>
   * @param buffer
   */
  public void initAllInsertSQL() {
    allInsertSQL = new StringBuffer();
  }

	/**
	 * オプション値を格納
	 * @param key
	 * @param val
	 */
	public void setOption(String key, String val) {
	    this.opt_map.put(key, val);
	}

}
