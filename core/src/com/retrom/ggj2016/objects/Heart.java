package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class Heart extends GameObject {
	
	private static float DURATION = 2f;
	private static float FADE_OUT_TIME = 1f;
	
	private static float GROW_IN_TIME = 0.15f;
	
	private float stateTime = 0;

	public Heart(float x, float y) {
		super(x, y, 0, 0);
	}
	
	public void update(float deltaTime) {
		if (stateTime >DURATION) {
			return;
		}
		stateTime += deltaTime;
	}
	
	public void render(SpriteBatch batch) {
		if (stateTime >DURATION) {
			return;
		}
		float alpha = 1;
		if (stateTime > DURATION - FADE_OUT_TIME) {
			alpha = 1 - ((stateTime - DURATION + FADE_OUT_TIME) / FADE_OUT_TIME); 
		}
		
		float scale = 1;
		if  (stateTime < GROW_IN_TIME) {
			scale = stateTime / GROW_IN_TIME * 1.2f;
		} else {
			scale = (float)Math.max(1, 1.2 + (GROW_IN_TIME - stateTime));
		}
		Sprite s = Assets.heart;
		s.setAlpha(alpha);
		s.setScale(scale);
		
		utils.drawCenter(batch, Assets.heart, position.x, position.y + stateTime * 20 + 100);
		
	}

}
