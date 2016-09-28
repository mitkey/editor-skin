package com.libgdx.skin.editor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.Res;
import com.libgdx.skin.editor.utils.MoveListener;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;
import com.libgdx.skin.editor.utils.scene2d.GeneralScreen;

/**
 * @作者 Mitkey
 * @时间 2016年9月28日 下午4:33:15
 * @类说明:
 * @版本 xx
 */
public class ProjectScreen extends GeneralScreen {

	FileHandle project;
	CustomSkin customSkin;

	public ProjectScreen(String projectName) {
		project = GlobalData.getProject(projectName);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		customSkin = new CustomSkin(project.child(Res.skinJson.split("/")[1]), new TextureAtlas(project.child(Res.skinAtlas.split("/")[1])));

		Image image = new Image(customSkin.getDrawable("default"));
		image.setPosition(200, 200);
		image.addListener(new MoveListener(image));
		stage().addActor(image);

		InputProcessor processor = new InputMultiplexer(stage(), new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if (Keys.ESCAPE == keycode) {
					// 返回欢迎页
					GlobalData.game.setScreen(new WelcomeScreen());
				}
				return super.keyDown(keycode);
			}
		});
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
		super.dispose();
		if (customSkin != null) {
			customSkin.dispose();
			customSkin = null;
		}
	}

}
