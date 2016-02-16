package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.game.World;
import com.retrom.ggj2016.utils.utils;

public class Bone extends DynamicGameObject {
	
	final private Sprite sprite = Assets.bones.random();
	
	final private float scale = (float) (0.75f + Math.random() * 0.25f);
	final private boolean flip = Math.random() < 0.5f;
	
	private float stateTime = 0;
	float rotation = (float) (Math.random() * 360f);
	int rotationSpeed = (int) Math.random() * 2 - 1;
	
	public Bone(Vector2 position) {
		super(position.x, position.y, 0, 0);
		velocity = utils.randomDir(400 + (float)Math.random() * 1200); 
	}

	public void update(float deltaTime) {
		stateTime += deltaTime;
		if (stateTime < Player.TIME_BEFORE_EXPLODE) return;
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		
		if (position.x > World.BOUNDS) velocity.x = -Math.abs(velocity.x);
		if (position.x < -World.BOUNDS) velocity.x = Math.abs(velocity.x);
		if (position.y > World.BOUNDS) velocity.y = -Math.abs(velocity.y);
		if (position.y < -World.BOUNDS) velocity.y = Math.abs(velocity.y);
		
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;
		
		velocity.x *= Math.pow(0.01, deltaTime);
		velocity.y *= Math.pow(0.01, deltaTime);
		
		rotation += rotationSpeed * velocity.len() * deltaTime;
	}
	
	public void render(SpriteBatch batch) {
		if (stateTime < Player.TIME_BEFORE_EXPLODE) return;
//		float alpha = 1;
//		if (stateTime > DURATION - FADE_OUT_TIME) {
//			alpha = 1 - ((stateTime - DURATION + FADE_OUT_TIME) / FADE_OUT_TIME); 
//		}
//		
//		float scale = 1;
//		if  (stateTime < GROW_IN_TIME) {
//			scale = stateTime / GROW_IN_TIME * 1.2f;
//		} else {
//			scale = (float)Math.max(1, 1.2 + (GROW_IN_TIME - stateTime));
//		}
//		Sprite s = Assets.heart;
//		s.setAlpha(alpha);
//		s.setScale(scale);
		sprite.setRotation(rotation);
		sprite.setScale(scale);
		sprite.setFlip(true, flip);
		utils.drawCenter(batch, sprite, position.x, position.y);
		
	}

}
