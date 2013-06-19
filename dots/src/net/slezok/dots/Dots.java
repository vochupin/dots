package net.slezok.dots;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import net.slezok.dots.screens.SplashScreen;

public class Dots extends Game {
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		Assets.load();
		setScreen(new SplashScreen(this));
		
		Gdx.app.setLogLevel(Application.LOG_INFO);
	}
	
}
