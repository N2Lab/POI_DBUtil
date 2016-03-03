package com.ams.poi.xls2sql.sqlfactory;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.ams.poi.xls2sql.bean.FieldDef;
import com.ams.poi.xls2sql.bean.TableDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableDefParseException;
import com.ams.poi.xls2sql.sqltypes.SQLITE3FieldTypes;

/**
 * <p>タイトル: DB2CreateSQL</p>
 * <p>説明: SQLITE3用CREATE SQL生成クラス</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/10/27</p>
 * @author 門田明彦
 * @version $Revision: 3244 $
 */
public class SQLITE3CreateSQL implements CreateSQL {

	/** デフォルトインデックス名接頭辞 */
	private static final String INDEX_PREFIX_DEF = "p_";

	/**
	 * コンストラクタ
	 */
	public SQLITE3CreateSQL() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.CreateSQL#getCreateSQL(com.ams.poi.xls2sql.bean.TableDef)
	 */
	public String getCreateSQL(TableDef td) throws TableDefParseException {
		StringBuffer out = new StringBuffer();
		int max_field = getMaxFieldLen(td);

		// CreateTable
		out.append("CREATE TABLE " + td.getTableNamePhysics() + " (\n");

		// CreateField
		Iterator it = td.getFieldList().iterator();
		while (it.hasNext()) {
			FieldDef fd = (FieldDef) it.next();

			// field name
			// "id" は "_id" に変換 (Objective-C対策)
			String field_name = fd.getFieldNamePhysics(); 
			if  (field_name.equals("id")) {
				field_name = "_" + field_name;
			}
			out.append(" " + field_name);
			out.append(
				StringUtils.repeat(
					" ",
					max_field - field_name.length() + 1));

			// type
			String type = SQLITE3FieldTypes.getTypeByXLS(fd.getTypeXls());
			out.append(type);

			// Default
			String defval = fd.getDefaultValue();
			if (!(fd.getDefaultValue() == null
				|| fd.getDefaultValue().equals(""))) {
			    
				if (type.startsWith("NUMBER")) {
					// NUMBER(整数)の場合は整数値に変換
					if (type.indexOf(",") != -1) {
						// 小数指定有り .. デフォルトのまま
					} else {
						// 整数指定 .. int 扱い
						defval = "" + (int)Double.parseDouble(defval);
					}
				} else if (type.startsWith("DATE")) {
				    // 日付の場合で 'yyyy/mm/dd' フォーマットの場合は
				    // DB2用 'yyyy-mm-dd' に変換する
//				    if (defval.matches("^[0-9]+/[0-9]+/[0-9]+$")) {
				        defval = defval.replace('/', '-');
//				    }
//				} else if (defval.startsWith("datetime")) {
//					// 日付の場合はそのまま出力
//				        defval = defval.replace('/', '-');
				}
					
				out.append("   DEFAULT " + defval);
			}

			// NOT NULL
			if (fd.isNotNull()) {
				out.append(" NOT NULL");
			}

			if (it.hasNext()) {
				out.append(",\n");
			} else {
				out.append("\n");
			}
		}

//		// pkey
//		if (td.getFieldList().size() > 0) {
//			out.append(
//				" PRIMARY KEY (");
//			int fdc = 0;
//			for(Iterator itf = td.getFieldList().iterator(); itf.hasNext();) {
//				FieldDef fd = (FieldDef)itf.next();
//				if (fd.isPrimaryKey()) {
//					out.append(fdc++ > 0 ? "," : "");
//					out.append(fd.getFieldNamePhysics());
//				}
//			}
//			out.append(") ) ");
//			if (td.getStrageEngineName() != null && td.getStrageEngineName().length() > 0) {
//			    out.append("TYPE = " + td.getStrageEngineName());
//			}
//			out.append(";\n\n");
//		}
		out.append(");\n\n");

		return out.toString();
	}

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.CreateSQL#getOutputFileString(com.ams.poi.xls2sql.bean.TableDef)
	 */
	public String getOutputFileString(TableDef td, boolean dropSql)
		throws TableDefParseException {
			StringBuffer out = new StringBuffer("");
		String pkey_name = INDEX_PREFIX_DEF + td.getTableNamePhysics();
		// int max_field = getMaxFieldLen(td);

		if (dropSql) {
			// DropTable
			if (td.isDropTableEnable()) {
				// DB2 では IF EXIST がない
				// out.append("DROP TABLE IF EXISTS " + td.getTableNamePhysics()
				// + ";\n");
				out.append("DROP TABLE " + td.getTableNamePhysics() + ";\n");
			}

			// DropIndex
			if (td.isDropIndexEnable()) {
				out.append("DROP INDEX " + pkey_name + ";\n\n");
			}
		}

		// add CreateSQL
		out.append(getCreateSQL(td));

		// add external INDEX
		out.append(getIndex(td));

		// else sql ....

		return out.toString();
	}

	/**
	 * getIndex<BR>
	 * キー以外のインデックスがあれば追加する
	 * @return インデックスSQL
	 */
	private String getIndex(TableDef td) {
		StringBuffer buf = new StringBuffer("");
		HashMap index_map = new HashMap();
		
		// インデックスのマップを作成してインデックスの数だけ
		// CREATE INDEX を出力
		for(Iterator it = td.getFieldList().iterator(); it.hasNext();) {
			FieldDef fd = (FieldDef) it.next();
			String index = fd.getIndexName();
			
			if (index != null && index.length() > 0) {
        index_map.put(index, fd);
//        index_map.put(index, index);
			}
		}
		
		// 各インデックスを解析
		for(Iterator it = index_map.keySet().iterator(); it.hasNext();) {
			String index = (String)it.next();
      FieldDef itfd = (FieldDef)index_map.get(index);
//      String index = itfd.getIndexName();
      String unique = itfd.isIndexUnique() ? "UNIQUE" : "";
      
			buf.append("CREATE " + unique +  " INDEX " +
				index + " ON " + td.getTableNamePhysics() + " (\n\t");
			
			// 対応するフィールドを探して追加
			int c = 0;
			for(Iterator i2 = td.getFieldList().iterator(); i2.hasNext();) {
				FieldDef fd = (FieldDef)i2.next();
				if (fd.getIndexName() != null &&
					fd.getIndexName().equals(index)) {
					if (c++ > 0) {
						buf.append(",");	
					}
					buf.append(fd.getFieldNamePhysics());
          
          if (fd.getIndexLength() > 0) {
            buf.append("(" + fd.getIndexLength() + ")");
          }
											
				}
			}
			buf.append("\n);\n");
		}
		
		return buf.toString();
	}

	/**
	 * getMaxFieldLen<BR>
	 * フィールド最大長を取得
	 * @param td テーブル定義
	 * @return フィールド最大長
	 */
	private int getMaxFieldLen(TableDef td) {
		int max_field = 0;
		Iterator it = td.getFieldList().iterator();
		while (it.hasNext()) {
			FieldDef fd = (FieldDef) it.next();
			max_field = Math.max(max_field, fd.getFieldNamePhysics().length());
		}

		return max_field;
	}

	/* (non-Javadoc)
	 * @see com.ams.poi.xls2sql.sqlfactory.CreateSQL#getCreateTableHeader()
	 */
	public String getCreateTableHeader() {
		return "BEGIN TRANSACTION;" + "\n";
	}
	
	public String getCreateTableFooter() {
		return "COMMIT;" + "\n";
	}

}
