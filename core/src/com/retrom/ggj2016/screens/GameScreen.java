package com.retrom.ggj2016.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.game.World;
import com.retrom.ggj2016.game.World.WorldListener;
import com.retrom.ggj2016.game.WorldRenderer;


public class GameScreen extends ScreenAdapter implements Screen {
	static final public float FRUSTUM_WIDTH = 1080;
	static final public float FRUSTUM_HEIGHT = 1080;
	
	SpriteBatch batch_ = new SpriteBatch();
	ShapeRenderer shapeRenderer_ = new ShapeRenderer();
	OrthographicCamera cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
	WorldRenderer renderer = new WorldRenderer(batch_);
	
	World world_;
	
	int level_ = 0;
	
	boolean isPaused_ = false;
	private int level;
	
	public GameScreen(int level) {
		level_ = level;
	}

	@Override
	public void show() {
		world_ = new World(new WorldListener() {
			
			@Override
			public void restart() {
				level = 0;
				((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(level));
			}

			@Override
			public void nextLevel() {
				((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(level+1));
			}
		}, level);
	}

	@Override
	public void render(float delta) {
		cam.update();
		batch_.setProjectionMatrix(cam.combined);
		shapeRenderer_.setProjectionMatrix(cam.combined);
		
		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		
		batch_.enableBlending();
		world_.render(batch_, shapeRenderer_);
		
		if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
			delta /= 10f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			for (int i=0; i < 10; i++) {
				update(delta);
			}
		}
		update(delta);
	}
	
	private void update(float deltaTime) {
		deltaTime = Math.min(1/30f, deltaTime);
		world_.update(deltaTime);
		
//		checkPause();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.HOME)) {
		}
	}
	
//	private void checkPause() {
//		if (Gdx.input.isKeyJustPressed(Input.Keys.P) || hub_.isPauseAreaTouched()) {
//			togglePause();
//		}
//	}

	@Override
	public void resize(int width, int height) {
	}

//	@Override
//	public void pause() {
//		isPaused_ = true;
//		SoundAssets.pauseAllSounds();
//		world_.pause();
//	}
//
//	@Override
//	public void resume() 
//	{
//		isPaused_ = false;
//		SoundAssets.resumeAllSounds();
//		world_.unpause();
//	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		pause();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}