package com.libgdx.skin.editor.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.libgdx.skin.editor.screen.ProjectScreen;

/**
 * @作者 Mitkey
 * @时间 2016年9月29日 下午3:19:54
 * @类说明:样式面板
 * @版本 xx
 */
public class StylePanel extends Table {

	ProjectScreen projectScreen;

	public StylePanel(ProjectScreen projectScreen) {
		super(projectScreen.customSkin);
		this.projectScreen = projectScreen;

		top().setBackground("default-pane");

		// TODO Auto-generated constructor stub
	}

}
