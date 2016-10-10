package com.libgdx.skin.editor.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.property.editor.actor.builder.PropertyEditorBuilder;
import com.libgdx.skin.editor.property.editor.actor.builder.PropertyEditorBuilder.OnEditorCall;
import com.libgdx.skin.editor.property.editor.actor.builder.PropertyUtils;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;

/**
 * @作者 Mitkey
 * @时间 2016年9月29日 下午3:16:46
 * @类说明:选项面板
 * @版本 xx
 */
public class OptionPanel extends Table {
	private static final String tag = OptionPanel.class.getSimpleName();

	OnEditorCall onEditorCall;
	Table tableProperty;

	public OptionPanel(OnEditorCall onEditorCall) {
		super(GlobalData.skin);
		this.onEditorCall = onEditorCall;

		// 背景
		center().setBackground("default-pane");
		defaults().pad(5);
		// 操作面板说明
		add("Style Property", "title").row();

		Skin skin = getSkin();
		// 具体属性
		tableProperty = new Table(skin);
		tableProperty.defaults().left().pad(5);
		tableProperty.columnDefaults(0).width(170);
		tableProperty.columnDefaults(1).width(200);
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

	public void toggleStyle(CustomSkin projectSkin, Object selectStyleObject) {
		tableProperty.clear();
		tableProperty.add("Name", "error");
		tableProperty.add("Value", "error").row();

		Class<? extends Object> clazz = selectStyleObject.getClass();
		Field[] fields = ClassReflection.getFields(clazz);
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				String fieldName = field.getName();
				tableProperty.add(fieldName, PropertyUtils.mustProperty(clazz, fieldName) ? "default" : "optional");
				tableProperty.add(PropertyEditorBuilder.generationEditorActor(projectSkin, selectStyleObject, field, onEditorCall)).height(32).row();
			} catch (Exception e) {
				Gdx.app.error(tag, "generation " + field.getType() + " fail", e);
			}
		}
	}

}
