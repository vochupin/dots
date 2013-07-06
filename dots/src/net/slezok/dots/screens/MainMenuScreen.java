package net.slezok.dots.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import net.slezok.dots.Assets;
import net.slezok.dots.Dots;

public class MainMenuScreen implements Screen {
	
	Dots game;
	Stage stage;
	TextButton gridButton;
	TextButton exitButton;
	
	public MainMenuScreen(Dots game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();
//		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		gridButton = new TextButton("Dict", Assets.skin);
		exitButton = new TextButton("Exit", Assets.skin);
		Image backImage = new Image(Assets.mainBackgroundTexture);
		backImage.setFillParent(true);
		
		backImage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new LevelsListScreen(game));
//				game.setScreen(new PropertiesScreen(game));
				return true;
			}
		});
		
		stage.addActor(backImage);
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
