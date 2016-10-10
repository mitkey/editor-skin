package com.libgdx.skin.editor.utils.scene2d;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.libgdx.skin.editor.GlobalData;

/**
 * @作者 Mitkey
 * @时间 2016年9月1日 上午11:19:05
 * @类说明:
 * @版本 xx
 */
public abstract class GeneralScreen extends ScreenAdapter {

	Stage stage;

	@Override
	public void show() {
		super.show();
		this.stage = new Stage(new StretchViewport(GlobalData.WIDTH, GlobalData.HEIGHT));
	}

	@Override
	public final void render(float delta) {
		super.render(delta = Math.min(delta, 1.0f / 30.0f));
		Gdx.gl20.glClearColor(GlobalData.ColorBackground.r, GlobalData.ColorBackground.g, GlobalData.ColorBackground.b, GlobalData.ColorBackground.a);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		stage.act(delta);

		int fpsNum = Gdx.graphics.getFramesPerSecond();
		float heapNum = Gdx.app.getJavaHeap() * 1f / 1024 / 1024;
		float nativeNum = Gdx.app.getNativeHeap() * 1f / 1024 / 1024;
		GlobalData.onMonitor.change(fpsNum, heapNum, nativeNum);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();
	}

	public final Stage stage() {
		return stage;
	}

}
