package com.libgdx.skin.editor.desktop;

import org.lwjgl.LWJGLException;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.libgdx.skin.editor.EditorGame;
import com.libgdx.skin.editor.GlobalData;

public class DesktopLauncher {
	public static void main(String[] arg) throws LWJGLException {
		if (GlobalData.HideTitleBar) {
			System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");// 隐藏头部的 bar
		}

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Editor Skin Json";
		config.width = GlobalData.WIDTH;
		config.height = GlobalData.HEIGHT;
		config.initialBackgroundColor = GlobalData.ColorBackground;
		config.resizable = false;
		config.vSyncEnabled = true;
		config.samples = 8;// 抗锯齿
		config.addIcon("icon.png", FileType.Internal);
		new LwjglApplication(new EditorGame(), config);

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// tooltip immediately show
		TooltipManager.getInstance().initialTime = 0f;
	}
}
