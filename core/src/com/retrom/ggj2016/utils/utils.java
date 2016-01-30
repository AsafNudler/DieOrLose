package com.retrom.ggj2016.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class utils {
	private static final float FPS = 30;

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

	public static boolean floatEquals(float a, float b, float epsilon) {
		return Math.abs(a-b) < epsilon;
	}
	
	public static Sprite getFrameLoop(Array<Sprite> anim, float stateTime) {
		return getFrameLoop(anim, stateTime, FPS);
	}
	
	public static Sprite getFrameLoop(Array<Sprite> anim, float stateTime, float fps) {
		return anim.get((int) (stateTime * fps) % anim.size);
	}
	
	public static Sprite getFrameUntilDone(Array<Sprite> anim, float stateTime, float fps) {
		if (stateTime < 0) return null;
		if (stateTime * fps > anim.size) return null;
		return anim.get((int) (stateTime * fps));
	}
}
