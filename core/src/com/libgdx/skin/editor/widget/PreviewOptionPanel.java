package com.libgdx.skin.editor.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.libgdx.skin.editor.screen.ProjectScreen;

/**
 * @作者 Mitkey
 * @时间 2016年9月29日 下午3:45:28
 * @类说明:预览配置面板
 * @版本 xx
 */
public class PreviewOptionPanel extends Table {

	ProjectScreen projectScreen;

	public PreviewOptionPanel(ProjectScreen projectScreen) {
		super(projectScreen.customSkin);
		this.projectScreen = projectScreen;

		top().left().setBackground("default-pane");

		// TODO Auto-generated constructor stub
	}

}
