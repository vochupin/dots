package net.slezok.dots.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.tablelayout.BaseTableLayout;

import net.slezok.dots.Assets;
import net.slezok.dots.Bridge;
import net.slezok.dots.DictGestureHandler;
import net.slezok.dots.InputHandler;
import net.slezok.dots.Dots;
import net.slezok.dots.Level;
import net.slezok.dots.OverlapTester;
import net.slezok.dots.actors.DictField;
import net.slezok.dots.actors.FallingMan;
import net.slezok.dots.actors.Platforms;

public class DictScreen implements Screen{
	private static final String TAG = "GridScreen";

	protected static final int STEP_SIZE = 5;

	private static final long CURRENT_SOUND_DELAY = 1500;

	private World world;
	private Dots game;
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
	
	private ImageButton repeatButton;
	
	private Label errorsLabel;
	private int errors = 0;
	
	private Sound wellDoneSound;
	private Sound gameOverSound;
	private Sound errorSound;

	private Sound upSound;
	private Sound downSound;
	private Sound leftSound;
	private Sound rightSound;

	private Level level;
	private int caretX, caretY;
	private int step;
	private int[] directions;

	private Sound currentSound = null;
	private long currentSoundPlayTime = Long.MAX_VALUE;
	
	public DictScreen(Dots game) {
		this.game = game;
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
        
        if(currentSound != null && currentSoundPlayTime < System.currentTimeMillis()){
        	currentSound.play();
        	currentSoundPlayTime = Long.MAX_VALUE;
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
		wellDoneSound = Gdx.audio.newSound(Gdx.files.internal("data/welldone.mp3"));
		gameOverSound = Gdx.audio.newSound(Gdx.files.internal("data/gameover.mp3"));
		errorSound = Gdx.audio.newSound(Gdx.files.internal("data/error.mp3"));
		
		upSound = Gdx.audio.newSound(Gdx.files.internal("data/up.mp3"));
		downSound = Gdx.audio.newSound(Gdx.files.internal("data/down.mp3"));
		leftSound = Gdx.audio.newSound(Gdx.files.internal("data/left.mp3"));
		rightSound = Gdx.audio.newSound(Gdx.files.internal("data/right.mp3"));
		
		world = new World(new Vector2(0f, -1), true);
		
		stage = new Stage();
		staticStage = new Stage();	
		staticStage.addListener(new DictGestureHandler(this));

		FileHandle file =  Gdx.files.internal("data/levels.json");
		Json json = new Json();
		@SuppressWarnings("unchecked")
		Array<Level> levels = json.fromJson(Array.class, Level.class, file);
		level = levels.get(0);

		caretX = level.getStartX();
		caretY = level.getStartY();
		step = 0;
		directions = level.getDirections();
		
		bridgesGrid = new DictField(world, level.getLevelFile());
		bridgesGrid.setPosition(0, 0);
		stage.addActor(bridgesGrid);
		
		final Table table = new Table(Assets.skin);
		upButton = new ImageButton(new TextureRegionDrawable(Assets.up));
		downButton = new ImageButton(new TextureRegionDrawable(Assets.down));
		leftButton = new ImageButton(new TextureRegionDrawable(Assets.left));
		rightButton = new ImageButton(new TextureRegionDrawable(Assets.right));
		
		repeatButton = new ImageButton(new TextureRegionDrawable(Assets.repeat));
		
		InputListener buttonListener = new InputListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Actor actor = event.getListenerActor();
				int direction = -1;
				int nextCaretX = -1, nextCaretY = -1;
				
				if(actor.equals(upButton)){
					direction = Bridge.DIRECTION_UP;
					nextCaretX = caretX;
					nextCaretY = caretY + STEP_SIZE;
				}else if(actor.equals(downButton)){
					direction = Bridge.DIRECTION_DOWN;
					nextCaretX = caretX;
					nextCaretY = caretY - STEP_SIZE;
				}else if(actor.equals(leftButton)){
					direction = Bridge.DIRECTION_LEFT;
					nextCaretX = caretX - STEP_SIZE;
					nextCaretY = caretY;
				}else if(actor.equals(rightButton)){
					direction = Bridge.DIRECTION_RIGHT;
					nextCaretX = caretX + STEP_SIZE;
					nextCaretY = caretY;
				}else if(actor.equals(repeatButton)){
					setCurrentSoundAndPlay(0);
				}
				
				if(direction >= 0){
					if(directions[step] != direction){
						Gdx.app.log(TAG, "WRONG STEP");
						errors++;
						errorsLabel.setText("Errors: " + errors);
						errorSound.play();
					}else{
						Gdx.app.log(TAG, "GOOD STEP");
						bridgesGrid.addBridge(new Bridge(caretX, caretY, direction, 1, STEP_SIZE));
						caretX = nextCaretX; caretY = nextCaretY;
						step++;
						if(step < directions.length){
							wellDoneSound.play();
							setCurrentSoundAndPlay(CURRENT_SOUND_DELAY);
						}
					}
					
					if(step >= directions.length){
						Gdx.app.log(TAG, "GAME COMPLETED");
						table.removeActor(upButton);
						table.removeActor(downButton);
						table.removeActor(leftButton);
						table.removeActor(rightButton);
						table.removeActor(repeatButton);
						
						wellDoneSound.stop();
						errorSound.stop();
						gameOverSound.play();
					}
				}
				return true;
			}
			
		};
		upButton.addListener(buttonListener);
		downButton.addListener(buttonListener);
		leftButton.addListener(buttonListener);
		rightButton.addListener(buttonListener);
		
		repeatButton.addListener(buttonListener);

		errorsLabel = new Label("Errors: 0", Assets.skin);
		errorsLabel.setFontScale(2);
		
		table.setFillParent(true);
		table.defaults().width(100).height(80);
//		table.debug();
		table.add(errorsLabel);
		table.row();
		table.add(upButton).padBottom(50).colspan(3).align(BaseTableLayout.CENTER);
		table.row();
		table.add(leftButton).padRight(100);
		table.add(repeatButton).align(BaseTableLayout.CENTER);
		table.add(rightButton).padLeft(100);
		table.row();
		table.add(downButton).padTop(50).colspan(3).align(BaseTableLayout.CENTER);
		
		Image bgrImage = new Image(Assets.backgroundTexture);
		bgrImage.setFillParent(true);
		bgrImage.setPosition(0,  0);
		staticStage.addActor(bgrImage);
		staticStage.addActor(table);
		
		Gdx.input.setInputProcessor(staticStage);
		
		setCurrentSoundAndPlay(CURRENT_SOUND_DELAY);
	}

	private void setCurrentSoundAndPlay(long delay) {
		switch(directions[step]){
		case Bridge.DIRECTION_UP:
			currentSound = upSound;
			break;
		case Bridge.DIRECTION_DOWN:
			currentSound = downSound;
			break;
		case Bridge.DIRECTION_LEFT:
			currentSound = leftSound;
			break;
		case Bridge.DIRECTION_RIGHT:
			currentSound = rightSound;
			break;
		}
		if(currentSound != null){
			currentSoundPlayTime = System.currentTimeMillis() + delay;
		}
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		wellDoneSound.dispose();
		gameOverSound.dispose();
		errorSound.dispose();
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
