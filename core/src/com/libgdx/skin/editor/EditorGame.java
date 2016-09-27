package com.libgdx.skin.editor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import com.libgdx.skin.editor.screen.WelcomeScreen;
import com.libgdx.skin.editor.utils.LazyBitmapFont;

public class EditorGame extends Game {

	@Override
	public void create() {
		VisUI.load(SkinScale.X1);
		GlobalData.game = this;
		GlobalData.fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(Res.font));
		GlobalData.skin = new Skin(Gdx.files.internal(Res.skinJson), new TextureAtlas(Res.skinAtlas));
		LazyBitmapFont.setGlobalGenerator(GlobalData.fontGenerator);

		setScreen(new WelcomeScreen());
	}

	@Override
	public void dispose() {
		super.dispose();
		GlobalData.dispose();
		if (screen != null) {
			screen.dispose();
		}
	}

}
