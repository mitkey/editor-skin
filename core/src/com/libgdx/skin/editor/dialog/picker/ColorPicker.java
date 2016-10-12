package com.libgdx.skin.editor.dialog.picker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.reflect.Field;
import com.libgdx.skin.editor.property.editor.actor.builder.PropertyEditorBuilder;
import com.libgdx.skin.editor.property.editor.actor.builder.PropertyEditorBuilder.OnEditorCall;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;
import com.libgdx.skin.editor.utils.scene2d.Dialogs;
import com.libgdx.skin.editor.utils.scene2d.Dialogs.OkCancelDialogListener;

/**
 * @作者 Mitkey
 * @时间 2016年10月12日 下午4:34:15
 * @类说明:
 * @版本 xx
 */
public class ColorPicker extends BasePickerDialog {

	private static final String tag = ColorPicker.class.getSimpleName();

	public ColorPicker(CustomSkin projectSkin) {
		this(projectSkin, null, null, null);
	}

	public ColorPicker(CustomSkin projectSkin, Object styleObject, Field field, OnEditorCall onEditorCall) {
		super("Color Picker", projectSkin, styleObject, field, onEditorCall);
	}

	@Override
	protected void initialWidget() {
		tableFonts.clear();
		ObjectMap<String, Color> objectMap = projectSkin.getAll(Color.class);
		if (objectMap == null || objectMap.size == 0) {
			tableFonts.add("color collection is empty", "error");
		} else {
			Color useColor;
			try {
				useColor = styleObject == null ? null : (Color) field.get(styleObject);
			} catch (Exception e) {
				useColor = null;
				Gdx.app.error(tag, "get color erro", e);
			}
			// 标题
			tableFonts.add("Color Name", "title").width(250);
			tableFonts.add("Value", "title").colspan(2).width(250);
			tableFonts.add("Operate", "title").colspan(2).expandX().fillX().row();
			// 内容
			Entries<String, Color> iterator = objectMap.iterator();
			while (iterator.hasNext()) {
				Entry<String, Color> entry = iterator.next();
				String styleName = entry.key;
				Color color = entry.value;

				Skin skin = getSkin();
				// 标识该 color 是否为当前正在使用的
				tableFonts.add((useColor == color ? "#####" : "") + styleName);
				tableFonts.add(new Image(PropertyEditorBuilder.createColorPixmap(color)));
				tableFonts.add(color.toString());
				// 选中按钮
				if (field != null) {
					tableFonts.add(new TextButton("Select", skin)).getActor().addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							// 改变 styleObject 的值
							try {
								field.set(styleObject, color);
							} catch (Exception e) {
								Gdx.app.error(tag, "set styleObject error", e);
								Dialogs.showOkDialog(getStage(), "Select Color Result", "Error", skin);
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
						String title = "Delete Color";
						String text = "You are sure you want to delete \nthis color(styleName is " + styleName + ")?";
						Dialogs.showOkCancelDialog(getStage(), title, text, skin, new OkCancelDialogListener() {
							@Override
							public void ok() {
								String textTemp = "Success";
								if (CustomSkin.isResInUse(projectSkin, color)) {
									textTemp = "Color already in use!";
								} else {
									// 从 skin 中移除
									objectMap.remove(styleName);
									// 重新渲染
									initialWidget();
								}
								Dialogs.showOkDialog(getStage(), "Delete Color Result", textTemp, skin);
							}
						});
					}
				});
				tableFonts.row();
			}
		}
	}

}
