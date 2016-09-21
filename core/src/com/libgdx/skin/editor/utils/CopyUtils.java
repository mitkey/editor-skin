package com.libgdx.skin.editor.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * @作者 Mitkey
 * @时间 2016年9月21日 下午3:47:06
 * @类说明:
 * @版本 xx
 */
public class CopyUtils {

	private CopyUtils() {
	}

	/**
	 * 把 FileType.Internal 中的数据 copy 到 FileType.Local
	 * 
	 * @param source
	 * @param target
	 */
	public static void copyI2L(String source, String target) {
		FileHandle local = Gdx.files.local(target);
		if (!local.exists()) {
			local.mkdirs();
		}

		FileHandle internal = Gdx.files.internal(source);
		if (internal.exists()) {
			internal.copyTo(local);
		} else {
			throw new GdxRuntimeException("File not found: " + internal.file() + " (" + internal.type() + ")");
		}
	}

}
