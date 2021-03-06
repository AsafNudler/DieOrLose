package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

public class Enemy extends DynamicGameObject {
	
	private static final float ENEMY_SIZE = 80;
	
	private static final float MAX_START_TIME = 3;
	private static final float MIN_START_TIME = 1;
	
	private float time_till_start;
	
	protected float stateTime = 0;

	protected float alpha = 0;

	public boolean onPlayer;

	public Enemy(float x, float y) {
		super(x, y, ENEMY_SIZE, ENEMY_SIZE);
		time_till_start = (float) (MIN_START_TIME + Math.random() * (MAX_START_TIME - MIN_START_TIME));
	}
	
	public void startNow() {
		time_till_start = 0;
	}
	
	public void appearNow() {
		alpha = 1;
		time_till_start = 0;
		stateTime = (float) (Math.random()*100f) + 50f;
	}
	
	public void update(float deltaTime) {
		if (time_till_start > 0) {
			time_till_start -= deltaTime;
			return;
		} else if (time_till_start > -0.5) {
			time_till_start -= deltaTime;
			alpha  = -time_till_start*2;
			alpha = Math.min(1, alpha);
		} else {
			alpha = 1;
		}
		
		stateTime += deltaTime;
		
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;
	}
	
	public void render(SpriteBatch batch) {
		BatchUtils.setBlendFuncAdd(batch);
		{
			Sprite s = utils.getFrameLoop(Assets.enemyFire, stateTime, 30);
			s.setColor(alpha, alpha, alpha, 1);
			utils.drawCenter(batch, s, position.x, position.y + 40);
		}
		
		BatchUtils.setBlendFuncNormal(batch);
		{
			float fps = onPlayer ? 8 : 2;
			Sprite s = utils.getFrameLoop(Assets.enemy, stateTime, fps);
			s.setAlpha(alpha);
			utils.drawCenter(batch, s, position.x, position.y);
		}
	}
}
