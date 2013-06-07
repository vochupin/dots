package net.slezok.dots;

import com.badlogic.gdx.Game;
import net.slezok.dots.screens.SplashScreen;

public class Dots extends Game {
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		Assets.load();
		setScreen(new SplashScreen(this));
	}
	
}
