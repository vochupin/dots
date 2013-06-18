package net.slezok.dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import net.slezok.dots.actors.FallingMan;

public class InputHandler extends InputListener {
	
	private static final String TAG = "InputHandler";
	FallingMan piggy;
	
	public InputHandler(FallingMan piggy) {
		this.piggy = piggy;
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		Gdx.app.log(TAG, "down x=" + x +" y=" + y + " " + piggy.getX());
		if(piggy.getX() < x){
			piggy.moveFallingManLeftRight(5f);
		}else{
			piggy.moveFallingManLeftRight(-5f);			
		}
		return true;
	}
}
