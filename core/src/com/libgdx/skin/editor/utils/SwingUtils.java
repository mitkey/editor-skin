package com.libgdx.skin.editor.utils;

import java.awt.Frame;

/**
 * @作者 Mitkey
 * @时间 2016年9月13日 下午7:16:14
 * @类说明:
 * @版本 xx
 */
public class SwingUtils {

	/** 使用该方式在第一时间获取焦点 */
	public static void forceFocus() {
		// 使用该方式在第一时间获取焦点
		// Need to steal focus first with this hack (Thanks to Z-Man)
		Frame frame = new Frame();
		frame.setUndecorated(true);
		frame.setOpacity(0);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.toFront();
		frame.setVisible(false);
		frame.dispose();
	}

}
