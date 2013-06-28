package net.slezok.dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Assets {
	public static TextureRegion platform;
	public static TextureRegion fallingManSplash;
	public static Animation fallingManAnim;
	public static Texture backgroundTexture;
	public static Skin skin;
	
	public static TextureRegion up;
	public static TextureRegion down;
	public static TextureRegion left;
	public static TextureRegion right;
	public static TextureRegion repeat;
	
	public static void load () {
		
		TextureAtlas textureAtlas = new TextureAtlas("data/PigTest.pack");
		fallingManAnim = new Animation(0.2f, textureAtlas.findRegion("falling1"), textureAtlas.findRegion("falling2"));
		platform = textureAtlas.findRegion("platform");
		fallingManSplash = textureAtlas.findRegion("rip");
		backgroundTexture = new Texture(Gdx.files.internal("data/back.jpg"));
		
		up = new TextureRegion(new Texture(Gdx.files.internal("data/up.png")));
		down = new TextureRegion(new Texture(Gdx.files.internal("data/down.png")));
		left = new TextureRegion(new Texture(Gdx.files.internal("data/left.png")));
		right = new TextureRegion(new Texture(Gdx.files.internal("data/right.png")));
		repeat = new TextureRegion(new Texture(Gdx.files.internal("data/repeat.png")));
		
		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
	}
}
