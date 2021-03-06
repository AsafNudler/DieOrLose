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
import com.retrom.ggj2016.objects.Bone;
import com.retrom.ggj2016.objects.Book;
import com.retrom.ggj2016.objects.Candle;
import com.retrom.ggj2016.objects.CandlePoint;
import com.retrom.ggj2016.objects.Enemy;
import com.retrom.ggj2016.objects.FollowerEnemy;
import com.retrom.ggj2016.objects.Hatch;
import com.retrom.ggj2016.objects.Heart;
import com.retrom.ggj2016.objects.Player;
import com.retrom.ggj2016.objects.PlayerExplode;
import com.retrom.ggj2016.objects.RandomWalkEnemy;
import com.retrom.ggj2016.screens.GameScreen;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class World {
	
	private enum GameState {
		BEFORE_CANDLES,
		CANDLES_ON,
		AFTER_CANDLES,
		DEATH,
		LEVEL_END,
		GAME_END;
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

	public static final int PENTAGRAM_LEVEL = 8;

	private LevelNumberHud lnh;
	private LifeBar lifebar = new LifeBar();
	private Player player = new Player(lifebar);
	private Heart heart = null;
	private Book book;
	final private List<Bone> bones = new ArrayList<Bone>();

	private Painting painting;
	
	private List<PaintingLine> bloodLines = new ArrayList<PaintingLine>();
	
	private List<Candle> candles = new ArrayList<Candle>();
	private List<CandlePoint> candlePoints = new ArrayList<CandlePoint>();
	
	private List<Enemy> enemies = new ArrayList<Enemy>();
	
	List<LineSegment> path = new ArrayList<LineSegment>();
	
	private Vector2 lastPosition;

	private final WorldListener listener_;

	private final int level;
	
	private float gameTime = 0;
	private float slashTime = 0;
	private float endTime = 0;
	
	private float painTime = 0;
	private float lowHealthFade = 0;
	
	private boolean newlevel = true;

	private Altar altar;

	private PlayerExplode explosion;

	private float deathTime = 0;

	private float fade = 0;
	private float whiteFade = 0;

	private boolean gameStarted = false;

	private boolean endLevelSoundStarted = false;

	private boolean bookIsOpen;

	private float bookOpenTime;

	private ArrayList<Vector2> pentagramLevelPoses = getEnemyRandomPos(100);
	private int pentagramLevelEnemyIndex = 0;

	private ArrayList<FollowerEnemy> followers;

	private boolean finish = false;

	private Hatch hatch = null;

	private boolean enterHatch;

	private boolean endScene;

	private void restartLevel()
	{
		SoundAssets.stopBloodSteps();
		altar = new Altar(level);
		state = GameState.BEFORE_CANDLES;
		enemies.clear();
		candles.clear();
		bones.clear();
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
		if (!finish) buildLevel(); 
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

		shuffle(res);

		return res;
	}
	
	private void shuffle(ArrayList<Vector2> res) {
		for (int i=0; i < res.size(); i++) {
			int j = (int) Math.floor(Math.random() * res.size());
			Vector2 tmp = res.get(i);
			res.set(i, res.get(j));
			res.set(j, tmp);
		}
	}

	private void buildLevel() {
		Level lvl = Level.getLevel(level);
		lnh = new LevelNumberHud(level);
		ArrayList<Vector2> poses = getEnemyRandomPos(lvl.RandomWalkEnemy + lvl.FollowerEnemy);
		int j = 0;
		for (int i=0; i < lvl.RandomWalkEnemy; i++) {
			Vector2 pos = poses.get(j++);
			enemies.add(new RandomWalkEnemy(pos.x, pos.y));
		}
		followers = new ArrayList<FollowerEnemy>();
		for (int i=0; i < lvl.FollowerEnemy; i++) {
			Vector2 pos = poses.get(j++);
			createFollowerEnemy(pos);
		}
		
		path = lvl.getPath();
		painting = new Painting(path, 17, new Painting.LineComplete() {
			@Override
			public void signale() {
				lineCompleted();
			}
		});
		if (level == 0) {
			book = new Book(-270, 400, player);
		}
		
		initCandlePoints(lvl);
	}

	private FollowerEnemy createFollowerEnemy(Vector2 pos) {
		FollowerEnemy en = new FollowerEnemy(pos.x, pos.y, player, followers);
		enemies.add(en);
		followers.add(en);
		return en;
	}

	private void lineCompleted()
	{
		SoundAssets.playSound(SoundAssets.lineComplete);
		lifebar.addLife();
		if (!finish) heart = new Heart(player.position);
		if (level == PENTAGRAM_LEVEL) {
			for (int i=0; i < 3; i++) {
				Vector2 pos = pentagramLevelPoses.get(pentagramLevelEnemyIndex++);
				Enemy en = createFollowerEnemy(pos);
				en.startNow();
			}
		}
	}
	
	private void initCandlePoints(Level level) {
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
		
		if (endScene) {
			gameTime += deltaTime;
			fade = Math.max(0, fade -= deltaTime);
			return;
		}
		
		if (enterHatch) {
			fade += deltaTime;
			if (fade >= 1) showEndScene();
			return;
		}
		
		gameTime += deltaTime;
//		if (level == PENTAGRAM_LEVEL && state == GameState.AFTER_CANDLES) {
//			if (Math.random() < deltaTime / 4) {
//				Vector2 pos = pentagramLevelPoses.get(pentagramLevelEnemyIndex++);
//				Enemy en = createFollowerEnemy(pos);
//				en.startNow();
//				
//			}
//		}
		
		if (painTime > 0) {
			painTime -= deltaTime;
		}
		
		if (gameTime < 4 && finish) {
			whiteFade = 1 - gameTime / 4;
		} else if (gameTime < 0.5f) {
			float val = 1-(gameTime * 2);
			if (newlevel && level > 0) {
				whiteFade = val;
			} else {
				fade = val;
			}
		} else {
			fade = 0;
			whiteFade = 0;
		}
		
		for (Bone bone : bones) {
			bone.update(deltaTime);
		}
		
		lifebar.update(deltaTime);
		altar.update(deltaTime);
		if (book != null) book.update(deltaTime);
		if (hatch != null) {
			hatch.update(deltaTime);
			if (hatch.bounds.overlaps(player.bounds)) {
				enterHatch = true;
				SoundAssets.stopMusic();
				SoundAssets.playSound(SoundAssets.hatchOpen);
				return;
			}
		}
		
		if (state == GameState.CANDLES_ON) {
			updateCandlesOn(deltaTime);
			return;
		}
		if (state == GameState.DEATH) {
			updateDeath(deltaTime);
			return;
		}
		if (state == GameState.LEVEL_END && !finish) {
			updateLevelEnd(deltaTime);
			return;
		}
		
		if (canStart() && !bookIsOpen) {
			player.update(deltaTime);
			updateBloodstepsVolume();
			dropRandomBlood(deltaTime);
		} else {
			SoundAssets.stopBloodSteps();
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
		
		if (lifebar.isLowHealth()) {
			if (lowHealthFade == 0)
			SoundAssets.startHeartBeat();
			lowHealthFade += deltaTime;
			
		} else {
			lowHealthFade = 0;
			SoundAssets.stopHeartBeat();
		}
		

		if (painting.isDone()) {
			if (player.position.len() < 60) {
				state = GameState.LEVEL_END;
				altar.stopUi();
			}
		}
		
		if (!gameStarted && !shouldShowLogo() && level == 0) {
			SoundAssets.startMusic();
			gameStarted = true;
		}
		
		if (finish && gameTime > 1) {
			if (hatch == null) hatch = new Hatch();
		}
		
	}

	private void showEndScene() {
		restartLevel();
		endScene = true;
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
			SoundAssets.playSound(level == PENTAGRAM_LEVEL
					? SoundAssets.levelCompleteFinal
					: SoundAssets.levelComplete);
		}
		
		endTime += deltaTime;
		if (endTime > 0.5) {
			
		}
		whiteFade = (endTime % 0.16f > 0.08f) ? 1 : 0;
		if (endTime > 0.8f) {
			player.putHorns();
		}
		if (endTime > 1.2f && level != PENTAGRAM_LEVEL) {
			listener_.nextLevel();
		}
		
		if (endTime > 3 && level == PENTAGRAM_LEVEL) {
			finishGame();
			return;
		}
		
		if (level == PENTAGRAM_LEVEL) {
			for (FollowerEnemy enemy : followers) {
				enemy.position.limit((float) (Math.pow(0.01f, deltaTime) * enemy.position.len()));
				enemy.setAlpha(Math.min(1, enemy.position.len() / 100));
			}
			if (endTime < 2.7f) {
				if (Math.random() < deltaTime * 70 * endTime / 3f) {
					Enemy en = createFollowerEnemy(utils.randomDir(450f));
					en.appearNow();
				}
			}
		}
	}

	private void finishGame() {
		state = GameState.GAME_END;
		finish = true;
		restartLevel();
		bloodLines.clear();
		SoundAssets.stopMusic();
		for (CandlePoint cp : candlePoints) {
			cp.state = CandlePoint.State.OFF;
		}
	}

	private void updateHotkeys() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			listener_.restart();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
			lifebar.life -= 0.1;
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
			SoundAssets.stopHeartBeat();
			player.die();
			SoundAssets.playSound(SoundAssets.playerDie);
			int numbones = (int) (Math.random() * 6 + 6);
			for (int i=0; i < numbones; i++) {
				bones.add(new Bone(player.position.cpy()));
			}
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
					player.position.y + bloodDir.y);
			bloodLines.add(line);
			
		}
	}

	private void dropBlood() {
		if (state == GameState.BEFORE_CANDLES || finish) {
			return;
		}
		if (lastPosition.dst(player.position) > BLOOD_SEGMENT_LENGTH) {
//			PaintingLineGlow lineGlow = new PaintingLineGlow(lastPosition.x, lastPosition.y, player.position.x, player.position.y);
			PaintingLine line = new PaintingLine(lastPosition.x, lastPosition.y, player.position.x, player.position.y);
			painting.addLine(line, lastPosition.x, lastPosition.y, player.position.x, player.position.y);
			bloodLines.add(line);
//			glowLines.add(lineGlow);
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
		if (allHaveCandles && state == GameState.BEFORE_CANDLES && !finish) {
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
		lifebar.startEyeBlink();
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
		
		PaintingLine line = new PaintingLine(player.position.x, player.position.y, newPos.x, newPos.y);
		bloodLines.add(line);
	}

	public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		if (endScene) {
			renderEndScene(batch, shapeRenderer);
			return;
		}
		
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
		
		if (!shouldShowLogo()) {
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
		
		if (!shouldShowLogo()) {
			if (book != null) book.render(batch);
			if (hatch != null) hatch.render(batch);
			player.render(batch);
		}
		
		for (Bone bone : bones) {
			bone.render(batch);
		}
		
		if (!shouldShowLogo()) {
			for (Candle candle : candles) {
				if (state == GameState.BEFORE_CANDLES && candle.overPlayer())
					candle.render(batch);
			}
			if ((gameTime - bookOpenTime > 2f)
					&& bookIsOpen
					&& (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input
							.justTouched())) {
				closeBook();
			}
			if (!bookIsOpen && book != null
					&& player.bounds.overlaps(book.bounds) && player.candle == null) {
				openBook();
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
		if (painting.isDone() && !finish) {
			if (!altar.shown) {
				painting.makeManyParticles();
			}
			altar.show();
			if (book != null) book.destroy();
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
		
		if (bookIsOpen) {
			BatchUtils.setBlendFuncNormal(batch);
			utils.drawCenter(batch, Assets.page, 0, 0);
		}
		if (lnh != null) lnh.render(batch);
		batch.end();
		
		if (!shouldShowLogo() && !bookIsOpen) {
			lifebar.render(shapeRenderer, batch);
		}
		
		if (shouldShowLogo()) {
			BatchUtils.setBlendFuncNormal(batch);
			batch.begin();
			utils.drawCenter(batch, Assets.logo, 0, 0);
			batch.end();
		}
		renderFade(shapeRenderer);
	}
	
	private void renderEndScene(SpriteBatch batch, ShapeRenderer shapeRenderer) {
		BatchUtils.setBlendFuncNormal(batch);
		
		Sprite note = Assets.endNote;
		note.setAlpha(Math.min(1,gameTime * 2));
		
		gameTime -= 1; // Hack: move time backwards and then forward.
		if (gameTime < 0) {
			gameTime += 1;
			return;
		}
		
		batch.begin();
		if (gameTime < 1f) {
			float t = gameTime;
			t = 1 - (1-t) * (1-t);
			note.setScale(0.5f*(1-t) + t * 0.9f);
			note.setRotation((-5)*(1-t) + -2f * t);
			note.setAlpha(t);
		} else if (gameTime < 15f) {
			float t = (gameTime - 1f) / (15 - 1);
			t = (float) Math.sqrt(t);
			note.setScale(0.9f*(1-t) + 1 * t);
			note.setRotation((-2)*(1-t) + 0f * t);
			note.setAlpha(1);
		} else if (gameTime < 25) {
			note.setAlpha(1);
		} else if (gameTime < 30) {
			float t = (gameTime - 25) / (30 - 25);
			note.setAlpha(1-t);
		} else {
			note.setAlpha(0);
		}
		utils.drawCenter(batch, note, 0, 0);
		
		// First glow layer.
		Sprite noteGlow = Assets.endNoteGlow;
		if (gameTime < 20.5f){
			noteGlow.setAlpha(0);
		} else if (gameTime < 23) {
			float t = (gameTime - 20.5f) / (23f - 20.5f);
			noteGlow.setAlpha(Math.min(1, Math.max(0, t)));
		} else if (gameTime < 35){
			noteGlow.setAlpha(1);
		} else {
			float t = (gameTime - 35f) / (40f - 35f);
			noteGlow.setAlpha(Math.max(0,  1-t));
		}
		utils.drawCenter(batch, noteGlow, 0, 0);
		
		// Second glow layer.
		if (gameTime < 25) {
			noteGlow.setAlpha(0);
		} else if (gameTime < 35f) {
			float t = (gameTime - 25) / (35f - 25f);
			noteGlow.setAlpha((float) Math.sin(t * Math.PI));
		} else if (gameTime < 40f) {
			noteGlow.setAlpha(0);
		} else {
			noteGlow.setAlpha(0);
		}
		utils.drawCenter(batch, noteGlow, 0, 0);
		
		gameTime += 1;
		
		batch.end();
	}

	private void openBook() {
		bookIsOpen = true;
		player.velocity.x = player.velocity.y = 0;
		book = null;
		fade = 0.3f;
		bookOpenTime = gameTime;
		SoundAssets.playSound(SoundAssets.bookOpen);
	}
	private void closeBook() {
		bookIsOpen = false;
		fade = 0f;
		SoundAssets.playSound(SoundAssets.bookClose);
	}

	// Condition for the logo can disappear and start the game.
	private boolean canStart() {
		return level != 0 || gameTime > 5;
	}

	private boolean shouldShowLogo() {
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
		if (lowHealthFade > 0) {
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		    Gdx.gl.glEnable(GL20.GL_BLEND);
			shapeRenderer.begin(ShapeType.Filled);
			float alpha = 1 - (float) ((Math.cos(lowHealthFade * 6) + 1) / 2);
			alpha *= 0.15f;
			shapeRenderer.setColor(1, 0, 0, alpha);
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

	public boolean allowMusic() {
		return !endScene && canStart();
	}
}
