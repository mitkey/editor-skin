package com.libgdx.skin.editor.utils.scene2d;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.libgdx.skin.editor.GlobalData;

/**
 * @作者 Mitkey
 * @时间 2016年9月26日 下午5:09:04
 * @类说明:
 * @版本 xx
 */
public class Dialogs {

	public static Dialog showOkDialog(Stage stage, String title, String text, Skin skin) {
		Dialog dialog = new SimpleDialog(title, skin);
		dialog.text(text).button("confirm", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true);
		dialog.getButtonTable().pad(20);
		return dialog.show(stage);
	}

	public static Dialog showOkCancelDialog(Stage stage, String title, String text, Skin skin, final OkCancelDialogListener listener) {
		Dialog dialog = new SimpleDialog(title, skin) {
			@Override
			protected void result(Object object) {
				super.result(object);
				if ((Boolean) object) {
					listener.ok();
				}
			}
		};
		dialog.text(text).button("confirm", true).key(Keys.ENTER, true).button("cancel", false).key(Keys.ESCAPE, false);
		dialog.getButtonTable().pad(20);
		return dialog.show(stage);
	}

	public static Dialog showInputDialog(Stage stage, String title, String fieldName, String fieldValue, Skin skin, InputDialogListener inputDialogListener) {
		return showInputDialog(stage, title, fieldName, fieldValue, skin, null, inputDialogListener);
	}

	public static Dialog showInputDialog(Stage stage, String title, String fieldName, String fieldValue, Skin skin, //
			final TextFieldFilter textFieldFilter, final InputDialogListener inputDialogListener) {
		final TextField textField = new TextField(fieldValue, skin);
		textField.setTextFieldFilter(textFieldFilter);
		textField.setMessageText("please input content");
		textField.setCursorPosition(fieldValue.length());

		Dialog dialog = new SimpleDialog(title, skin) {
			@Override
			protected void result(Object object) {
				super.result(object);
				if ((Boolean) object) {
					inputDialogListener.finished(textField.getText());
				}
			}
		};
		dialog.getContentTable().add(fieldName + ":", "title").padRight(10);
		dialog.getContentTable().add(textField);

		dialog.button("confirm", true).key(Keys.ENTER, true).button("cancel", false).key(Keys.ESCAPE, false);
		dialog.getButtonTable().pad(20);
		stage.setKeyboardFocus(textField);
		return dialog.show(stage);
	}

	private static class SimpleDialog extends Dialog {
		public SimpleDialog(String title, Skin skin) {
			super(title, skin);
			getColor().a = 0;
		}
		@Override
		public float getPrefWidth() {
			return GlobalData.WIDTH / 3;
		}
		@Override
		public float getPrefHeight() {
			return GlobalData.HEIGHT / 3;
		}
	}

	public interface InputDialogListener {
		void finished(String input);
	}
	public interface OkCancelDialogListener {
		void ok();
	}

	// float 型过滤
	public static class InputDialogFloatFilter implements TextFieldFilter {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			return Character.isDigit(c) || (!textField.getText().contains(".") && '.' == c);
		}
	}
	// int 型字符过滤
	public static class InputDialogIntegerFilter implements TextFieldFilter {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			return Character.isDigit(c);
		}
	}
	// 文件名规范过滤
	public static class InputDialogFileNameSpecificationFilter implements TextFieldFilter {
		@Override
		public boolean acceptChar(TextField textField, char c) {
			// 字母或数字
			return Character.isLetterOrDigit(c) || '_' == c;
		}
	}

}
