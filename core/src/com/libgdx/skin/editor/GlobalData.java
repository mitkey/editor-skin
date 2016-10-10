package com.libgdx.skin.editor;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

/**
 * @作者 Mitkey
 * @时间 2016年9月21日 下午6:54:05
 * @类说明:
 * @版本 xx
 */
public final class GlobalData {

	public static final boolean HideTitleBar = false;
	public static final Color ColorBackground = new Color(0x25252AFF);
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int Desk_WIDTH = 1156;
	public static final int Desk_HEIGHT = 650;

	public static final String projectSaveDir = "gdx-editor-skin-workspace";
	public static final FileType projectSaveFileType = FileType.External;

	public static final String projectSkinJson = "uiskin.json";
	public static final String projectSkinAtlas = "uiskin.atlas";
	public static final String projectSkinRaw = "raw";

	public static boolean monitorView = false;
	public static Game game;
	public static FreeTypeFontGenerator fontGenerator;
	public static Skin skin;

	private GlobalData() {
	}

	public static FileHandle getProject(String name) {
		FileHandle fileHandle = Gdx.files.getFileHandle(new File(projectSaveDir, name).getPath(), projectSaveFileType);
		if (!fileHandle.exists()) {
			fileHandle.mkdirs();
		}
		return fileHandle;
	}

	public static Array<FileHandle> getProjectList() {
		FileHandle[] list = Gdx.files.getFileHandle(projectSaveDir, projectSaveFileType).list(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				// 非目录或不可用的
				if (!pathname.isDirectory() && !isUseful(pathname)) {
					return false;
				}

				File skinAtlaFile = new File(pathname, projectSkinAtlas);
				File skinJsonFile = new File(pathname, projectSkinJson);
				File skinRaw = new File(pathname, projectSkinRaw);

				boolean hasProjectElement = isUseful(skinAtlaFile) && skinAtlaFile.isFile() && // 有效的、是文件
				isUseful(skinJsonFile) && skinJsonFile.isFile() && // 有效的、是文件
				isUseful(skinRaw) && skinRaw.isDirectory();// 有效果的、是目录

				return hasProjectElement;
			}

			// 不为 null、存在的、不是隐藏的、可读的、可写的
			boolean isUseful(File file) {
				return file != null && file.exists() && !file.isHidden() && file.canRead() && file.canWrite();
			}
		});

		// 最近修改日期排序
		Arrays.sort(list, new Comparator<FileHandle>() {
			@Override
			public int compare(FileHandle o1, FileHandle o2) {
				return -Long.compareUnsigned(o1.lastModified(), o2.lastModified());
			}
		});
		return new Array<FileHandle>(list);
	}

	public static void dispose() {
		if (skin != null) {
			skin.dispose();
			skin = null;
		}
		if (fontGenerator != null) {
			fontGenerator.dispose();
			fontGenerator = null;
		}
	}

}
