package com.libgdx.skin.editor;

import java.io.File;

/**
 * @作者 Mitkey
 * @时间 2016年9月22日 下午8:15:36
 * @类说明:
 * @版本 xx
 */
public class Res {

	public static final String font = "font.ttf";
	public static final String skinJson = "resources/uiskin.json";
	public static final String skinAtlas = "resources/uiskin.atlas";

	public static void main(String[] args) {
		String projectname = System.getProperty("user.dir").replace("desktop", "core");
		File[] listFiles = new File(projectname + "/assets/resources").listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			File file = listFiles[i];
			if (file.isDirectory()) {
				File[] listFiles2 = file.listFiles();
				for (File file2 : listFiles2) {
					String format = "Gdx.files.internal(\"resources/%s/%s\").copyTo(projectRaw);";
					System.err.println(String.format(format, file.getName(), file2.getName()));
				}
			} else {
				String format = "Gdx.files.internal(\"resources/%s\").copyTo(project);";
				System.err.println(String.format(format, file.getName()));
			}
		}
	}

}
