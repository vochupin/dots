package net.slezok.graphdict.screens;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.slezok.graphdict.Assets;
import net.slezok.graphdict.Constants;
import net.slezok.graphdict.Dots;
import net.slezok.graphdict.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

	private Dots game;
	private Stage stage;
	private ScrollPane scroller = null;

	private ImageButton playButton;
	private ImageButton recordsButton;

	private ImageButton settButton;
	private ImageButton showNewOnlyCheckBox;

	private int levelIndex = -1;

	private Level[] levels;
	private Preferences globalPrefs;
	private Preferences playedLevelsPrefs;
	
	Image noNewLevelsImage;

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
		globalPrefs = Gdx.app.getPreferences(Constants.GLOBAL_PREFS);
		playedLevelsPrefs = Gdx.app.getPreferences(Constants.PLAYED_LEVELS_PREFS);

		stage = new Stage(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, true);
		Gdx.input.setInputProcessor(stage);

		levels = loadLevels(globalPrefs.getBoolean(Constants.ONLY_NEW_LEVELS));
		String[] levelNames = loadLevelNames(levels);

		noNewLevelsImage = new Image(Assets.noNewLevelsImage);
		if(levelNames.length > 0){
			List levelsList = createLevelsList(levelNames);
			scroller = new ScrollPane(levelsList);
		}else{
			scroller = new ScrollPane(noNewLevelsImage);
		}

		Image backImage = new Image(Assets.listBackgroundTexture);

		float scale = Constants.VIRTUAL_WIDTH / backImage.getWidth();
		backImage.setScale(scale);

		backImage.setX(stage.getGutterWidth());
		backImage.setY(stage.getGutterHeight() + (Constants.VIRTUAL_HEIGHT - backImage.getHeight() * scale) / 2);

		createButtons();

		Table subTable = new Table(Assets.skin);
		subTable.add(showNewOnlyCheckBox).width(250).height(80);
		subTable.row();
		subTable.add(settButton).width(250).height(80);

		Table table = new Table(Assets.skin);
		table.setWidth(600);
		table.setHeight(300);
		table.setX(350 + stage.getGutterWidth());
		table.setY(130 + stage.getGutterHeight());
		//		table.debug(); 
		table.add(scroller);
		table.add(subTable);
		table.row();
		table.add(playButton).width(250).height(80).padTop(50);
		
		if(globalPrefs.getBoolean(Constants.USE_SWARM)) table.add(recordsButton).width(250).height(80).padTop(50);

		stage.addActor(backImage);
		stage.addActor(table);
	}

	private void createButtons() {
		playButton = new ImageButton(new TextureRegionDrawable(Assets.play), new TextureRegionDrawable(Assets.playPressed));
		playButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if(levelIndex >= 0 && levelIndex < levels.length){
					Level level = levels[levelIndex];
					playedLevelsPrefs.putBoolean(level.getId(), true);
					playedLevelsPrefs.flush();
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
				if(globalPrefs.getBoolean(Constants.USE_SWARM)){
					Swarm.showLeaderboards();
				}
			}
		});

		settButton = new ImageButton(new TextureRegionDrawable(Assets.settings), new TextureRegionDrawable(Assets.settingsPressed));
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

		showNewOnlyCheckBox = new ImageButton(new TextureRegionDrawable(Assets.onlyNewOff), 
				new TextureRegionDrawable(Assets.onlyNewPressed), new TextureRegionDrawable(Assets.onlyNewOn));
		showNewOnlyCheckBox.setChecked(globalPrefs.getBoolean(Constants.ONLY_NEW_LEVELS));
		showNewOnlyCheckBox.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				ImageButton cb = (ImageButton)event.getListenerActor();
				boolean checked = !cb.isChecked(); //will be inverted in near future
				levels = loadLevels(checked);
				String[] levelNames = loadLevelNames(levels);
				
				if(levels.length > 0){
					Actor actor = scroller.getWidget();
					if(actor instanceof List){
						List levelsList = (List)actor;
						levelsList.setItems(levelNames);
					}else{
						List levelsList = createLevelsList(levelNames);
						scroller.setWidget(levelsList);
					}
				}else{
					scroller.setWidget(noNewLevelsImage);
				}
				
				globalPrefs.putBoolean(Constants.ONLY_NEW_LEVELS, checked);
				globalPrefs.flush();
				return true;
			}
		});
	}

	private List createLevelsList(String[] levelNames) {
		List levelsList = new List(levelNames, Assets.skin);
		levelsList.setSelectedIndex(0);
		if(levels.length > 0) levelIndex = 0;
		levelsList.addListener(new EventListener(){
			@Override
			public boolean handle(Event event) {
				List list = (List)event.getListenerActor();
				levelIndex = list.getSelectedIndex();
				return true;
			}
		});
		return levelsList;
	}

	private Level[] loadLevels(boolean newOnly) {
		FileHandle levelsFile =  Gdx.files.internal("data/levels.json");
		Json levelsJson = new Json();
		@SuppressWarnings("unchecked")
		final Array<Level> allLevels = levelsJson.fromJson(Array.class, Level.class, levelsFile);

		java.util.List<Level> levels = new java.util.ArrayList<Level>(); 
		for(Level level : allLevels){
			if(newOnly == false || playedLevelsPrefs.getBoolean(level.getId()) == false) {
				level.unpackDirections();
				levels.add(level);
			}
		}

		return levels.toArray(new Level[levels.size()]);
	}

	private String[] loadLevelNames(final Level[] levels) {
		FileHandle namesFile =  Gdx.files.internal("data/level_names_ru.json");
		Json namesJson = new Json();
		OrderedMap namesMap;
		try {
			namesMap = (OrderedMap)new JsonReader().parse(new InputStreamReader(namesFile.read(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new SerializationException(e);
		}

		java.util.List<String> levelNames = new java.util.ArrayList<String>();
		for(Level level : levels){
			String id = level.getId();
			String levelName = (String) namesMap.get(id);

			if(levelName != null){
				levelNames.add(levelName);
				level.setName(levelName);
			}else{
				levelNames.add(id);
				level.setName(id);
			}
		}

		return levelNames.toArray(new String[levelNames.size()]);
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
