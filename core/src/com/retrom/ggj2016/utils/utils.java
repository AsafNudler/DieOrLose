package com.retrom.ggj2016.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class utils {
	public static void drawCenter(SpriteBatch batch, Sprite keyFrame, float x, float y) {
		batch.draw(keyFrame, x - keyFrame.getRegionWidth()/2, y - keyFrame.getRegionHeight()/2);
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
