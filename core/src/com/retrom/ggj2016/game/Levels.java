package com.retrom.ggj2016.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.utils.utils;

public class Levels {
	
	private static float RADIUS = 400;
	
	ArrayList<LineSegment> path = new ArrayList<LineSegment>();
	private final int level;
	
	Levels(int level) {
		this.level = level;
		
		switch(level) {
		case 0:
			path.add(GetLineSegment(0, 180));
			break;
		case 1:
			path.add(GetLineSegment(-30, 90));
			path.add(GetLineSegment(90, 210));
			path.add(GetLineSegment(210, 330));
			break;
		default:
			for (int i=0; i < level; i++) {
				path.add(GetLineSegment((float)Math.random() * 360, (float) (Math.random() * 360)));
			}
		}
	}
	
	// Angles are in degrees.
	private LineSegment GetLineSegment(float angle1, float angle2) {
		float rad1 = (float) (angle1 / 180 * Math.PI);
		float rad2 = (float) (angle2 / 180 * Math.PI);
		
		Vector2 vec1 = utils.dirVec(rad1, RADIUS); 
		Vector2 vec2 = utils.dirVec(rad2, RADIUS);
		
		return new LineSegment(vec1.x, vec1.y, vec2.x, vec2.y);
	}
	
	public ArrayList<LineSegment> getPath() {
		return path;
	}
}
