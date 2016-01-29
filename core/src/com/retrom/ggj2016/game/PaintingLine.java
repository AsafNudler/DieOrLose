package com.retrom.ggj2016.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	
	Sprite sprite = setupSprite();
	Vector2 offset = utils.randomDir(14);
	float scale = (float) (Math.random() * 1f + 0.5f);
	float rotation = (float) (Math.random() * 360);
	
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
	
	private Sprite setupSprite() {
		Sprite s = Assets.smallBlood.random();
		if (Math.random() < 0.05) {
			s = Assets.blood.random();
			scale = 1;
		}
		return s;
	}

	public void render(SpriteBatch batch) {

//		int circles = (int)Math.floor(Math.max(Math.abs(pt1.x - pt2.x)/30, Math.abs(pt1.y - pt2.y)/30))+1;
//		if (onPath) {
//			batch.setColor(1, 1, 1, 1);
//		} else {
//			batch.setColor(0.5f, 0, 0, 1);
//		}
//		for (int i = 0; i < circles; i++) {
//			final float x = pt1.x + (pt2.x - pt1.x)*(((float)i)/((float)circles));
//			float y = pt1.y + (pt2.y - pt1.y)*(((float)i)/((float)circles));
			
			final float x = pt1.x;
			float y = pt1.y;
			sprite.setScale(scale);
			sprite.setRotation(rotation);
			utils.drawCenter(batch, sprite, x + offset.x, y + offset.y);
//		}
//		batch.setColor(1, 1, 1, 1);


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
