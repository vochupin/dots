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
		super.fling(event, velocityX, velocityY, button);
	}

	@Override
	public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
		Gdx.app.log(TAG, "pan: " + x + ", " + y + ", " + deltaX + ", " + deltaY);
		super.pan(event, x, y, deltaX, deltaY);
	}
	
	

//	@Override
//	public boolean touchDown(float x, float y, int pointer, int button) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean tap(float x, float y, int count, int button) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean longPress(float x, float y) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean fling(float velocityX, float velocityY, int button) {
//		Gdx.app.log(TAG, "fling: " + velocityX + ", " + velocityY + ", " + button);
//		return true;
//	}
//
//	@Override
//	public boolean pan(float x, float y, float deltaX, float deltaY) {
//		Gdx.app.log(TAG, "pan: " + x + ", " + y + ", " + deltaX + ", " + deltaY);
//		return true;
//	}
//
//	@Override
//	public boolean zoom(float initialDistance, float distance) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
//			Vector2 pointer1, Vector2 pointer2) {
//		// TODO Auto-generated method stub
//		return false;
//	}
}
