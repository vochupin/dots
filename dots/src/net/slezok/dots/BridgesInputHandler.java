package net.slezok.dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import net.slezok.dots.screens.GridScreen;

public class BridgesInputHandler extends InputListener {
	private static final String TAG = "InputHandler";
	
	private float startX = 0, startY = 0;
	GridScreen gridScreen;
	
	public BridgesInputHandler(GridScreen gridScreen) {
		this.gridScreen = gridScreen;
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		startX = x; startY = y;
		Gdx.app.log(TAG, "startX: " + startX + " startY: " + startY);
		return true;
	}

	@Override
	public void touchDragged(InputEvent event, float x, float y, int pointer) {
		Gdx.app.log(TAG, "startX: " + startX + " startY: " + startY + " currX: " + x + " currY: " + y);
		gridScreen.moveRelatively(startX, startY, x, y);
	}

}
