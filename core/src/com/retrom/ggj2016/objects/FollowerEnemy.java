package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

public class FollowerEnemy extends Enemy {

	private Player player;
	
	private static final float VEL = 60;

	public FollowerEnemy(float x, float y, Player player) {
		super(x, y);
		this.player = player;
	}
	
	@Override
	public void update(float deltaTime) {
		this.velocity.x = - this.position.x + player.position.x;
		this.velocity.y = - this.position.y + player.position.y;
		velocity.limit(VEL);
		super.update(deltaTime);
		
	}
	
	@Override
	public void render(SpriteBatch batch) {
		BatchUtils.setBlendFuncAdd(batch);
		{
			Sprite s = utils.getFrameLoop(Assets.enemyFollowerFire, stateTime, 30);
			s.setColor(alpha,alpha,alpha,1);
			utils.drawCenter(batch, s, position.x, position.y + 40);
		}
		
		BatchUtils.setBlendFuncNormal(batch);
		{
		Sprite s = utils.getFrameLoop(Assets.enemyFollower, stateTime, 2);
		s.setAlpha(alpha);
		utils.drawCenter(batch, s, position.x, position.y);
		}
	}

}
