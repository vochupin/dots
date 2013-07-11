package net.slezok.dots;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import net.slezok.dots.screens.IntroScreen;

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
