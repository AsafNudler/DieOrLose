package com.retrom.ggj2016.objects;

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

}
