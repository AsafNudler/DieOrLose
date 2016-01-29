package com.retrom.ggj2016.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class PaintingLine {
	
	public PaintingLine(float x1, float y1, float x2, float y2) {
		pt1 = new Vector2(x1, y1);
		pt2 = new Vector2(x2, y2);
	}
	
	public boolean onPath;
	public boolean pathDone;
	
	final public Vector2 pt1;
	final public Vector2 pt2;
	
	public void render(ShapeRenderer renderer) {
		Gdx.gl.glLineWidth(2);
		renderer.begin(ShapeType.Line);
		
		if (pathDone) {
			renderer.setColor(1, 1, 0, 1);
		} else if (onPath) {
			renderer.setColor(1, 0, 0, 1);
		} else {
			renderer.setColor(0.5f, 0, 0, 1);
		}
		
		renderer.line(pt1.x, pt1.y, pt2.x, pt2.y);
		renderer.end();
	}
}
