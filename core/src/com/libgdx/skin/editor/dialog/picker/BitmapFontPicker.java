package com.libgdx.skin.editor.dialog.picker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.libgdx.skin.editor.property.editor.actor.builder.PropertyEditorBuilder.OnEditorCall;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;
import com.libgdx.skin.editor.utils.scene2d.Dialogs;
import com.libgdx.skin.editor.utils.scene2d.Dialogs.OkCancelDialogListener;

/**
 * @作者 Mitkey
 * @时间 2016年10月12日 上午10:59:21
 * @类说明: bitmap font 的管理
 * @版本 xx
 */
public class BitmapFontPicker extends BasePickerDialog {

	private static final String tag = BitmapFontPicker.class.getSimpleName();
	private static final String textPreview = "Sample Text Content";

	public BitmapFontPicker(CustomSkin projectSkin) {
		this(projectSkin, null, null, null);
	}

	public BitmapFontPicker(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) {
		super("Bitmap Font Picker", projectSkin, styleObject, field, onEditorCall);
	}

	protected void initialWidget() {
		tableFonts.clear();
		ObjectMap<String, BitmapFont> objectMap = projectSkin.getAll(BitmapFont.class);
		if (objectMap == null || objectMap.size == 0) {
			tableFonts.add("bitmap font collection is empty", "error");
		} else {
			BitmapFont useBitmapFont;
			try {
				useBitmapFont = styleObject == null ? null : (BitmapFont) field.get(styleObject);
			} catch (ReflectionException e) {
				useBitmapFont = null;
				Gdx.app.error(tag, "get bitmapfont error", e);
			}
			// 标题
			tableFonts.add("Font Name", "title").width(250);
			tableFonts.add("Value", "title").width(250);
			tableFonts.add("Operate", "title").colspan(2).expandX().fillX().row();
			// 内容
			Entries<String, BitmapFont> iterator = objectMap.iterator();
			while (iterator.hasNext()) {
				Entry<String, BitmapFont> entry = iterator.next();
				String styleName = entry.key;
				BitmapFont bitmapFont = entry.value;

				Skin skin = getSkin();
				// 标识是否当前使用的 bitmapfont
				tableFonts.add((bitmapFont == useBitmapFont ? "###### " : "") + styleName);
				tableFonts.add(new Label(textPreview, new LabelStyle(bitmapFont, bitmapFont.getColor())));
				// 选中按钮
				if (field != null && useBitmapFont != bitmapFont) {
					tableFonts.add(new TextButton("Select", skin)).getActor().addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							// 改变对象值
							try {
								field.set(styleObject, bitmapFont);
							} catch (ReflectionException e) {
								Gdx.app.error(tag, "set styleObject error", e);
								Dialogs.showOkDialog(getStage(), "Select Bitmap Font Result", "Error", skin);
							}
							onEditorCall.call();
							hide();
						}
					});
				}
				// 删除按钮
				tableFonts.add(new TextButton("Remove", skin)).expandX().getActor().addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						String title = "Delete Bitmap Font";
						String text = "You are sure you want to delete \nthis bitmap font(styleName is " + styleName + ")?";
						Dialogs.showOkCancelDialog(getStage(), title, text, skin, new OkCancelDialogListener() {
							@Override
							public void ok() {
								String textTemp = "Success";
								if (CustomSkin.isResInUse(projectSkin, bitmapFont)) {
									textTemp = "Bitmap font already in use!";
								} else {
									// 删除文件
									bitmapFont.getData().fontFile.delete();
									// 从 skin 中移除
									objectMap.remove(styleName);
									// 重新渲染
									initialWidget();
								}
								Dialogs.showOkDialog(getStage(), "Delete Bitmap Font Result", textTemp, skin);
							}
						});
					}
				});
				tableFonts.row();
			}
		}
	}

}
