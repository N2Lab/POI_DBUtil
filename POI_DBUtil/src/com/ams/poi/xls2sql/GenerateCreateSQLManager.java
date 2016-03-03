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
 * <p>タイトル: GenerateCreateSQLManager</p>
 * <p>説明: 
 * XLSファイルDBテーブル定義書を読み取り
 * Create SQL ファイルを出力する。<BR>
 * 後でフォーマットをpropertiesファイルで指定できるようにする。
 * </p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
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

	// テーブル定義SheetName (Table##)
	private static final String TABLE_DEF_SHEETNAME = "Table";
  
  // 全CreateSQL出力ファイル名 (DROP, CREATE)
  public static final String CREATE_ALL_SQL_FILE = "create_all.sql";

  // 全CreateSQL出力ファイル名 (DROP なし, CREATEのみ)
  public static final String CREATE_ALL_NODROP_SQL_FILE = "create_all_first.sql";

  // JAVA ファイル出力ディレクトリ名
  private static final String JAVA_DIR = File.separator + "java";

  // Objective-C ファイル出力ディレクトリ名
private static final String OBJECTIVE_C_DIR = File.separator + "objective-c";

// JAVA(JDO) ファイル出力ディレクトリ名
private static final String JAVA_JDO_DIR = File.separator + "java_jdo";

	/**
	 * コンストラクタ
	 * @param xls_file XLSファイル
	 * @param out_dir 出力ディレクトリ
	 */
	public GenerateCreateSQLManager(String xls_file, String out_dir)
		throws FileNotFoundException, IOException {

		// Create POIFS
		fs = new POIFSFileSystem(new FileInputStream(xls_file));

		// Create WorkBook
		workbook = new HSSFWorkbook(fs);

		// get sheet
		//		HSSFSheet sheet = workbook.getSheet("Sheet1"); // 全角はだめ

		outDir = out_dir;
	}

	/**
	 * コンストラクタ
	 */
	private GenerateCreateSQLManager() {
		super();
	}

////	/**
////	 * generateCreateSQL<BR>
////	 * @param xls_file 読み込むXLSファイル
////	 * @param out_dir 出力するディレクトリ
////	 * @return 生成したSQLファイル数
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

      // 全シートに対してシート名が 'TableXxx' であれば
      // 解析してCreateSQL を出力する。

      for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

        String sheet_name = workbook.getSheetName(i);

        if (sheet_name.startsWith(TABLE_DEF_SHEETNAME)) {
          crate_sql_num += sheet2CreateSQLs(workbook.getSheetAt(i), dbms, output_sql_encode);
          create_sheet_num++;
        }
      }

      // 全SQLを出力(drop有)
      System.out.print("Export to " + CREATE_ALL_SQL_FILE + "...");
      String output_file = outDir + File.separator + CREATE_ALL_SQL_FILE;

      try {
        FileUtil.writeFile(output_file, getAllCreateSQL().toString(), output_sql_encode);
        System.out.println(" ok.");

      } catch (IOException e) {
        System.out.println(" fail.");
      } finally {
      }
      
      // 全SQLを出力(drop無)
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
	 * 引数のシートからテーブルを取得しCreateSQLファイルを作成。<BR>
	 * 1シートに複数のテーブル定義が記述されているとする。
	 * @param sheet テーブル定義が記述されているシート
     * @param dbms DBMS 
	 * @return 作成したSQLファイル数
	 * @throws Exception
	 */
	private int sheet2CreateSQLs(HSSFSheet sheet, DbmsType dbms, String output_sql_encode) throws Exception {

// short koumoku_col = 1;
		short table_col = 1;
		// final String KOUMOKU_COLUMN = "項目";
		final String TABLE_START_MARK = "(";

		CreateSQLFactory factory = new CreateSQLFactory();
		CreateSQL create_sql = factory.getCreateSQL(dbms);
		
		// Objective C DBMgr manager 
		ObjectiveCDBMgrMaker oc_dbmgr = new ObjectiveCDBMgrMaker();
		
		// create_all.sql prefix を追加
		allCreateSQL.append(create_sql.getCreateTableHeader());
		allCreateSQL_nodrop.append(create_sql.getCreateTableHeader());

		// テーブル解析 --
		Iterator rowit = sheet.rowIterator();
		int create_sql_num = 0;
		int r = 1;
		HSSFCell before_cell = null;
		while (rowit.hasNext()) {
			HSSFRow row = (HSSFRow) rowit.next();
			TableDef td = null;
			int table_row_top, table_row_bottom;

			// TODO ２行連続で nullだったらbreakするか
			if (before_cell == null && row.getCell(table_col) == null && row.getRowNum() > 0) {
				break;
			}
			before_cell = row.getCell(table_col);
			if (row.getCell(table_col) == null) {
				continue;
			}

			String cell_value = row.getCell(table_col).getStringCellValue();
			if (cell_value.startsWith(TABLE_START_MARK)) {

				// "(" の行 をテーブル先頭行として取得
				table_row_top = row.getRowNum();
				table_row_bottom = table_row_top;

				// テーブル定義最終行を探す
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

				// キャッシュに追加
				TableDefCache.add(td);

				System.out.print("Export to " + td.getTableNamePhysics()
						+ ".sql ...");

				// テーブル定義オプション
				setTableDefOptions(td);

				// テーブル定義をsqlファイルに出力(Drop有)
				String file_out = create_sql.getOutputFileString(td, true);
				String output_file = outDir + File.separator
						+ td.getTableNamePhysics() + ".sql";

				try {
					FileUtil
							.writeFile(output_file, file_out, output_sql_encode);
					System.out.println(" ok.");
					create_sql_num++;
					// 全ファイルバッファ(Drop有)に追加
					allCreateSQL.append(file_out);

				} catch (IOException e) {
					System.out.println(" fail.");
				} finally {
				}

				////// 出力1 create sql ///////////////
				// テーブル定義をsqlファイルに出力(Drop無)
				String file_out_nodrop = create_sql.getOutputFileString(td,
						false);
				// 全ファイルバッファ(Drop無し)に追加
				allCreateSQL_nodrop.append(file_out_nodrop);

				////// 出力2 Java Model  ///////////////
				outputForJava(output_sql_encode, td);
				
				////// 出力3 Objective-C  ///////////////
				outputForObjectiveC(output_sql_encode, td, oc_dbmgr);
				
				////// 出力4 JDO (for Google App Engine) ///////////
				outputForJdo(output_sql_encode, td);
				
			}
			r++;
		}
		
		// Objective-C DBMgrを出力
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
	 * JDOモデルを追加
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
		// TODO xxxById, findXxxAll, findXxx (2つ目の項目,コメント出力), create, insert, update
		// Table td の定義に基づいて各メソッドのソースを内部に出力
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
		// Java Bean 属性出力
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
	 * テーブル定義オプション設定
	 * @param td テーブル定義
	 */
	private void setTableDefOptions(TableDef td) {

		td.setDropTableEnable(true);
		td.setDropIndexEnable(false);
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
			if (n1 == -1 || n2 == -1) {
				// 解析不可
				return td;
			}
			td.setTableNameLogic(org_text.substring(n1 + 1, n2));
			td.setTableNamePhysics(org_text.substring(n2 + 1, n3));
			
			// ストーレジエンジン名(for MySQL)
			if (row.getCell(strage_engine_col) != null) {
			    td.setStrageEngineName(row.getCell(strage_engine_col).getStringCellValue());
			}
			
			// キャラクタセット(for MySQL)
			if (row.getCell(character_set_col) != null) {
				td.setCharacterSetName(row.getCell(character_set_col).getStringCellValue());
			}

			// データ圧縮(for MySQL)
			if (row.getCell(row_format_col) != null) {
				td.setRowFormatName(row.getCell(row_format_col).getStringCellValue());
			}
		}

		// 各フィールド情報を解析
		for (int r = table_row_top + 2; r <= table_row_bottom; r++) {
			FieldDef fd = new FieldDef();
			HSSFRow row = sheet.getRow(r);
			
			fd.setFieldNameLogic(row.getCell(koumoku).getStringCellValue());
			
			// 論理名がnullか""の場合は読み込み中止
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

			// default値 は 文字列 or 数値
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
				    // それ以外の場合はデフォルト値なし
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
				// インデックス未設定は無視
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
      
      // note (備考)
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
