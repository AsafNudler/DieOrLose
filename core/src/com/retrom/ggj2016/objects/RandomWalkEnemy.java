package com.retrom.ggj2016.objects;

import com.retrom.ggj2016.game.World;
import com.retrom.ggj2016.utils.utils;

public class RandomWalkEnemy extends Enemy {
	
	private static final int VEL = 150;
	private float stateTime = 0;
	private float angle = 0;
	private float deltaAngle = 0;
	
	public RandomWalkEnemy(float x, float y) {
		super(x, y);
		angle = (float) (Math.random() * 360f);
	}
	@Override
	public void update(float deltaTime) {
		stateTime += deltaTime;
		
		angle += deltaTime * deltaAngle;
		
		if (stateTime > 1) {
			deltaAngle = (float) (Math.random() * 6 - 3);
			stateTime = 0;
		}
		
		if (Math.max(Math.abs(position.x),Math.abs(position.y)) > World.BOUNDS) {
			angle = (float) Math.atan2(position.x, -position.y);
			angle = (float) (angle / Math.PI * 180);
			deltaAngle = 0;
			stateTime = -1;
		}
		velocity = utils.dirVec(angle, VEL);
		
		super.update(deltaTime);
	}
	

}
