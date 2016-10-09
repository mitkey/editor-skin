package com.libgdx.skin.editor.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;

/**
 * @作者 Mitkey
 * @时间 2016年9月29日 下午3:19:54
 * @类说明:样式面板
 * @版本 xx
 */
public class StylePanel extends Table {
	private static final String tag = StylePanel.class.getSimpleName();

	CustomSkin projectSkin;

	Class<?> styleClazz;
	List<String> listStyleNames;
	Object selectStyleObject;

	public StylePanel(CustomSkin projectSkin) {
		super(GlobalData.skin);
		this.projectSkin = projectSkin;

		center().setBackground("default-pane");
		defaults().pad(5);

		add("Style Names", "title").row();

		Skin skin = getSkin();

		listStyleNames = new List<String>(skin, "dimmed");
		listStyleNames.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeStyleNameSelect();
			}
		});
		ScrollPane scrollPane = new ScrollPane(listStyleNames, skin);
		add(scrollPane).expand().fill().row();;
	}

	/** 改变选择的样式 name */
	public void changeStyleNameSelect() {
		String selectStyleName = getSelectStyleName();
		Gdx.app.debug(tag, "change select style name " + selectStyleName);

		if (selectStyleName != null) {
			selectStyleObject = projectSkin.get(selectStyleName, styleClazz);
		}
	}

	/** 改变 style 类型 */
	public final void changeStyleType(Class<?> styleClazz) {
		Gdx.app.debug(tag, "change style type " + styleClazz.getSimpleName());

		this.styleClazz = styleClazz;

		// 当前 style 类型的全部 style 集合
		ObjectMap<String, ?> objectMap = projectSkin.getAll(styleClazz);

		Array<String> newItems = new Array<String>();
		if (objectMap == null || objectMap.size == 0) {
			selectStyleObject = null;
			listStyleNames.setItems(newItems);
		} else {
			newItems.addAll(objectMap.keys().toArray());
			listStyleNames.setItems(newItems);

			// 默认选中第一个
			selectStyleObject = newItems.first();
			listStyleNames.setSelectedIndex(0);
		}
	}

	/** 当前 style 类型中选择的 style name */
	public final String getSelectStyleName() {
		return listStyleNames.getSelected();
	}

	/** 当前 style 类型中选择的 style object */
	public final Object getSelectStyleObject() {
		return selectStyleObject;
	}

}
