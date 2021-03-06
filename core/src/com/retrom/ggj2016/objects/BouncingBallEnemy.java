package com.retrom.ggj2016.objects;

import com.retrom.ggj2016.game.World;
import com.retrom.ggj2016.screens.GameScreen;
import com.retrom.ggj2016.utils.utils;

public class BouncingBallEnemy extends Enemy {
	
	private final float VEL = 250;

	public BouncingBallEnemy(float x, float y) {
		super(x, y);
		velocity = utils.randomDir(VEL);
		if (velocity.dot(position) < 0) {
			velocity.scl(-1);
		}
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (position.x > World.BOUNDS) {
			velocity.x = - Math.abs(velocity.x);
		}
		if (position.x < - World.BOUNDS) {
			velocity.x = Math.abs(velocity.x);
		}
		if (position.y > World.BOUNDS) {
			velocity.y = - Math.abs(velocity.y);
		}
		if (position.y < - World.BOUNDS) {
			velocity.y = Math.abs(velocity.y);
		}
	}
}
