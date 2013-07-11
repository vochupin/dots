package net.slezok.dots.client;

import net.slezok.dots.Dots;
import net.slezok.dots.PlatformDependencies;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;

public class GwtLauncher extends GwtApplication implements PlatformDependencies{
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(480, 320);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new Dots(this);
	}

	@Override
	public FileHandle openAssetFileCopyIfNeeded(String assetPath) {
		return Gdx.files.internal(assetPath);
	}
}