package com.libgdx.skin.editor.property.editor.actor.builder;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip.TextTooltipStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.TreeStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.Array;

/**
 * @作者 Mitkey
 * @时间 2016年9月14日 上午10:35:15
 * @类说明:
 * @版本 xx
 */
public class PropertyUtils {
	private static final Map<Class<?>, Array<String>> styleMustProperties = new HashMap<Class<?>, Array<String>>() {
		private static final long serialVersionUID = 1L;
		{
			put(LabelStyle.class, generateArray("font"));
			put(TextFieldStyle.class, generateArray("font", "fontColor"));
			put(ButtonStyle.class, generateArray());// 都是可选的
			put(TextButtonStyle.class, generateArray("font"));
			put(ScrollPaneStyle.class, generateArray());// 都是可选的
			put(ListStyle.class, generateArray("font", "fontColorSelected", "fontColorUnselected", "selection"));
			put(SplitPaneStyle.class, generateArray("handle"));
			put(ProgressBarStyle.class, generateArray("background"));
			put(SliderStyle.class, generateArray("background"));
			put(WindowStyle.class, generateArray("titleFont"));

			put(SelectBoxStyle.class, generateArray("font", "fontColor", "scrollStyle", "listStyle"));
			put(CheckBoxStyle.class, generateArray("checkboxOn", "checkboxOff", "font"));
			put(TouchpadStyle.class, generateArray("background"));
			put(TreeStyle.class, generateArray("plus", "minus"));

			put(ImageButtonStyle.class, generateArray());// 都是可选的
			put(TextTooltipStyle.class, generateArray("label"));
			put(ImageTextButtonStyle.class, generateArray("font"));
		}
	};

	public static boolean mustProperty(Class<?> clazz, String fieldName) {
		Array<String> array = styleMustProperties.get(clazz);
		if (array == null || array.size == 0) {
			return false;
		}
		return array.contains(fieldName, false);
	}

	public class Property {
		public Array<String> mustFields = new Array<String>();
	}

	static Array<String> generateArray(String... array) {
		return new Array<String>(array);
	}

}
