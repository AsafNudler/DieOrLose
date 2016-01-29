package com.retrom.ggj2016.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class Player extends DynamicGameObject {
	
	private static final float VEL = 200f;
	private static final float ACCEL = 2000f;

	public Player() {
		super(0, 0, 1, 1);
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
		if (Gdx.input.isTouched())
		{
			Vector2 touch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			float ang = touch.angle();
			if ((ang >= 0 && ang <= 60) || (ang >= 300))
			{
				velocity.x += ACCEL * deltaTime;
			}
			else if (ang >= 120 && ang <= 240)
			{
				velocity.x -= ACCEL * deltaTime;
			}
			if (ang >= 30 && ang <= 150)
			{
				velocity.y += ACCEL * deltaTime;
			}
			else if (ang >= 210 && ang <= 330)
			{
				velocity.y -= ACCEL * deltaTime;
			}
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
		Sprite s = Assets.playerFront;
		if (Math.abs(velocity.y) > Math.abs(velocity.x)) {
			if (velocity.y > 0) {
				s = Assets.playerBack;
			} else if (velocity.y < 0) {
				s = Assets.playerFront;
			}
		} else if (velocity.x > 0) {
			s = Assets.playerSide;
			s.setFlip(false, false);
		} else if (velocity.x < 0) {
			s = Assets.playerSide;
			s.setFlip(true, false);
		}
		utils.drawCenter(batch, s, position.x, position.y);
		utils.drawCenter(batch, Assets.playerHead, position.x, position.y + 57);
	}

}
