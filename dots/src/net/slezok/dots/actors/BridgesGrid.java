package net.slezok.dots.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class BridgesGrid extends Group {
	private static final String TAG = "Bridges";

	private static final float FIELD_WIDTH = 100;
	private final float FIELD_HEIGHT = 100;
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

		WORLD_HEIGHT = SCREEN_HEIGHT * 1;
		WORLD_WIDTH = SCREEN_WIDTH * 1;
		
//		VIEW_HEIGHT = VIEW_WIDTH * SCREEN_HEIGHT / SCREEN_WIDTH;
		Gdx.app.log(TAG, "Screen width: " + SCREEN_WIDTH + " Screen height: " + SCREEN_HEIGHT);
		Gdx.app.log(TAG, "Field width: " + FIELD_WIDTH + " Field height: " + FIELD_HEIGHT);
		
		FileHandle file =  Gdx.files.internal("data/grid.json");
		Json json = new Json();
		@SuppressWarnings("unchecked")
		Array<Bridge> bridges = json.fromJson(Array.class, Bridge.class, file);	
				
		for (Bridge bridge : bridges) {
			addBridge(bridge);
		}
		for(int x = 0; x < FIELD_WIDTH; x++){
			for(int y = 0; y < FIELD_HEIGHT; y++){
				addDot(x,y);
			}
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
		float scale = SCREEN_WIDTH / FIELD_WIDTH;

		Image platform = new Image(Assets.platform);
		platform.setPosition(bridge.getX() * scale, bridge.getY() * scale);
		platform.setWidth(bridge.getWidth() * scale);
		platform.setHeight(bridge.getLength() * scale);
		platform.setRotation(bridge.getDirection());

		createPlatformBody(bridge.getX(), bridge.getY(), platform.getWidth(), platform.getHeight());

		addActor(platform);
	}

	public void addDot(int x, int y) {
		float scale = SCREEN_WIDTH / FIELD_WIDTH;

		Image dot = new Image(Assets.platform);
		float xPos = (float) (x * scale - 0.5);
		float yPos = (float) (y * scale - 0.5);
		dot.setPosition(xPos, yPos);
		dot.setWidth(1);
		dot.setHeight(1);
		dot.setRotation(0);

		createPlatformBody(xPos, yPos, 1, 1);

		addActor(dot);
	}

	public float getScreenWidth() {
		return SCREEN_WIDTH;
	}

	public float getScreenHeight() {
		return SCREEN_HEIGHT;
	}

	public float getWorldWidth() {
		return WORLD_WIDTH;
	}

	public float getWorldHeight() {
		return WORLD_HEIGHT;
	}
}
