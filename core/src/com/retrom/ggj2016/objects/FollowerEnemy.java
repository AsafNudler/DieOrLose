package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

import java.util.ArrayList;

public class FollowerEnemy extends Enemy {

	private Player player;
	
	private static final float VEL = 60;

	private ArrayList<FollowerEnemy> followers;

	public FollowerEnemy(float x, float y, Player player, ArrayList<FollowerEnemy> followers) {
		super(x, y);
		this.player = player;
		this.followers = followers;
	}
	
	@Override
	public void update(float deltaTime) {
		this.velocity.x = - this.position.x + player.position.x;
		this.velocity.y = - this.position.y + player.position.y;
		velocity.limit(VEL);
		Vector2 repel = new Vector2(0, 0);
		for (FollowerEnemy follower : followers) {
			if (follower != this)
			{
				repel.x -= (-this.position.x + follower.position.x) * Math.random() * 4.0f;
				repel.y -= (-this.position.y + follower.position.y) * Math.random() * 4.0f;
			}
		}
		repel.limit(VEL/3);
		velocity.add(repel);
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
