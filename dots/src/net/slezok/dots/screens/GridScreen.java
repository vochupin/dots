package net.slezok.dots.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
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
import net.slezok.dots.InputHandler;
import net.slezok.dots.Dots;
import net.slezok.dots.OverlapTester;
import net.slezok.dots.actors.Bridges;
import net.slezok.dots.actors.FallingMan;
import net.slezok.dots.actors.Platforms;

public class GridScreen implements Screen{
	
	World world;
	Dots game;
	Stage stage;
	Stage staticStage;
	public static final float WORLD_WIDTH = 30;
	public static final float FRUSTUM_HEIGHT = 20;
	public static final float WORLD_HEIGHT = FRUSTUM_HEIGHT * 20;
	public static final float FRUSTUM_WIDTH = WORLD_WIDTH;
	
	private Bridges bridges;
	
	enum GameState {
		Play, Pause
	}
	
	GameState gameState = GameState.Play;
	
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	public GridScreen(Dots game) {
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
		
		bridges = new Bridges(world);
		stage.addActor(bridges);
		
		Image bgrImage = new Image(Assets.backgroundTexture);
		bgrImage.setFillParent(true);
		bgrImage.setPosition(0,  0);
		staticStage.addActor(bgrImage);
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
	
}