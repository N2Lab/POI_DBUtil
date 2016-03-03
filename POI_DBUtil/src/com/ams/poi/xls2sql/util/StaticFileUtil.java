package com.ams.poi.xls2sql.util;


/**
 * @author Akihiko Monden
 * 
 */
public class StaticFileUtil {

	/**
	 * @param string
	 * @param string2
	 */
	public static void output(String resource, String output_path,
			String output_java_encode) {
		System.out.print("Export " + resource + "to " + output_path);
		try {
			String source = FileUtil.readFile(resource);
			FileUtil.writeFile(output_path, source, output_java_encode);
			System.out.println("ok." + output_path);
		} catch (Exception e) {
			System.out.println(" Export fail." + e.getMessage());
		}

	}

}
