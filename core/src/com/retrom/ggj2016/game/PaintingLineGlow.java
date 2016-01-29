package com.retrom.ggj2016.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class PaintingLineGlow {

	public PaintingLineGlow(float x1, float y1, float x2, float y2) {
		pt1 = new Vector2(x1, y1);
		pt2 = new Vector2(x2, y2);
		display = false;
	}
	
	public boolean display;
	
	final public Vector2 pt1;
	final public Vector2 pt2;
	
	public void render(ShapeRenderer renderer) {
		if (display)
		{
			int circles = (int)Math.floor(Math.max(Math.abs(pt1.x - pt2.x)/10, Math.abs(pt1.y - pt2.y)/10))+1;
			renderer.begin(ShapeType.Filled);
			renderer.setColor(0.5f, 0.5f, 0, 0.5f);
			for (int i = 0; i < circles; i++) {
				renderer.circle(pt1.x + (pt2.x - pt1.x)*(((float)i)/((float)circles)), pt1.y + (pt2.y - pt1.y)*(((float)i)/((float)circles)), 10f);
			}
			renderer.end();
		}
	}
}
