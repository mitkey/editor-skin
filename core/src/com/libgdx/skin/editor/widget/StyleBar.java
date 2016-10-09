package com.libgdx.skin.editor.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.screen.ProjectScreen;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;

/**
 * @作者 Mitkey
 * @时间 2016年9月28日 下午6:08:49
 * @类说明:
 * @版本 xx
 */
public class StyleBar extends Table {

	static final int lineCount = 7;

	ProjectScreen projectScreen;
	ButtonGroup<Button> buttonGroup;

	public StyleBar(ProjectScreen projectScreen) {
		super(GlobalData.skin);
		this.projectScreen = projectScreen;

		left().defaults().expand().fill().uniform().pad(5).getTable().setBackground("default-pane");

		buttonGroup = new ButtonGroup<Button>();
		int curIndex = 0;
		for (String styleName : CustomSkin.widgets) {
			curIndex++;
			add(createTextButton(styleName));
			if (curIndex % lineCount == 0) {
				row();
			}
		}

		// 设置默认选中
		buttonGroup.getButtons().first().setChecked(true);
	}

	Button createTextButton(String text) {
		TextButton textButton = new TextButton(text, getSkin(), "toggle");
		textButton.setUserObject(text);
		textButton.setProgrammaticChangeEvents(false);
		textButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				projectScreen.toggleStyle();
			}
		});

		buttonGroup.add(textButton);
		return textButton;
	}

	public String getSelectStyleType() {
		return (String) buttonGroup.getChecked().getUserObject();
	}

}
