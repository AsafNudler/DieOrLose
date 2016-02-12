package com.retrom.ggj2016;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.assets.SoundAssets;
import com.retrom.ggj2016.screens.GameScreen;

public class GGJ2016 extends Game {
	SpriteBatch batch;
	
	@Override
	public void create () {
		Assets.init();
		SoundAssets.load();
		
		setScreen(new GameScreen(0));
		
		//batch = new SpriteBatch();
	}

//	@Override
//	public void render () {
//		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(Assets.player, 0, 0);
//		batch.end();
//	}
}
