package com.libgdx.skin.editor.widget;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.libgdx.skin.editor.GlobalData;

/**
 * @作者 Mitkey
 * @时间 2016年9月29日 下午3:16:46
 * @类说明:选项面板
 * @版本 xx
 */
public class OptionPanel extends Table {

	Table tableProperty;

	public OptionPanel() {
		super(GlobalData.skin);

		// 背景
		center().setBackground("default-pane");
		defaults().pad(5);

		// 操作面板说明
		add("Style Property", "title").row();

		Skin skin = getSkin();

		// 具体属性
		tableProperty = new Table(skin);
		tableProperty.left().top().background("dialogDim");

		// 包裹具体属性的滚动面板
		ScrollPane scrollPane = new ScrollPane(tableProperty, skin);
		scrollPane.setScrollingDisabled(true, false);
		add(scrollPane).expand().fill().row();;
	}

	public void toggleStyle(Object curSelectStyleObject) {
		// TODO Auto-generated constructor stub
	}

}
