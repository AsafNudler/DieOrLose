package com.retrom.ggj2016.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
	
	private float stateTime;
	private boolean blinking;
	
	public float life = 1;
	private float addLifeBlinkTime = 0;
	private float eyesAlpha = 0;
	
	private float cutTime = -1;
	
	private static final float LOW_HEALTH_MARKER = 0.2f;
	
	public void render(ShapeRenderer renderer, SpriteBatch batch) {
		BatchUtils.setBlendFuncNormal(batch);
		batch.begin();
		utils.drawCenter(batch, Assets.lifeBarBg, 0, Y);
		batch.end();
		
		drawColor(renderer);
		
		batch.begin();
		utils.drawCenter(batch, Assets.lifeBarOver, 0, Y);
		{
			Sprite s = Assets.lifeBarEyes;
			s.setAlpha(eyesAlpha);
			utils.drawCenter(batch, s, 0, Y);
		}
		batch.end();
	}
	
	public boolean isLowHealth() {
		return life < LOW_HEALTH_MARKER;
	}

	private void drawColor(ShapeRenderer renderer) {
		renderer.begin(ShapeType.Filled);
		
		if (blinking && stateTime > 1.8f) {
			blinking = false;
			stateTime = 0;
		}
		if (addLifeBlinkTime > 0 || (blinking  && stateTime > 0.4f && (stateTime % 0.3f > 0.15f))) {
			renderer.setColor(1, 0.15f, 0.1f, 1);
		} else {
			renderer.setColor(0.33f, 0, 0, 1);
		}
		
		renderer.rect(X, Y - 44f, MAX_WIDTH * life, HEIGHT);
		renderer.end();
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
		addLifeBlinkTime -= deltaTime;
		if (cutTime >= 0) {
			cutTime += deltaTime;
			if (cutTime < 1.5f) {
				eyesAlpha = Math.min(cutTime * 5, 1); 
			} else {
				eyesAlpha = (float) (Math.cos((cutTime - 1.5f) * 4) + 1) / 2;
			}
			
		}
	}
	
	public void startEyeBlink() {
		if (cutTime < 0) cutTime = 0;
	}
	
	public void blink() {
		stateTime = 0;
		blinking = true;
	}

	public void addLife() {
		life += 0.15f;
		life = Math.min(1, life);
		
		addLifeBlinkTime = 0.1f;
	}
}
