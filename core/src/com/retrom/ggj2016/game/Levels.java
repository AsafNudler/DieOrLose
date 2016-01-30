package com.retrom.ggj2016.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.utils.utils;

public class Levels {
	
	private static float RADIUS = 350;
	
	ArrayList<LineSegment> path = new ArrayList<LineSegment>();
	ArrayList<Vector2> candles = new ArrayList<Vector2>();
	private int level;

	public int RandomWalkEnemy = 0;
	public int FollowerEnemy = 0;

	static ArrayList<Levels> levels = null;

	private Levels()
	{

	}

	Levels(int level) {
		if (levels == null)
		{
			try {
				levels = new ArrayList<Levels>();
				FileHandle fl = Gdx.files.internal("levels.txt");
				String cont = fl.readString();
				String[] lines = cont.replace("\r", "").split("\n");
				Levels nextLevel = null;
				for (String line : lines) {
					if (line.trim().equals(""))
					{
						if (null != nextLevel)
						{
							levels.add(nextLevel);
						}
						nextLevel = null;
						continue;
					}
					if (nextLevel == null)
					{
						nextLevel = new Levels();
					}
					String action[] = line.split(":");
					if (action[0].equals("Segment"))
					{
						nextLevel.path.add(GetLineSegment(Float.parseFloat(action[1]), Float.parseFloat(action[2])));
					}
					else if (action[0].equals("RandomWalkEnemy"))
					{
						nextLevel.RandomWalkEnemy = Integer.parseInt(action[1]);
					}
					else if (action[0].equals("FollowerEnemy"))
					{
						nextLevel.FollowerEnemy = Integer.parseInt(action[1]);
					}
					else if (action[0].equals("Candle"))
					{
						nextLevel.candles.add(GetCandle(Float.parseFloat(action[1])));
					}
					else
					{
						throw new IOException("Wrong format: " + action[0]);
					}
				}
				if (null != nextLevel)
				{
					levels.add(nextLevel);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.level = level;

		if (level >= levels.size())
		{
			for (int i=0; i < level; i++) {
				path.add(GetLineSegment((float)Math.random() * 360, (float) (Math.random() * 360)));
			}
			RandomWalkEnemy = level - 1;
			FollowerEnemy = 1;
			return;
		}

		Levels lvl = levels.get(level);

		path = lvl.path;
		RandomWalkEnemy = lvl.RandomWalkEnemy;
		FollowerEnemy = lvl.FollowerEnemy;
		candles = lvl.candles;
		

	}
	
	// Angles are in degrees.
	private LineSegment GetLineSegment(float angle1, float angle2) {
		float rad1 = (float) (angle1 / 180 * Math.PI);
		float rad2 = (float) (angle2 / 180 * Math.PI);
		
		Vector2 vec1 = utils.dirVec(rad1, RADIUS); 
		Vector2 vec2 = utils.dirVec(rad2, RADIUS);
		
		return new LineSegment(vec1.x, vec1.y, vec2.x, vec2.y);
	}

	private Vector2 GetCandle(float angle1) {
		float rad1 = (float) (angle1 / 180 * Math.PI);

		Vector2 vec1 = utils.dirVec(rad1, RADIUS);

		return vec1;
	}
	
	public ArrayList<LineSegment> getPath() {
		return path;
	}
}
