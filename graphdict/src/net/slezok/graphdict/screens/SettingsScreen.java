package net.slezok.graphdict.screens;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.slezok.graphdict.Assets;
import net.slezok.graphdict.Constants;
import net.slezok.graphdict.Dict;
import net.slezok.graphdict.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.swarmconnect.Swarm;

public class SettingsScreen implements Screen {
	private static final String TAG = "SettingsScreen";
	
	private Dict game;
	private Stage stage;
	private ImageButton enableSwarmButton;
	private ImageButton backMusicButton;
	private Level level = null;
	
	private Preferences globalPrefs;

	public SettingsScreen(Dict game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.8f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
//		Table.drawDebug(stage);
		
		if (Gdx.input.isKeyPressed(Keys.BACK)){ 
			new Timer().scheduleTask(new Task(){
				@Override
				public void run() {
					game.setScreen(new LevelsListScreen(game));
				}
			}, 0.5F);
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		globalPrefs = Gdx.app.getPreferences(Constants.GLOBAL_PREFS);
		
		stage = new Stage(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, true);
		Gdx.input.setInputProcessor(stage);
		
		Image backImage = new Image(Assets.settingsBackgroundTexture);
		
		float scale = Constants.VIRTUAL_WIDTH / backImage.getWidth();
		backImage.setScale(scale);

		backImage.setX(stage.getGutterWidth());
		backImage.setY(stage.getGutterHeight() + (Constants.VIRTUAL_HEIGHT - backImage.getHeight() * scale) / 2);
		
		backMusicButton = new ImageButton(new TextureRegionDrawable(Assets.musicOff), 
				new TextureRegionDrawable(Assets.musicPressed), new TextureRegionDrawable(Assets.musicOn));
		backMusicButton.setChecked(globalPrefs.getBoolean(Constants.PLAY_BACKGROUND_MUSIC));
		backMusicButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ImageButton cb = (ImageButton)event.getListenerActor();
				boolean checked = !cb.isChecked(); //will be inverted in near future
				globalPrefs.putBoolean(Constants.PLAY_BACKGROUND_MUSIC, checked);
				globalPrefs.flush();
				return true;
			}
		});

		enableSwarmButton = new ImageButton(new TextureRegionDrawable(Assets.swarmOff), 
				new TextureRegionDrawable(Assets.swarmPressed), new TextureRegionDrawable(Assets.swarmOn));;
		enableSwarmButton.setChecked(globalPrefs.getBoolean(Constants.USE_SWARM));
		enableSwarmButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ImageButton cb = (ImageButton)event.getListenerActor();
				boolean checked = !cb.isChecked(); //will be inverted in near future
				
				if(checked){
					game.enableSwarm();
				}else{
					game.disableSwarm();
				}
				
				globalPrefs.putBoolean(Constants.USE_SWARM, checked);
				globalPrefs.flush();
				return true;
			}
		});

		Label l1 = new Label("������� ������", Assets.skin);
		l1.setFontScale(2);
		Label l2 = new Label("������� � Swarm", Assets.skin);
		l2.setFontScale(2);

		Table table = new Table(Assets.skin);
		table.setWidth(900);
		table.setHeight(400);
		table.align(Align.top);
		table.setX(stage.getGutterWidth());
		table.setY(stage.getGutterHeight() + 200);
//		table.debug(); 
		table.add(backMusicButton).width(100).height(200);
		table.add(l1).width(400).height(200);
		table.row();
		table.add(enableSwarmButton).width(100).height(200);
		table.add(l2).width(400).height(200);
		
		stage.addActor(backImage);
		stage.addActor(table);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
