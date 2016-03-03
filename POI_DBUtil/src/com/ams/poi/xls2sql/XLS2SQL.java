package com.ams.poi.xls2sql;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.ams.poi.xls2sql.sqlfactory.DbmsType;
import com.ams.poi.xls2sql.util.Path;
import com.ams.poi.xls2sql.util.StaticFileUtil;

/**
 * <p>タイトル: XLS2SQL</p>
 * <p>説明: XLS > SQL 出力メインクラス
 * 
 * Mroonga無効化モード (AWSなど) 
 * "I:\DropBoxMondenpowerbeans\Dropbox\Work\opt\LINEビジネスコネクト\4.DB設計\【ビジコネ】テーブル設計_TableAllList.xls" I:\DropBoxMondenpowerbeans\Dropbox\Work\opt\LINEビジネスコネクト\4.DB設計\sql mysql UTF8 UTF8 utf8mb4 utf8 MROONGA_DISABLE
 *
 * Mroonga有効モード (IDCFなど)
 * "I:\DropBoxMondenpowerbeans\Dropbox\Work\opt\LINEビジネスコネクト\4.DB設計\【ビジコネ】テーブル設計_TableAllList.xls" I:\DropBoxMondenpowerbeans\Dropbox\Work\opt\LINEビジネスコネクト\4.DB設計\sql mysql UTF8 UTF8 utf8mb4 utf8  MYSQL_UTF8MB4_DISABLE
 * 
 * </p>
 * <p>Created on 2003/07/25</p>
 * @author 門田明彦
 * @version 1.0
 */
public class XLS2SQL {

	public static void main(String[] args) throws Exception {

		String xls_file = "XLS2SQL.xls";
		String out_dir = "";
		DbmsType dbms = DbmsType.ORACLE;
		String output_sql_encode = "EUC";
		String output_java_encode = "MS932";
		String output_jsp_encode = "Windows-31J";

		String set_char_set = ""; // MySQL only

		if (args.length < 5) {
			// usage
			printUsage();
			return;
		}

		xls_file = args[0];
		out_dir = args[1];
		dbms = DbmsType.valueOf(args[2].toUpperCase());
		output_sql_encode = args[3];
		output_java_encode = args[4];

		if (args.length >= 6) {
			set_char_set = args[5];
		}

		if (args.length >= 7) {
			output_jsp_encode = args[6];
		}
		
		// set param to env
		System.setProperty("dbms", dbms.name());
		
		for (String arg : args) {
			System.setProperty(arg, arg);
		}

		// generate CreateSQL files -----------------
		{
		GenerateCreateSQLManager mgr;
		int num = 0;
		try {
			mgr = new GenerateCreateSQLManager(xls_file, out_dir);
			num = mgr.generateCreateSQL(dbms, output_sql_encode,
					output_java_encode);

		} catch (FileNotFoundException e) {
			System.err.println("ファイル:" + xls_file + " の読み取りエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		} catch (IOException e) {
			System.err.println("ファイル:" + xls_file + " のI/Oエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		}

		System.out.println("出力ディレクトリ:" + out_dir);
		System.out.println("Create SQL ファイルを " + num + "ファイル出力しました。");
		System.out.println("全 Create SQL を "
				+ GenerateCreateSQLManager.CREATE_ALL_SQL_FILE + " に出力しました。");
		System.out.println("全 Create SQL(DROP無) を "
				+ GenerateCreateSQLManager.CREATE_ALL_NODROP_SQL_FILE
				+ " に出力しました。");
		}

		// generate InsertSQL files -----------
		{
		GenerateInsertSQLManager imgr;
		int num2 = 0;
		try {
			imgr = new GenerateInsertSQLManager(xls_file, out_dir);
			imgr.setOption("set_char_set", set_char_set);
			num2 = imgr.generateInsertSQL(dbms, output_sql_encode,
					output_java_encode);
		} catch (FileNotFoundException e) {
			System.err.println("ファイル:" + xls_file + " の読み取りエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		} catch (IOException e) {
			System.err.println("ファイル:" + xls_file + " のI/Oエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		}

		System.out.println("Insert SQL ファイルを " + num2 + "ファイル出力しました。");
		System.out.println("全 Insert SQL を "
				+ GenerateInsertSQLManager.INSERT_ALL_SQL_FILE + " に出力しました。");
		}
		
		// generate JAVA Enum files -----------
		generateJavaEnum(xls_file, out_dir, dbms, output_sql_encode,
				output_java_encode, output_jsp_encode, set_char_set);
		
		// generate Jersey Resourse files -----------
		generateJavaResource(xls_file, out_dir, dbms, output_sql_encode,
				output_java_encode, output_jsp_encode, set_char_set);
		
		// generate CakePHP Model files -----------
		generateCakePHPModels(xls_file, out_dir, dbms, output_sql_encode,
				output_java_encode, output_jsp_encode, set_char_set);

		// generate Rails Model files -----------
		generateRailsModels(xls_file, out_dir, dbms, output_sql_encode,
				output_java_encode, output_jsp_encode, set_char_set);

	}

	/**
	 * output JAVA Enum
	 * @param xls_file
	 * @param out_dir
	 * @param dbms
	 * @param output_sql_encode
	 * @param output_java_encode
	 * @param output_jsp_encode
	 * @param set_char_set
	 * @throws Exception
	 */
	private static void generateJavaEnum(String xls_file, String out_dir,
			DbmsType dbms, String output_sql_encode, String output_java_encode,
			String output_jsp_encode, String set_char_set) throws Exception {
		{
		GenerateJavaEnumManager imgr;
		int num3 = 0;
		try {
			imgr = new GenerateJavaEnumManager(xls_file, out_dir);
			imgr.setOption("set_char_set", set_char_set);
			num3 = imgr.generateEnums(dbms, output_sql_encode,
					output_java_encode, output_jsp_encode);
		} catch (FileNotFoundException e) {
			System.err.println("ファイル:" + xls_file + " の読み取りエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		} catch (IOException e) {
			System.err.println("ファイル:" + xls_file + " のI/Oエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		}

		System.out.println("JAVA Enum ファイルを " + num3 + "ファイル出力しました。");
		}
	}
	
	/**
	 * output JAVA JerseyResource
	 * @param xls_file
	 * @param out_dir
	 * @param dbms
	 * @param output_sql_encode
	 * @param output_java_encode
	 * @param output_jsp_encode
	 * @param set_char_set
	 * @throws Exception
	 */
	private static void generateJavaResource(String xls_file, String out_dir,
			DbmsType dbms, String output_sql_encode, String output_java_encode,
			String output_jsp_encode, String set_char_set) throws Exception {
		{
		GenerateJavaJerseyResourceManager imgr;
		int num3 = 0;
		try {
			imgr = new GenerateJavaJerseyResourceManager(xls_file, out_dir);
			imgr.setOption("set_char_set", set_char_set);
			num3 = imgr.generate(dbms, output_sql_encode,
					output_java_encode, output_jsp_encode);
			
			if (num3 > 0) {
				// 1件以上出力したら固定ソースクラスを出力
				StaticFileUtil.output("resource/java/POQLManagerFactory.java", out_dir + "/" + Path.JAVA_API_RESOURCE + "/POQLManagerFactory.java" , output_java_encode);
				StaticFileUtil.output("resource/java/poql.properties", out_dir + "/" + Path.JAVA_API_RESOURCE + "/poql.properties" , output_java_encode);
				
			}
		} catch (FileNotFoundException e) {
			System.err.println("ファイル:" + xls_file + " の読み取りエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		} catch (IOException e) {
			System.err.println("ファイル:" + xls_file + " のI/Oエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		}

		System.out.println("JAVA WEBAPI Jersey Resource ファイルを " + num3 + "ファイル出力しました。");
		}
	}

	/**
	 * output cakephp2.x model class
	 * @param xls_file
	 * @param out_dir
	 * @param dbms
	 * @param output_sql_encode
	 * @param output_java_encode
	 * @param output_jsp_encode
	 * @param set_char_set
	 * @throws Exception
	 */
	private static void generateCakePHPModels(String xls_file, String out_dir,
			DbmsType dbms, String output_sql_encode, String output_java_encode,
			String output_jsp_encode, String set_char_set) throws Exception {
		{
		int num3 = 0;
		try {
			GenerateCakePHPModelManager imgr = new GenerateCakePHPModelManager(xls_file, out_dir);
			
//			imgr.setOption("set_char_set", set_char_set);
			num3 = imgr.generate(dbms, output_sql_encode,
					output_java_encode, output_jsp_encode);
			
			if (num3 > 0) {
//				// 1件以上出力したら固定ソースクラスを出力
//				StaticFileUtil.output("resource/java/POQLManagerFactory.java", out_dir + "/" + Path.JAVA_API_RESOURCE + "/POQLManagerFactory.java" , output_java_encode);
//				StaticFileUtil.output("resource/java/poql.properties", out_dir + "/" + Path.JAVA_API_RESOURCE + "/poql.properties" , output_java_encode);
				
			}
		} catch (FileNotFoundException e) {
			System.err.println("ファイル:" + xls_file + " の読み取りエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		} catch (IOException e) {
			System.err.println("ファイル:" + xls_file + " のI/Oエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		}

		System.out.println("Cake PHP Model ファイルを " + num3 + "ファイル出力しました。");
		}
	}

	/**
	 * output cakephp2.x model class
	 * @param xls_file
	 * @param out_dir
	 * @param dbms
	 * @param output_sql_encode
	 * @param output_java_encode
	 * @param output_jsp_encode
	 * @param set_char_set
	 * @throws Exception
	 */
	private static void generateRailsModels(String xls_file, String out_dir,
			DbmsType dbms, String output_sql_encode, String output_java_encode,
			String output_jsp_encode, String set_char_set) throws Exception {
		{
		int num3 = 0;
		try {
			GenerateRailsModelManager imgr = new GenerateRailsModelManager(xls_file, out_dir);
			
//			imgr.setOption("set_char_set", set_char_set);
			num3 = imgr.generate(dbms, output_sql_encode,
					output_java_encode, output_jsp_encode);
			
			if (num3 > 0) {
//				// 1件以上出力したら固定ソースクラスを出力
//				StaticFileUtil.output("resource/java/POQLManagerFactory.java", out_dir + "/" + Path.JAVA_API_RESOURCE + "/POQLManagerFactory.java" , output_java_encode);
//				StaticFileUtil.output("resource/java/poql.properties", out_dir + "/" + Path.JAVA_API_RESOURCE + "/poql.properties" , output_java_encode);
				
			}
		} catch (FileNotFoundException e) {
			System.err.println("ファイル:" + xls_file + " の読み取りエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		} catch (IOException e) {
			System.err.println("ファイル:" + xls_file + " のI/Oエラー\n"
					+ e.getMessage());
			printUsage();
			return;
		}

		System.out.println("Rails Model ファイルを " + num3 + "ファイル出力しました。");
		}
	}
		
	/**
   * printUsage<BR>
   * 
   */
  public static void printUsage() {
    System.out.println("[usage]\njava -jar xls2sql.jar tableDef.xls outDir dbms(oracle|mysql|db2|sqlite3) outputSQLEncode outputJavaEncode [mysql charset] [outputJspEncode]");
    System.out.println("[example]\njava -jar xls2sql.jar tableDef.xls C:\\tmp mysql EUC MS932 utf8 Windows-31J");
	}
}
