package com.retrom.ggj2016.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.game.World;
import com.retrom.ggj2016.utils.TouchToPoint;
import com.retrom.ggj2016.utils.utils;

public class Player extends DynamicGameObject {
	
	private static final float VEL = 250f;
	private static final float ACCEL = 2000f;

	private static TouchToPoint ttp = TouchToPoint.create();
	public Candle candle = null;
	private boolean bloodStarted = false;

	public Player() {
		super(0, 0, 50, 50);
		// TODO Auto-generated constructor stub
	}
	
	public void update(float deltaTime) {
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			velocity.y -= ACCEL * deltaTime * (bloodStarted ? 1 : 1.5f);
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			velocity.y += ACCEL * deltaTime * (bloodStarted ? 1 : 1.5f);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			velocity.x -= ACCEL * deltaTime * (bloodStarted ? 1 : 1.5f);
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			velocity.x += ACCEL * deltaTime * (bloodStarted ? 1 : 1.5f);
		}
		if (Gdx.input.isTouched())
		{
			Vector2 touch = ttp.toPoint(Gdx.input.getX(), Gdx.input.getY());
			touch.sub(position);
			touch.scl(ACCEL * deltaTime * (bloodStarted ? 1 : 1.5f));
			velocity.x += touch.x;
			velocity.y += touch.y;

			/*float ang = touch.angle();

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
			}*/
		}
		velocity.clamp(0, VEL* (bloodStarted ? 1 : 1.5f));
		velocity.x *= Math.pow(0.01, deltaTime);
		velocity.y *= Math.pow(0.01, deltaTime);
		
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		
		if (position.x > World.BOUNDS) {
			position.x = World.BOUNDS;
		} else if (position.x < -World.BOUNDS) {
			position.x = -World.BOUNDS;
		}
		
		if (position.y > World.BOUNDS) {
			position.y = World.BOUNDS;
		} else if (position.y < -World.BOUNDS) {
			position.y = -World.BOUNDS;
		}
		
		
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;
	}
	
	public void render(SpriteBatch batch) {
		Sprite body = Assets.playerFront;
		Sprite head = Assets.playerHead;
		if (velocity.y > 0) {
			head = Assets.playerHeadBack;
		} else {
			head = Assets.playerHead;
		}
		if (Math.abs(velocity.y) > Math.abs(velocity.x)) {
			if (velocity.y > 0) {
				body = Assets.playerBack;
			} else if (velocity.y < 0) {
				body = Assets.playerFront;
			}
		} else if (velocity.x > 0) {
			body = Assets.playerSide;
			body.setFlip(false, false);
		} else if (velocity.x < 0) {
			body = Assets.playerSide;
			body.setFlip(true, false);
		}
		utils.drawCenter(batch, body, position.x, position.y);
		utils.drawCenter(batch, head, position.x, position.y + 57);
	}

	public void startBlood() {
		bloodStarted  = true;
	}

}
