package com.retrom.ggj2016.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.objects.BouncingBallEnemy;
import com.retrom.ggj2016.objects.Enemy;
import com.retrom.ggj2016.objects.Player;
import com.retrom.ggj2016.objects.Target;
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
	private List<PaintingLineGlow> glowLines = new ArrayList<PaintingLineGlow>();
	
	private List<Enemy> enemies = new ArrayList<Enemy>();
	
	private List<Target> targets = new ArrayList<Target>();
	
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
		for (int i=0; i < level + 1; i++) {
			Vector2 pos = utils.randomDir(400);
			targets.add(new Target(pos.x, pos.y));
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
		painting = new Painting(path, 17);
		// TODO Auto-generated method stub
		
	}

	public void update(float deltaTime) {
		player.update(deltaTime);
		painting.step();
		if (lastPosition.dst(player.position) > BLOOD_SEGMENT_LENGTH) {
			PaintingLineGlow lineGlow = new PaintingLineGlow(lastPosition.x, lastPosition.y, player.position.x, player.position.y);
			PaintingLine line = new PaintingLine(lastPosition.x, lastPosition.y, player.position.x, player.position.y, lineGlow);
			painting.addLine(line, lastPosition.x, lastPosition.y, player.position.x, player.position.y);
			bloodLines.add(line);
			glowLines.add(lineGlow);
			lastPosition = player.position.cpy();
		}
		for (Enemy enemy : enemies) {
			if (enemy.bounds.overlaps(player.bounds)) {
				listener_.restart();
			}
			enemy.update(deltaTime);
		}
		
		for (Target target : targets) {
			if (target.bounds.overlaps(player.bounds)) {
				System.out.println("target taken");
				target.take();
			}
		}
		
		if (allTargetsTaken()) {
			listener_.nextLevel();
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			listener_.restart();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
			listener_.nextLevel();
		}
	}
	
	private boolean allTargetsTaken() {
		for (Target target : targets) {
			if (!target.taken()) {
				return false;
			}
		}
		return true;
	}

	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		batch.begin();
		utils.drawCenter(batch, Assets.bg, 0, 0);
		batch.end();

		for (PaintingLine line : bloodLines) {
			if (!line.onPath) {
				line.render(shapeRenderer);
			}
		}

		for (PaintingLineGlow line : glowLines) {
			line.render(shapeRenderer);
		}

		for (PaintingLine line : bloodLines) {
			if (line.onPath) {
				line.render(shapeRenderer);
			}
		}
		batch.begin();
		for (Target target : targets) {
			target.render(batch);
		}
		player.render(batch);
		for (Enemy enemy : enemies) {
			enemy.render(batch);
		}
		batch.end();
	}
}
