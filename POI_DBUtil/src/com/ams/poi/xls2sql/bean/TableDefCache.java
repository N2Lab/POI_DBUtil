package com.ams.poi.xls2sql.bean;

import java.util.HashMap;

/**
 * TableDef�L���b�V���Ǘ��N���X
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
     * �L���b�V����TableDef��ǉ�
     * @param td
     */
    public static final void add(TableDef td) {
        cache.put(td.getTableNamePhysics(), td);
    }
    
    /**
     * �L���b�V������TableDef���擾
     * @param table_name_physics �����e�[�u����
     * @return
     */
    public static final TableDef get(String table_name_physics) {
        return (TableDef) cache.get(table_name_physics);
    }
}
