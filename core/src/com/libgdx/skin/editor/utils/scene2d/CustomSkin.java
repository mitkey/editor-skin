/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.libgdx.skin.editor.utils.scene2d;

import java.io.StringWriter;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.PrettyPrintSettings;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class CustomSkin extends com.badlogic.gdx.scenes.scene2d.ui.Skin {
	public final static String[] widgets = {"Label", "Button", "TextButton", "ImageButton", "ImageTextButton", "TextTooltip", //
			"CheckBox", "TextField", "List", "SelectBox", "ProgressBar", //
			"Slider", "ScrollPane", "SplitPane", "Window", "Touchpad", "Tree"};

	// 定义需要进行 save 到 skin json 文件的资源 type，并且有写入和加载排序的作用
	private static final Array<Class<? extends Object>> saveResoureTypes = new Array<Class<? extends Object>>() {
		{
			// 某些顺序不能改变。如 ListStyle 必须在 SelectBoxStyle 前面，//
			// 因为解析 SelectBoxStyle 时它的字段配置中包含 ListStyle ，而 ListStyle 还未加载
			// 在执行到 get (String name, Class<T> type) 的 resources.get(type) 是否为 null 时会抛出异常
			add(com.badlogic.gdx.graphics.g2d.BitmapFont.class);
			add(com.badlogic.gdx.graphics.Color.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.Skin.TintedDrawable.class);// TODO 加上该样式的管理
			add(com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle.class);

			add(com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.Tree.TreeStyle.class);
			add(com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle.class);

			add(com.badlogic.gdx.scenes.scene2d.ui.TextTooltip.TextTooltipStyle.class);
		}
	};

	public CustomSkin() {
	}
	public CustomSkin(FileHandle skinFile) {
		super(skinFile);
	}

	public CustomSkin(TextureAtlas atlas) {
		super(atlas);
	}
	public CustomSkin(FileHandle skinFile, TextureAtlas atlas) {
		super(skinFile, atlas);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	protected Json getJsonLoader(FileHandle skinFile) {
		Json jsonLoader = super.getJsonLoader(skinFile);

		// 覆盖父类 TintedDrawable 对应的 ReadOnlySerializer
		jsonLoader.setSerializer(TintedDrawable.class, new ReadOnlySerializer() {
			public Object read(Json json, JsonValue jsonData, Class type) {
				/**
				 * <pre>
				 * ######################## 
				 * 父类实现 TintedDrawable 的 ReadOnlySerializer read 方法 逻辑
				 * ########################
				 * </pre>
				 */
				String name = json.readValue("name", String.class, jsonData);
				Color color = json.readValue("color", Color.class, jsonData);
				Drawable drawable = newDrawable(name, color);
				if (drawable instanceof BaseDrawable) {
					BaseDrawable named = (BaseDrawable) drawable;
					named.setName(jsonData.name + " (" + name + ", " + color + ")");
				}
				// end

				/**
				 * <pre>
				 * ######################## 
				 * 新增保存 TintedDrawable 记录的需求逻辑
				 * ########################
				 * </pre>
				 */
				TintedDrawable resource = new TintedDrawable();
				resource.color = color;
				resource.name = name;
				add(jsonData.name, resource, type);
				// end

				return drawable;
			}
		});
		return jsonLoader;
	}

	/** Store all resources in the specified skin JSON file. */
	public boolean save(FileHandle skinFile) {
		StringWriter jsonText = new StringWriter();

		Json json = new Json();
		json.setWriter(new JsonWriter(jsonText));

		// 遍历每种资源类型 type
		json.writeObjectStart();
		for (Class<? extends Object> resType : saveResoureTypes) {
			ObjectMap<String, ? extends Object> typeResources = super.getAll(resType);
			if (emptyMap(typeResources)) {
				continue;
			}
			// 写入 type 全类型开头
			json.writeObjectStart(resType.getName());
			write1ResType(typeResources, resType, json);
			json.writeObjectEnd();

		}
		json.writeObjectEnd();

		PrettyPrintSettings settings = new PrettyPrintSettings();
		settings.outputType = OutputType.minimal;
		settings.singleLineColumns = 50;

		// 写入数据到文件
		skinFile.writeString(json.prettyPrint(jsonText.toString(), settings), false);
		return true;
	}

	void write1ResType(ObjectMap<String, ? extends Object> typeResources, Class<? extends Object> resType, Json json) {
		// 构建临时的 styleName 数组，防止内嵌发生.避免并发问题
		Array<String> styleNames = typeResources.keys().toArray();
		// 遍历当前 resType 资源类型 的所有样式
		for (String styleName : styleNames) {
			json.writeObjectStart(styleName);
			write1Style(json, resType, typeResources, styleName);
			json.writeObjectEnd();
		}
	}

	void write1Style(Json json, Class<? extends Object> resType, ObjectMap<String, ? extends Object> typeResources, String styleName) {
		// 该 name 对应的 style
		Object objStyle = typeResources.get(styleName);
		Field[] fields = ClassReflection.getFields(resType);

		// 特殊处理 BitmapFont。无需处理 fields（没必要且 fields 长度为 0）
		if (objStyle instanceof com.badlogic.gdx.graphics.g2d.BitmapFont) {
			BitmapFont font = (BitmapFont) objStyle;
			json.writeValue("file", font.getData().fontFile.name());
			return;
		}

		// Handle fields
		for (Field field : fields) {
			// 忽略使用如下修饰符修改的 field
			if (field.isFinal() || field.isStatic() || field.isTransient()) {
				continue;
			}
			try {
				write1Filed(json, objStyle, field);
			} catch (Exception e) {
				String tag = getClass().getSimpleName();
				String message = String.format("resTypes:%s 写 field:%s 到 json 失败", resType, field);
				Gdx.app.debug(tag, message, e);
			}
		}
	}

	void write1Filed(Json json, Object objStyle, Field field) throws ReflectionException {
		Object fieldValue = field.get(objStyle);
		if (fieldValue == null) {
			return;
		}

		Object writeValue = null;
		if (fieldValue instanceof BitmapFont) {
			writeValue = resolveObjectName(BitmapFont.class, fieldValue);
		} else if (fieldValue instanceof Float) {
			// 忽略 float 属性为 0 的情况
			writeValue = ((Float) fieldValue != 0.0f) ? fieldValue : null;
		} else if (fieldValue instanceof Color) {
			String colorName = resolveObjectName(Color.class, fieldValue);
			if (colorName == null) {
				writeValue = fieldValue;
			} else {
				writeValue = colorName;
			}
		} else if (fieldValue instanceof Drawable) {
			writeValue = resolveObjectName(Drawable.class, fieldValue);
		} else if (fieldValue instanceof ListStyle) {
			writeValue = resolveObjectName(ListStyle.class, fieldValue);
		} else if (fieldValue instanceof ScrollPaneStyle) {
			writeValue = resolveObjectName(ScrollPaneStyle.class, fieldValue);
		} else if (fieldValue instanceof String) {
			writeValue = fieldValue;
		} else if (fieldValue instanceof LabelStyle) {
			writeValue = resolveObjectName(LabelStyle.class, fieldValue);
		} else if (fieldValue instanceof char[]) {
			// Don't store.
		} else {
			throw new IllegalArgumentException("resource object type is unknown: " + fieldValue.getClass().getCanonicalName());
		}

		if (writeValue != null) {
			json.writeValue(field.getName(), writeValue);
		} else {
			String fieldName = field.getName();
			String objType = objStyle.getClass().getSimpleName();
			String fieldValueType = fieldValue.getClass().getSimpleName();
			String message = String.format("objType:%s \t fieldName:%s \t fieldValue:%s \t fieldValueType:%s", objType, fieldName, fieldValue, fieldValueType);
			Gdx.app.debug("save skin to json", message);
		}
	}

	/** 反解出 name */
	public String resolveObjectName(Class<?> classType, Object object) {
		ObjectMap<String, ?> objectMap = super.getAll(classType);
		if (objectMap == null) {
			return null;
		}
		Iterator<String> keys = objectMap.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object obj = objectMap.get(key);
			if (obj.equals(object)) {
				return key;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	ObjectMap<Class<?>, ObjectMap<String, Object>> getResourcesMap() {
		Field depthFiled = getDepthFiled("resources");
		depthFiled.setAccessible(true);
		try {
			return (ObjectMap<Class<?>, ObjectMap<String, Object>>) depthFiled.get(this);
		} catch (ReflectionException e) {
			throw new RuntimeException("获取 com.badlogic.gdx.scenes.scene2d.ui.Skin 中的 resources 属性失败", e);
		}
	}

	// 深度搜索，截止到 Object 为止
	Field getDepthFiled(String name) {
		Class<?> clazz = getClass();
		while (!Object.class.equals(clazz)) {
			Field declaredField = null;
			try {
				declaredField = ClassReflection.getDeclaredField(clazz, name);
			} catch (ReflectionException e) {
				// 当前 class 找不到
			} finally {
				if (declaredField != null) {
					return declaredField;
				}
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}

	/** 检测该样式是否在别处被引用 */
	public static <T> boolean isStyleInUse(Skin skin, String targetStyleName, Class<T> targetStyleClazz) {
		if (skin == null || targetStyleName == null || targetStyleName.trim().length() == 0 || targetStyleClazz == null) {
			throw new IllegalArgumentException("skin、targetStyleName and targetStyleClazz can not null");
		}
		try {
			for (String widget : widgets) {
				Class<?> resType = Class.forName("com.badlogic.gdx.scenes.scene2d.ui." + widget + "$" + widget + "Style");
				// 忽略当前类型
				if (resType == targetStyleClazz) {
					continue;
				}
				ObjectMap<String, ?> typeResources = skin.getAll(resType);
				if (emptyMap(typeResources)) {
					continue;
				}
				// 构建临时的 styleName 数组，防止内嵌发生.避免并发问题
				Array<String> styleNames = typeResources.keys().toArray();
				// 遍历当前 resType 资源类型 的所有样式
				for (String styleName : styleNames) {
					Object objStyle = typeResources.get(styleName);
					Field[] fields = ClassReflection.getFields(objStyle.getClass());
					for (Field field : fields) {
						// 忽略使用如下修饰符修改的 field
						if (field.isFinal() || field.isStatic() || field.isTransient()) {
							continue;
						}
						if (field.getType() == targetStyleClazz) {
							@SuppressWarnings("unchecked")
							T fieldObj = (T) field.get(objStyle);
							String fieldObjStyleName = skin.find(fieldObj);
							if (targetStyleName.equals(fieldObjStyleName)) {
								return true;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** 检测该资源是否在别处被引用 */
	public static <T> boolean isResInUse(Skin skin, T target) {
		if (skin == null || target == null) {
			throw new IllegalArgumentException("skin and target can not null");
		}
		try {
			Class<?> clazzTarget = target.getClass();
			for (String widget : widgets) {
				Class<?> resType = Class.forName("com.badlogic.gdx.scenes.scene2d.ui." + widget + "$" + widget + "Style");
				// 忽略当前类型
				if (resType == clazzTarget) {
					continue;
				}
				ObjectMap<String, ?> typeResources = skin.getAll(resType);
				if (emptyMap(typeResources)) {
					continue;
				}
				// 构建临时的 styleName 数组，防止内嵌发生.避免并发问题
				Array<String> styleNames = typeResources.keys().toArray();
				// 遍历当前 resType 资源类型 的所有样式
				for (String styleName : styleNames) {
					Object objStyle = typeResources.get(styleName);
					Field[] fields = ClassReflection.getFields(objStyle.getClass());
					for (Field field : fields) {
						// 忽略使用如下修饰符修改的 field
						if (field.isFinal() || field.isStatic() || field.isTransient()) {
							continue;
						}
						if (field.getType() == clazzTarget) {
							@SuppressWarnings("unchecked")
							T f = (T) field.get(objStyle);
							if (target.equals(f)) {
								return true;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	static boolean emptyMap(ObjectMap<?, ?> objectMap) {
		return objectMap == null || objectMap.size == 0;
	}

}
