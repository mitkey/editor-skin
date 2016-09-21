package com.libgdx.skin.editor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.libgdx.skin.editor.utils.MoveListener;
import com.libgdx.skin.editor.utils.scene2d.GeneralScreen;

/**
 * @作者 Mitkey
 * @时间 2016年9月21日 下午7:05:03
 * @类说明:
 * @版本 xx
 */
public class WelcomeScreen extends GeneralScreen {

	Texture texture;

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		texture = new Texture("badlogic.jpg");
		Image image = new Image(texture);
		image.setPosition(250, 150);
		image.addListener(new MoveListener(image));
		stage().addActor(image);

		InputProcessor processor = new InputMultiplexer(stage());
		Gdx.input.setInputProcessor(processor);
	}

	@Override
	protected void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void draw(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();

		texture.dispose();
	}

}
