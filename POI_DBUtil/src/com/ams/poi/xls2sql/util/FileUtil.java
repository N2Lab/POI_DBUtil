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
 * �v���W�F�N�g��: POI_DBUtil
 * </p>
 * <p>
 * �^�C�g��: FileUtil
 * </p>
 * <p>
 * ����:
 * </p>
 * <p>
 * Created on 2003/12/15
 * </p>
 * 
 * @author ��c���F
 * @version $Revision: 1202 $
 */
public final class FileUtil {

	/**
	 * writeFile<BR>
	 * �t�@�C���փe�L�X�g���o��
	 * 
	 * @param file
	 * @param text
	 * @parma encode
	 * @throws IOException
	 */
	public static void writeFile(String file, String text, String encode)
			throws IOException {
		// �t�@�C�������݂��Ȃ��ꍇ�͋�t�@�C�����쐬����

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
			System.err.println("�t�@�C�� " + file + " �ւ̏o�͎��s");
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * writeFile<BR>
	 * �t�@�C���փe�L�X�g���o��
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
		// �t�@�C�������݂��Ȃ��ꍇ�͋�t�@�C�����쐬����

		try {
			File f = new File(file);
			if (!f.isFile()) {
				System.err.println("�t�@�C�� " + f.getCanonicalPath() + " �����݂��Ȃ�");
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
			System.err.println("�t�@�C�� " + file + " �̃I�[�v���Ɏ��s");
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
