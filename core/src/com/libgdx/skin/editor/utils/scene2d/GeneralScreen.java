package com.libgdx.skin.editor.utils.scene2d;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.libgdx.skin.editor.utils.LazyBitmapFont;

/**
 * @作者 Mitkey
 * @时间 2016年9月1日 上午11:19:05
 * @类说明:
 * @版本 xx
 */
public abstract class GeneralScreen extends ScreenAdapter {

	public static int GameWidth = 1280;
	public static int GameHeight = 720;

	Stage stage;
	BitmapFont font;

	@Override
	public void show() {
		super.show();
		this.stage = new Stage(new StretchViewport(GameWidth, GameHeight));

		initMonitor();

	}

	private void initMonitor() {
		this.font = new LazyBitmapFont(18);
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);

		NumberLabel<Integer> labFps = new NumberLabel<Integer>("Fps: ", -1, labelStyle) {
			@Override
			public Integer getValue() {
				return Gdx.graphics.getFramesPerSecond();
			}
		};
		NumberLabel<Float> labHeap = new NumberLabel<Float>("Heap: ", -1f, labelStyle) {
			@Override
			public Float getValue() {
				return Gdx.app.getJavaHeap() * 1f / 1024 / 1024;
			}
		};
		NumberLabel<Float> labNative = new NumberLabel<Float>("Native: ", -1f, labelStyle) {
			@Override
			public Float getValue() {
				return Gdx.app.getNativeHeap() * 1f / 1024 / 1024;
			}
		};

		Table table = new Table();
		table.defaults().width(110).left().pad(5);
		table.add(labFps).row();
		table.add(labHeap).row();
		table.add(labNative).row();
		table.setPosition(10, 10);
		table.pack();
		table.layout();
		stage.addActor(table);
		table.toFront();
	}

	@Override
	public final void render(float delta) {
		super.render(delta = Math.min(delta, 1.0f / 30.0f));
		Gdx.gl20.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		stage.act(delta);

		update(delta);
		draw(delta);
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
		font.dispose();
	}

	public final Stage stage() {
		return stage;
	}

	protected abstract void update(float delta);

	protected abstract void draw(float delta);

}
