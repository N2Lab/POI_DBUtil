package com.ams.poi.xls2sql.util;

/**
 * <p>プロジェクト名: POI_DBUtil</p>
 * <p>タイトル: EscapeUtil</p>
 * <p>説明: フィールド値等のEscape処理Utilクラス</p>
 * <p>Created on 2004/04/16</p>
 * @author 門田明彦
 * @version $Revision: 69 $
 */
public final class EscapeUtil {

  /**
   * escapeMySQL<BR>
   * ' などのMySQL文字列リテラルエスケープ対象文字を '' のようにエスケープして返す。<BR>
   * 他のエスケープ対象文字情報BR>
   * <A href="http://dev.mysql.com/doc/mysql/ja/String_syntax.html" target="_blank">
   * MySQL リファレンス http://dev.mysql.com/doc/mysql/ja/String_syntax.html</A>
   * 
   * @param val エスケープ対象文字列
   * @return エスケープ後文字列
   */
  public static String escapeMySQL(String val) {

    if (val == null || val.length() == 0) {
      return val;
    }

    // ' を '' に置換
    // ' の後が '' じゃないか確認した方がいいかも
    // 
    return val.replaceAll("'", "''");
    
  }

}
