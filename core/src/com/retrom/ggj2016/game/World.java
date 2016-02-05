package com.retrom.ggj2016.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.assets.SoundAssets;
import com.retrom.ggj2016.objects.Candle;
import com.retrom.ggj2016.objects.CandlePoint;
import com.retrom.ggj2016.objects.Enemy;
import com.retrom.ggj2016.objects.FollowerEnemy;
import com.retrom.ggj2016.objects.Heart;
import com.retrom.ggj2016.objects.Player;
import com.retrom.ggj2016.objects.PlayerExplode;
import com.retrom.ggj2016.objects.RandomWalkEnemy;
import com.retrom.ggj2016.screens.GameScreen;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class World {
	
	private enum GameState {
		BEFORE_CANDLES,
		CANDLES_ON,
		AFTER_CANDLES,
		DEATH,
		LEVEL_END;
	}
	
	public interface WorldListener {
		public void restart();
		public void nextLevel();
		public void restartGame();
	}
	
	GameState state = GameState.BEFORE_CANDLES;
	
	private static final float BLOOD_SEGMENT_LENGTH = 2f;

	private static final float BLOOD_LOSE_RATE = 15e-5f;

	private static final int WALL_WIDTH = 67;

	public static final float BOUNDS = 450;

	private static final float PAIN_EFFECT_TIME = 1;
	
	private LifeBar lifebar = new LifeBar();
	private Player player = new Player(lifebar);
	private Heart heart = null;

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
	private float endTime = 0;
	
	private float painTime = 0;
	
	private boolean newlevel = true;

	private Altar altar;

	private PlayerExplode explosion;

	private float deathTime = 0;

	private float fade = 0;
	private float whiteFade = 0;

	private boolean gameStarted = false;

	private boolean endLevelSoundStarted = false;

	private void restartLevel()
	{
		SoundAssets.stopBloodSteps();
		altar = new Altar(level);
		state = GameState.BEFORE_CANDLES;
		enemies.clear();
		candles.clear();
		explosion = null;
		deathTime = 0;
		slashTime = 0;
		gameTime = 0;
		whiteFade = 0;
		for (PaintingLine bloodLine : bloodLines) {
			bloodLine.pathDone = false;
			bloodLine.onPath = false;
		}
		lifebar = new LifeBar();
		player = new Player(lifebar);
		lastPosition = player.position.cpy();
		buildLevel();
	}
	
	public World(WorldListener listener, int level) {
		this.listener_ = listener;
		this.level = level;
		lastPosition = player.position.cpy();
		altar = new Altar(level);
		buildLevel();
	}

	private ArrayList<Vector2> getEnemyRandomPos(int num)
	{
		double wallLength = (GameScreen.FRUSTUM_HEIGHT - 2 * WALL_WIDTH);
		double pos = Math.random() * (wallLength * 4);
		ArrayList<Vector2> res = new ArrayList<Vector2>();
		for (int i = 0; i < num; i++) {
			Vector2 loc;
			pos += ((wallLength * 4)/(double)num) +  (Math.random() * 60) - 30;
			pos %= wallLength * 4;
			if (pos < wallLength)
			{
				loc =  new Vector2((float)pos, WALL_WIDTH);
			}
			else if (pos < 2 * wallLength)
			{
				loc = new Vector2((float) GameScreen.FRUSTUM_HEIGHT - (float) WALL_WIDTH, (float) pos - (float) wallLength * 1);

			}
			else if (pos < 3 * wallLength)
			{
				loc =  new Vector2((float)pos - (float)wallLength*2, (float)GameScreen.FRUSTUM_HEIGHT - (float)WALL_WIDTH);

			}
			else {
				loc =  new Vector2(WALL_WIDTH, (float)pos - (float)wallLength*3);
			}
			loc.sub(new Vector2(GameScreen.FRUSTUM_HEIGHT / 2, GameScreen.FRUSTUM_HEIGHT / 2));
			res.add(loc);
		}

		Collections.shuffle(res);

		return res;
	}
	
	private void buildLevel() {
		Levels lvl = new Levels(level);
		ArrayList<Vector2> poses = getEnemyRandomPos(lvl.RandomWalkEnemy + lvl.FollowerEnemy);
		int j = 0;
		for (int i=0; i < lvl.RandomWalkEnemy; i++) {
			Vector2 pos = poses.get(j++);
			enemies.add(new RandomWalkEnemy(pos.x, pos.y));
		}
		ArrayList<FollowerEnemy> followers = new ArrayList<FollowerEnemy>();
		for (int i=0; i < lvl.FollowerEnemy; i++) {
			Vector2 pos = poses.get(j++);
			FollowerEnemy en = new FollowerEnemy(pos.x, pos.y, player, followers);
			enemies.add(en);
			followers.add(en);
		}
		
		path = lvl.getPath();
		painting = new Painting(path, 17, new Painting.LineComplete() {
			@Override
			public void signale() {
				lineCompleted();
			}
		});
		
		initCandlePoints(lvl);
	}

	private void lineCompleted()
	{
		SoundAssets.playSound(SoundAssets.lineComplete);
		lifebar.addLife();
		heart = new Heart(player.position);
	}
	
	private void initCandlePoints(Levels level) {
		candlePoints.clear();
		for (Vector2 candle : level.candles) {
			tryAddCandlePoint(candle.x, candle.y, true);
		}
		for (LineSegment ls : path) {
			tryAddCandlePoint(ls.startX, ls.startY, false);
			tryAddCandlePoint(ls.endX, ls.endY, false);
		}
	}
	
	private void tryAddCandlePoint(float x, float y, boolean withCandle) {
		for (CandlePoint cp : candlePoints) {
			if (utils.floatEquals(x, cp.position.x, 1f) && utils.floatEquals(y, cp.position.y, 1f)) {
				return;
			}
		}
		candlePoints.add(new CandlePoint(x, y, player, level, withCandle));
		if (withCandle) {
			spawnCandle();
		}
	}

	private void spawnCandle() {
		float x ,y; 
		x = y = 380 * (Math.random() > 0.5 ? 1 : -1);
		x += Math.random() * 80 - 40;
		y += Math.random() * 80 - 40;
		candles.add(new Candle(x, y, player, level));
	}

	public void update(float deltaTime) {
		updateHotkeys();
		
		gameTime += deltaTime;
		
		if (painTime > 0) {
			painTime -= deltaTime;
		}
		
		if (gameTime < 0.5f) {
			float val = 1-(gameTime * 2);
			if (newlevel) {
				whiteFade = val;
			} else {
				fade = val;
			}
		} else {
			fade = 0;
		}
		
		lifebar.update(deltaTime);
		altar.update(deltaTime);
		
		if (state == GameState.CANDLES_ON) {
			updateCandlesOn(deltaTime);
			return;
		}
		if (state == GameState.DEATH) {
			updateDeath(deltaTime);
			return;
		}
		if (state == GameState.LEVEL_END) {
			updateLevelEnd(deltaTime);
			return;
		}
		
		if (canStart()) {
			player.update(deltaTime);
			updateBloodstepsVolume();
			dropRandomBlood(deltaTime);
		}
		if (state == GameState.AFTER_CANDLES) {
			lifebar.life -= BLOOD_LOSE_RATE * deltaTime * Math.max(player.velocity.len(), 50f);
		}
		
		if (heart != null) {
			heart.update(deltaTime);
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
				enemy.onPlayer = true;
				lifebar.life -= deltaTime * 0.4;
				for (int i=0; i < deltaTime * 500; i++) {
					splashBlood();
				}
				if (painTime <= 0) {
					painTime = PAIN_EFFECT_TIME;
					SoundAssets.playRandomSound(SoundAssets.enemyHit);
					// TODO: play enemy hit sound effect.
				}	
			} else {
				enemy.onPlayer = false;
			}
		}
		
		for (Candle candle : candles) {
			if (player.candle == null && !candle.taken && candle.bounds.overlaps(player.bounds)) {
				candle.taken = true;
				player.candle = candle;
				SoundAssets.playSound(SoundAssets.candlePick);
			}
			candle.update(deltaTime);
		}
		
		if (lifebar.life <= 0) {
			state = GameState.DEATH;
		}
		

		if (painting.isDone()) {
			if (player.position.len() < 60)
				state = GameState.LEVEL_END; 
		}
		
		if (!gameStarted && !showLogo() && level == 0) {
			SoundAssets.startMusic();
			gameStarted = true;
		}
		
	}

	private void updateBloodstepsVolume() {
		if (state == GameState.AFTER_CANDLES) {
			float vol = Math.min(1, player.velocity.len() / player.maxVel() / 0.8f);
			SoundAssets.setBloodStepsVolume(vol);
		} else {
			SoundAssets.setBloodStepsVolume(0);
		}
	}

	private void updateLevelEnd(float deltaTime) {
		player.putInCenter();
		
		player.position.x *= (3f/4);
		player.position.y *= (3f/4);
		
		player.bounds.x = player.position.x - player.bounds.width / 2;
		player.bounds.y = player.position.y - player.bounds.height / 2;
		
		if (!endLevelSoundStarted) {
			endLevelSoundStarted = true;
			SoundAssets.playSound(SoundAssets.levelComplete);
		}
		
		endTime += deltaTime;
		if (endTime > 0.5) {
			
		}
		whiteFade = (endTime % 0.16f > 0.08f) ? 1 : 0;
		if (endTime > 0.8f) {
			player.putHorns();
		}
		if (endTime > 1.2f) {
			listener_.nextLevel();
		}
	}

	private void updateHotkeys() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			listener_.restart();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
			listener_.nextLevel();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
			heart = new Heart(player.position);
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			lifebar.life = 0;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			state = GameState.LEVEL_END;
		}
	}

	private void updateDeath(float deltaTime) {
		if (explosion == null) {
			explosion = new PlayerExplode(player.position);
			player.die();
			SoundAssets.playSound(SoundAssets.playerDie);
		}
		if (explosion.getStarted()) {
			for (int i=0; i < 600; i++) {
				splashBlood(100);
			}
		}
		if (explosion.stateTime > 0 && explosion.stateTime < 0.7f) {
			for (int i=0; i < deltaTime * 500; i++) {
				splashBlood(200);
			}
		}
		player.update(deltaTime);
		explosion.update(deltaTime);
		deathTime += deltaTime;
		if (deathTime > 2.5) {
			fade = (deathTime - 2.5f)*2; 
		}
		
		if (deathTime > 3f) {
			newlevel = false;
			restartLevel();
		}
	}

	private void updateCandlesOn(float deltaTime) {
		slashTime += deltaTime;
		for (CandlePoint cp : candlePoints) {
			cp.update(deltaTime);
		}
		if (slashTime > 1.3) {
			startCandlePhase();
		}
		if (slashTime > 0.7f) {
			for (int i=0; i < deltaTime * 300; i++) {
				splashBlood(60);
			}
		}
	}

	// Drops blood all the time (after slash, even when player does not move).
	private void dropRandomBlood(float deltaTime) {
		if (state != GameState.AFTER_CANDLES) {
			return;
		}
		if (Math.random() < deltaTime * 100) {
			Vector2 bloodDir = utils.randomDir((float) (Math.random()*10));
			PaintingLine line = new PaintingLine(player.position.x,
					player.position.y, player.position.x + bloodDir.x,
					player.position.y + bloodDir.y, null);
			bloodLines.add(line);
			
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
		for (CandlePoint cp : candlePoints) {
			cp.update(deltaTime);
		}
		
		if (state == GameState.AFTER_CANDLES)
		{
			return;
		}
		boolean allHaveCandles = true;
		for (CandlePoint cp : candlePoints) {
			if (cp.state == CandlePoint.State.NOCANDLE) {
				allHaveCandles = false;
			}
			if (cp.bounds.overlaps(player.bounds)) {
				if (player.candle != null && cp.state == CandlePoint.State.NOCANDLE) {
					cp.putCandle();
					removeCandle(player.candle);
					player.candle = null;
					SoundAssets.playSound(SoundAssets.candlePlace);
				}
			}
		}
		if (allHaveCandles && state == GameState.BEFORE_CANDLES) {
			startCandleOn();
		}
	}
	
	private void startCandleOn() {
		state = GameState.CANDLES_ON;
		candles.clear();
		SoundAssets.playSound(SoundAssets.bloodSlashes);
		for (CandlePoint cp : candlePoints) {
			cp.turnOnCandle();
		}
		player.knife = true;
		lifebar.blink();
	}

	private void startCandlePhase() {
		state = GameState.AFTER_CANDLES;
		player.knife = false;
		lastPosition = player.position.cpy();
		player.startBlood();
		SoundAssets.playBloodSteps();
	}

	private void removeCandle(Candle c) {
		for (Iterator<Candle> it = candles.iterator(); it.hasNext();) {
			if (it.next() == c) {
				it.remove();
			}
		}
	}
	
	private void splashBlood() {
		splashBlood(100);
	}
	
	private void splashBlood(float radius) {
		Vector2 newPos = utils.randomDir((float) (Math.random()*radius));
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
			if (!painting.isDone()) {
				painting.master_alpha = 1;
			}
			else {
				if (painting.master_alpha > 0) {
					painting.master_alpha -= 0.025;
					if (painting.master_alpha < 0)
					{
						painting.master_alpha = 0;
					}
				}
			}
			painting.render(shapeRenderer);
		}
		if (state == GameState.CANDLES_ON) {
			painting.master_alpha = Math.min((slashTime - 0.9f) * 5, 1);
			if (painting.isDone()) {
				painting.master_alpha = Math.max(0,
						Math.min(altar.stateTime, 1));
			}
			painting.render(shapeRenderer);
		}


		batch.begin();
		for (PaintingLine line : bloodLines) {
			line.render(batch);
		}



		altar.render(batch);

		for (CandlePoint cp : candlePoints) {
			cp.render(batch);
			if (cp.position.y > player.position.y - 40) {
				cp.renderFire(batch);
			}
		}
		
		if (!showLogo()) {
		for (Candle candle : candles) {
			if (state == GameState.BEFORE_CANDLES && !candle.overPlayer()) candle.render(batch);
		}
		}

		if (state == GameState.AFTER_CANDLES || state == GameState.CANDLES_ON) {
			painting.renderFire(batch);
		}

		for (Enemy enemy : enemies) {
			if (enemy.position.y >= player.position.y)
				enemy.render(batch);
		}
		
		if (!showLogo()) {
			player.render(batch);
		}
		if (!showLogo()) {
		for (Candle candle : candles) {
			if (state == GameState.BEFORE_CANDLES && candle.overPlayer()) candle.render(batch);
		}
		}
		
		for (CandlePoint cp : candlePoints) {
			if (cp.position.y <= player.position.y - 40) {
				cp.renderFire(batch);
			}
		}
		
		for (Enemy enemy : enemies) {
			if (enemy.position.y < player.position.y)
			enemy.render(batch);
		}
		
		if (explosion != null) {
			explosion.render(batch);
		}
		
		if (heart != null) {
			heart.render(batch);
		}
		batch.end();
		
		BatchUtils.setBlendFuncAdd(batch);
		batch.begin();
		if (painting.isDone()) {
			altar.show();
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
				s.setRotation(slashTime * 60);
				float tint = Math.max(0,Math.min(1, Math.min(slashTime*2, 1.5f - slashTime*2))); 
				s.setColor(tint, tint, tint, 1);
				utils.drawCenter(batch, s, player.position.x-30, player.position.y + 10);
				BatchUtils.setBlendFuncNormal(batch);
			}
		}
		
		batch.end();
		
		if (!showLogo()) {
			lifebar.render(shapeRenderer, batch);
		}
		
		renderFade(shapeRenderer);
		
		
		if (showLogo()) {
			BatchUtils.setBlendFuncNormal(batch);
			batch.begin();
			utils.drawCenter(batch, Assets.logo, 0, 0);
			batch.end();
		}
	}
	
	// Condition for the logo can disappear and start the game.
	private boolean canStart() {
		return level != 0 || gameTime > 5;
	}

	private boolean showLogo() {
		return level == 0 && player.position.x == 0 && player.position.y == 0;
	}

	private void renderFade(ShapeRenderer shapeRenderer) {
		if (painTime > 0) {
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		    Gdx.gl.glEnable(GL20.GL_BLEND);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1, 0, 0, painTime / PAIN_EFFECT_TIME * 0.3f);
			shapeRenderer.rect(- GameScreen.FRUSTUM_WIDTH / 2, - GameScreen.FRUSTUM_HEIGHT / 2, GameScreen.FRUSTUM_WIDTH, GameScreen.FRUSTUM_HEIGHT);
			shapeRenderer.end();
		}
		if (fade > 0) {
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		    Gdx.gl.glEnable(GL20.GL_BLEND);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 0, 0, fade);
			shapeRenderer.rect(- GameScreen.FRUSTUM_WIDTH / 2, - GameScreen.FRUSTUM_HEIGHT / 2, GameScreen.FRUSTUM_WIDTH, GameScreen.FRUSTUM_HEIGHT);
			shapeRenderer.end();
		}
		if (whiteFade > 0) {
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		    Gdx.gl.glEnable(GL20.GL_BLEND);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(1, 1, 1, whiteFade);
			shapeRenderer.rect(- GameScreen.FRUSTUM_WIDTH / 2, - GameScreen.FRUSTUM_HEIGHT / 2, GameScreen.FRUSTUM_WIDTH, GameScreen.FRUSTUM_HEIGHT);
			shapeRenderer.end();
		}
	}
}
