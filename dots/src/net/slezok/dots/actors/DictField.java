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
import net.slezok.dots.Level;

public class DictField extends Group {
	private static final String TAG = "Bridges";
	private static final float DOT_HALF_SIZE = 0.1F;

	private final float FIELD_WIDTH;
	private final float FIELD_HEIGHT;
	private final float SCREEN_WIDTH;
	private final float SCREEN_HEIGHT;
	private final float WORLD_WIDTH;
	private final float WORLD_HEIGHT;
		
	private World world;
	
	public DictField(World world, Level level) {
		
		FIELD_HEIGHT = level.getHeight();
		FIELD_WIDTH = level.getWidth();
		this.world = world;
		
		SCREEN_WIDTH = Gdx.graphics.getWidth();
		SCREEN_HEIGHT = Gdx.graphics.getHeight();

		WORLD_HEIGHT = SCREEN_HEIGHT * 2;
		WORLD_WIDTH = SCREEN_WIDTH * 1;
		
		Gdx.app.log(TAG, "Screen width: " + SCREEN_WIDTH + " Screen height: " + SCREEN_HEIGHT);
		Gdx.app.log(TAG, "Field width: " + FIELD_WIDTH + " Field height: " + FIELD_HEIGHT);
		
		FileHandle file =  Gdx.files.internal(level.getLevelFile());
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

		Image image = new Image(Assets.platform);
		image.setPosition((bridge.getX() - 0.5F) * scale, (bridge.getY() - 0.5F) * scale);
		image.setWidth(bridge.getWidth() * scale);
		image.setHeight(bridge.getLength() * scale);
		image.setRotation(bridge.getDirectionAngle());

		createPlatformBody(bridge.getX(), bridge.getY(), image.getWidth(), image.getHeight());

		addActor(image);
	}

	public void addDot(int x, int y) {
		float scale = SCREEN_WIDTH / FIELD_WIDTH;

		Image dot = new Image(Assets.platform);
		float xPos = (float) ((x - DOT_HALF_SIZE) * scale);
		float yPos = (float) ((y - DOT_HALF_SIZE) * scale);
		dot.setPosition(xPos, yPos);
		dot.setWidth(DOT_HALF_SIZE * scale * 2);
		dot.setHeight(DOT_HALF_SIZE * scale * 2);
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
