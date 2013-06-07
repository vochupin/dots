package net.slezok.dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import net.slezok.dots.actors.FallingMan;

public class InputHandler extends InputListener {
	
	FallingMan piggy;
	
	public InputHandler(FallingMan piggy) {
		this.piggy = piggy;
	}
	
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		System.out.println("down x=" + x +" y=" + y + " " + piggy.getX());
		if(piggy.getX() < x){
			piggy.moveFallingManLeftRight(5f);
		}else{
			piggy.moveFallingManLeftRight(-5f);			
		}
		return true;
	}

//	@Override
//	public boolean keyDown(InputEvent event, int keycode) {
//		// TODO Auto-generated method stub
//
//		switch (keycode) {
//		case Keys.W:
//			Gdx.app.log("", "Key down pressed");
//			piggy.moveFallingManUpDown(2f);
////			pigModel.setLinearVelocity(0f, 1.0f);
//			// felix.getVelocity().y = 1;
//			break;
//		case Keys.S:
//			// felix.getVelocity().y = -4;
//			
//			break;
//		case Keys.A:
//			// felix.getVelocity().x = -1;
//			piggy.moveFallingManLeftRight(-5f);
//			break;
//		case Keys.D:
//			// felix.getVelocity().x = 1;
//			piggy.moveFallingManLeftRight(5f);
//			break;
//		default:
//			break;
//		}
//		return true;
//	}

//	@Override
//	public boolean keyUp(InputEvent event, int keycode) {
//
//		switch (keycode) {
//		case Keys.W:
//			Gdx.app.log("", "Key up pressed");
//			piggy.moveFallingManUpDown(-2f);
////			pigModel.setLinearVelocity(0f, -2.0f);
//			break;
//		// case Keys.S:
//		// if(felix.getVelocity().y == -1)
//		// // felix.getVelocity().y = 0;
//		// break;
//		// case Keys.A:
//		// if(felix.getVelocity().x == -1)
//		// felix.getVelocity().x = 0;
//		// break;
//		// case Keys.D:
//		// if(felix.getVelocity().x == 1)
//		// felix.getVelocity().x = 0;
//		// break;
//		default:
//			break;
//		}

//		return true;
//	}
	
//	public void accelerometerChange(float acc) {
//		
//		piggy.moveFallingManLeftRight(-acc);
//		
////		felix = world.getFelix();
////		felix.getVelocity().x = -acc;
//		
//	}
}
