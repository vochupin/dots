package net.slezok.dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Assets {
	private static final int MAXIMUM_STEPS_NUMBER = 10;
	
	public static TextureRegion platform;
	public static TextureRegion fallingManSplash;
	public static Animation fallingManAnim;
	public static Texture mainBackgroundTexture;
	public static Texture listBackgroundTexture;
	public static Texture dictBackgroundTexture;
	public static Skin skin;
	
	public static TextureRegion up;
	public static TextureRegion down;
	public static TextureRegion left;
	public static TextureRegion right;
	public static TextureRegion repeat;

	public static TextureRegion upLeft;
	public static TextureRegion downLeft;
	public static TextureRegion upRight;
	public static TextureRegion downRight;
	
	public static Sound wellDoneSound;
	public static Sound gameOverSound;
	public static Sound errorSound;

	public static Sound upSound;
	public static Sound downSound;
	public static Sound leftSound;
	public static Sound rightSound;

	public static Sound upRightSound;
	public static Sound upLeftSound;
	public static Sound downRightSound;
	public static Sound downLeftSound;
	
	private static Sound[] stepSounds;
	
	public static Music backMusic;
	
	public static void load () {
		
		TextureAtlas textureAtlas = new TextureAtlas("data/PigTest.pack");
		fallingManAnim = new Animation(0.2f, textureAtlas.findRegion("falling1"), textureAtlas.findRegion("falling2"));
		platform = textureAtlas.findRegion("platform");
		fallingManSplash = textureAtlas.findRegion("rip");
		mainBackgroundTexture = new Texture(Gdx.files.internal("data/menuback.jpg"));
		listBackgroundTexture = new Texture(Gdx.files.internal("data/listback.jpg"));
		dictBackgroundTexture = new Texture(Gdx.files.internal("data/dictback.jpg"));
		
		up = new TextureRegion(new Texture(Gdx.files.internal("data/button/up_128.png")));
		down = new TextureRegion(new Texture(Gdx.files.internal("data/button/down_128.png")));
		left = new TextureRegion(new Texture(Gdx.files.internal("data/button/left_128.png")));
		right = new TextureRegion(new Texture(Gdx.files.internal("data/button/right_128.png")));
		repeat = new TextureRegion(new Texture(Gdx.files.internal("data/button/repeat_128.png")));

		upLeft = new TextureRegion(new Texture(Gdx.files.internal("data/button/up_left_128.png")));
		downLeft = new TextureRegion(new Texture(Gdx.files.internal("data/button/down_left_128.png")));
		upRight = new TextureRegion(new Texture(Gdx.files.internal("data/button/up_right_128.png")));
		downRight = new TextureRegion(new Texture(Gdx.files.internal("data/button/down_right_128.png")));

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		loadSounds();
	}

	private static void loadSounds() {
		wellDoneSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/welldone.mp3"));
		gameOverSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/gameover.mp3"));
		errorSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/error.mp3"));
		
		upSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/up.mp3"));
		downSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/down.mp3"));
		leftSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/left.mp3"));
		rightSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/right.mp3"));

		upRightSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/up_right.mp3"));
		upLeftSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/up_left.mp3"));
		downRightSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/down_right.mp3"));
		downLeftSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/down_left.mp3"));
		
		backMusic = Gdx.audio.newMusic(Gdx.files.internal("data/bluevagon.mp3"));
	}
	
	public static void loadStepSounds(int maximumSteps) {
		if(maximumSteps > MAXIMUM_STEPS_NUMBER) maximumSteps = MAXIMUM_STEPS_NUMBER;
		stepSounds = new Sound[maximumSteps];
		for(int i = 1; i <= maximumSteps; i++){
			stepSounds[i - 1] = Gdx.audio.newSound(Gdx.files.internal("data/sound/" + i + "steps.mp3"));
		}
				
	}
	
	public static Sound getStepSound(int numberOfSteps){
		if(numberOfSteps < MAXIMUM_STEPS_NUMBER){
			return stepSounds[numberOfSteps];
		}else{
			return stepSounds[MAXIMUM_STEPS_NUMBER - 1];
		}
	}
}
