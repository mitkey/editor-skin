package com.libgdx.skin.editor.property.editor.actor.builder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Values;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.dialog.picker.BitmapFontPicker;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;

/**
 * @作者 Mitkey
 * @时间 2016年10月9日 下午6:49:25
 * @类说明: style 属性编辑 actor 的构建者
 * @版本 xx
 */
public abstract class PropertyEditorBuilder {
	private static final String DefaultSelectItem = "-------";
	private static final ObjectMap<Color, Texture> cachesColorPixmapMaps = new ObjectMap<Color, Texture>();
	private static final ObjectMap<Class<?>, PropertyEditorBuilder> BUILDER_MAP = new ObjectMap<Class<?>, PropertyEditorBuilder>() {
		{
			putBuilder(new DefaultBuilder());
			putBuilder(new DrawableBuilder());
			putBuilder(new LabelStyleBuilder());
			putBuilder(new ScrollPanelStyleBuilder());
			putBuilder(new ListStyleBuilder());
			putBuilder(new FloatBuilder());
			putBuilder(new BitmapFontBuilder());
			putBuilder(new ColorBuilder());
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

	protected final ImageTextButton generationImageTextButton(String text, boolean hasValue) {
		ImageTextButton imageTextButton = new ImageTextButton(text, skin, hasValue ? "default" : "nullValue");
		// 超出内容剪切
		imageTextButton.setClip(true);
		// 可以变形：放大缩小
		imageTextButton.setTransform(true);

		if (!hasValue) {
			ImageTextButtonStyle buttonStyle = new ImageTextButtonStyle(imageTextButton.getStyle());
			buttonStyle.fontColor = Color.RED;
			imageTextButton.setStyle(buttonStyle);
		}
		return imageTextButton;
	}
	protected final SelectBox<String> generationSelectBox(Array<String> list, String selectItem) {
		Array<String> newItems = new Array<String>();
		newItems.add(DefaultSelectItem);
		if (list != null) {
			newItems.addAll(list);
		}

		SelectBox<String> selectBox = new SelectBox<String>(skin);
		selectBox.setItems(newItems);
		selectBox.setSelected(selectItem == null ? DefaultSelectItem : selectItem);
		return selectBox;
	}

	public static final Actor generationEditorActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException {
		PropertyEditorBuilder editorBuilder = BUILDER_MAP.get(field.getType());
		if (editorBuilder == null) {
			editorBuilder = BUILDER_MAP.get(Object.class);
		}
		return editorBuilder.buildActor(projectSkin, styleObject, field, onEditorCall);
	}

	public static void dispose() {
		Values<Texture> iterator = cachesColorPixmapMaps.values().iterator();
		while (iterator.hasNext()) {
			Texture entry = iterator.next();
			entry.dispose();
			iterator.remove();
		}
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
			ImageTextButton imageTextButton = generationImageTextButton("=un support type=", false);

			ImageTextButtonStyle buttonStyle = new ImageTextButtonStyle(imageTextButton.getStyle());
			buttonStyle.fontColor = Color.RED;
			imageTextButton.setStyle(buttonStyle);
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
			Drawable drawable = (Drawable) field.get(styleObject);
			ImageTextButton imageTextButton;
			if (drawable == null) {
				imageTextButton = generationImageTextButton("not set", false);
			} else {
				imageTextButton = generationImageTextButton(projectSkin.resolveObjectName(fieldType, drawable), true);
				drawable = projectSkin.newDrawable(drawable);
				drawable.setMinHeight(20);
				drawable.setMinWidth(20);

				ImageTextButtonStyle buttonStyle = new ImageTextButtonStyle(imageTextButton.getStyle());
				buttonStyle.imageUp = drawable;
				imageTextButton.setStyle(buttonStyle);
			}

			// TODO use onEditorCall
			return imageTextButton;
		}
	}

	/** LabelStyle */
	public static class LabelStyleBuilder extends PropertyEditorBuilder {
		public LabelStyleBuilder() {
			super(LabelStyle.class);
		}
		@Override
		public Actor buildActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException {
			ObjectMap<String, ?> objectMap = projectSkin.getAll(fieldType);
			boolean noList = objectMap == null || objectMap.size == 0;

			String selectItem = projectSkin.resolveObjectName(fieldType, field.get(styleObject));
			SelectBox<String> selectBox = generationSelectBox(noList ? null : objectMap.keys().toArray(), selectItem);

			// TODO use onEditorCall
			return selectBox;
		}
	}

	/** ScrollPaneStyle */
	public static class ScrollPanelStyleBuilder extends PropertyEditorBuilder {
		public ScrollPanelStyleBuilder() {
			super(ScrollPaneStyle.class);
		}
		@Override
		public Actor buildActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException {
			ObjectMap<String, ?> objectMap = projectSkin.getAll(fieldType);
			boolean noList = objectMap == null || objectMap.size == 0;

			String selectItem = projectSkin.resolveObjectName(fieldType, field.get(styleObject));
			SelectBox<String> selectBox = generationSelectBox(noList ? null : objectMap.keys().toArray(), selectItem);

			// TODO use onEditorCall
			return selectBox;
		}
	}

	/** ListStyle */
	public static class ListStyleBuilder extends PropertyEditorBuilder {
		public ListStyleBuilder() {
			super(ListStyle.class);
		}
		@Override
		public Actor buildActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException {
			ObjectMap<String, ?> objectMap = projectSkin.getAll(fieldType);
			boolean noList = objectMap == null || objectMap.size == 0;

			String selectItem = projectSkin.resolveObjectName(fieldType, field.get(styleObject));
			SelectBox<String> selectBox = generationSelectBox(noList ? null : objectMap.keys().toArray(), selectItem);

			// TODO use onEditorCall
			return selectBox;
		}
	}

	/** float */
	public static class FloatBuilder extends PropertyEditorBuilder {
		public FloatBuilder() {
			super(float.class);
		}
		@Override
		public Actor buildActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException {
			float value = (float) field.get(styleObject);
			ImageTextButton imageTextButton = generationImageTextButton(String.valueOf(value), true);;

			// TODO use onEditorCall
			return imageTextButton;
		}
	}

	/** BitmapFont */
	public static class BitmapFontBuilder extends PropertyEditorBuilder {
		public BitmapFontBuilder() {
			super(BitmapFont.class);
		}
		@Override
		public Actor buildActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException {
			BitmapFont font = (BitmapFont) field.get(styleObject);
			ImageTextButton imageTextButton;
			if (font == null) {
				imageTextButton = generationImageTextButton("not set", false);
			} else {
				imageTextButton = generationImageTextButton(projectSkin.resolveObjectName(BitmapFont.class, font), true);

				ImageTextButtonStyle buttonStyle = new ImageTextButtonStyle(imageTextButton.getStyle());
				buttonStyle.font = font;
				imageTextButton.setStyle(buttonStyle);
			}
			imageTextButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					new BitmapFontPicker(projectSkin, styleObject, field, onEditorCall).show(imageTextButton.getStage());
				}
			});
			return imageTextButton;
		}
	}

	/** Color */
	public static class ColorBuilder extends PropertyEditorBuilder {
		public ColorBuilder() {
			super(Color.class);
		}
		@Override
		public Actor buildActor(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) throws ReflectionException {
			Color color = (Color) field.get(styleObject);
			ImageTextButton imageTextButton;
			if (color == null) {
				imageTextButton = generationImageTextButton("not set", false);
			} else {
				String text = projectSkin.resolveObjectName(Color.class, color);
				text += "(" + color.toString() + ")";
				imageTextButton = generationImageTextButton(text, true);

				ImageTextButtonStyle buttonStyle = new ImageTextButtonStyle(imageTextButton.getStyle());
				buttonStyle.imageUp = new SpriteDrawable(new Sprite(createColorPixmap(color)));
				imageTextButton.setStyle(buttonStyle);
			}

			// TODO use onEditorCall
			return imageTextButton;
		}
		private Texture createColorPixmap(Color color) {
			Texture texture;
			if (cachesColorPixmapMaps.containsKey(color)) {
				texture = cachesColorPixmapMaps.get(color);
			} else {
				Pixmap pixmap = new Pixmap(18, 18, Pixmap.Format.RGBA8888);
				pixmap.setColor(color);
				pixmap.fill();
				pixmap.setColor(Color.BLACK);
				pixmap.drawRectangle(0, 0, 18, 18);
				cachesColorPixmapMaps.put(color, texture = new Texture(pixmap));
				pixmap.dispose();
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			}
			return texture;
		}
	}

	public static interface OnEditorCall {
		void call();
	}
}
