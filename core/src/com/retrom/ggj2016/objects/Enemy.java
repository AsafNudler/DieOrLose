package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class Enemy extends DynamicGameObject {
	
	private static final float ENEMY_SIZE = 108;

	public Enemy(float x, float y) {
		super(x, y, ENEMY_SIZE, ENEMY_SIZE);
	}
	
	public void update(float deltaTime) {
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;
	}
	
	public void render(SpriteBatch batch) {
		utils.drawCenter(batch, Assets.enemy, position.x, position.y);
	}
}
