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
import com.libgdx.skin.editor.property.editor.actor.builder.PropertyEditorBuilder;
import com.libgdx.skin.editor.property.editor.actor.builder.PropertyEditorBuilder.OnEditorCall;
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

	CustomSkin customSkin;

	StyleBar styleBar;
	OptionPanel optionPanel;
	PreviewPanel previewPanel;
	StylePanel stylePanel;
	PreviewOptionPanel previewOptionPanel;

	OnEditorCall onEditorCall = new OnEditorCall() {
		@Override
		public void call() {
			optionPanel.toggleStyle(customSkin, stylePanel.getSelectStyleObject());
			previewPanel.updatePreview(customSkin,stylePanel.getSelectStyleObject());
			// TODO 调用其他 panel 中绘制信息。如 previewPanel 或 previewOptionPanel
		}
	};

	public ProjectScreen(String projectName) {
		GlobalData.projectFileHandle = GlobalData.getProject(projectName);
	}

	@Override
	public void show() {
		super.show();
		FileHandle projectFileHandle = GlobalData.projectFileHandle;
		FileHandle skinFile = projectFileHandle.child(Res.skinJson.split("/")[1]);
		TextureAtlas textureAtlas = new TextureAtlas(projectFileHandle.child(Res.skinAtlas.split("/")[1]));
		customSkin = new CustomSkin(skinFile, textureAtlas);

		// 根 group
		Table tableRoot = new Table();
		tableRoot.setFillParent(true);
		stage().addActor(tableRoot);

		// 样式类型切换 bar
		tableRoot.add(styleBar = new StyleBar(this)).expandX().fillX().top().colspan(3).row();
		// 选项面板
		tableRoot.add(optionPanel = new OptionPanel(onEditorCall)).expandY().fillY().left().width(420);
		// 预览面板
		tableRoot.add(previewPanel = new PreviewPanel()).expand().fill();
		// 右边上下两部分
		Table tableRight = new Table();
		tableRight.defaults().expand().fill().uniform();
		tableRoot.add(tableRight).width(GlobalData.WIDTH / 4).expandY().fillY().right();
		// 样式面板
		tableRight.add(stylePanel = new StylePanel(customSkin, onEditorCall)).row();
		// 预览选项面板
		tableRight.add(previewOptionPanel = new PreviewOptionPanel());

		setInputProcessor();
		toggleStyle();
	}

	private void setInputProcessor() {
		// 用户输入监听
		InputProcessor processor = new InputMultiplexer(stage(), new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if (Keys.ESCAPE == keycode) {// 返回欢迎页
					GlobalData.game.setScreen(new WelcomeScreen());
				}
				return super.keyDown(keycode);
			}
		});
		Gdx.input.setInputProcessor(processor);
	}

	public void toggleStyle() {
		String selectStyleType = styleBar.getSelectStyleType();
		Gdx.app.debug(tag, "toggle style type:" + selectStyleType);

		Class<?> styleClass;
		try {
			styleClass = Class.forName("com.badlogic.gdx.scenes.scene2d.ui." + selectStyleType + "$" + selectStyleType + "Style");
		} catch (ClassNotFoundException e) {
			Gdx.app.error(tag, "can find style class", e);
			return;
		}
		// style name list 改变
		stylePanel.changeStyleType(styleClass);
	}

	@Override
	public void dispose() {
		super.dispose();
		PropertyEditorBuilder.dispose();
		if (customSkin != null) {
			customSkin.dispose();
			customSkin = null;
		}
	}

}
