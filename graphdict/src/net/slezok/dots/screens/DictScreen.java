package net.slezok.dots.screens;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.esotericsoftware.tablelayout.BaseTableLayout;
import com.swarmconnect.SwarmLeaderboard;

import net.slezok.dots.Assets;
import net.slezok.dots.Bridge;
import net.slezok.dots.Constants;
import net.slezok.dots.Dots;
import net.slezok.dots.Level;
import net.slezok.dots.actors.DictField;

public class DictScreen extends ActorGestureListener implements Screen {
	private static final String TAG = "DictScreen";

	private static final long CURRENT_SOUND_DELAY = 800;

	protected static final float NORMAL_STEP_TIME = 6000;

	private static final float SETT_HANDLER_WIDTH = 50;
	
	private static final float CONTROL_PANEL_MOVE_TIME = 2;
	private static final float UNVISIBLE_X_POSITION = -300;
	private static final float CONTROL_MUSIC_Y_POSITION = 400;

	private final Dots game;
	private final Level level;

	private Stage stage;
	private Stage staticStage;
	private Stage settStage;
	
	private DictField dictField;

	//for zoom
	private float oldInitialDistance = 0;
	private float initialScale = 0;

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	private ImageButton upButton;
	private ImageButton downButton;
	private ImageButton leftButton;
	private ImageButton rightButton;
	private ImageButton upRightButton;
	private ImageButton upLeftButton;
	private ImageButton downRightButton;
	private ImageButton downLeftButton;

	private ImageButton repeatButton;

	private Label scoreLabel;
	private long prevStepTime;
	private int score = 0;

	private int caretX, caretY;
	private int step;
	private int[] directions;
	
	private Music music;

	private class SoundMessage{
		public Sound sound;
		public long time;
		public long soundPlaceTime;

		public SoundMessage(Sound sound, long time) {
			super();
			this.sound = sound;
			this.time = time;
			this.soundPlaceTime = System.currentTimeMillis();
		}
	}

	private List<SoundMessage> soundMessages = new LinkedList<SoundMessage>();

	private int identicalSteps = 0;

	private Table table;

	private int[] magicSequence = new int[]{
			Bridge.DIRECTION_UP,
			Bridge.DIRECTION_DOWN,
			Bridge.DIRECTION_UP,
			Bridge.DIRECTION_DOWN,
			Bridge.DIRECTION_RIGHT,
			Bridge.DIRECTION_LEFT,
			Bridge.DIRECTION_RIGHT,
			Bridge.DIRECTION_LEFT
	};
	private int magicSeqCounter = 0;
	
	private Image settBackground;
	private ImageButton controlMusicButton;

	public DictScreen(Dots game, Level level) {
		this.game = game;
		this.level = level;
		Assets.loadStepSounds(level.getMaximumIdenticalSteps());
	}

	@Override
	public void render(float delta) {

		Camera camera = stage.getCamera();
		camera.update();
		camera.apply(Gdx.gl10);
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		staticStage.act(delta);
		staticStage.draw();
		//        Table.drawDebug(staticStage);

		stage.act(delta);
		stage.draw();
		
		settStage.act(delta);
		settStage.draw();

		//        debugRenderer.render(world, camera.combined);

		if(soundMessages.size() != 0){
			SoundMessage msg = soundMessages.get(0);
			long currentTime = System.currentTimeMillis();

			if((msg.time + msg.soundPlaceTime) < currentTime){
				msg.sound.play();
				soundMessages.remove(0);

				if(soundMessages.size() != 0){
					soundMessages.get(0).soundPlaceTime = currentTime;
				}
			}
		}
		
		if (Gdx.input.isKeyPressed(Keys.BACK)){ 
			Gdx.app.log(TAG, "Back key was pressed.");
			stopBackgroundMusic();
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
		if(dictField != null){
			stage.setViewport(dictField.getScreenWidth(), dictField.getScreenHeight(), false);
		}
	}

	@Override
	public void show() {
		startBackgroundMusic();

		stage = new Stage();
		staticStage = new Stage(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, false);	
		staticStage.addListener(this);
		settStage = new Stage(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, false);

		caretX = level.getStartX();
		caretY = level.getStartY();
		step = 0;
		directions = level.getDirections();

		dictField = new DictField(level);
		dictField.setPosition(0, 0);
		stage.addActor(dictField);

		table = new Table(Assets.skin);
		createButtons();

		scoreLabel = new Label("Счет: 0", Assets.skin);
		scoreLabel.setFontScale(2);

		table.setFillParent(true);
		table.defaults().width(100).height(80);
		//		table.debug();
		table.add(scoreLabel);
		table.row();
		table.add(upLeftButton).padBottom(50).padRight(100);
		table.add(upButton).padBottom(50).align(BaseTableLayout.CENTER);
		table.add(upRightButton).padBottom(50).padLeft(100);
		table.row();
		table.add(leftButton).padRight(100);
		table.add(repeatButton).align(BaseTableLayout.CENTER);
		table.add(rightButton).padLeft(100);
		table.row();
		table.add(downLeftButton).padTop(50).padRight(100);
		table.add(downButton).padTop(50).align(BaseTableLayout.CENTER);
		table.add(downRightButton).padTop(50).padLeft(100);

		Image bgrImage = new Image(Assets.dictBackgroundTexture);
		bgrImage.setFillParent(true);
		bgrImage.setPosition(0,  0);
		staticStage.addActor(bgrImage);
		staticStage.addActor(table);
		
		settBackground = new Image(Assets.settPanelBackgroundTexture);
		settBackground.setY(0);
		settBackground.setX(SETT_HANDLER_WIDTH - settBackground.getWidth());
		settStage.addActor(settBackground);
		controlMusicButton.setPosition(UNVISIBLE_X_POSITION, CONTROL_MUSIC_Y_POSITION);
		settStage.addActor(controlMusicButton);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(settStage); // WTF if settStage will be added to mux after staticStage then it will not works!!!
		multiplexer.addProcessor(staticStage);
		Gdx.input.setInputProcessor(multiplexer);

		recalculateIdenticalStepsAndPlaySound();
		prevStepTime = System.currentTimeMillis();
	}

	private void startBackgroundMusic() {
		music = Assets.backMusic;

		if(music != null){
			music.setVolume(0.3f);
			music.setLooping(true);
			music.play();
		}
	}

	private void stopBackgroundMusic() {
		if(music != null){
			music.stop();
		}
	}

	private void createButtons() {
		upButton = new ImageButton(new TextureRegionDrawable(Assets.up), new TextureRegionDrawable(Assets.upPressed));
		downButton = new ImageButton(new TextureRegionDrawable(Assets.down), new TextureRegionDrawable(Assets.downPressed));
		leftButton = new ImageButton(new TextureRegionDrawable(Assets.left), new TextureRegionDrawable(Assets.leftPressed));
		rightButton = new ImageButton(new TextureRegionDrawable(Assets.right), new TextureRegionDrawable(Assets.rightPressed));
		upLeftButton = new ImageButton(new TextureRegionDrawable(Assets.upLeft), new TextureRegionDrawable(Assets.upLeftPressed));
		downLeftButton = new ImageButton(new TextureRegionDrawable(Assets.downLeft), new TextureRegionDrawable(Assets.downLeftPressed));
		upRightButton = new ImageButton(new TextureRegionDrawable(Assets.upRight), new TextureRegionDrawable(Assets.upRightPressed));
		downRightButton = new ImageButton(new TextureRegionDrawable(Assets.downRight), new TextureRegionDrawable(Assets.downRightPressed));

		repeatButton = new ImageButton(new TextureRegionDrawable(Assets.repeat), new TextureRegionDrawable(Assets.repeatPressed));
		
		upButton.addListener(buttonListener);
		downButton.addListener(buttonListener);
		leftButton.addListener(buttonListener);
		rightButton.addListener(buttonListener);

		upLeftButton.addListener(buttonListener);
		downLeftButton.addListener(buttonListener);
		upRightButton.addListener(buttonListener);
		downRightButton.addListener(buttonListener);

		repeatButton.addListener(buttonListener);
		
		controlMusicButton = new ImageButton(new TextureRegionDrawable(Assets.musicOn), 
				new TextureRegionDrawable(Assets.musicPressed), new TextureRegionDrawable(Assets.musicOff));
		controlMusicButton.setSize(controlMusicButton.getPrefWidth()*0.5f, controlMusicButton.getPrefHeight()*0.5f);
		controlMusicButton.addListener(settButtonListener);
	}

	private InputListener buttonListener = new InputListener() {

		int stepX, stepY;

		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			Actor actor = event.getListenerActor();
			int direction = getDirection(actor);
			calcSteps(direction);

			if(direction >= 0){
				if(directions[step] != direction){
					Gdx.app.log(TAG, "WRONG STEP");
					
					score -= 100;
					if(score < 0) score = 0;
					scoreLabel.setText("Счет: " + score);
					
					soundMessages.add(new SoundMessage(Assets.errorSound, 0));
				}else{
					Gdx.app.log(TAG, "GOOD STEP");
					dictField.addBridge(new Bridge(caretX, caretY, direction, DictField.LINE_HALF_WIDTH * 2, getLength(stepX, stepY)));
					caretX += stepX; caretY += stepY;

					long currTime = System.currentTimeMillis();
					float diff = currTime - prevStepTime;
					if(diff < CURRENT_SOUND_DELAY) diff = CURRENT_SOUND_DELAY;
					score += 10 * NORMAL_STEP_TIME / diff;
					prevStepTime = currTime;
					scoreLabel.setText("Счет: " + score);

					step++;
					if(step < directions.length){
						soundMessages.clear();
						soundMessages.add(new SoundMessage(Assets.moveSound, 0));
						if(identicalSteps == 0){
							soundMessages.add(new SoundMessage(Assets.wellDoneSound, 500));
						}
						recalculateIdenticalStepsAndPlaySound();
					}
				}

				if(step >= directions.length){
					stopGame();
				}

				if(magicSequence[magicSeqCounter] == direction){
					magicSeqCounter++;
					if(magicSeqCounter >= magicSequence.length){
						while(step < directions.length){
							calcSteps(directions[step]);
							dictField.addBridge(new Bridge(caretX, caretY, directions[step], DictField.LINE_HALF_WIDTH * 2, getLength(stepX, stepY)));
							caretX += stepX; caretY += stepY;
							step++;
						}
						stopGame();
						score = 0;
						scoreLabel.setText("Волшебство есть!!!");
					}
				}else{
					magicSeqCounter = 0;
				}
			}
			
			return false;
		}

		private float getLength(int stepX, int stepY) {
			float x = (stepX >= 0 ? stepX + 2 * DictField.LINE_HALF_WIDTH : stepX - 2 * DictField.LINE_HALF_WIDTH);
			float y = (stepY >= 0 ? stepY + 2 * DictField.LINE_HALF_WIDTH : stepY - 2 * DictField.LINE_HALF_WIDTH);
			return (float) Math.sqrt(x * x + y * y);
		}

		private int getDirection(Actor actor){
			int direction = -1;

			if(actor.equals(upButton)){
				direction = Bridge.DIRECTION_UP;
			}else if(actor.equals(downButton)){
				direction = Bridge.DIRECTION_DOWN;
			}else if(actor.equals(leftButton)){
				direction = Bridge.DIRECTION_LEFT;
			}else if(actor.equals(rightButton)){
				direction = Bridge.DIRECTION_RIGHT;
			}else if(actor.equals(upRightButton)){
				direction = Bridge.DIRECTION_UP_RIGHT;
			}else if(actor.equals(upLeftButton)){
				direction = Bridge.DIRECTION_UP_LEFT;
			}else if(actor.equals(downRightButton)){
				direction = Bridge.DIRECTION_DOWN_RIGHT;
			}else if(actor.equals(downLeftButton)){
				direction = Bridge.DIRECTION_DOWN_LEFT;
			}else if(actor.equals(repeatButton)){
				soundMessages.add(new SoundMessage(Assets.moveSound, 0));
				setCurrentSoundAndPlay(CURRENT_SOUND_DELAY);
			}
			return direction;
		}

		private void calcSteps(int direction){
			switch(direction){
			case Bridge.DIRECTION_UP:
				stepX = 0;
				stepY = 1;
				break;
			case Bridge.DIRECTION_DOWN:
				stepX = 0;
				stepY = -1;
				break;
			case Bridge.DIRECTION_LEFT:
				stepX = -1;
				stepY = 0;
				break;
			case Bridge.DIRECTION_RIGHT:
				stepX = 1;
				stepY = 0;
				break;
			case Bridge.DIRECTION_UP_RIGHT:
				stepX = 1;
				stepY = 1;
				break;
			case Bridge.DIRECTION_UP_LEFT:
				stepX = -1;
				stepY = 1;
				break;
			case Bridge.DIRECTION_DOWN_RIGHT:
				stepX = 1;
				stepY = -1;
				break;
			case Bridge.DIRECTION_DOWN_LEFT:
				stepX = -1;
				stepY = -1;
				break;
			default:
				stepX = 1; stepY = 1;
			}
		}

		private void stopGame() {
			Gdx.app.log(TAG, "GAME COMPLETED");
			table.removeActor(upButton);
			table.removeActor(downButton);
			table.removeActor(leftButton);
			table.removeActor(rightButton);
			table.removeActor(repeatButton);
			table.removeActor(upLeftButton);
			table.removeActor(downLeftButton);
			table.removeActor(upRightButton);
			table.removeActor(downRightButton);

			soundMessages.add(new SoundMessage(Assets.gameOverSound, CURRENT_SOUND_DELAY));
			
			if(Gdx.app.getType() == ApplicationType.Android){
				SwarmLeaderboard.submitScore(Constants.GRAPHDICT_LEADERBOARD_ID, (float)score);
			}
		}
	};

	private void recalculateIdenticalStepsAndPlaySound(){
		if(this.identicalSteps != 0) {
			this.identicalSteps--;
		}else{
			int index = step + 1;
			int identicalSteps = 0;
			while(index < directions.length && directions[step] == directions[index++]) identicalSteps++;

			Gdx.app.log(TAG, "Calculated identical steps: " + identicalSteps);

			if(identicalSteps >= Assets.MAXIMUM_STEPS_NUMBER) identicalSteps = Assets.MAXIMUM_STEPS_NUMBER - 1;
			this.identicalSteps = identicalSteps;
			
			setCurrentSoundAndPlay(CURRENT_SOUND_DELAY);
		}
	}
	
	private void setCurrentSoundAndPlay(long delay) {
		
		Sound currentSound = null;

		if(identicalSteps > 0) {
			soundMessages.add(new SoundMessage(Assets.getStepSound(identicalSteps), delay));
		}

		switch(directions[step]){
		case Bridge.DIRECTION_UP:
			currentSound = Assets.upSound;
			break;
		case Bridge.DIRECTION_DOWN:
			currentSound = Assets.downSound;
			break;
		case Bridge.DIRECTION_LEFT:
			currentSound = Assets.leftSound;
			break;
		case Bridge.DIRECTION_RIGHT:
			currentSound = Assets.rightSound;
			break;
		case Bridge.DIRECTION_UP_LEFT:
			currentSound = Assets.upLeftSound;
			break;
		case Bridge.DIRECTION_DOWN_LEFT:
			currentSound = Assets.downLeftSound;
			break;
		case Bridge.DIRECTION_UP_RIGHT:
			currentSound = Assets.upRightSound;
			break;
		case Bridge.DIRECTION_DOWN_RIGHT:
			currentSound = Assets.downRightSound;
			break;
		}
		if(currentSound != null){
			soundMessages.add(new SoundMessage(currentSound, delay));
		}
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
		staticStage.dispose();
		Assets.wellDoneSound.dispose();
		Assets.gameOverSound.dispose();
		Assets.errorSound.dispose();
		stopBackgroundMusic();
	}

	public void moveRelatively(float x, float y) {
		Camera camera = stage.getCamera();

		camera.position.x -= x;
		if(camera.position.x < 0) camera.position.x = 0;
		if(camera.position.x > dictField.getWorldWidth()) camera.position.x = dictField.getWorldWidth();

		camera.position.y -= y;
		if(camera.position.y < 0) camera.position.y = 0;
		if(camera.position.y > dictField.getWorldHeight()) camera.position.y = dictField.getWorldHeight();

		Gdx.app.log(TAG, "New camera position: x=" + camera.position.x + " y=" + camera.position.y);
	}

	public void addBridge(Bridge bridge) {
		dictField.addBridge(bridge);
	}

	public void zoom(float initialDistance, float distance) {
		if(oldInitialDistance != initialDistance){
			initialScale = dictField.getScaleX();
			oldInitialDistance = initialDistance;
		}else{
			dictField.setScale(initialScale * distance / initialDistance);
		}
	}

	public void setNormalZoom() {
		dictField.setScale(1);		
	}

	@Override
	public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
		Gdx.app.debug(TAG, "pan: " + x + ", " + y + ", " + deltaX + ", " + deltaY);
		if(x < (settBackground.getWidth() + settBackground.getX())){
			if(deltaX > 0){
				showSettingsPanel();
			}else{
				hideSettingsPanel();
			}
		}else{
			moveRelatively(deltaX, deltaY);
		}
	}

	private void showSettingsPanel() {
		settBackground.addAction(Actions.moveTo(-SETT_HANDLER_WIDTH, 0, CONTROL_PANEL_MOVE_TIME, Interpolation.elasticOut));
		float buttonX = (settBackground.getWidth()  - 2 * SETT_HANDLER_WIDTH - controlMusicButton.getWidth()) / 2;
		controlMusicButton.addAction(Actions.moveTo(buttonX, CONTROL_MUSIC_Y_POSITION, CONTROL_PANEL_MOVE_TIME, Interpolation.bounceOut));
	}

	private void hideSettingsPanel() {
		settBackground.addAction(Actions.moveTo(SETT_HANDLER_WIDTH - settBackground.getWidth(), 0, CONTROL_PANEL_MOVE_TIME, Interpolation.elasticOut));
		controlMusicButton.addAction(Actions.moveTo(UNVISIBLE_X_POSITION, CONTROL_MUSIC_Y_POSITION, CONTROL_PANEL_MOVE_TIME, Interpolation.exp5Out));
	}
	
	private InputListener settButtonListener = new InputListener() {

		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			Actor actor = event.getListenerActor();
			
			if(actor.equals(controlMusicButton)){
				if(music.isPlaying()){
					music.pause();
				}else{
					music.play();
				}
			}
			return false;
		}
	};

	@Override
	public boolean longPress(Actor actor, float x, float y) {
		setNormalZoom();
		return true;
	}

	@Override
	public void zoom(InputEvent event, float initialDistance, float distance) {
		zoom(initialDistance, distance);
	}
}
