package net.slezok.dots.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
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
import net.slezok.dots.Assets;
import net.slezok.dots.Bridge;
import net.slezok.dots.BridgesGestureHandler;
import net.slezok.dots.InputHandler;
import net.slezok.dots.Dots;
import net.slezok.dots.OverlapTester;
import net.slezok.dots.actors.Bridges;
import net.slezok.dots.actors.FallingMan;
import net.slezok.dots.actors.Platforms;

public class GridScreen implements Screen{
	private static final String TAG = "GridScreen";

	World world;
	Dots game;
	Stage stage;
	Stage staticStage;
	public final float WORLD_WIDTH;
	public final float FRUSTUM_HEIGHT;
	public final float WORLD_HEIGHT;
	public final float FRUSTUM_WIDTH;
	
	private Bridges bridges;
	private BridgesGestureHandler inputHandler;
	
	enum GameState {
		Play, Pause
	}
	
	GameState gameState = GameState.Play;
	
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	public GridScreen(Dots game) {
		WORLD_WIDTH = Gdx.graphics.getWidth();
		FRUSTUM_HEIGHT = Gdx.graphics.getHeight();
		WORLD_HEIGHT = FRUSTUM_HEIGHT * 20;
		FRUSTUM_WIDTH = WORLD_WIDTH;
		Gdx.app.log(TAG, "WORLD_WIDTH: " + WORLD_WIDTH + " WORLD_HEIGHT: " + WORLD_HEIGHT);
		this.game = game;
	}

	@Override
	public void render(float delta) {
		
//		if (Gdx.app.getType() == Application.ApplicationType.Android) {
//			inputHandler.accelerometerChange(Gdx.input.getAccelerometerX());
//		}
		
		Camera camera = stage.getCamera();
		Gdx.gl.glClearColor(1f, 0f, 1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		staticStage.act(delta);
        staticStage.draw();
        stage.act(delta);
        stage.draw();
        
        world.step(1/60f, 6, 2);
//        debugRenderer.render(world, camera.combined);
        
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(FRUSTUM_WIDTH, FRUSTUM_HEIGHT, false);
	}

	@Override
	public void show() {
		world = new World(new Vector2(0f, -1), true);
		
		stage = new Stage();
		staticStage = new Stage();	
		staticStage.addListener(new BridgesGestureHandler(this));

		bridges = new Bridges(world);
		bridges.setPosition(0, 0);
		
		stage.addActor(bridges);
		
		Image bgrImage = new Image(Assets.backgroundTexture);
		bgrImage.setFillParent(true);
		bgrImage.setPosition(0,  0);
		staticStage.addActor(bgrImage);
		
		Gdx.input.setInputProcessor(staticStage);
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
	}

	
	
	public void moveRelatively(float x, float y) {
		Camera camera = stage.getCamera();
		camera.position.y -= y;
		if(camera.position.y < 0) camera.position.y = 0;
		Gdx.app.log(TAG, "New Y camera position: " + camera.position.y + " y: " + y);
	}

	public void addBridge(Bridge bridge) {
		bridges.addBridge(bridge);
	}	
}
