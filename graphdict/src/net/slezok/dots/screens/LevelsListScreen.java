package net.slezok.dots.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.swarmconnect.Swarm;

import net.slezok.dots.Assets;
import net.slezok.dots.Constants;
import net.slezok.dots.Dots;
import net.slezok.dots.Level;

public class LevelsListScreen implements Screen {
	private static final String TAG = "LevelsListScreen";
	
	private Dots game;
	private Stage stage;
	private List levelsList;
	private ImageButton playButton;
	private ImageButton recordsButton;
	private Level level = null;

	public LevelsListScreen(Dots game) {
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
			Gdx.app.log(TAG, "Back key was pressed.");
			new Timer().scheduleTask(new Task(){
				@Override
				public void run() {
					Gdx.app.exit();
				}
			}, 1);
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		stage = new Stage(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, true);
		Gdx.input.setInputProcessor(stage);
		
		FileHandle file =  Gdx.files.internal("data/levels.json");
		Json json = new Json();
		@SuppressWarnings("unchecked")
		final Array<Level> levels = json.fromJson(Array.class, Level.class, file);

		String[] levelNames = new String[levels.size];
		for(int i = 0; i < levels.size; i++){
			levelNames[i] = levels.get(i).getDescription();
			levels.get(i).unpackDirections();
		}

		levelsList = new List(levelNames, Assets.skin);
		levelsList.setSelectedIndex(0);
		level = levels.get(0);
		levelsList.addListener(new EventListener(){
			@Override
			public boolean handle(Event event) {
				List list = (List)event.getListenerActor();
				level = levels.get(list.getSelectedIndex());
				return true;
			}
		});
		ScrollPane scroller = new ScrollPane(levelsList);

		Image backImage = new Image(Assets.listBackgroundTexture);
		backImage.setX(stage.getGutterWidth());
		backImage.setY(stage.getGutterHeight());
		
		playButton = new ImageButton(new TextureRegionDrawable(Assets.play), new TextureRegionDrawable(Assets.playPressed));
		playButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if(level != null){
					game.setScreen(new DictScreen(game, level));
				}
			}
		});

		recordsButton = new ImageButton(new TextureRegionDrawable(Assets.records), new TextureRegionDrawable(Assets.recordsPressed));
		recordsButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Swarm.showLeaderboards();
			}
		});

		Table table = new Table(Assets.skin);
		table.setWidth(600);
		table.setHeight(300);
		table.setX(350 + stage.getGutterWidth());
		table.setY(100 + stage.getGutterHeight());
//		table.debug(); 
		table.add(scroller).width(200).height(200);
		table.row();
		table.add(playButton).width(250).height(80).padTop(50);
		table.add(recordsButton).width(250).height(80).padTop(50);
		
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
