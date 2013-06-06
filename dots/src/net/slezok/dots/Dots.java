package net.slezok.dots;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Dots implements ApplicationListener {
	protected static final String TAG = Dots.class.getSimpleName();
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture hlineTexture;
	private Texture vlineTexture;
	
	private List<Sprite> sprites = new ArrayList<Sprite>();
	private Sprite currentSprite = null;
	private float w, h;
	
	private Intersector intersector = new Intersector();
	private Rectangle CREATE_HOR_LINE_RECTANGLE;
	private Rectangle CREATE_VERT_LINE_RECTANGLE;
	private final float BUTTON_HEIGHT = 50;
	private final float BUTTON_WIDTH = 50;
	
	private InputProcessor inputProcessor = new InputProcessor(){
		@Override
		public boolean keyDown(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer,	int button) {
//			Gdx.app.log(TAG, "touchDown: x=" + screenX + " y=" + screenY + " pointer=" + pointer + " button=" + button);
			Rectangle touchRectangle = new Rectangle(screenX, screenY, 10,10);
			if(intersector.overlapRectangles(CREATE_HOR_LINE_RECTANGLE, touchRectangle)){
				TextureRegion region = new TextureRegion(hlineTexture, 0, 0, 64, 8);
				
				currentSprite = new Sprite(region);
				currentSprite.setSize(64, 8);
				Gdx.app.log(TAG, "Sprite width: " + currentSprite.getWidth() + " height:" + currentSprite.getHeight());
				currentSprite.setOrigin(0, 0);
				setSpritePosition(screenX, screenY);
				sprites.add(currentSprite);
				Gdx.app.log(TAG, "Sprites size:" + sprites.size());
			}else if(intersector.overlapRectangles(CREATE_VERT_LINE_RECTANGLE, touchRectangle)){
				TextureRegion region = new TextureRegion(vlineTexture, 0, 0, 8, 64);
				
				currentSprite = new Sprite(region);
				currentSprite.setSize(8, 64);
				Gdx.app.log(TAG, "Sprite width: " + currentSprite.getWidth() + " height:" + currentSprite.getHeight());
				currentSprite.setOrigin(0, 0);
				setSpritePosition(screenX, screenY);
				sprites.add(currentSprite);
				Gdx.app.log(TAG, "Sprites size:" + sprites.size());
			}
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			Rectangle touchRectangle = new Rectangle(screenX, screenY, 10,10);
			if(currentSprite != null && 
					intersector.overlapRectangles(CREATE_HOR_LINE_RECTANGLE, touchRectangle) == false &&
					intersector.overlapRectangles(CREATE_VERT_LINE_RECTANGLE, touchRectangle) == false){
				setSpritePosition(screenX, screenY);
			}
			return true;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}

		private void setSpritePosition(int screenX, int screenY) {
			int xPos = (int)(screenX - w/2);
			int yPos = (int)(h/2 - screenY);
			currentSprite.setPosition(xPos, yPos);
		}
	};
	
	@Override
	public void create() {		
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		CREATE_HOR_LINE_RECTANGLE = new Rectangle(w - BUTTON_WIDTH, h - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
		CREATE_VERT_LINE_RECTANGLE = new Rectangle(0, h - BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);

		Gdx.app.log(TAG, "Screen width: " + w + " height:" + h);

		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();
	
		hlineTexture = new Texture(Gdx.files.internal("data/hline.png"));
		hlineTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		vlineTexture = new Texture(Gdx.files.internal("data/vline.png"));
		vlineTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				
		Gdx.input.setInputProcessor(inputProcessor);
	}

	@Override
	public void dispose() {
		batch.dispose();
		hlineTexture.dispose();
		vlineTexture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glViewport( 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(Sprite sprite : sprites){
			sprite.draw(batch);
		}
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
