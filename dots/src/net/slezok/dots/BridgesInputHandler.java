package net.slezok.dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import net.slezok.dots.actors.FallingMan;
import net.slezok.dots.screens.GridScreen;

public class BridgesInputHandler extends InputListener {
	
	private static final String TAG = "InputHandler";
	GridScreen gridScreen;
	
	public BridgesInputHandler(GridScreen gridScreen) {
		this.gridScreen = gridScreen;
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//		Gdx.app.log(TAG, "down x=" + x +" y=" + y + " " + piggy.getX());
//		if(piggy.getX() < x){
//			piggy.moveFallingManLeftRight(5f);
//		}else{
//			piggy.moveFallingManLeftRight(-5f);			
//		}
		return true;
	}
}
