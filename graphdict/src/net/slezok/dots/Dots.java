package net.slezok.dots;

import net.slezok.dots.screens.IntroScreen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Dots extends Game {
	
	private PlatformDependencies deps;
	
	public Dots(PlatformDependencies deps) {
		this.deps = deps;
	}

	@Override
	public void create() {
		Assets.load(deps);
		setScreen(new IntroScreen(this));
		
		Gdx.app.setLogLevel(Application.LOG_INFO);
	}
	
}
