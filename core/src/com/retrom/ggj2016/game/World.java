package com.retrom.ggj2016.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.objects.BouncingBallEnemy;
import com.retrom.ggj2016.objects.Enemy;
import com.retrom.ggj2016.objects.FollowerEnemy;
import com.retrom.ggj2016.objects.Player;
import com.retrom.ggj2016.objects.RandomWalkEnemy;
import com.retrom.ggj2016.screens.GameScreen;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.TouchToPoint;
import com.retrom.ggj2016.utils.utils;

public class World {
	
	public interface WorldListener {
		public void restart();
		public void nextLevel();
	}
	
	private static final float BLOOD_SEGMENT_LENGTH = 5f;

	private static final float BLOOD_LOSE_RATE = 15e-5f;

	private static final int WALL_WIDTH = 67;

	public static final float BOUNDS = 450;
	
	private Player player = new Player();
	
	private LifeBar lifebar = new LifeBar();

	private Painting painting;
	
	private List<PaintingLine> bloodLines = new ArrayList<PaintingLine>();
	private List<PaintingLineGlow> glowLines = new ArrayList<PaintingLineGlow>();
	
	private List<Enemy> enemies = new ArrayList<Enemy>();
	
	ArrayList<LineSegment> path = new ArrayList<LineSegment>();
	
	private Vector2 lastPosition;

	private final WorldListener listener_;

	private final int level;

	private void restartLevel()
	{
		enemies = new ArrayList<Enemy>();
		buildLevel();
		for (PaintingLine bloodLine : bloodLines) {
			bloodLine.pathDone = false;
			bloodLine.onPath = false;
		}
		lifebar = new LifeBar();
		player = new Player();
		lastPosition = player.position.cpy();
	}
	
	public World(WorldListener listener, int level) {
		this.listener_ = listener;
		this.level = level;
		lastPosition = player.position.cpy();
		buildLevel();
	}

	private Vector2 getEnemyRandomPos()
	{
		double wallLength = (GameScreen.FRUSTUM_HEIGHT - 2 * WALL_WIDTH);
		double pos = Math.random() * (wallLength * 4);
		Vector2 res;
		if (pos < wallLength)
		{
			res =  new Vector2((float)pos, WALL_WIDTH);
		}
		else if (pos < 2 * wallLength)
		{
			res =  new Vector2((float)pos - (float)wallLength, (float)GameScreen.FRUSTUM_HEIGHT - (float)WALL_WIDTH);
		}
		else if (pos < 3 * wallLength)
		{
			res =  new Vector2(WALL_WIDTH, (float)pos - (float)wallLength*2);
		}
		else {
			res = new Vector2((float) GameScreen.FRUSTUM_HEIGHT - (float) WALL_WIDTH, (float) pos - (float) wallLength * 3);
		}
		res.sub(new Vector2(GameScreen.FRUSTUM_HEIGHT/2, GameScreen.FRUSTUM_HEIGHT/2));
		return res;
	}
	
	private void buildLevel() {
		Levels lvl = new Levels(level);
		for (int i=0; i < lvl.RandomWalkEnemy; i++) {
			Vector2 pos = getEnemyRandomPos();
			enemies.add(new RandomWalkEnemy(pos.x, pos.y));
		}
		
		path = lvl.getPath();
		painting = new Painting(path, 17);
		// TODO Auto-generated method stub
		
	}

	public void update(float deltaTime) {
		player.update(deltaTime);
		lifebar.life -= BLOOD_LOSE_RATE * deltaTime * player.velocity.len();
		
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
			enemy.update(deltaTime);
			if (enemy.bounds.overlaps(player.bounds)) {
				lifebar.life -= deltaTime * 0.4;
				splashBlood();
			}
		}
		if (lifebar.life <= 0) {
			restartLevel();
		}
		

		if (painting.isDone()) {
			if (player.position.len() < 30)
				listener_.nextLevel();
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			listener_.restart();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
			listener_.nextLevel();
		}
	}
	
	private void splashBlood() {
		Vector2 newPos = utils.randomDir((float) (Math.random()*200));
		newPos.add(player.position);
		
		PaintingLineGlow lineGlow = new PaintingLineGlow(player.position.x, player.position.y, newPos.x, newPos.y);
		PaintingLine line = new PaintingLine(player.position.x, player.position.y, newPos.x, newPos.y, lineGlow);
//		painting.addLine(line, lastPosition.x, lastPosition.y, player.position.x, player.position.y);
		bloodLines.add(line);
		glowLines.add(lineGlow);
	}

	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		BatchUtils.setBlendFuncNormal(batch);
		batch.begin();
		utils.drawCenter(batch, Assets.bg, 0, 0);
		batch.end();

		for (LineSegment line : path) {
			renderLineSegment(shapeRenderer, line);
		}
//		for (PaintingLine line : bloodLines) {
//			if (!line.onPath) {
//				line.render(shapeRenderer);
//			}
//		}
//
//		for (PaintingLineGlow line : glowLines) {
//			line.render(shapeRenderer);
//		}
//
//		for (PaintingLine line : bloodLines) {
//			if (line.onPath) {
//				line.render(shapeRenderer);
//			}
//		}
		batch.begin();
		for (PaintingLine line : bloodLines) {
//			if (!line.onPath) {
				line.render(batch);
//			}
		}
		player.render(batch);
		for (Enemy enemy : enemies) {
			enemy.render(batch);
		}
		if (level == 0) {
			utils.drawCenter(batch, Assets.logo, 0, 350);
		}
		batch.end();
		
		BatchUtils.setBlendFuncAdd(batch);
		batch.begin();
		if (painting.isDone()) {
			utils.drawCenter(batch, Assets.centerGlow, 0, 0);
		}
		batch.end();
		
		lifebar.render(shapeRenderer);
		
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
