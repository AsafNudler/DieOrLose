package com.retrom.ggj2016.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.game.World;
import com.retrom.ggj2016.game.WorldRenderer;


public class GameScreen extends ScreenAdapter implements Screen {
	static final public float FRUSTUM_WIDTH = 1080;
	static final public float FRUSTUM_HEIGHT = 1080;
	
	SpriteBatch batch_ = new SpriteBatch();
	OrthographicCamera cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
	WorldRenderer renderer = new WorldRenderer(batch_);
	
	World world_;
	
	boolean isPaused_ = false;

	@Override
	public void show() {
		world_ = new World();
	}

	@Override
	public void render(float delta) {
		cam.update();
		batch_.setProjectionMatrix(cam.combined);
		
		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		
		batch_.enableBlending();
		batch_.begin();
		world_.render(batch_);
		batch_.end();
		
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
