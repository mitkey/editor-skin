package com.libgdx.skin.editor.property.editor.actor.builder;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;

/**
 * @作者 Mitkey
 * @时间 2016年10月9日 下午6:49:25
 * @类说明: style 属性编辑 actor 的构建者
 * @版本 xx
 */
public abstract class PropertyEditorBuilder {
	private static final ObjectMap<Class<?>, PropertyEditorBuilder> BUILDER_MAP = new ObjectMap<Class<?>, PropertyEditorBuilder>() {
		{
			putBuilder(new DefaultBuilder());
			putBuilder(new DrawableBuilder());
		}
		private void putBuilder(PropertyEditorBuilder builder) {
			put(builder.fieldType, builder);
		}
	};

	public Skin skin = GlobalData.skin;
	public Class<?> fieldType;

	public PropertyEditorBuilder(Class<?> clazz) {
		this.fieldType = clazz;
	}
	public abstract Actor buildActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException;

	protected ImageTextButton generationImageTextButton(String resName, boolean hasValue) {
		ImageTextButton imageTextButton = new ImageTextButton(resName, skin, hasValue ? "default" : "nullValue");
		// 超出内容剪切
		imageTextButton.setClip(true);
		// 可以变形：放大缩小
		imageTextButton.setTransform(true);
		return imageTextButton;
	}

	public static Actor generationEditorActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException {
		PropertyEditorBuilder editorBuilder = BUILDER_MAP.get(field.getType());
		if (editorBuilder == null) {
			editorBuilder = BUILDER_MAP.get(Object.class);
		}
		return editorBuilder.buildActor(projectSkin, styleObject, field, onEditorCall);
	}

	// ============================================

	/** 默认 */
	public static class DefaultBuilder extends PropertyEditorBuilder {
		public DefaultBuilder() {
			super(Object.class);
		}
		@Override
		public Actor buildActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException {
			// TODO use onEditorCall
			ImageTextButton imageTextButton = generationImageTextButton("un support type", false);
			return imageTextButton;
		}
	}
	/** Drawable */
	public static class DrawableBuilder extends PropertyEditorBuilder {
		public DrawableBuilder() {
			super(Drawable.class);
		}
		@Override
		public Actor buildActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException {
			// TODO use onEditorCall
			Drawable drawable = (Drawable) field.get(styleObject);

			String resName = "";
			ImageTextButton imageTextButton;
			if (drawable == null) {
				imageTextButton = generationImageTextButton(resName, false);
			} else {
				resName = projectSkin.resolveObjectName(fieldType, drawable);
				imageTextButton = generationImageTextButton(resName, true);
				drawable = projectSkin.newDrawable(drawable);
				drawable.setMinHeight(20);
				drawable.setMinWidth(20);
				imageTextButton.getStyle().imageUp = drawable;
			}
			return imageTextButton;
		}
	}
	public static class LabelStyleBuilder {
		// TODO
	}
	public static class ScrollPanelStyleBuilder {
		// TODO
	}
	public static class ListStyleBuilder {
		// TODO
	}

	public static class FloatBuilder {
		// TODO
	}
	public static class BitmapFontBuilder {
		// TODO
	}
	public static class ColorBuilder {
		// TODO
	}

	public static interface OnEditorCall {
		void call();
	}
}
