package com.retrom.ggj2016.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class Player extends DynamicGameObject {
	
	private static final float VEL = 200f;
	private static final float ACCEL = 2000f;

	public Player() {
		super(0, 0, 108, 108);
		// TODO Auto-generated constructor stub
	}
	
	public void update(float deltaTime) {
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			velocity.y -= ACCEL * deltaTime;
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			velocity.y += ACCEL * deltaTime;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			velocity.x -= ACCEL * deltaTime;
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			velocity.x += ACCEL * deltaTime;
		}
		velocity.clamp(0, VEL);
		velocity.x *= Math.pow(0.01, deltaTime);
		velocity.y *= Math.pow(0.01, deltaTime);
		
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;
	}
	
	public void render(SpriteBatch batch) {
		Sprite s;
		if (velocity.y > 0) {
			s = Assets.playerBack;
		} else if (velocity.y < 0) {
			s = Assets.playerFront;
		} else if (velocity.x > 0) {
			s = Assets.playerSide;
			s.setFlip(false, false);
		} else if (velocity.x < 0) {
			s = Assets.playerSide;
			s.setFlip(true, false);
		} else {
			s = Assets.playerFront;
		}
		utils.drawCenter(batch, s, position.x, position.y);
		utils.drawCenter(batch, Assets.playerHead, position.x, position.y + 57);
	}

}
