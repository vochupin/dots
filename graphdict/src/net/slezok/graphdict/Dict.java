package net.slezok.graphdict;

import net.slezok.graphdict.screens.IntroScreen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Dict extends Game {
	
	private static final String FIRST_RUN = "firstRun";
	private static final String TAG = "Dict";
	private PlatformDependencies deps;
	private Preferences globalPrefs;
	
	public Dict(PlatformDependencies deps) {
		this.deps = deps;
	}

	@Override
	public void create() {
		globalPrefs = Gdx.app.getPreferences(Constants.GLOBAL_PREFS);
		if(globalPrefs.getBoolean(FIRST_RUN, true)){
			Gdx.app.log(TAG, "First run detected.");
			globalPrefs.putBoolean(Constants.PLAY_BACKGROUND_MUSIC, true);
			globalPrefs.putBoolean(FIRST_RUN, false);
			globalPrefs.flush();
		}
		
		deps.enableSwarm(globalPrefs.getBoolean(Constants.USE_SWARM));
		
		Assets.load(deps);
		setScreen(new IntroScreen(this));
		
		Gdx.app.setLogLevel(Application.LOG_INFO);
	}
	
	public void enableSwarm(){
		deps.enableSwarm(true);
	}

	public void disableSwarm(){
		deps.enableSwarm(false);
	}
}

