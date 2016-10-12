package com.libgdx.skin.editor.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
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
public class OptionPanel extends BaseOptionWidget {
	private static final String tag = OptionPanel.class.getSimpleName();

	OnEditorCall onEditorCall;

	public OptionPanel(OnEditorCall onEditorCall) {
		super("Style Property", 170, 200);
		this.onEditorCall = onEditorCall;
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
