package com.retrom.ggj2016.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.objects.Candle;
import com.retrom.ggj2016.objects.CandlePoint;
import com.retrom.ggj2016.objects.Enemy;
import com.retrom.ggj2016.objects.FollowerEnemy;
import com.retrom.ggj2016.objects.Player;
import com.retrom.ggj2016.objects.RandomWalkEnemy;
import com.retrom.ggj2016.screens.GameScreen;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

public class World {
	
	private enum GameState {
		BEFORE_CANDLES,
		CANDLES_ON,
		AFTER_CANDLES;
	}
	
	public interface WorldListener {
		public void restart();
		public void nextLevel();
		public void restartGame();
	}
	
	GameState state = GameState.BEFORE_CANDLES;
	
	private static final float BLOOD_SEGMENT_LENGTH = 5f;

	private static final float BLOOD_LOSE_RATE = 15e-5f;

	private static final int WALL_WIDTH = 67;

	public static final float BOUNDS = 450;
	
	private Player player = new Player();
	
	private LifeBar lifebar = new LifeBar();

	private Painting painting;
	
	private List<PaintingLine> bloodLines = new ArrayList<PaintingLine>();
	private List<PaintingLineGlow> glowLines = new ArrayList<PaintingLineGlow>();
	
	private List<Candle> candles = new ArrayList<Candle>();
	private List<CandlePoint> candlePoints = new ArrayList<CandlePoint>();
	
	private List<Enemy> enemies = new ArrayList<Enemy>();
	
	ArrayList<LineSegment> path = new ArrayList<LineSegment>();
	
	private Vector2 lastPosition;

	private final WorldListener listener_;

	private final int level;
	
	private float gameTime = 0;
	private float slashTime = 0;

	private void restartLevel()
	{
		state = GameState.BEFORE_CANDLES;
		enemies.clear();
		candles.clear();
		for (PaintingLine bloodLine : bloodLines) {
			bloodLine.pathDone = false;
			bloodLine.onPath = false;
		}
		lifebar = new LifeBar();
		player = new Player();
		lastPosition = player.position.cpy();
		buildLevel();
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
		for (int i=0; i < lvl.FollowerEnemy; i++) {
			Vector2 pos = getEnemyRandomPos();
			enemies.add(new FollowerEnemy(pos.x, pos.y, player));
		}
		
		path = lvl.getPath();
		painting = new Painting(path, 17);
		
		initCandlePoints(lvl);
	}
	
	private void initCandlePoints(Levels level) {
		candlePoints.clear();
		for (Vector2 candle : level.candles) {
			tryAddCandlePoint(candle.x, candle.y);
		}
		
	}
	
	private void tryAddCandlePoint(float x, float y) {
		for (CandlePoint cp : candlePoints) {
			if (utils.floatEquals(x, cp.position.x, 1f) && utils.floatEquals(y, cp.position.y, 1f)) {
				return;
			}
		}
		candlePoints.add(new CandlePoint(x, y, player));
	}

	private void spawnCandle() {
		float x ,y; 
		x = y = 380 * (Math.random() > 0.5 ? 1 : -1);
		candles.add(new Candle(x, y, player));
	}

	public void update(float deltaTime) {
		gameTime += deltaTime;
		if (state == GameState.CANDLES_ON) {
			updateCandlesOn(deltaTime);
			return;
		}
		
		if (candles.isEmpty() && state == GameState.BEFORE_CANDLES) {
			spawnCandle();
		}
		
		player.update(deltaTime);
		if (state == GameState.AFTER_CANDLES) {
			lifebar.life -= BLOOD_LOSE_RATE * deltaTime * player.velocity.len();
		}

		dropBlood();
		
		for (PaintingLine bloodLine : bloodLines) {
			bloodLine.update(deltaTime);
		}
		for (Iterator<PaintingLine> it = bloodLines.iterator(); it.hasNext();) {
			PaintingLine line = it.next();
			if (line.offTime < 0) {
				it.remove();
			}
		}
		updateCandlePoints(deltaTime);
		
		for (Enemy enemy : enemies) {
			enemy.update(deltaTime);
			if (enemy.bounds.overlaps(player.bounds)) {
				lifebar.life -= deltaTime * 0.4;
				splashBlood();
			}
		}
		
		for (Candle candle : candles) {
			if (player.candle == null && !candle.taken && candle.bounds.overlaps(player.bounds)) {
				candle.taken = true;
				player.candle = candle;
			}
			candle.update(deltaTime);
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			listener_.restartGame();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			startCandlePhase();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
			startCandleOn();
		}
	}

	private void updateCandlesOn(float deltaTime) {
		slashTime += deltaTime;
		if (slashTime > 1) {
			startCandlePhase();
		}
	}

	private void dropBlood() {
		if (state == GameState.BEFORE_CANDLES) {
			return;
		}
		if (lastPosition.dst(player.position) > BLOOD_SEGMENT_LENGTH) {
			PaintingLineGlow lineGlow = new PaintingLineGlow(lastPosition.x, lastPosition.y, player.position.x, player.position.y);
			PaintingLine line = new PaintingLine(lastPosition.x, lastPosition.y, player.position.x, player.position.y, lineGlow);
			painting.addLine(line, lastPosition.x, lastPosition.y, player.position.x, player.position.y);
			bloodLines.add(line);
			glowLines.add(lineGlow);
			lastPosition = player.position.cpy();
		}
	}

	private void updateCandlePoints(float deltaTime) {
		if (state == GameState.AFTER_CANDLES)
		{
			return;
		}
		boolean allHaveCandles = true;
		for (CandlePoint cp : candlePoints) {
			cp.update(deltaTime);
			if (cp.state == CandlePoint.State.NOCANDLE) {
				allHaveCandles = false;
			}
			if (cp.bounds.overlaps(player.bounds)) {
				if (player.candle != null && cp.state == CandlePoint.State.NOCANDLE) {
					cp.putCandle();
					removeCandle(player.candle);
					player.candle = null;
				}
			}
		}
		if (allHaveCandles && state == GameState.BEFORE_CANDLES) {
			startCandleOn();
		}
	}
	
	private void startCandleOn() {
		state = GameState.CANDLES_ON;
		player.knife = true;
	}

	private void startCandlePhase() {
		state = GameState.AFTER_CANDLES;
		player.knife = false;
		candles.clear();
		for (CandlePoint cp : candlePoints) {
			cp.turnOnCandle();
		}
		lastPosition = player.position.cpy();
		player.startBlood();
	}

	private void removeCandle(Candle c) {
		for (Iterator<Candle> it = candles.iterator(); it.hasNext();) {
			if (it.next() == c) {
				it.remove();
			}
		}
	}
	
	private void splashBlood() {
		Vector2 newPos = utils.randomDir((float) (Math.random()*100));
		newPos.add(player.position);
		
		PaintingLineGlow lineGlow = new PaintingLineGlow(player.position.x, player.position.y, newPos.x, newPos.y);
		PaintingLine line = new PaintingLine(player.position.x, player.position.y, newPos.x, newPos.y, lineGlow);
		bloodLines.add(line);
		glowLines.add(lineGlow);
	}

	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		BatchUtils.setBlendFuncNormal(batch);
		batch.begin();
		utils.drawCenter(batch, Assets.bg, 0, 0);
		batch.end();

		if (state == GameState.AFTER_CANDLES) {
			painting.render(shapeRenderer);
		}

		batch.begin();
		for (PaintingLine line : bloodLines) {
			line.render(batch);
		}
		
		for (CandlePoint cp : candlePoints) {
			cp.render(batch);
		}
		
		for (Candle candle : candles) {
			if (state == GameState.BEFORE_CANDLES && !candle.overPlayer()) candle.render(batch);
		}

		player.render(batch);
		for (Candle candle : candles) {
			if (state == GameState.BEFORE_CANDLES && candle.overPlayer()) candle.render(batch);
		}
		
		for (Enemy enemy : enemies) {
			enemy.render(batch);
		}
		if (level == 0) {
			Assets.logo.setAlpha(Math.max(0, 1 - gameTime / 10));
			utils.drawCenter(batch, Assets.logo, 0, 350);
		}
		batch.end();
		
		BatchUtils.setBlendFuncAdd(batch);
		batch.begin();
		if (painting.isDone()) {
			utils.drawCenter(batch, Assets.centerGlow, 0, 0);
		}
		if (GameState.CANDLES_ON == state) {
			float anim_time = slashTime - 0.5f;
			{
			Sprite s = utils.getFrameUntilDone(Assets.slash, anim_time, 15);
			if (s != null) {
				utils.drawCenter(batch, s, player.position.x, player.position.y);
			}
			}
			{
				BatchUtils.setBlendFuncAdd(batch);
				Sprite s = Assets.knifeFlare;
				float tint = Math.min(1, Math.min(slashTime, 1 - slashTime)); 
				s.setColor(tint, tint, tint, 1);
				utils.drawCenter(batch, s, player.position.x-30, player.position.y + 10+ slashTime * 20);
				BatchUtils.setBlendFuncNormal(batch);
			}
		}
		batch.end();
		
		lifebar.render(shapeRenderer, batch);
		
		
	}

	private void renderFloorSegments(ShapeRenderer shapeRenderer) {
		if (state == GameState.AFTER_CANDLES) {
			for (LineSegment line : path) {
				renderLineSegment(shapeRenderer, line);
			}
		}
	}

	private void PaintBloodPath(ShapeRenderer shapeRenderer) {
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
	}

	private void renderLineSegment(ShapeRenderer renderer, LineSegment line) {
		// TODO: try to call these once and not in every segment.
		Gdx.gl.glLineWidth(5);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.begin(ShapeType.Line);
		
		renderer.setColor(1, 0.2f, 0.2f, 0.3f);
		
		renderer.line(line.startX, line.startY - 6, line.endX, line.endY - 6);
		renderer.end();
	}
}
