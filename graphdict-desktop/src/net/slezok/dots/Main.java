package net.slezok.dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;

public class Main implements PlatformDependencies{
	
	@Override
	public FileHandle openAssetFileCopyIfNeeded(String assetPath) {
		return Gdx.files.internal(assetPath);
	}

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "dots";
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 768;
		
		new LwjglApplication(new Dots(new Main()), cfg);
	}
}
