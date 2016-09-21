package com.libgdx.skin.editor.utils;

import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.L;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.MINUS;// -
import static com.badlogic.gdx.Input.Keys.N;
import static com.badlogic.gdx.Input.Keys.O;
import static com.badlogic.gdx.Input.Keys.P;
import static com.badlogic.gdx.Input.Keys.PERIOD;// .
import static com.badlogic.gdx.Input.Keys.PLUS;// +
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.Z;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;

/**
 * @作者 Mitkey
 * @时间 2016年4月11日 上午9:35:08
 * @类说明:可以拖动改变坐标、可以使用键盘的方向键改变坐标。若需要打印当前 actor 的坐标，按下【P】键
 * @版本 xx
 */
public class MoveListener extends DragListener {
	private static final String TAG = MoveListener.class.getSimpleName();
	private static final String format = "%s 当前 X 坐标为 %s、Y坐标为 %s、移动步长为 %s";
	private static int STEP_XY = 1;

	Actor actor;
	float originX;
	float originY;
	float x;
	float y;
	boolean debug;

	public MoveListener(Actor actor) {
		this.actor = actor;
		this.originX = actor.getOriginX();
		this.originY = actor.getOriginY();
		this.x = actor.getX();
		this.y = actor.getY();
	}

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		// 在该 actor 获取到焦点时，通知 stage 该 actor 需要处理“键盘点击”事件
		actor.getStage().setKeyboardFocus(actor);
		return super.touchDown(event, x, y, pointer, button);
	}

	@Override
	public void drag(InputEvent event, float x, float y, int pointer) {
		super.drag(event, x, y, pointer);
		actor.moveBy(x - getTouchDownX(), y - getTouchDownY());
	}

	@Override
	public boolean keyDown(InputEvent event, int keycode) {
		switch (keycode) {
			case UP :
				actor.moveBy(0, STEP_XY);
				break;
			case DOWN :
				actor.moveBy(0, -STEP_XY);
				break;
			case LEFT :
				actor.moveBy(-STEP_XY, 0);
				break;
			case RIGHT :
				actor.moveBy(STEP_XY, 0);
				break;
			case P :// 打印位置
				Gdx.app.log(TAG, String.format(format, actor, actor.getX(), actor.getY(), STEP_XY));
				break;
			case PERIOD :// . 符号重置步长
				STEP_XY = 1;
				break;
			case PLUS :// + 符号增加步长
				STEP_XY += 1;
				break;
			case MINUS :// - 符号减少步长
				STEP_XY -= 1;
				break;
			case D :// debug 模式
				debug = !debug;
				actor.getStage().setDebugAll(debug);
				break;
			case L :// 突出当前 actor
				actor.addAction(generalLightAction());
				break;
			case Z :// 重置位置
				actor.setPosition(x, y);
				break;
			case N :// 重新设置初始位置
				x = actor.getX();
				y = actor.getY();
				break;
			case O :
				actor.setPosition(0, 0);
				break;
			default :
				break;
		}
		return false;
	}

	private Action generalLightAction() {
		actor.clearActions();
		actor.setRotation(0);
		actor.setOrigin(originX, originY);
		return Actions.sequence(//
				Actions.run(new Runnable() {
					@Override
					public void run() {
						actor.setOrigin(Align.center);
					}
				}), //
				Actions.repeat(2,
						Actions.sequence(//
								Actions.rotateTo(360, 0.2f), //
								Actions.rotateTo(0, 0)//
						)), //
				Actions.run(new Runnable() {
					@Override
					public void run() {
						// 还原
						actor.setOrigin(originX, originY);
					}
				}));
	}

}
