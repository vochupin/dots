package net.slezok.dots;

import net.slezok.dots.screens.IntroScreen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Dots extends Game {
	
	private PlatformDependencies deps;
	private Preferences globalPrefs;
	
	public Dots(PlatformDependencies deps) {
		this.deps = deps;
	}

	@Override
	public void create() {
		globalPrefs = Gdx.app.getPreferences(Constants.GLOBAL_PREFS);
		
		deps.enableSwarm(globalPrefs.getBoolean(Constants.USE_SWARM));
		
		Assets.load(deps);
		setScreen(new IntroScreen(this));
		
		Gdx.app.setLogLevel(Application.LOG_INFO);
	}
}

