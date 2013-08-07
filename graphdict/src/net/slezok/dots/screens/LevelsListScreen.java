package net.slezok.dots.screens;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.slezok.dots.Assets;
import net.slezok.dots.Constants;
import net.slezok.dots.Dots;
import net.slezok.dots.Level;

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
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.swarmconnect.Swarm;

public class LevelsListScreen implements Screen {
	private static final String TAG = "LevelsListScreen";

	private static final String PLAYED_LEVELS = "played";
	
	private Dots game;
	private Stage stage;
	private List levelsList;
	
	private ImageButton playButton;
	private ImageButton recordsButton;

	private TextButton settButton;
	private CheckBox showNewOnlyCheckBox;

	private Level level = null;
	private Map<String, Level> playableLevelsMap = new HashMap<String, Level>();
	
	private Array<Level> allLevels;
	private Preferences prefs;

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
		prefs = Gdx.app.getPreferences(PLAYED_LEVELS);
		
		stage = new Stage(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, true);
		Gdx.input.setInputProcessor(stage);
		
		allLevels = loadAllLeves();
		String[] levelNames = getPlayableLevelNames(allLevels, false);

		levelsList = new List(levelNames, Assets.skin);
		levelsList.setSelectedIndex(0);
		level = playableLevelsMap.get(levelNames[0]);
		levelsList.addListener(new EventListener(){
			@Override
			public boolean handle(Event event) {
				List list = (List)event.getListenerActor();
				level = playableLevelsMap.get(list.getSelection());
				return true;
			}
		});
		ScrollPane scroller = new ScrollPane(levelsList);

		Image backImage = new Image(Assets.listBackgroundTexture);
		
		float scale = Constants.VIRTUAL_WIDTH / backImage.getWidth();
		backImage.setScale(scale);

		backImage.setX(stage.getGutterWidth());
		backImage.setY(stage.getGutterHeight() + (Constants.VIRTUAL_HEIGHT - backImage.getHeight() * scale) / 2);
		
		playButton = new ImageButton(new TextureRegionDrawable(Assets.play), new TextureRegionDrawable(Assets.playPressed));
		playButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if(level != null){
					prefs.putBoolean(level.getId(), true);
					prefs.flush();
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
		
		settButton = new TextButton("Настройки", Assets.skin);
		settButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new SettingsScreen(game));
			}
		});

		showNewOnlyCheckBox = new CheckBox("Только новые", Assets.skin);
		showNewOnlyCheckBox.setChecked(false);
		showNewOnlyCheckBox.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				CheckBox cb = (CheckBox)event.getListenerActor();
				levelsList.setItems(getPlayableLevelNames(allLevels, !cb.isChecked()));
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//				game.setScreen(new SettingsScreen(game));
			}
		});

		Label spacer = new Label("  ", Assets.skin);
		VerticalGroup additionalButtons = new VerticalGroup();
		additionalButtons.addActor(showNewOnlyCheckBox);
		additionalButtons.addActor(spacer);
		additionalButtons.addActor(settButton);
		
		Table table = new Table(Assets.skin);
		table.setWidth(600);
		table.setHeight(300);
		table.setX(350 + stage.getGutterWidth());
		table.setY(100 + stage.getGutterHeight());
//		table.debug(); 
		table.add(scroller).width(200).height(200).padTop(50);
		table.add(additionalButtons);
		table.row();
		table.add(playButton).width(250).height(80).padTop(50);
		table.add(recordsButton).width(250).height(80).padTop(50);
		
		stage.addActor(backImage);
		stage.addActor(table);
	}

	private String[] getPlayableLevelNames(final Array<Level> levels, boolean newOnly) {
		FileHandle namesFile =  Gdx.files.internal("data/level_names_ru.json");
		Json namesJson = new Json();
		OrderedMap namesMap;
		try {
			namesMap = (OrderedMap)new JsonReader().parse(new InputStreamReader(namesFile.read(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new SerializationException(e);
		}
		
		java.util.List<String> levelNames = new java.util.ArrayList<String>();
		playableLevelsMap.clear();
		
		for(int i = 0; i < levels.size; i++){
			Level lev = levels.get(i);
			String id = lev.getId();
			String levelName = (String) namesMap.get(id);

			if(newOnly == false || prefs.getBoolean(id) == false) {
				if(levelName != null){
					levelNames.add(levelName);
					lev.setName(levelName);
					playableLevelsMap.put(levelName, lev);
				}else{
					levelNames.add(id);
					lev.setName(id);
					playableLevelsMap.put(id, lev);
				}
			}
		}
		
		String[] retval = new String[levelNames.size()];
		levelNames.toArray(retval);
		return retval;
	}

	private Array<Level> loadAllLeves() {
		FileHandle levelsFile =  Gdx.files.internal("data/levels.json");
		Json levelsJson = new Json();
		@SuppressWarnings("unchecked")
		final Array<Level> levels = levelsJson.fromJson(Array.class, Level.class, levelsFile);

		for(Level level : levels){
			level.unpackDirections();
		}

		return levels;
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
