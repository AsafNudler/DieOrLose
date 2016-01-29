package com.retrom.ggj2016.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.screens.GameScreen;

public class TouchToPoint {
	final float camWidth;
	final float camHeight;
	
	final int screenWidth;
	final int screenHeight;
	
	public TouchToPoint(int screenWidth, int screenHeight, float camWidth,
			float camHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.camWidth = camWidth;
		this.camHeight = camHeight;
	}
	
	public Vector2 toPoint(int x, int y) {
		float rel_x = (float)x / screenWidth;
		float rel_y = (float)y / screenHeight;
		
		float res_x = (rel_x - 0.5f) * camWidth;
		float res_y = -(rel_y - 0.5f) * camHeight;
		return new Vector2(res_x, res_y);
	}
	
	public static TouchToPoint create() {
		return new TouchToPoint(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(),
				GameScreen.FRUSTUM_WIDTH,
				GameScreen.FRUSTUM_HEIGHT);
	}
}
