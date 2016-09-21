package com.libgdx.skin.editor.utils.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.StringBuilder;

/**
 * @作者 Mitkey
 * @时间 2016年9月1日 下午3:12:37
 * @类说明 使用泛型的原因，而不是直接使用 Number 接口的原因是。<br>
 *      在 new 时传入的 initValue 为 float（Float），而 getValue 为 int（Integer） 。<br>
 *      进行比较时类型不一致
 * @版本 xx
 */
public abstract class NumberLabel<T extends Number> extends Label {

	T oldValue;
	int appendIndex;

	public NumberLabel(CharSequence text, T initValue, LabelStyle style) {
		super(text.toString() + initValue, style);
		this.oldValue = initValue;
		this.appendIndex = text.length();
	}

	/** 钩子函数，变化的 number */
	public abstract T getValue();

	@Override
	public void act(float delta) {
		T newValue = getValue();
		if (!oldValue.equals(newValue)) {
			oldValue = newValue;
			StringBuilder builder = this.getText();
			builder.setLength(appendIndex);
			builder.append(oldValue);
			this.invalidateHierarchy();
		}
		super.act(delta);
	}

}
