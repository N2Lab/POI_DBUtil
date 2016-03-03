package com.ams.poi.xls2sql.bean;

import java.util.HashMap;

/**
 * TableDefキャッシュ管理クラス
 * @author A.Monden
 */
public class TableDefCache {

    private static HashMap cache = new HashMap();
    
    /**
     * 
     */
    private TableDefCache() {
        super();
    }
    /**
     * キャッシュにTableDefを追加
     * @param td
     */
    public static final void add(TableDef td) {
        cache.put(td.getTableNamePhysics(), td);
    }
    
    /**
     * キャッシュからTableDefを取得
     * @param table_name_physics 物理テーブル名
     * @return
     */
    public static final TableDef get(String table_name_physics) {
        return (TableDef) cache.get(table_name_physics);
    }
}
