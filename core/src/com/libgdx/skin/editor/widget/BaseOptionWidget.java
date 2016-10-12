package com.libgdx.skin.editor.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.libgdx.skin.editor.GlobalData;

/**
 * @作者 Mitkey
 * @时间 2016年10月12日 上午10:31:56
 * @类说明:
 * @版本 xx
 */
public abstract class BaseOptionWidget extends Table {

	protected Table tableProperty;

	public BaseOptionWidget(String title, int nameWidth, int valueWidth) {
		super(GlobalData.skin);
		center().top().setBackground("default-pane");
		defaults().pad(5);

		add(title, "title").row();;

		Skin skin = getSkin();
		// 具体属性
		tableProperty = new Table(skin);
		tableProperty.defaults().left().pad(5);
		tableProperty.columnDefaults(0).width(nameWidth);
		tableProperty.columnDefaults(1).width(valueWidth);
		tableProperty.left().top().background("dialogDim");
		// 包裹具体属性的滚动面板
		ScrollPane scrollPane = new ScrollPane(tableProperty, skin);
		scrollPane.setScrollingDisabled(true, false);// 禁止x轴滚动
		scrollPane.setFadeScrollBars(false);// 滚动条不会淡出，即一直显示（滚动内容超出显示大小时）
		scrollPane.setFlickScroll(false);// 不回弹
		scrollPane.setScrollbarsOnTop(true);
		add(scrollPane).pad(5).expand().fill().row();
		addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				getStage().setScrollFocus(scrollPane);
			}
		});
	}

}
