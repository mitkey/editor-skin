package com.libgdx.skin.editor.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;
import com.libgdx.skin.editor.GlobalData;
import com.libgdx.skin.editor.utils.scene2d.CustomSkin;

/**
 * @作者 Mitkey
 * @时间 2016年9月29日 下午3:18:14
 * @类说明:预览面板
 * @版本 xx
 */
public class PreviewPanel extends Table {

	private static final String tag = PreviewPanel.class.getSimpleName();
	private static final OrderedMap<String, Color> backgroupColorMgr = new OrderedMap<String, Color>() {
		{
			put("White", Color.WHITE);
			put("Black", Color.BLACK);
			put("Red", Color.RED);
			put("Gray", Color.GRAY);
		}
	};

	Container<Actor> container;
	Skin skin;

	public PreviewPanel() {
		super(GlobalData.skin);
		top().pad(10).setBackground("default-pane");
		defaults().space(20);
		
		skin = getSkin();
		
		container = new Container<Actor>();
		container.setBackground(skin.getDrawable("dialogDim"));
		
		ButtonGroup<TextButton> buttonGroup = new ButtonGroup<TextButton>();
		Table tableButton = new Table(skin);
		tableButton.defaults().space(10);
		Entries<String, Color> iterator = backgroupColorMgr.entries().iterator();
		while (iterator.hasNext()) {
			Entry<String, Color> entry = iterator.next();
			String key = entry.key;
			Color color = entry.value;
			TextButton textButton = new TextButton(key, skin, "toggle");
			textButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					container.setColor(color);
				}
			});
			tableButton.add(textButton);
			buttonGroup.add(textButton);
		}

		add(tableButton).expandX().fillX().row();
		add(container).pad(20).expand().fill();
	}

	public void updatePreview(CustomSkin projectSkin, Object styleObject) {
		Class<? extends Object> clazz = styleObject.getClass();
		ObjectMap<String, ?> objectMap = projectSkin.getAll(clazz);
		String simpleName = clazz.getSimpleName();
		Gdx.app.log(tag, "update preview view:" + simpleName);
		if (objectMap == null || objectMap.size == 0) {
			return;
		}

		String widget = clazz.getSimpleName().substring(0, simpleName.indexOf("Style"));
		String styleName = projectSkin.resolveObjectName(clazz, styleObject);
		Actor previewActor = generationPreviewActor(projectSkin, styleName, widget);
		container.setActor(previewActor);
	}
	InputListener stopTouchDown = new InputListener() {
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			event.stop();
			return false;
		}
	};

	Actor generationPreviewActor(CustomSkin projectSkin, String styleName, String widget) {
		Actor actor;
		if (widget.equals("Label")) {
			actor = new Label("This is a label widget", projectSkin, styleName);
		} else if (widget.equals("Button")) {
			actor = new Button(projectSkin, styleName);
		} else if (widget.equals("TextButton")) {
			actor = new TextButton("This is a TextButton widget", projectSkin, styleName);
		} else if (widget.equals("ImageButton")) {
			actor = new ImageButton(projectSkin, styleName);
		} else if (widget.equals("CheckBox")) {
			actor = new CheckBox("This is a CheckBox widget", projectSkin, styleName);
		} else if (widget.equals("TextField")) {
			TextField textField = new TextField("This is a TextField widget", projectSkin, styleName);
			textField.addListener(stopTouchDown);
			actor = textField;
		} else if (widget.equals("List")) {
			List<String> list = new List<String>(projectSkin, styleName);
			Array<String> items = new Array<String>();
			items.add("This is a list widget 1");
			items.add("This is a list widget 2");
			items.add("This is a list widget 3");
			items.add("This is a list widget 4");
			items.add("This is a list widget 5");
			items.add("This is a list widget 6");
			items.add("This is a list widget 7");
			list.setItems(items);
			actor = list;
		} else if (widget.equals("SelectBox")) {
			SelectBox<String> selectBox = new SelectBox<String>(projectSkin, styleName);
			Array<String> items = new Array<String>();
			items.add("This is a SelectBox widget 1");
			items.add("This is a SelectBox widget 2");
			items.add("This is a SelectBox widget 3");
			items.add("This is a SelectBox widget 4");
			items.add("This is a SelectBox widget 5");
			items.add("This is a SelectBox widget 6");
			items.add("This is a SelectBox widget 7");
			selectBox.setItems(items);
			actor = selectBox;
		} else if (widget.equals("ProgressBar")) {
			ProgressBar progressBar = new ProgressBar(0, 100, 5, false, projectSkin, styleName);
			progressBar.setValue(50);
			progressBar.addListener(stopTouchDown);
			actor = progressBar;
		} else if (widget.equals("Slider")) {
			Slider slider = new Slider(0, 100, 5, true, projectSkin, styleName);
			slider.addListener(stopTouchDown);
			actor = slider;
		} else if (widget.equals("ScrollPane")) {
			Table table = new Table(skin);
			for (int i = 0; i < 20; i++) {
				table.add("This is a ScrollPane Widget").padRight(10);
				table.add("This is a ScrollPane Widget").padRight(10);
				table.add("This is a ScrollPane Widget").row();
			}
			ScrollPane scrollPane = new ScrollPane(table, projectSkin, styleName);
			scrollPane.addListener(stopTouchDown);
			scrollPane.setFlickScroll(true);
			scrollPane.setScrollbarsOnTop(true);
			scrollPane.setScrollBarPositions(true, true);
			scrollPane.setFadeScrollBars(false);
			actor = scrollPane;
		} else if (widget.equals("SplitPane")) {
			Table table1 = new Table(skin);
			table1.setBackground("default-rect");
			Table table2 = new Table(skin);
			table2.setBackground("default-rect");
			for (int i = 0; i < 20; i++) {
				table1.add("This is a SplitPane Widget Table one " + i).pad(10).row();
				table2.add("This is a SplitPane Widget Table Two " + i).pad(10).row();
			}
			SplitPane splitPane = new SplitPane(new ScrollPane(table1, skin), new ScrollPane(table2, skin), true, projectSkin, styleName);
			actor = splitPane;
		} else if (widget.equals("Window")) {
			Table table = new Table(skin);
			for (int i = 0; i < 5; i++) {
				table.add("This is a Window Widget").row();
			}
			Window window = new Window("This is a Window Widget", projectSkin, styleName);
			window.addListener(stopTouchDown);
			window.add(table);
			actor = window;
		} else if (widget.equals("Touchpad")) {
			Touchpad touchpad = new Touchpad(0, projectSkin, styleName);
			touchpad.addListener(stopTouchDown);
			actor = touchpad;
		} else if (widget.equals("Tree")) {
			Tree tree = new Tree(projectSkin, styleName);
			for (int i = 0; i < 4; i++) {
				Node node = new Tree.Node(new Label("node" + i, skin));
				for (int j = 0; j < 3; j++) {
					Node inNode = new Node(new Label("inNode" + j, skin));
					for (int k = 0; k < 3; k++) {
						Node ininNode = new Node(new Label("ininNode" + j, skin));
						inNode.add(ininNode);
					}
					node.add(inNode);
				}
				tree.add(node);
			}
			tree.expandAll();
			actor = tree;
		} else if (widget.equals("ImageTextButton")) {
			ImageTextButton imageTextButton = new ImageTextButton("i is image text button", projectSkin, styleName);
			actor = imageTextButton;
		} else if (widget.equals("TextTooltip")) {
			Label label = new Label("focus on me show text tooltip", projectSkin);
			label.addListener(new TextTooltip("i is text tool tip", projectSkin, styleName));
			actor = label;
		} else {
			actor = new Label("unknown widget type:" + widget, skin, "error");
		}
		return actor;
	}
}
