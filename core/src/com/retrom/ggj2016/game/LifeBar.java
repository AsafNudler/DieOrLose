package com.retrom.ggj2016.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.retrom.ggj2016.screens.GameScreen;

public class LifeBar {
	
	private static final float MAX_WIDTH = GameScreen.FRUSTUM_WIDTH - 200;
	private static final float HEIGHT = 50;
	
	private static final float X = -GameScreen.FRUSTUM_WIDTH/2 + 100;
	private static final float Y = GameScreen.FRUSTUM_HEIGHT/2 - 100;
	
	public float life = 1;
	public void render(ShapeRenderer renderer) {
		renderer.begin(ShapeType.Filled);
		renderer.setColor(1, 0, 0, 1);
		renderer.rect(X, Y, MAX_WIDTH * life, HEIGHT);
		renderer.end();
	}
}
