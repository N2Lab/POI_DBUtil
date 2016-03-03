package com.ams.poi.xls2sql.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * <p>
 * プロジェクト名: POI_DBUtil
 * </p>
 * <p>
 * タイトル: FileUtil
 * </p>
 * <p>
 * 説明:
 * </p>
 * <p>
 * Created on 2003/12/15
 * </p>
 * 
 * @author 門田明彦
 * @version $Revision: 1202 $
 */
public final class FileUtil {

	/**
	 * writeFile<BR>
	 * ファイルへテキストを出力
	 * 
	 * @param file
	 * @param text
	 * @parma encode
	 * @throws IOException
	 */
	public static void writeFile(String file, String text, String encode)
			throws IOException {
		// ファイルが存在しない場合は空ファイルを作成する

		File f = new File(file);
		if (!f.isFile()) {
			File parent_dir = f.getParentFile();
			parent_dir.mkdirs();
			f.createNewFile();
		}

		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), encode));

			writer.write(text);
			writer.close();

		} catch (IOException e) {
			System.err.println("ファイル " + file + " への出力失敗");
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * writeFile<BR>
	 * ファイルへテキストを出力
	 * 
	 * @param file
	 * @param text
	 * @throws IOException
	 */
	public static void writeFile(String file, String text) throws IOException {
		writeFile(file, text, "UTF-8");
	}

	/**
	 * @param encode 
	 * @param resource
	 * @return
	 * @throws IOException 
	 */
	public static String readFile(String file, String encode) throws IOException {
		// ファイルが存在しない場合は空ファイルを作成する

		try {
			File f = new File(file);
			if (!f.isFile()) {
				System.err.println("ファイル " + f.getCanonicalPath() + " が存在しない");
				return null;
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), encode));

			StringBuilder buf = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buf.append(line);
				buf.append("\r\n");
			}

			return buf.toString();
		} catch (IOException e) {
			System.err.println("ファイル " + file + " のオープンに失敗");
			e.printStackTrace();
			throw e;
		}
	}
	
	
	/**
	 * @param encode 
	 * @param resource
	 * @return
	 * @throws IOException 
	 */
	public static String readFile(String file) throws IOException {
		return readFile(file, "UTF-8");
	}
}
