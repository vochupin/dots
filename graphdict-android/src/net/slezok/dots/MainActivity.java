package net.slezok.dots;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.swarmconnect.Swarm;

public class MainActivity extends AndroidApplication {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		initialize(new Dots(), cfg);

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
}