package net.slezok.dots.screens;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.tablelayout.BaseTableLayout;

import net.slezok.dots.Assets;
import net.slezok.dots.Bridge;
import net.slezok.dots.DictGestureHandler;
import net.slezok.dots.Dots;
import net.slezok.dots.Level;
import net.slezok.dots.actors.DictField;

public class DictScreen implements Screen{
	private static final String TAG = "GridScreen";

	private static final long CURRENT_SOUND_DELAY = 800;

	private World world;

	private final Dots game;
	private final Level level;

	private Stage stage;
	private Stage staticStage;
	private DictField bridgesGrid;
	private DictGestureHandler inputHandler;

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

	private Label errorsLabel;
	private int errors = 0;

	private int caretX, caretY;
	private int step;
	private int[] directions;

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

	public DictScreen(Dots game, Level level) {
		this.game = game;
		this.level = level;
		Assets.loadStepSounds(level.getMaximumIdenticalSteps());
	}

	@Override
	public void render(float delta) {

		Camera camera = stage.getCamera();

		Gdx.gl.glClearColor(1f, 0f, 1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		staticStage.act(delta);
		staticStage.draw();
		//        Table.drawDebug(staticStage);

		stage.act(delta);
		stage.draw();

		world.step(1/60f, 6, 2);
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
	}

	@Override
	public void resize(int width, int height) {
		if(bridgesGrid != null){
			stage.setViewport(bridgesGrid.getScreenWidth(), bridgesGrid.getScreenHeight(), false);
		}
	}

	@Override
	public void show() {
		world = new World(new Vector2(0f, -1), true);
		
		Music music = Assets.backMusic;
		
		music.setVolume(0.3f);
		music.setLooping(true);
		music.play();

		stage = new Stage();
		staticStage = new Stage();	
		staticStage.addListener(new DictGestureHandler(this));

		caretX = level.getStartX();
		caretY = level.getStartY();
		step = 0;
		directions = level.getDirections();

		bridgesGrid = new DictField(world, level);
		bridgesGrid.setPosition(0, 0);
		stage.addActor(bridgesGrid);

		table = new Table(Assets.skin);
		createButtons();

		errorsLabel = new Label("Errors: 0", Assets.skin);
		errorsLabel.setFontScale(2);

		table.setFillParent(true);
		table.defaults().width(100).height(80);
		//		table.debug();
		table.add(errorsLabel);
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

		Gdx.input.setInputProcessor(staticStage);

		setCurrentSoundAndPlay(CURRENT_SOUND_DELAY);
	}

	private void createButtons() {
		upButton = new ImageButton(new TextureRegionDrawable(Assets.up));
		downButton = new ImageButton(new TextureRegionDrawable(Assets.down));
		leftButton = new ImageButton(new TextureRegionDrawable(Assets.left));
		rightButton = new ImageButton(new TextureRegionDrawable(Assets.right));
		upLeftButton = new ImageButton(new TextureRegionDrawable(Assets.upLeft));
		downLeftButton = new ImageButton(new TextureRegionDrawable(Assets.downLeft));
		upRightButton = new ImageButton(new TextureRegionDrawable(Assets.upRight));
		downRightButton = new ImageButton(new TextureRegionDrawable(Assets.downRight));

		repeatButton = new ImageButton(new TextureRegionDrawable(Assets.repeat));

		upButton.addListener(buttonListener);
		downButton.addListener(buttonListener);
		leftButton.addListener(buttonListener);
		rightButton.addListener(buttonListener);

		upLeftButton.addListener(buttonListener);
		downLeftButton.addListener(buttonListener);
		upRightButton.addListener(buttonListener);
		downRightButton.addListener(buttonListener);

		repeatButton.addListener(buttonListener);
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
					errors++;
					errorsLabel.setText("Errors: " + errors);
					Assets.errorSound.play();
				}else{
					Gdx.app.log(TAG, "GOOD STEP");
					bridgesGrid.addBridge(new Bridge(caretX, caretY, direction, DictField.LINE_HALF_WIDTH * 2, getLength(stepX, stepY)));
					caretX += stepX; caretY += stepY;
					step++;
					if(step < directions.length){
						Assets.wellDoneSound.play();
						soundMessages.clear();
						setCurrentSoundAndPlay(CURRENT_SOUND_DELAY);
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
							bridgesGrid.addBridge(new Bridge(caretX, caretY, directions[step], DictField.LINE_HALF_WIDTH * 2, getLength(stepX, stepY)));
							caretX += stepX; caretY += stepY;
							step++;
						}
						stopGame();
						errors = 0;
						errorsLabel.setText("Волшебство есть!!!");
					}
				}else{
					magicSeqCounter = 0;
				}
			}
			return true;
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
				setCurrentSoundAndPlay(0);
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

			Assets.wellDoneSound.stop();
			Assets.errorSound.stop();
			Assets.gameOverSound.play();
		}

	};

	private void setCurrentSoundAndPlay(long delay) {

		Sound currentSound = null;

		checkForIdenticalSteps(delay);

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

	private void checkForIdenticalSteps(long delay) {
		int index = step + 1;
		int identicalSteps = 0;
		while(index < directions.length && directions[step] == directions[index++]) identicalSteps++;
		if(identicalSteps > 0) {
			soundMessages.add(new SoundMessage(Assets.getStepSound(identicalSteps), delay));
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
		Assets.wellDoneSound.dispose();
		Assets.gameOverSound.dispose();
		Assets.errorSound.dispose();
	}

	public void moveRelatively(float x, float y) {
		Camera camera = stage.getCamera();

		camera.position.x -= x;
		if(camera.position.x < 0) camera.position.x = 0;
		if(camera.position.x > bridgesGrid.getWorldWidth()) camera.position.x = bridgesGrid.getWorldWidth();

		camera.position.y -= y;
		if(camera.position.y < 0) camera.position.y = 0;
		if(camera.position.y > bridgesGrid.getWorldHeight()) camera.position.y = bridgesGrid.getWorldHeight();

		Gdx.app.log(TAG, "New camera position: x=" + camera.position.x + " y=" + camera.position.y);
	}

	public void addBridge(Bridge bridge) {
		bridgesGrid.addBridge(bridge);
	}

	public void zoom(float initialDistance, float distance) {
		if(oldInitialDistance != initialDistance){
			initialScale = bridgesGrid.getScaleX();
			oldInitialDistance = initialDistance;
		}else{
			bridgesGrid.setScale(initialScale * distance / initialDistance);
		}
	}

	public void setNormalZoom() {
		bridgesGrid.setScale(1);		
	}	
}
