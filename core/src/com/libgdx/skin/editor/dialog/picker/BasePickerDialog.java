package com.libgdx.skin.editor.dialog.picker;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.reflect.Field;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.property.editor.actor.builder.PropertyEditorBuilder.OnEditorCall;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;

/**
 * @作者 Mitkey
 * @时间 2016年10月12日 下午4:34:50
 * @类说明:覆写 getPrefWidth()、getPrefHeight() 的必要性：show(stage) 时，会重新调用 setSize方法，<br>
 *         而该方法使用的 width、height 的参数即为 getPrefWidth()、 getPrefHeight() 的返回值
 * @版本 xx
 */
public abstract class BasePickerDialog extends Dialog {
	private static final float FullRate = .6f;

	CustomSkin projectSkin;
	Object styleObject;
	Field field;
	OnEditorCall onEditorCall;

	public BasePickerDialog(String title, CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) {
		super(title, GlobalData.skin);
		this.projectSkin = projectSkin;
		this.styleObject = styleObject;
		this.field = field;
		this.onEditorCall = onEditorCall;

		getColor().a = 0;
		// 按钮
		button("Cancel", false).key(Keys.ESCAPE, false).getButtonTable().padBottom(20);
	}

	@Override
	protected void result(Object object) {
		super.result(object);
		if (!(boolean) object) {
			hide();
		}
	}

	@Override
	public float getPrefWidth() {
		return GlobalData.WIDTH * FullRate;
	}

	@Override
	public float getPrefHeight() {
		return GlobalData.HEIGHT * FullRate;
	}

}
