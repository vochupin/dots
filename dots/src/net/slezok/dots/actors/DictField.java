package net.slezok.dots.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import net.slezok.dots.Assets;
import net.slezok.dots.Bridge;
import net.slezok.dots.Level;
import net.slezok.dots.screens.DictScreen;

public class DictField extends Group {
	private static final String TAG = "Bridges";
	public static final float DOT_HALF_SIZE = 0.02F;
	public static final float LINE_HALF_WIDTH = 0.1F;

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
			addLine(x, 0, FIELD_HEIGHT, 0);
		}

		for(int y = 0; y < FIELD_HEIGHT; y++){
			addLine(0, y, FIELD_WIDTH, 270);
		}
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
	
	public void addBridge(Bridge bridge) {
		float scale = SCREEN_WIDTH / FIELD_WIDTH;

		float halfWidth = bridge.getWidth() / 2;		
		
		Image image = new Image(Assets.platform);
		image.setPosition((bridge.getX() - halfWidth) * scale, (bridge.getY() - halfWidth) * scale);
		image.setWidth(bridge.getWidth() * scale);
		image.setHeight(bridge.getLength() * scale);
		image.setRotation(bridge.getDirectionAngle());
		image.setOrigin(halfWidth * scale, halfWidth * scale);

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

		addActor(dot);
	}

	public void addLine(float x, float y, float length, int rotation) {
		float scale = SCREEN_WIDTH / FIELD_WIDTH;

		Image line = new Image(Assets.platform);
		
		float xPos = (float) ((x - DOT_HALF_SIZE) * scale);
		float yPos = (float) ((y - DOT_HALF_SIZE) * scale);
		line.setPosition(xPos, yPos);
		line.setWidth(DOT_HALF_SIZE * scale * 2);
		line.setHeight(length * scale);
		line.setRotation(rotation);
		line.setColor(new Color(0f, 0f, 0.5f, 0.4f));

		addActor(line);
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
