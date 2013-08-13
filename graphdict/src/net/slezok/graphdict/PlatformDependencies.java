package net.slezok.graphdict;

import com.badlogic.gdx.files.FileHandle;

public interface PlatformDependencies {
	
	public FileHandle openAssetFileCopyIfNeeded(String assetPath);
	
	public void enableSwarm(boolean enable);

}
