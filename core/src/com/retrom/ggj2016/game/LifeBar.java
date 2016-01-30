package com.retrom.ggj2016.game;

import java.sql.BatchUpdateException;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.screens.GameScreen;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

public class LifeBar {
	
	private static final float MAX_WIDTH = 260;
	private static final float HEIGHT = 25;
	
	private static final float X = -MAX_WIDTH / 2;
	private static final float Y = - GameScreen.FRUSTUM_HEIGHT/2 + 80;
	
	public float life = 1;
	public void render(ShapeRenderer renderer, SpriteBatch batch) {
		BatchUtils.setBlendFuncNormal(batch);
		batch.begin();
		utils.drawCenter(batch, Assets.lifeBarBg, 0, Y);
		batch.end();
		
		renderer.begin(ShapeType.Filled);
		renderer.setColor(0.33f, 0, 0, 1);
		renderer.rect(X, Y - 44f, MAX_WIDTH * life, HEIGHT);
		renderer.end();
		
		batch.begin();
		utils.drawCenter(batch, Assets.lifeBarOver, 0, Y);
		batch.end();
	}
}
