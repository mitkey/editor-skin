package com.libgdx.skin.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @作者 Mitkey
 * @时间 2016年9月21日 下午6:54:05
 * @类说明:
 * @版本 xx
 */
public final class GlobalData {

	public static final boolean HideTitleBar = false;
	public static final Color ColorBackground = new Color(0x25252AFF);
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 576;

	public static Game game;
	public static Skin skin;
	public static FreeTypeFontGenerator fontGenerator;

	private GlobalData() {
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
