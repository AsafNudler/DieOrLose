package com.retrom.ggj2016.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
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
	private List<PaintingLineGlow> glowLines = new ArrayList<PaintingLineGlow>();
	
	private List<Enemy> enemies = new ArrayList<Enemy>();
	
	ArrayList<LineSegment> path = new ArrayList<LineSegment>();
	
	private Vector2 lastPosition;

	private final WorldListener listener_;

	private final int level;
	
	public World(WorldListener listener, int level) {
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
		
		path = new Levels(level).getPath();
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
		

		if (painting.isDone()) {
			listener_.nextLevel();
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			listener_.restart();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
			listener_.nextLevel();
		}
	}
	
	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		batch.begin();
		utils.drawCenter(batch, Assets.bg, 0, 0);
		batch.end();

		for (LineSegment line : path) {
			renderLineSegment(shapeRenderer, line);
		}
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
		player.render(batch);
		for (Enemy enemy : enemies) {
			enemy.render(batch);
		}
		batch.end();
	}

	private void renderLineSegment(ShapeRenderer renderer, LineSegment line) {
		// TODO Auto-generated method stub
		Gdx.gl.glLineWidth(2);
		renderer.begin(ShapeType.Line);
		
		renderer.setColor(1, 1, 1, 1);
		
		renderer.line(line.startX, line.startY, line.endX, line.endY);
		renderer.end();
	}
}
