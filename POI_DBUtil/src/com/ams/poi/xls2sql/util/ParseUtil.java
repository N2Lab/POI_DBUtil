package com.ams.poi.xls2sql.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;

/**
 * @author Akihiko Monden
 *
 */
public class ParseUtil {

	/**
	 * テーブル定義を取得
	 * @param sheet 対象シート
	 * @param cell_text テーブル定義名 例)(XX)メニュー分類マスタ(MstMenuBunrui)
	 * @return テーブル定義
	 */
	public static TableDef getTableDef(HSSFSheet sheet, String cell_text) {
		TableDef td = new TableDef();

		final short table_all_col = 1;
		final short strage_engine_col = 2; // MySQL
		final short character_set_col = 3; // MySQL

		final short koumoku = 1;
		final short field_name = 2;
		final short type = 3;
		final short is_pkey = 4;
		final short is_null = 5;
		final short default_val = 6;
		final short index_unique = 7;
		final short index_name = 8;
		final short index_length = 9;
		final short note = 10;
		short table_row_top = 0;

		// String null_on = "×";
		String default_system = "System";

		// セルテキスト = cell_text の行を探す
		for (int i = 0;; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFRow nextRow = sheet.getRow(i + 1);
			if (row == null && nextRow == null) { System.err.println("not found table name:" + cell_text); return null;}
			if (row == null) continue;
			HSSFCell cell = row.getCell(table_all_col);
			HSSFCell nextCell = nextRow == null ? null : nextRow.getCell(table_all_col);

			if ((cell == null || StringUtils.isEmpty(cell.getStringCellValue())
					&& (nextCell == null || StringUtils.isEmpty(nextCell
							.getStringCellValue())))) {
				// テーブルみつからず
				System.err.println("not found table name:" + cell_text);
				return null;
			}

			if ((cell == null || StringUtils.isEmpty(cell.getStringCellValue())))
				continue;

			String org_text = row.getCell(table_all_col).getStringCellValue();
			if (org_text != null && org_text.equals(cell_text)) {

				// 解析開始
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
					td.setStrageEngineName(row.getCell(strage_engine_col)
							.getStringCellValue());
				}
				// キャラクタセット(for MySQL)
				if (row.getCell(character_set_col) != null) {
					td.setCharacterSetName(row.getCell(character_set_col)
							.getStringCellValue());
				}

				table_row_top = (short) i;
				break;
			}
		}

		// 各フィールド情報を解析
		for (int r = table_row_top + 2;; r++) {
			FieldDef fd = new FieldDef();
			HSSFRow row = sheet.getRow(r);
			if (row == null) {
				break;
			}

			HSSFCell cell = row.getCell(koumoku);
			if (cell == null || StringUtils.isEmpty(cell.getStringCellValue())) {
				break;
			}
			fd.setFieldNameLogic(row.getCell(koumoku).getStringCellValue());

			// 論理名がnullか""の場合は読み込み中止
			if (fd.getFieldNameLogic() == null
					|| "".equals(fd.getFieldNameLogic())) {
				break;
			}

			fd.setFieldNamePhysics(StringUtils.trim(row.getCell(field_name)
					.getStringCellValue()));
			fd.setTypeXls(StringUtils.trim(row.getCell(type)
					.getStringCellValue()));

			String pkey = StringUtils.trim(row.getCell(is_pkey)
					.getStringCellValue());
			fd.setPrimaryKey(!(pkey == null || pkey.length() == 0));

			String not_null = StringUtils.trim(row.getCell(is_null)
					.getStringCellValue());
			fd.setNotNull(!(not_null == null || not_null.length() == 0));

			// default値 は 文字列 or 数値
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

			default:
				// それ以外の場合はデフォルト値なし
				// System.out.println("* row " + r + ", unsupport type " +
				// row.getCell(default_val).getCellType());
				// continue;
			}

			// index
			if (row.getCell(index_unique) == null) {
				fd.setIndexUnique(false);
			} else {
				String isindex_unique = StringUtils.trim(row.getCell(
						index_unique).getStringCellValue());
				fd.setIndexUnique(!(isindex_unique == null || isindex_unique
						.length() == 0));
			}

			if (row.getCell(index_name) != null) {
				// インデックス未設定は無視
				try {
					fd.setIndexName(StringUtils.trim(row.getCell(index_name)
							.getStringCellValue()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (row.getCell(index_length).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					fd.setIndexLength((int) row.getCell(index_length)
							.getNumericCellValue());
				}
			}

			// note (備考)
			{
				HSSFCell note_cell = row.getCell(note);
				if (note_cell != null
						&& note_cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					fd.setNote(StringUtils.trim(row.getCell(note)
							.getStringCellValue()));

				}
			}

			td.getFieldList().add(fd);
		}

		return td;
	}

}
