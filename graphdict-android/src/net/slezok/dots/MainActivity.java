package net.slezok.dots;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.swarmconnect.Swarm;

public class MainActivity extends AndroidApplication implements PlatformDependencies{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		initialize(new Dots(this), cfg);

		//Initialize swarm
		Swarm.setActive(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		Swarm.setActive(this);
		Swarm.init(this, Constants.GRAPHDICT_APP_ID, Constants.GRAPHDICT_APP_KEY);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Swarm.setInactive(this);
	}

	@Override
	public FileHandle openAssetFileCopyIfNeeded(String assetPath) {
		return Gdx.files.internal(assetPath);
	}
}