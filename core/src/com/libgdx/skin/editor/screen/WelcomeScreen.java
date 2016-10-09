package com.libgdx.skin.editor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.utils.common.StrUtil;
import com.libgdx.skin.editor.utils.scene2d.Dialogs;
import com.libgdx.skin.editor.utils.scene2d.Dialogs.InputDialogFileNameSpecificationFilter;
import com.libgdx.skin.editor.utils.scene2d.Dialogs.InputDialogListener;
import com.libgdx.skin.editor.utils.scene2d.Dialogs.OkCancelDialogListener;
import com.libgdx.skin.editor.utils.scene2d.GeneralScreen;

/**
 * @作者 Mitkey
 * @时间 2016年9月21日 下午7:05:03
 * @类说明:
 * @版本 xx
 */
public class WelcomeScreen extends GeneralScreen {

	private static final String tag = WelcomeScreen.class.getSimpleName();

	List<String> listProjects;
	Array<String> itemProjects;

	TextButton buttonOpen;
	TextButton buttonDelete;
	TextButton buttonRename;

	@Override
	public void show() {
		super.show();

		final Skin skin = GlobalData.skin;

		Table tableRoot = new Table(skin);
		tableRoot.defaults().pad(10);

		// 项目列表
		{
			Table tableLeft = new Table(skin);
			tableLeft.defaults().pad(5);

			tableLeft.add("Project List", "title").row();

			(listProjects = new List<String>(skin)).addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					boolean noSelected = listProjects.getSelected() == null;
					buttonDelete.setDisabled(noSelected);
					buttonOpen.setDisabled(noSelected);
					buttonRename.setDisabled(noSelected);
				}
			});
			final ScrollPane scrollPane = new ScrollPane(listProjects, skin);
			stage().addListener(new ClickListener() {
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					super.enter(event, x, y, pointer, fromActor);
					stage().setScrollFocus(scrollPane);
				}
			});
			tableLeft.add(scrollPane).size(400, 300);

			tableRoot.add(tableLeft).left();
		}
		// 操作按钮
		{
			Table tableButton = new Table(skin);
			tableButton.left().defaults().left().size(140, 30).fill().expand();
			tableButton.add(createTextButton("New", skin, false)).padTop(20).getActor().addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					showNewDialog(skin);
				}
			});
			tableButton.row().getTable().add(buttonOpen = createTextButton("Open", skin, true)).getActor().addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// 切换 screen
					GlobalData.game.setScreen(new ProjectScreen(listProjects.getSelected()));
				}
			});
			tableButton.row().getTable().add(buttonDelete = createTextButton("Delete", skin, true)).getActor().addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					showDeleteDialog(skin);
				}
			});
			tableButton.row().getTable().add(buttonRename = createTextButton("Rename", skin, true)).getActor().addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					showRenameDialog(skin);
				}
			});
			tableButton.row().getTable().add(createTextButton("Exit", skin, false)).getActor().addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					Gdx.app.exit();
				}
			});
			tableRoot.add(tableButton).expand().fill();
		}

		tableRoot.layout();
		tableRoot.pack();
		tableRoot.setPosition(GlobalData.WIDTH / 2 - tableRoot.getWidth() / 2, GlobalData.HEIGHT / 2 - tableRoot.getHeight() / 2);
		stage().addActor(tableRoot);

		InputProcessor processor = new InputMultiplexer(stage());
		Gdx.input.setInputProcessor(processor);

		notifyProjectLists();
	}

	TextButton createTextButton(String text, Skin skin, boolean defaultDisabled) {
		TextButton textButton = new TextButton(text, skin);
		textButton.setDisabled(defaultDisabled);
		return textButton;
	}

	void notifyProjectLists() {
		Gdx.app.debug(tag, "notifyProjectLists");

		itemProjects = new Array<String>();
		Array<FileHandle> projectList = GlobalData.getProjectList();
		for (FileHandle fileHandle : projectList) {
			itemProjects.add(fileHandle.name());
		}
		listProjects.setItems(itemProjects);
	}

	void showNewDialog(final Skin skin) {
		Dialogs.showInputDialog(stage(), "please input project name", "project name", "", skin, new Dialogs.InputDialogFileNameSpecificationFilter(), new InputDialogListener() {
			@Override
			public void finished(String input) {
				String dialogText = "success";
				if (StrUtil.isBlank(dialogText)) {
					dialogText = "project can not be empty";
				} else if (listProjects.getItems().contains(input, false)) {
					dialogText = "project name already existed!";
				} else {
					try {
						// 复制文件
						FileHandle project = GlobalData.getProject(input);
						FileHandle projectRaw = project.child("raw");
						projectRaw.mkdirs();
						Gdx.files.internal("resources/default.fnt").copyTo(project);
						Gdx.files.internal("resources/raw/check-off.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/check-on.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/cursor.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-pane-noborder.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-pane.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-rect-down.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-rect-pad.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-rect.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-round-down.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-round-large.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-round.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-scroll.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-select-selection.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-select.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-slider-knob.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-slider.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-splitpane-vertical.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-splitpane.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default-window.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/default.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/selection.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/textfield.9.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/tree-minus.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/tree-plus.png").copyTo(projectRaw);
						Gdx.files.internal("resources/raw/white.png").copyTo(projectRaw);
						Gdx.files.internal("resources/uiskin.atlas").copyTo(project);
						Gdx.files.internal("resources/uiskin.json").copyTo(project);
						Gdx.files.internal("resources/uiskin.png").copyTo(project);

						notifyProjectLists();
					} catch (Exception e) {
						String message = "fail";
						dialogText = message;
						Gdx.app.error(tag, message, e);
					}
				}
				Dialogs.showOkDialog(stage(), "new project result", dialogText, skin);
			}
		});
	}

	void showDeleteDialog(final Skin skin) {
		final String selectedProject = listProjects.getSelected();
		String title = selectedProject + " project delete confirm";
		String text = "are you sure want to delete this project?";
		Dialogs.showOkCancelDialog(stage(), title, text, skin, new OkCancelDialogListener() {
			@Override
			public void ok() {
				String dialogText = "success";
				try {
					// 删除该项目
					GlobalData.getProject(selectedProject).deleteDirectory();

					notifyProjectLists();
				} catch (Exception e) {
					String message = "fail";
					dialogText = message;
					Gdx.app.error(tag, message, e);
				}
				Dialogs.showOkDialog(stage(), "delete project result", dialogText, skin);
			}
		});
	}

	void showRenameDialog(final Skin skin) {
		final String selected = listProjects.getSelected();
		String title = "please input new project name";
		String fieldName = "project name";
		String fieldValue = selected;
		InputDialogFileNameSpecificationFilter textFieldFilter = new InputDialogFileNameSpecificationFilter();
		Dialogs.showInputDialog(stage(), title, fieldName, fieldValue, skin, textFieldFilter, new InputDialogListener() {
			@Override
			public void finished(String input) {
				String dialogText = "success";
				if (StrUtil.isBlank(input)) {
					dialogText = "project can not be empty";
				} else if (listProjects.getItems().contains(input, false)) {
					dialogText = "project name already existed!";
				} else {
					try {
						// 重命名
						GlobalData.getProject(selected).moveTo(GlobalData.getProject(input));

						notifyProjectLists();
					} catch (Exception e) {
						String message = "fail";
						dialogText = message;
						Gdx.app.error(tag, message, e);
					}
				}
				Dialogs.showOkDialog(stage(), "rename project name result", dialogText, skin);
			}
		});
	}

	@Override
	protected void update(float delta) {
	}

	@Override
	protected void draw(float delta) {
	}

	@Override
	public void resume() {
		super.resume();
		notifyProjectLists();
	}

}
