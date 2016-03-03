package com.ams.poi.xls2sql.sqlfactory;

import java.util.ArrayList;
import java.util.HashMap;

import com.ams.poi.xls2sql.bean.TableValuesDef;
import com.ams.poi.xls2sql.sqlfactory.exceptions.TableValuesDefParseException;

/**
 * <p>タイトル: InsertSQL</p>
 * <p>説明: InsertSQL ファイル生成クラスのインターフェース</p>
 * <p>著作権: Copyright (c) 2009 PowerBEANS Inc</p>
 * <p>PowerBEANS.Inc</p>
 * <p>Created on 2003/07/28</p>
 * @author 門田明彦
 * @version 1.0
 */
public abstract class InsertSQL {
	
	/** DELETE FROM tableName 文を追加するかどうかのフラグ, true 追加しない */
	boolean add_only; 
    private HashMap options = new HashMap();

	/**
	 * getInsertSQL<BR>
	 * INSERT SQL のみ取得
	 * @param tvd テーブル定義
	 * @param rec_list １レコードデータ
	 * @return INSERT SQL
	 * @throws TableValuesDefParseException
	 */
	public abstract String getInsertSQL(TableValuesDef tvd, ArrayList rec_list)  throws TableValuesDefParseException;

	/**
	 * getOutputFileString<BR>
	 * CreateSQL ファイル出力文字列取得
	 * @return ファイル出力文字列
	 */
	public abstract String getOutputFileString(TableValuesDef tvd) throws TableValuesDefParseException;

	/**
	 * @return Returns the is_add_only.
	 */
	public boolean isAdd_only() {
		return add_only;
	}

	/**
	 * @param is_add_only The is_add_only to set.
	 */
	public void setAdd_only(boolean is_add_only) {
		this.add_only = is_add_only;
	}

    /**
     * オプションHashMapを格納
     * @param opt_map
     */
    public void setOptions(HashMap opt_map) {
        this.options = opt_map;
    }

    /**
     * @return Returns the options.
     */
    public HashMap getOptions() {
        return options;
    }
}
