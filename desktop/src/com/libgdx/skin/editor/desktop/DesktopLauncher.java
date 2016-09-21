package com.libgdx.skin.editor.desktop;

import java.io.IOException;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager;
import com.libgdx.skin.editor.EditorGame;

public class DesktopLauncher {
	static int width = 1280;
	static int height = 720;
	static float scale = 1.02f;
	public static void main(String[] arg) throws IOException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.y = 0;
		config.width = Math.round(width / scale);
		config.height = Math.round(height / scale);
		config.resizable = false;
		config.backgroundFPS = 1;
		config.vSyncEnabled = true;
		new LwjglApplication(new EditorGame(), config);

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// tooltip immediately show
		TooltipManager.getInstance().initialTime = 0f;
	}
}
