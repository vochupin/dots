package net.slezok.graphdict.screens;

import net.slezok.graphdict.Assets;
import net.slezok.graphdict.Bridge;
import net.slezok.graphdict.Dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PropertiesScreen implements Screen{
	private static final String TAG = "PropertiesScreen";

	private static final long CURRENT_SOUND_DELAY = 800;

	private World world;

	private final Dots game;

	private Stage staticStage;
	
	private Label nameLabel;

	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	public PropertiesScreen(Dots game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1f, 0f, 1f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		staticStage.act(delta);
		staticStage.draw();
		//        Table.drawDebug(staticStage);

		world.step(1/60f, 6, 2);
		//        debugRenderer.render(world, camera.combined);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		world = new World(new Vector2(0f, -1), true);

		Table table = new Table(Assets.skin);
		table.setFillParent(true);
		
		nameLabel = new Label("Ãëåá", Assets.skin);
		nameLabel.setFontScale(3);//FIXME
		
		nameLabel.addListener(new InputListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				MyTextInputListener listener = new MyTextInputListener();
				Gdx.input.getTextInput(listener, "Èìÿ:", nameLabel.getText().toString());
				return true;
			}
		});
		
		table.add(new Label("Èìÿ: ", Assets.skin)).width(100).height(120);//FIXME
		table.add(nameLabel).width(300).height(120);

		staticStage = new Stage();

		Image bgrImage = new Image(Assets.dictBackgroundTexture);
		bgrImage.setFillParent(true);
		bgrImage.setPosition(0,  0);
		staticStage.addActor(bgrImage);
		staticStage.addActor(table);
		
		Gdx.input.setInputProcessor(staticStage);
	}

	public class MyTextInputListener implements TextInputListener {
		   @Override
		   public void input (String text) {
			   if(nameLabel != null) nameLabel.setText(text);
		   }

		   @Override
		   public void canceled () {
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
		staticStage.dispose();
	}

	public void addBridge(Bridge bridge) {
	}

	public void setNormalZoom() {
	}
}
