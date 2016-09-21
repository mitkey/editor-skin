package com.libgdx.skin.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.libgdx.skin.editor.screen.WelcomeScreen;
import com.libgdx.skin.editor.utils.LazyBitmapFont;

public class EditorGame extends Game {

	FreeTypeFontGenerator fontGenerator;

	@Override
	public void create() {
		GlobalData.GdxGame = this;
		LazyBitmapFont.setGlobalGenerator(fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf")));

		setScreen(new WelcomeScreen());
	}

	@Override
	public void dispose() {
		super.dispose();
		fontGenerator.dispose();
		if (screen != null) {
			screen.dispose();
		}
	}

}
