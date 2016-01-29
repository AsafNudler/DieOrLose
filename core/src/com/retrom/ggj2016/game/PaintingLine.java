package com.retrom.ggj2016.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class PaintingLine {
	
	public PaintingLine(float x1, float y1, float x2, float y2, PaintingLineGlow glow) {
		pt1 = new Vector2(x1, y1);
		pt2 = new Vector2(x2, y2);
		myGlow = glow;
	}
	
	public boolean onPath;
	public boolean pathDone;

	private PaintingLineGlow myGlow;
	
	final public Vector2 pt1;
	final public Vector2 pt2;
	
	public void render(ShapeRenderer renderer) {

		int circles = (int)Math.floor(Math.max(Math.abs(pt1.x - pt2.x)/10, Math.abs(pt1.y - pt2.y)/10))+1;
		renderer.begin(ShapeType.Filled);
		if (onPath) {
			renderer.setColor(1, 0, 0, 1);
		} else {
			renderer.setColor(0.5f, 0, 0, 1);
		}
		for (int i = 0; i < circles; i++) {
			renderer.circle(pt1.x + (pt2.x - pt1.x)*(((float)i)/((float)circles)), pt1.y + (pt2.y - pt1.y)*(((float)i)/((float)circles)), 4f);
		}
		renderer.end();


		if (pathDone)
		{
			myGlow.display = true;
		}
		else
		{
			myGlow.display = false;
		}
	}
	
	public void render(SpriteBatch batch) {

		int circles = (int)Math.floor(Math.max(Math.abs(pt1.x - pt2.x)/10, Math.abs(pt1.y - pt2.y)/10))+1;
		if (onPath) {
			batch.setColor(1, 0, 0, 1);
		} else {
			batch.setColor(0.5f, 0, 0, 1);
		}
		batch.begin();
		for (int i = 0; i < circles; i++) {
			final float x = pt1.x + (pt2.x - pt1.x)*(((float)i)/((float)circles));
			float y = pt1.y + (pt2.y - pt1.y)*(((float)i)/((float)circles));
			utils.drawCenter(batch, Assets.blood.get(0), x, y);
		}
		batch.end();


		if (pathDone)
		{
			myGlow.display = true;
		}
		else
		{
			myGlow.display = false;
		}
	}
}
