package com.retrom.ggj2016.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.utils.utils;

public class Levels {
	
	private static float RADIUS = 350;
	
	ArrayList<LineSegment> path = new ArrayList<LineSegment>();
	private final int level;

	public int RandomWalkEnemy;
	
	Levels(int level) {
		this.level = level;
		
		switch(level) {
		case 0:
			path.add(GetLineSegment(0, 180));
			RandomWalkEnemy = 0;
			break;
		case 1:
			path.add(GetLineSegment(135,45));
			path.add(GetLineSegment(315,225));
			path.add(GetLineSegment(90,270));
			RandomWalkEnemy = 1;
			break;
		case 2:
			path.add(GetLineSegment(45,135));
			path.add(GetLineSegment(90,270));
			path.add(GetLineSegment(135,270));
			path.add(GetLineSegment(270,45));
			RandomWalkEnemy = 2;
			break;
		case 3:
			path.add(GetLineSegment(45,135));
			path.add(GetLineSegment(135,315));
			path.add(GetLineSegment(45,225));
			path.add(GetLineSegment(225,315));
			RandomWalkEnemy = 2;
			break;
		case 4:
			path.add(GetLineSegment(22.5f,157.5f));
			path.add(GetLineSegment(202.5f,337.5f));
			path.add(GetLineSegment(67.5f,292.5f));
			path.add(GetLineSegment(112.5f,247.5f));
			path.add(GetLineSegment(22.5f,337.5f));
			path.add(GetLineSegment(67.5f,112.5f));
			path.add(GetLineSegment(157.5f,202.5f));
			path.add(GetLineSegment(247.5f,292.5f));
			RandomWalkEnemy = 3;
			break;
		case 5:
			path.add(GetLineSegment(90,0));
			path.add(GetLineSegment(90,180));
			path.add(GetLineSegment(180,270));
			path.add(GetLineSegment(270,0));
			path.add(GetLineSegment(45,135));
			path.add(GetLineSegment(135,225));
			path.add(GetLineSegment(225,315));
			path.add(GetLineSegment(45,315));
			RandomWalkEnemy = 3;
			break;
		case 6:
			path.add(GetLineSegment(0,120));
			path.add(GetLineSegment(120,240));
			path.add(GetLineSegment(240,0));
			path.add(GetLineSegment(60,180));
			path.add(GetLineSegment(180,300));
			path.add(GetLineSegment(300,60));
			RandomWalkEnemy = 3;
			break;
		case 7:
			path.add(GetLineSegment(90,0));
			path.add(GetLineSegment(90,45));
			path.add(GetLineSegment(90,135));
			path.add(GetLineSegment(90,180));
			path.add(GetLineSegment(90,225));
			path.add(GetLineSegment(90,270));
			path.add(GetLineSegment(90,315));
			path.add(GetLineSegment(270,0));
			path.add(GetLineSegment(270,45));
			path.add(GetLineSegment(270,135));
			path.add(GetLineSegment(270,180));
			path.add(GetLineSegment(270,225));
			path.add(GetLineSegment(270,315));
			RandomWalkEnemy = 4;
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
