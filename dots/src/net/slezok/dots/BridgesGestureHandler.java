package net.slezok.dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import net.slezok.dots.screens.GridScreen;

public class BridgesGestureHandler extends ActorGestureListener{//implements GestureDetector.GestureListener {
	private static final String TAG = "BridgesInputHandler";
	
	GridScreen gridScreen;
	
	public BridgesGestureHandler(GridScreen gridScreen) {
		this.gridScreen = gridScreen;
	}

	@Override
	public void fling(InputEvent event, float velocityX, float velocityY, int button) {
		Gdx.app.log(TAG, "fling: " + velocityX + ", " + velocityY + ", " + button);
	}

	@Override
	public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
		gridScreen.moveRelatively(deltaX, deltaY);
	}

	@Override
	public boolean longPress(Actor actor, float x, float y) {
		gridScreen.setNormalZoom();
		return true;
	}

	@Override
	public void zoom(InputEvent event, float initialDistance, float distance) {
		gridScreen.zoom(initialDistance, distance);
	}
}
