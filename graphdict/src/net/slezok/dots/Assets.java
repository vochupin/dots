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
	
	public static Music backMusic;
	
	public static void load () {
		
		TextureAtlas textureAtlas = new TextureAtlas("data/PigTest.pack");
		fallingManAnim = new Animation(0.2f, textureAtlas.findRegion("falling1"), textureAtlas.findRegion("falling2"));
		platform = textureAtlas.findRegion("platform");
		fallingManSplash = textureAtlas.findRegion("rip");
		mainBackgroundTexture = new Texture(Gdx.files.internal("data/intro_screen.png"));
		listBackgroundTexture = new Texture(Gdx.files.internal("data/menu_screen.png"));
		dictBackgroundTexture = new Texture(Gdx.files.internal("data/dictback.jpg"));
		
		up = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/up.png")));
		down = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/down.png")));
		left = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/left.png")));
		right = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/right.png")));
		repeat = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/repeat.png")));
		upLeft = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/up_left.png")));
		downLeft = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/down_left.png")));
		upRight = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/up_right.png")));
		downRight = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/down_right.png")));

		upPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/up_pressed.png")));
		downPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/down_pressed.png")));
		leftPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/left_pressed.png")));
		rightPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/right_pressed.png")));
		repeatPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/repeat_pressed.png")));
		upLeftPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/up_left_pressed.png")));
		downLeftPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/down_left_pressed.png")));
		upRightPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/up_right_pressed.png")));
		downRightPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/down_right_pressed.png")));
		
		play = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/play.png")));
		playPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/play_pressed.png")));

		records = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/records.png")));
		recordsPressed = new TextureRegion(new Texture(Gdx.files.internal("data/buttons/records_pressed.png")));

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
		
		moveSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/cup-drop.mp3"));
		
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
