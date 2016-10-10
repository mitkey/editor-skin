package com.libgdx.skin.editor.utils.scene2d;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
	Table tableMonitor;

	@Override
	public void show() {
		super.show();
		this.stage = new Stage(new StretchViewport(GlobalData.WIDTH, GlobalData.HEIGHT)) {
			// 覆写 stage 方法。确保 tableMonitor 在最顶层
			@Override
			public void addActor(Actor actor) {
				super.addActor(actor);
				if (GlobalData.monitorView && tableMonitor != null && tableMonitor.getZIndex() != Integer.MAX_VALUE) {
					tableMonitor.toFront();
				}
			}
		};

		if (GlobalData.monitorView) {
			LabelStyle labelStyle = new LabelStyle(GlobalData.skin.getFont("default-font"), Color.WHITE);
			tableMonitor = new Table();
			tableMonitor.defaults().width(200).left().pad(5);
			tableMonitor.add(new NumberLabel<Integer>("Fps: ", -1, labelStyle) {
				@Override
				public Integer getValue() {
					return Gdx.graphics.getFramesPerSecond();
				}
			}).row();
			tableMonitor.add(new NumberLabel<Float>("Heap: ", -1f, labelStyle) {
				@Override
				public Float getValue() {
					return Gdx.app.getJavaHeap() * 1f / 1024 / 1024;
				}
			}).row();
			tableMonitor.add(new NumberLabel<Float>("Native: ", -1f, labelStyle) {
				@Override
				public Float getValue() {
					return Gdx.app.getNativeHeap() * 1f / 1024 / 1024;
				}
			}).row();
			tableMonitor.getColor().a = .3f;
			tableMonitor.setPosition(20, 20);
			tableMonitor.pack();
			tableMonitor.layout();
			stage.addActor(tableMonitor);
		}
	}

	@Override
	public final void render(float delta) {
		super.render(delta = Math.min(delta, 1.0f / 30.0f));
		Gdx.gl20.glClearColor(GlobalData.ColorBackground.r, GlobalData.ColorBackground.g, GlobalData.ColorBackground.b, GlobalData.ColorBackground.a);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		stage.act(delta);
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
