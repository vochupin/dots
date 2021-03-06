package net.slezok.graphdict;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.slezok.graphdict.Constants;
import net.slezok.graphdict.Dict;
import net.slezok.graphdict.PlatformDependencies;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.swarmconnect.Swarm;

public class MainActivity extends AndroidApplication implements PlatformDependencies{
	private static final String TAG = null;
	private static final int IO_BUFFER_SIZE = 4 * 1024;
	
	private boolean swarmEnabled = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;
		cfg.useAccelerometer = false;
		cfg.useCompass = false;
		initialize(new Dict(this), cfg);

		//Initialize swarm
		if(swarmEnabled){
			Swarm.setActive(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(swarmEnabled){
			Swarm.setActive(this);
			Swarm.init(this, Constants.GRAPHDICT_APP_ID, Constants.GRAPHDICT_APP_KEY);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(swarmEnabled){
			Swarm.setInactive(this);
		}
	}

	@Override
	public FileHandle openAssetFileCopyIfNeeded(String assetPath) {
		try {
			AssetManager am = getAssets();

			AssetFileDescriptor afd = am.openFd(assetPath);
			String assetFileName = new File(assetPath).getName();
			File copyFile = new File(getFilesDir().getAbsolutePath() + "/" + assetFileName);

			if(!copyFile.exists() || copyFile.length() != afd.getLength()){
				Gdx.app.log(TAG, "Copy asset file to internal storage.");
				if(!copyFile.createNewFile()) throw new GdxRuntimeException("Can't create asset file copy: " + assetPath);
				//world readable is to play music on lenovo a800
				//because it plays music in other process
				//TODO: move all resources on SD-CARD...
				OutputStream os = openFileOutput(assetFileName, MODE_WORLD_READABLE); 
				InputStream is = am.open(assetPath);
				copy(is, os);
				is.close();
				os.close();
			}else{
				Gdx.app.log(TAG, "Read pre-stored on internal storage asset file.");
			}
			return Gdx.files.absolute(copyFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			throw new GdxRuntimeException("Exception in openAssetFileCopyIfNeeded: " + e.getMessage());
		}
	}

	private static void copy(InputStream in, OutputStream out) throws IOException {  
		byte[] b = new byte[IO_BUFFER_SIZE];  
		int read;  
		while ((read = in.read(b)) != -1) {  
			out.write(b, 0, read);  
		}
		out.flush();
	}

	@Override
	public void enableSwarm(boolean enable) {
		if(enable){
			Swarm.setActive(this);
			Swarm.init(this, Constants.GRAPHDICT_APP_ID, Constants.GRAPHDICT_APP_KEY);
		}else{
			if(Swarm.isEnabled()) Swarm.setInactive(this);
		}
		swarmEnabled = enable;
	}  
}