package net.slezok.dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import net.slezok.dots.screens.GridScreen;

public class BridgesInputHandler extends ActorGestureListener{//implements GestureDetector.GestureListener {
	private static final String TAG = "BridgesInputHandler";
	
	GridScreen gridScreen;
	
	public BridgesInputHandler(GridScreen gridScreen) {
		this.gridScreen = gridScreen;
	}

	@Override
	public void fling(InputEvent event, float velocityX, float velocityY, int button) {
		Gdx.app.log(TAG, "fling: " + velocityX + ", " + velocityY + ", " + button);
	}

	@Override
	public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
		gridScreen.moveRelatively(x, y, deltaX, deltaY);
	}
}
