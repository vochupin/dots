package net.slezok.graphdict.actors;

import net.slezok.graphdict.Assets;
import net.slezok.graphdict.Bridge;
import net.slezok.graphdict.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class DictField extends Group {
	private static final String TAG = "Bridges";
	public static final float DOT_HALF_SIZE = 0.02F;
	public static final float LINE_HALF_WIDTH = 0.1F;

	private final float FIELD_WIDTH; //field size in game steps
	private final float FIELD_HEIGHT;
	private final float SCREEN_WIDTH; // screen size in pixels
	private final float SCREEN_HEIGHT;
	private final float WORLD_WIDTH; //field size in pixels
	private final float WORLD_HEIGHT;
	
	private final float SCALE;
		
	private World world;
	
	public DictField(Level level) {
		
		FIELD_HEIGHT = level.getHeight();
		FIELD_WIDTH = level.getWidth();

		SCREEN_WIDTH = Gdx.graphics.getWidth();
		SCREEN_HEIGHT = Gdx.graphics.getHeight();
		
		SCALE = SCREEN_WIDTH / FIELD_WIDTH;

		WORLD_HEIGHT = SCREEN_WIDTH * FIELD_HEIGHT / FIELD_WIDTH;
		WORLD_WIDTH = SCREEN_WIDTH;
		
		Gdx.app.log(TAG, "Screen width: " + SCREEN_WIDTH + " Screen height: " + SCREEN_HEIGHT);
		Gdx.app.log(TAG, "Field width: " + FIELD_WIDTH + " Field height: " + FIELD_HEIGHT);
		
		drawGrid();
		drawFrame();
	}

	private void drawGrid() {
		for(int x = 0; x < FIELD_WIDTH; x++){
			addLine(x, 0, FIELD_HEIGHT, 0);
		}
		for(int y = 0; y < FIELD_HEIGHT; y++){
			addLine(0, y, FIELD_WIDTH, 270);
		}
	}

	private void drawFrame() {
		addLine(0, 0, FIELD_HEIGHT, 0, LINE_HALF_WIDTH * 2);
		addLine(0, 0, FIELD_WIDTH, 270, LINE_HALF_WIDTH * 2);
		addLine(FIELD_WIDTH, 0, FIELD_HEIGHT, 0, LINE_HALF_WIDTH * 2);
		addLine(0, FIELD_HEIGHT, FIELD_WIDTH, 270, LINE_HALF_WIDTH * 2);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
	
	public void addBridge(Bridge bridge) {
		float halfWidth = bridge.getWidth() / 2;		
		
		Image image = new Image(Assets.blackbar);
		image.setPosition((bridge.getX() - halfWidth) * SCALE, (bridge.getY() - halfWidth) * SCALE);
		image.setWidth(bridge.getWidth() * SCALE);
		image.setHeight(bridge.getLength() * SCALE);
		image.setRotation(bridge.getDirectionAngle());
		image.setOrigin(halfWidth * SCALE, halfWidth * SCALE);

		addActor(image);
	}

	public void addDot(int x, int y) {
		float scale = SCREEN_WIDTH / FIELD_WIDTH;

		Image dot = new Image(Assets.bluebar);
		
		float xPos = (float) ((x - DOT_HALF_SIZE) * scale);
		float yPos = (float) ((y - DOT_HALF_SIZE) * scale);
		dot.setPosition(xPos, yPos);
		dot.setWidth(DOT_HALF_SIZE * scale * 2);
		dot.setHeight(DOT_HALF_SIZE * scale * 2);
		dot.setRotation(0);

		addActor(dot);
	}

	public void addLine(float x, float y, float length, int rotation) {
		addLine(x, y, length, rotation, DOT_HALF_SIZE * 2);
	}

	public void addLine(float x, float y, float length, int rotation, float width) {
		Image line = new Image(Assets.bluebar);
		
		float scaledWidth = SCALE * width;
		float scaledHalfWidth = scaledWidth / 2;
		
		float xPos = (float) (SCALE * x - scaledHalfWidth);
		float yPos = (float) (SCALE * y - scaledHalfWidth);
		line.setPosition(xPos, yPos);
		line.setWidth(scaledWidth);
		line.setHeight(length * SCALE + scaledWidth);
		line.setRotation(rotation);
		line.setOrigin(scaledHalfWidth, scaledHalfWidth);

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
