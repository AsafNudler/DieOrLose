package com.retrom.ggj2016.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.objects.BouncingBallEnemy;
import com.retrom.ggj2016.objects.Enemy;
import com.retrom.ggj2016.objects.Player;
import com.retrom.ggj2016.utils.utils;

public class World {
	
	public interface WorldListener {
		public void restart();
		public void nextLevel();
	}
	
	private static final float BLOOD_SEGMENT_LENGTH = 5f;
	
	private Player player = new Player();

	private Painting painting;
	
	private List<PaintingLine> bloodLines = new ArrayList<PaintingLine>();
	
	private List<Enemy> enemies = new ArrayList<Enemy>();
	
	private Vector2 lastPosition;

	private final WorldListener listener_;

	private final int level;
	
	public World(WorldListener listener, int level) {
		System.out.println("level="+level);
		this.listener_ = listener;
		this.level = level;
		lastPosition = player.position.cpy();
		buildLevel();
	}
	
	private void buildLevel() {
		for (int i=0; i < level; i++) {
			Vector2 pos = utils.randomDir(400);
			enemies.add(new BouncingBallEnemy(pos.x, pos.y));
		}

		LineSegment s1 = new LineSegment();
		s1.startX = 0;
		s1.startY = 0;
		s1.endX = 500;
		s1.endY = 500;
		LineSegment s2 = new LineSegment();
		s2.startX = 500;
		s2.startY = 500;
		s2.endX = 500;
		s2.endY = -500;
		ArrayList<LineSegment> path = new ArrayList<LineSegment>();
		path.add(s1);
		path.add(s2);
		painting = new Painting(path, 10);
		// TODO Auto-generated method stub
		
	}

	public void update(float deltaTime) {
		player.update(deltaTime);
		painting.step();
		if (lastPosition.dst(player.position) > BLOOD_SEGMENT_LENGTH) {
			PaintingLine line = new PaintingLine(lastPosition.x, lastPosition.y, player.position.x, player.position.y);
			painting.addLine(line, lastPosition.x, lastPosition.y, player.position.x, player.position.y);
			bloodLines.add(line);
			lastPosition = player.position.cpy();
		}
		for (Enemy enemy : enemies) {
			if (enemy.bounds.overlaps(player.bounds)) {
				listener_.restart();
				System.out.println("HIT");
			}
			enemy.update(deltaTime);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			listener_.restart();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
			listener_.nextLevel();
		}
	}
	
	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		for (PaintingLine line : bloodLines) {
			line.render(shapeRenderer);
		}
		batch.begin();
		player.render(batch);
		for (Enemy enemy : enemies) {
			enemy.render(batch);
		}
		batch.end();
	}
}
