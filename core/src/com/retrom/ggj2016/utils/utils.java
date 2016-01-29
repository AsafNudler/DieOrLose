package com.retrom.ggj2016.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class utils {
	public static void drawCenter(SpriteBatch batch, Sprite sprite, float x, float y) {
		sprite.setPosition(x - sprite.getRegionWidth()/2, y - sprite.getRegionHeight()/2);
		sprite.draw(batch);
	}
	
	public static Vector2 randomDir(float size) {
		float dir = (float) (Math.random() * Math.PI * 2);
		return dirVec(dir, size);
	}
	
	public static Vector2 dirVec(float dir, float size) {
		Vector2 vec = new Vector2();
		vec.x = (float) (Math.cos(dir) * size);
		vec.y = (float) (Math.sin(dir) * size);
		return vec;
	}
}
