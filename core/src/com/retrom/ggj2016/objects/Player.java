package com.retrom.ggj2016.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.game.LifeBar;
import com.retrom.ggj2016.game.World;
import com.retrom.ggj2016.utils.TouchToPoint;
import com.retrom.ggj2016.utils.utils;

public class Player extends DynamicGameObject {
	
	private static final float VEL = 250f;
	private static final float ACCEL = 2000f;
	public static final float TIME_BEFORE_EXPLODE = 1f;
	public static final float SLOW_DOWN_HP = 0.25f;

	private static TouchToPoint ttp = TouchToPoint.create();
	public Candle candle = null;
	private boolean bloodStarted = false;
	
	private float stateTime = 0;
	public boolean knife = false;
	public boolean dies = false;
	
	private LifeBar lifebar;

	public Player(LifeBar lifebar) {
		super(0, 0, 50, 50);
		this.lifebar = lifebar;
		// TODO Auto-generated constructor stub
	}

	private boolean controlled = false;
	private boolean horns = false;
	private boolean eyes;
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
		if (dies) {
			return;
		}
		controlled = false;
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !Gdx.input.isKeyPressed(Input.Keys.UP)) {
			velocity.y -= ACCEL * deltaTime * (lifebar.life < SLOW_DOWN_HP ? 1 : (bloodStarted?1.2f:1.5f));
			controlled = true;
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			velocity.y += ACCEL * deltaTime * (lifebar.life < SLOW_DOWN_HP ? 1 : (bloodStarted?1.2f:1.5f));
			controlled = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			velocity.x -= ACCEL * deltaTime * (lifebar.life < SLOW_DOWN_HP ? 1 : (bloodStarted?1.2f:1.5f));
			controlled = true;
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			velocity.x += ACCEL * deltaTime * (lifebar.life < SLOW_DOWN_HP ? 1 : (bloodStarted?1.2f:1.5f));
			controlled = true;
		}
		if (Gdx.input.isTouched())
		{
			Vector2 touch = ttp.toPoint(Gdx.input.getX(), Gdx.input.getY());
			touch.sub(position);
			touch.scl(ACCEL * deltaTime * (lifebar.life < SLOW_DOWN_HP ? 1 : (bloodStarted ? 1.25f : 1.5f)));
			velocity.x += touch.x;
			velocity.y += touch.y;
			controlled = true;

		}
		velocity.clamp(0, VEL* (lifebar.life < SLOW_DOWN_HP ? 1 : (bloodStarted?1.2f:1.5f)));
		velocity.x *= Math.pow(0.0001, deltaTime);
		velocity.y *= Math.pow(0.0001, deltaTime);
		
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
		if (velocity.y > 10) {
			head = Assets.playerHeadBack;
			body = Assets.playerBack;
		} else {
			head = Assets.playerHead;
		}
		if (velocity.len() > 30 && controlled) {
			if (velocity.y > 10) {
				body = utils.getFrameLoop(Assets.playerWalkBack, stateTime, 10);
			} else {
				body = utils.getFrameLoop(Assets.playerWalkFront, stateTime, 10);
			}	
		}
		
		if (knife) {
			head = Assets.playerHead;
			body = Assets.playerBodyKnife;
		}
		
		if (dies) {
			if (stateTime > TIME_BEFORE_EXPLODE) {
				return;
			}
			head = Assets.playerHeadDies;
			body = Assets.playerBodyDies;
			utils.drawCenter(batch, body, position.x, position.y);
			utils.drawCenter(batch, head, position.x, position.y + 57 + (float)Math.random()*5-3);
			return;
		}
		
		float headOffset = 0;
		if (horns) {
			head = Assets.playerComplete.get(1);
			headOffset = 12;
		} else if (eyes) {
			head = Assets.playerComplete.get(0);
			headOffset = 12;
		}
		
		utils.drawCenter(batch, body, position.x, position.y);
		utils.drawCenter(batch, head, position.x, position.y + 57 + headOffset);
	}

	public void startBlood() {
		bloodStarted  = true;
	}

	public void die() {
		if (dies == true) {
			return;
		}
		dies = true;
		stateTime = 0;
	}

	public void putInCenter() {
		eyes = true;
	}

	public void putHorns() {
		horns  = true;
	}

}
