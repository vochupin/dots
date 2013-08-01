package net.slezok.dots.screens;

import net.slezok.dots.Assets;
import net.slezok.dots.Constants;
import net.slezok.dots.Dots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class IntroScreen implements Screen {
	
	private static final String TAG = "MainMenuScreen";

	private Dots game;
	private Stage stage;
	
	public IntroScreen(Dots game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
        // clear previous frame
		Gdx.gl.glClearColor(0.2f, 0.2f, 1f, 1);
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
		stage = new Stage(Constants.VIRTUAL_WIDTH, Constants.VIRTUAL_HEIGHT, true);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setInputProcessor(stage);
		
		Image backImage = new Image(Assets.mainBackgroundTexture);

		float scale = Constants.VIRTUAL_WIDTH / backImage.getWidth();
		backImage.setScale(scale);

		backImage.setX(stage.getGutterWidth());
		backImage.setY(stage.getGutterHeight() + (Constants.VIRTUAL_HEIGHT - backImage.getHeight() * scale) / 2);
		backImage.addAction( Actions.sequence( Actions.fadeOut( 0.001f ), Actions.fadeIn( 1f ) ) );
		
		backImage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new LevelsListScreen(game));
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
