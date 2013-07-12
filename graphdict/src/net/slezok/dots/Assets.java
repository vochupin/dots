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
	public static final int MAXIMUM_STEPS_NUMBER = 10;

	private static final String TAG = "Assets";
	
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

	public static TextureRegion upPressed;
	public static TextureRegion downPressed;
	public static TextureRegion leftPressed;
	public static TextureRegion rightPressed;
	public static TextureRegion repeatPressed;
	public static TextureRegion upLeftPressed;
	public static TextureRegion downLeftPressed;
	public static TextureRegion upRightPressed;
	public static TextureRegion downRightPressed;

	public static TextureRegion play;
	public static TextureRegion playPressed;
	
	public static TextureRegion records;
	public static TextureRegion recordsPressed;

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
	
	public static Sound moveSound;
	
	private static Sound[] stepSounds;
	
	public static Music backMusic = null;
	
	public static void load (PlatformDependencies deps) {
		
		TextureAtlas textureAtlas = new TextureAtlas("data/PigTest.pack");
		fallingManAnim = new Animation(0.2f, textureAtlas.findRegion("falling1"), textureAtlas.findRegion("falling2"));
		platform = textureAtlas.findRegion("platform");
		fallingManSplash = textureAtlas.findRegion("rip");
		mainBackgroundTexture = new Texture(Gdx.files.internal("data/intro_screen.png"));
		listBackgroundTexture = new Texture(Gdx.files.internal("data/menu_screen.png"));
		dictBackgroundTexture = new Texture(Gdx.files.internal("data/dictback.jpg"));
		
		TextureAtlas buttonsAtlas = new TextureAtlas("data/buttons/pack.atlas");
		up = buttonsAtlas.findRegion("up");
		down = buttonsAtlas.findRegion("down");
		left = buttonsAtlas.findRegion("left");
		right = buttonsAtlas.findRegion("right");
		repeat = buttonsAtlas.findRegion("repeat");
		upLeft = buttonsAtlas.findRegion("up_left");
		downLeft = buttonsAtlas.findRegion("down_left");
		upRight = buttonsAtlas.findRegion("up_right");
		downRight = buttonsAtlas.findRegion("down_right");

		upPressed = buttonsAtlas.findRegion("up_pressed");
		downPressed = buttonsAtlas.findRegion("down_pressed");
		leftPressed = buttonsAtlas.findRegion("left_pressed");
		rightPressed = buttonsAtlas.findRegion("right_pressed");
		repeatPressed = buttonsAtlas.findRegion("repeat_pressed");
		upLeftPressed = buttonsAtlas.findRegion("up_left_pressed");
		downLeftPressed = buttonsAtlas.findRegion("down_left_pressed");
		upRightPressed = buttonsAtlas.findRegion("up_right_pressed");
		downRightPressed = buttonsAtlas.findRegion("down_right_pressed");
		
		play = buttonsAtlas.findRegion("play");
		playPressed = buttonsAtlas.findRegion("play_pressed");

		records = buttonsAtlas.findRegion("sett");
		recordsPressed = buttonsAtlas.findRegion("sett_pressed");

		skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		loadSounds(deps);
	}

	private static void loadSounds(PlatformDependencies deps) {
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
		
		moveSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/cup-drop.mp3"));
		
		try{
			backMusic = Gdx.audio.newMusic(deps.openAssetFileCopyIfNeeded("data/bluevagon.mp3"));
		}catch(Exception e){
			e.printStackTrace();
			Gdx.app.error(TAG, "Exception when loading background music: " + e.getMessage());
		}
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
