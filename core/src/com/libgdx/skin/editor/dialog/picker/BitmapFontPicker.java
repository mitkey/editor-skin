package com.libgdx.skin.editor.dialog.picker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.property.editor.actor.builder.PropertyEditorBuilder.OnEditorCall;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;
import com.libgdx.skin.editor.utils.scene2d.Dialogs;
import com.libgdx.skin.editor.utils.scene2d.Dialogs.OkCancelDialogListener;

/**
 * @作者 Mitkey
 * @时间 2016年10月12日 上午10:59:21
 * @类说明: bitmap font 的管理。<br>
 *       覆写 getPrefWidth()、getPrefHeight() 的必要性：show(stage) 时，会重新调用 setSize方法，<br>
 *       而该方法使用的 width、height 的参数即为 getPrefWidth()、 getPrefHeight() 的返回值
 * @版本 xx
 */
public class BitmapFontPicker extends Dialog {
	private static final String tag = BitmapFontPicker.class.getSimpleName();
	private static final float FullRate = .6f;
	private static final String textPreview = "Sample Text Content";

	Table tableFonts;

	CustomSkin projectSkin;
	Object styleObject;
	Field field;
	OnEditorCall onEditorCall;

	public BitmapFontPicker(CustomSkin projectSkin) {
		this(projectSkin, null, null, null);
	}

	public BitmapFontPicker(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) {
		super("Bitmap Font Picker", GlobalData.skin);
		this.projectSkin = projectSkin;
		this.styleObject = styleObject;
		this.field = field;
		this.onEditorCall = onEditorCall;

		Skin skin = getSkin();
		// 字体内容
		tableFonts = new Table(skin);
		tableFonts.left().top().pad(10).defaults().left().pad(5);
		this.initialFont();
		// 包裹内容的滚动层
		ScrollPane scrollPane = new ScrollPane(tableFonts, skin);
		scrollPane.setScrollingDisabled(true, false);
		scrollPane.setFadeScrollBars(false);
		scrollPane.setScrollbarsOnTop(true);
		scrollPane.setFlickScroll(false);
		getContentTable().add(scrollPane).expand().fill().pad(20);
		// 按钮
		button("Cancel", false).key(Keys.ESCAPE, false).getButtonTable().padBottom(20);
		getColor().a = 0;
	}

	private void initialFont() {
		ObjectMap<String, BitmapFont> objectMap = projectSkin.getAll(BitmapFont.class);
		if (objectMap == null || objectMap.size == 0) {
			tableFonts.add("bitmap font empty", "error");
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
								Dialogs.showOkDialog(getStage(), "Select Bitmap Font Result", "Error", getSkin());
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
						String title = "Delete Bitmap Font Result";
						String text = "You are sure you want to delete \nthis bitmap font(styleName is " + styleName + ")?";
						Dialogs.showOkCancelDialog(getStage(), title, text, skin, new OkCancelDialogListener() {
							@Override
							public void ok() {
								dealWidth();
							}
						});
					}
					private void dealWidth() {
						String textTemp = "Success";
						if (CustomSkin.isResInUse(projectSkin, bitmapFont)) {
							textTemp = "Bitmap font already in use!";
						} else {
							// 删除文件
							bitmapFont.getData().fontFile.delete();
							// 从 skin 中移除
							objectMap.remove(styleName);
							// 重新渲染
							initialFont();
						}
						Dialogs.showOkDialog(getStage(), "Delete Bitmap Font Result", textTemp, skin);
					}
				});
			}
		}
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
