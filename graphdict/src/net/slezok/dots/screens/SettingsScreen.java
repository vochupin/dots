package net.slezok.dots.screens;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.slezok.dots.Assets;
import net.slezok.dots.Constants;
import net.slezok.dots.Dots;
import net.slezok.dots.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
import com.swarmconnect.Swarm;

public class SettingsScreen implements Screen {
	private static final String TAG = "SettingsScreen";
	
	private Dots game;
	private Stage stage;
	private CheckBox enableSwarmButton;
	private CheckBox backMusicButton;
	private Level level = null;

	public SettingsScreen(Dots game) {
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
		stage = new Stage(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, true);
		Gdx.input.setInputProcessor(stage);
		
		Image backImage = new Image(Assets.dictBackgroundTexture);
		
		float scale = Constants.VIRTUAL_WIDTH / backImage.getWidth();
		backImage.setScale(scale);

		backImage.setX(stage.getGutterWidth());
		backImage.setY(stage.getGutterHeight() + (Constants.VIRTUAL_HEIGHT - backImage.getHeight() * scale) / 2);
		
		backMusicButton = new CheckBox("Фоновая музыка", Assets.skin);
		backMusicButton.scale(2);
		backMusicButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log(TAG, "MUSIC");
				return true;
			}
		});

		enableSwarmButton = new CheckBox("Включить Swarm", Assets.skin);
		enableSwarmButton.scale(2);
		enableSwarmButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log(TAG, "SWARM");
				return true;
			}
		});


		Table table = new Table(Assets.skin);
		table.setWidth(900);
		table.setHeight(300);
		table.align(Align.top);
		table.setX(stage.getGutterWidth());
		table.setY(stage.getGutterHeight());
//		table.debug(); 
		table.add(backMusicButton).width(450).height(200).padTop(50);
		table.add(enableSwarmButton).width(450).height(200).padTop(50);
		
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
