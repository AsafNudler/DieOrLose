package com.retrom.ggj2016.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.retrom.ggj2016.game.World;
import com.retrom.ggj2016.game.World.WorldListener;
import com.retrom.ggj2016.utils.utils;
import com.retrom.ggj2016.assets.SoundAssets;


public class GameScreen extends ScreenAdapter implements Screen {
	static final public float FRUSTUM_WIDTH = 1080;
	static final public float FRUSTUM_HEIGHT = 1080;
	
	static boolean openingPlayed = false;
	
	SpriteBatch batch_ = new SpriteBatch();
	ShapeRenderer shapeRenderer_ = new ShapeRenderer();
	OrthographicCamera cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
	private Viewport viewport;
	
	World world_;
	
	final int level_;
	
	boolean isPaused_ = false;
	
	public GameScreen(int level) {
		level_ = level;
		viewport = new FitViewport(FRUSTUM_WIDTH, FRUSTUM_HEIGHT, cam);
	}
	
	boolean dontPerformPause;

	@Override
	public void show() {
		if (!openingPlayed) {
			openingPlayed = true;
			SoundAssets.playSound(SoundAssets.opening);
		}
		world_ = new World(new WorldListener() {
			
			@Override
			public void restart() {
				dontPerformPause = true;
				((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(level_));
			}

			@Override
			public void nextLevel() {
				dontPerformPause = true;
				((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(level_+1));
			}

			@Override
			public void restartGame() {
				((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(0));
			}
		}, level_);
	}

	@Override
	public void render(float delta) {
		checkPause();
		cam.update();
		batch_.setProjectionMatrix(cam.combined);
		shapeRenderer_.setProjectionMatrix(cam.combined);
		
		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		batch_.enableBlending();
		world_.render(batch_, shapeRenderer_);
		
		if (utils.DEBUG_ENABLED && Gdx.input.isKeyPressed(Input.Keys.TAB)) {
			delta /= 10f;
		}
		if (!isPaused_) {
			if (utils.DEBUG_ENABLED && Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
				for (int i = 0; i < 10; i++) {
					update(delta);
				}
			}
			if (utils.DEBUG_ENABLED && Gdx.input.isKeyPressed(Input.Keys.F)) {
				if (Gdx.graphics.isFullscreen()) {
					Gdx.graphics.setWindowedMode(800, 800);
				} else {
					Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode(Gdx.graphics.getMonitor()));
				}
			}
			if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
				SoundAssets.toggleSound();
			}
			update(delta);
		}
	}
	
	private void update(float deltaTime) {
//		deltaTime = Math.min(1/30f, deltaTime);
		world_.update(deltaTime);
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.HOME)) {
		}
	}
	
	private void checkPause() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			togglePause();
		}
	}

	private void togglePause() {
		if (isPaused_) {
			resume();
		} else {
			pause();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		if (dontPerformPause) return;
		isPaused_ = true;
		System.out.println("Pausing!");
		SoundAssets.pauseMusic();
	}

	@Override
	public void resume() 
	{
		isPaused_ = false;
		if (world_.allowMusic())
			SoundAssets.resumeMusic();
	}

	@Override
	public void hide() {
		pause();
	}
}
