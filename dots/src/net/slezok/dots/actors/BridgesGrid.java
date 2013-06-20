package net.slezok.dots.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import net.slezok.dots.Assets;
import net.slezok.dots.Bridge;
import net.slezok.dots.GameItem;

public class BridgesGrid extends Group {
	private static final String TAG = "Bridges";

	private static final float VIEW_WIDTH = 100;
	private final float VIEW_HEIGHT = 100;
	private final float SCREEN_WIDTH;
	private final float SCREEN_HEIGHT;
	private final float WORLD_WIDTH;
	private final float WORLD_HEIGHT;

		
	final int gameUnitConst = 32;
	private World world;
	
	public BridgesGrid(World world) {
		this.world = world;
		
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		SCREEN_HEIGHT = Gdx.graphics.getHeight();

		WORLD_HEIGHT = SCREEN_HEIGHT * 5;
		WORLD_WIDTH = SCREEN_WIDTH * 5;
		
//		VIEW_HEIGHT = VIEW_WIDTH * SCREEN_HEIGHT / SCREEN_WIDTH;
		Gdx.app.log(TAG, "Screen width: " + SCREEN_WIDTH + " Screen height: " + SCREEN_HEIGHT);
		Gdx.app.log(TAG, "View width: " + VIEW_WIDTH + " View height: " + VIEW_HEIGHT);
		
		FileHandle file =  Gdx.files.internal("data/grid.json");
		Json json = new Json();
		@SuppressWarnings("unchecked")
		Array<Bridge> bridges = json.fromJson(Array.class, Bridge.class, file);	
				
		for (Bridge bridge : bridges) {
			addBridge(bridge);
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
	
	private void createPlatformBody(float x, float y, float width, float height) {
		// Create our body definition
		BodyDef groundBodyDef = new BodyDef();
		// Set its world position
		groundBodyDef.position.set(x + width/2, y + height/2);
		
		// Create a body from the defintion and add it to the world
		Body platformBody = world.createBody(groundBodyDef);

		// Create a polygon shape
		PolygonShape groundBox = new PolygonShape();
		// Set the polygon shape as a box which is twice the size of our view
		// port and 10 high
		groundBox.setAsBox(width/2, 0.5f);
		// Create a fixture from our polygon shape and add it to our ground body
		platformBody.createFixture(groundBox, 0.0f);
	}

	public void addBridge(Bridge bridge) {
		float scale = SCREEN_WIDTH / VIEW_WIDTH;

		Image platform = new Image(Assets.platform);
		platform.setPosition(bridge.getX() * scale, bridge.getY() * scale);
		platform.setWidth(bridge.getWidth() * scale);
		platform.setHeight(bridge.getHeight() * scale);
		platform.setRotation(bridge.getDirection());

		createPlatformBody(bridge.getX(), bridge.getY(), platform.getWidth(), platform.getHeight());

		addActor(platform);
	}

	public float getScreenWidth() {
		return SCREEN_WIDTH;
	}

	public float getScreenHeight() {
		return SCREEN_HEIGHT;
	}
	
}
