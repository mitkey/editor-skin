package com.libgdx.skin.editor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.Res;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;
import com.libgdx.skin.editor.utils.scene2d.GeneralScreen;
import com.libgdx.skin.editor.widget.OptionPanel;
import com.libgdx.skin.editor.widget.PreviewOptionPanel;
import com.libgdx.skin.editor.widget.PreviewPanel;
import com.libgdx.skin.editor.widget.StyleBar;
import com.libgdx.skin.editor.widget.StylePanel;

/**
 * @作者 Mitkey
 * @时间 2016年9月28日 下午4:33:15
 * @类说明:
 * @版本 xx
 */
public class ProjectScreen extends GeneralScreen {
	private static final String tag = ProjectScreen.class.getSimpleName();

	public CustomSkin customSkin;

	FileHandle project;

	StyleBar styleBar;
	OptionPanel optionPanel;
	PreviewPanel previewPanel;
	StylePanel stylePanel;
	PreviewOptionPanel previewOptionPanel;

	public ProjectScreen(String projectName) {
		project = GlobalData.getProject(projectName);
	}

	@Override
	public void show() {
		super.show();
		customSkin = new CustomSkin(project.child(Res.skinJson.split("/")[1]), new TextureAtlas(project.child(Res.skinAtlas.split("/")[1])));

		// 根 group
		Table tableRoot = new Table(customSkin);
		tableRoot.setFillParent(true);
		stage().addActor(tableRoot);

		// 样式类型切换 bar
		styleBar = new StyleBar(this);
		tableRoot.add(styleBar).expandX().fillX().top().colspan(3).row();
		// 选项面板
		optionPanel = new OptionPanel(this);
		tableRoot.add(optionPanel).expandY().fillY().left().width(GlobalData.WIDTH / 4);
		// 预览面板
		previewPanel = new PreviewPanel(this);
		tableRoot.add(previewPanel).expand().fill();
		// 右边上下两部分
		{
			Table tableRight = new Table(customSkin);
			tableRight.defaults().expand().fill().uniform();
			tableRoot.add(tableRight).width(GlobalData.WIDTH / 4).expandY().fillY().right();

			// 样式面板
			stylePanel = new StylePanel(this);
			tableRight.add(stylePanel).row();

			// 预览选项面板
			previewOptionPanel = new PreviewOptionPanel(this);
			tableRight.add(previewOptionPanel);
		}

		// 用户输入监听
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

		toggleStyle();
	}

	public void toggleStyle() {
		Gdx.app.debug(tag, "toggle style type:" + styleBar.getSelectStyleType());

		// TODO
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
