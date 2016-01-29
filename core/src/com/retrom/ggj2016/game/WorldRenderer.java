/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.retrom.ggj2016.game;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.ggj2016.assets.Assets;

public class WorldRenderer {
	static final public float FRUSTUM_WIDTH = 540;
	static final public float FRUSTUM_HEIGHT = 540; //FRUSTUM_WIDTH / Gdx.graphics.getWidth() * Gdx.graphics.getHeight();
	
	public static final float FPS = 30f;
	public static final float FRAME_TIME = 1 / FPS;
	
	private static final List<Integer> wallBounceArray = Arrays.asList(1,6,8,6,3,1,2,3,2,0,1,0);
	
	private static final float PILLAR_POS = FRUSTUM_WIDTH / 2 - 40;
	
	
	SpriteBatch batch;
	
	OrthographicCamera cam;
	private float cam_target;
	private float cam_position;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();

	public WorldRenderer (SpriteBatch batch) {
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		cam_position = this.cam.position.y = FRUSTUM_HEIGHT / 3f;
		this.batch = batch;
	}
	
	public static final float CAM_SPEED = 40f;
	
	
	public void render(float deltaTime, boolean isPaused) {
		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		
		batch.enableBlending();
		batch.begin();
		
//		drawCenter(Assets.player, 100, 100);
		batch.end();
	}

	public float offset = 0;

	private static float snapToPixels(float cam_position) {
		return (float) (Math.floor(cam_position / FRUSTUM_HEIGHT * Gdx.graphics.getHeight()) * FRUSTUM_HEIGHT  / Gdx.graphics.getHeight());
	}

	public void renderBackground () {
		
	}
	
//	static class DrawTask {
//		public final Sprite keyFrame;
//		public final float x;
//		public final float y;
//		public DrawTask(Sprite keyFrame, float x, float y) {
//			this.keyFrame = keyFrame;
//			this.x = x;
//			this.y = y;
//		}
//	}
//	
//	public void drawPillar(Deque<Background.Element> pillar, float x, float y, boolean flip) {
//		LinkedList<DrawTask> drawTasks = new LinkedList<DrawTask>();
//		
//		for (Background.Element e : pillar) {
//			Sprite keyFrame = null;
//			switch (e) {
//			case PILLAR_1:
//			case PILLAR_2:
//			case PILLAR_3:
//				int index = e.index();
//				keyFrame = Assets.pillars.get(index);
//				break;
//			case PILLAR_BIG_1:
//			case PILLAR_BIG_2:
//				keyFrame = Assets.pillars_big.get(e.index() % Assets.pillars_big.size);
//				break;
//			case PILLAR_END:
//				keyFrame = Assets.pillars_end;
//				break;
//			case PILLAR_START:
//				keyFrame = Assets.pillars_start;
//				break;
//			case PILLAR_HOLE:
//				keyFrame = Assets.pillars_hole;
//				break;
//			case PILLAR_HOLE_BG:
//				keyFrame = Assets.pillars_hole_bg;
//				break;
//			case BACKGROUND_BASE:
//				keyFrame = Assets.background;
//				keyFrame.rotate(30f);
//				break;
//			case BACKGROUND_WORLD1_1:
//			case BACKGROUND_WORLD1_2:
//			case BACKGROUND_WORLD1_3:
//			case BACKGROUND_WORLD1_4:
//			case BACKGROUND_WORLD1_5:
//			case BACKGROUND_WORLD1_6:
//				keyFrame = Assets.bg_world1.get(e.index());
//				break;
//			case BACKGROUND_OVERLAY1_1:
//			case BACKGROUND_OVERLAY1_2:
//			case BACKGROUND_OVERLAY1_3:
//			case BACKGROUND_OVERLAY1_4:
//			case BACKGROUND_OVERLAY1_5:
//			case BACKGROUND_OVERLAY1_6:
//			case BACKGROUND_OVERLAY1_7:
//			case BACKGROUND_OVERLAY1_8:
//				keyFrame = Assets.bg_overlay_world1.get(e.index());
//				break;
//			case BACKGROUND_WORLD2_1:
//			case BACKGROUND_WORLD2_2:
//			case BACKGROUND_WORLD2_3:
//			case BACKGROUND_WORLD2_4:
//			case BACKGROUND_WORLD2_5:
//			case BACKGROUND_WORLD2_6:
//				keyFrame = Assets.bg_world2.get(e.index());
//				break;
//			case BACKGROUND_OVERLAY2_1:
//			case BACKGROUND_OVERLAY2_2:
//			case BACKGROUND_OVERLAY2_3:
//			case BACKGROUND_OVERLAY2_4:
//			case BACKGROUND_OVERLAY2_5:
//			case BACKGROUND_OVERLAY2_6:
//			case BACKGROUND_OVERLAY2_7:
//			case BACKGROUND_OVERLAY2_8:
//				keyFrame = Assets.bg_overlay_world2.get(e.index());
//				break;
//			case BACKGROUND_WORLD3_1:
//			case BACKGROUND_WORLD3_2:
//				keyFrame = Assets.bg_world3.get(e.index());
//			default:
////				Gdx.app.log("ERROR", "Unhandled pillar type: " + e);
//				break;
//			}
//			keyFrame.setFlip(flip, false);
//			drawTasks.addFirst(new DrawTask(keyFrame, x, y));
//			y += e.height();
//		}
//		for (DrawTask task : drawTasks) {
//			drawCenterBottom(task.keyFrame, task.x, task.y);
//		}
//	}
//	
//	public void renderObjects () {
//		batch.enableBlending();
//		BatchUtils.setBlendFuncNormal(batch);
//		batch.begin();
//		renderPillarBg();
//		renderWalls();
//		BatchUtils.setBlendFuncAdd(batch);
//		renderEffects(world.addEffectsUnder);
//		BatchUtils.setBlendFuncNormal(batch);
//		
//		renderPlayer();
//		renderEnemies();
//		renderGoldSacks();
//		renderCoins();
//		renderFloor();
//		
//		drawPillar(world.background.leftPillar, -PILLAR_POS, world.background.leftBaseY(), false);
//		drawPillar(world.background.rightPillar, PILLAR_POS, world.background.rightBaseY(), true);
//		
//		renderEffects(world.effects);
//		BatchUtils.setBlendFuncScreen(batch);
//		renderEffects(world.screenEffects);
//		BatchUtils.setBlendFuncAdd(batch);
//		renderEffects(world.addEffects);
//		batch.end();
//	}
//
//	private void renderPillarBg() {
//		for (float f : world.background.leftHoleList) {
//			Assets.pillars_hole_bg.setFlip(false, false);
//			drawCenterBottom(Assets.pillars_hole_bg, -PILLAR_POS, f);
//		}
//		for (float f : world.background.rightHoleList) {
//			Assets.pillars_hole_bg.setFlip(true, false);
//			drawCenterBottom(Assets.pillars_hole_bg, +PILLAR_POS, f);
//		}
//	}
//
//	private void renderEnemies() {
//		for (Enemy e : world.enemies_) {
//			Sprite s = e.accept(new Enemy.Visitor<Sprite>() {
//				@Override
//				public Sprite visit(Flame flame) {
//					return null;
//				}
//
//				@Override
//				public Sprite visit(TopFireball topFireball) {
//					return null;
//				}
//
//				@Override
//				public Sprite visit(Spitter spitter) {
//					Sprite $ = getFrameStopAtLastFrame(Assets.spitter, spitter.stateTime());
//					$.setFlip(spitter.side(), false);
//					return $;
//				}
//
//				@Override
//				public Sprite visit(SideFireball sideFireball) {
//					return null;
//				}
//			});
//			if (s == null) {
//				continue;
//			}
//			drawCenter(s, e.position);
//		}
//	}
//
//	private void renderEffects(List<Effect> effects) {
//		for (Effect e : effects) {
//			Sprite s = e.accept(new EffectVisitor<Sprite>() {
//
//				@Override
//				public Sprite visit(Score1Effect effect) {
//					Sprite s = Assets.scoreNum1;
//					s.setAlpha(effect.getAlpha());
//					s.setScale(effect.getScale());
//					return s;
//				}
//
//				@Override
//				public Sprite visit(Score3Effect effect) {
//					Sprite s = Assets.scoreNum3;
//					s.setAlpha(effect.getAlpha());
//					s.setScale(effect.getScale());
//					return s;
//				}
//
//				@Override
//				public Sprite visit(Score4Effect effect) {
//					Sprite s = Assets.scoreNum4;
//					s.setAlpha(effect.getAlpha());
//					s.setScale(effect.getScale());
//					return s;
//				}
//
//				@Override
//				public Sprite visit(Score5Effect effect) {
//					Sprite s = Assets.scoreNum5;
//					s.setAlpha(effect.getAlpha());
//					s.setScale(effect.getScale());
//					return s;
//				}
//
//				@Override
//				public Sprite visit(Score6Effect effect) {
//					Sprite s = Assets.scoreNum6;
//					s.setAlpha(effect.getAlpha());
//					s.setScale(effect.getScale());
//					return s;
//				}
//
//				@Override
//				public Sprite visit(Score10Effect effect) {
//					Sprite s = Assets.scoreNum10;
//					s.setAlpha(effect.getAlpha());
//					s.setScale(effect.getScale());
//					return s;
//				}
//
//				@Override
//				public Sprite visit(Score15GreenEffect effect) {
//					Sprite s = Assets.scoreNum15green;
//					s.setAlpha(effect.getAlpha());
//					s.setScale(effect.getScale());
//					return s;
//				}
//
//				@Override
//				public Sprite visit(Score15PurpleEffect effect) {
//					Sprite s = Assets.scoreNum15purple;
//					s.setAlpha(effect.getAlpha());
//					s.setScale(effect.getScale());
//					return s;
//				}
//
//				@Override
//				public Sprite visit(Score15TealEffect effect) {
//					Sprite s = Assets.scoreNum15teal;
//					s.setAlpha(effect.getAlpha());
//					s.setScale(effect.getScale());
//					return s;
//				}
//
//				@Override
//				public Sprite visit(Score25Effect effect) {
//					Sprite s = Assets.scoreNum25;
//					s.setAlpha(effect.getAlpha());
//					s.setScale(effect.getScale());
//					return s;
//				}
//
//				@Override
//				public Sprite visit(FiniteAnimationEffect effect) {
//					return getFrameStopAtLastFrame(effect.getAnimation(), effect.stateTime());
//				}
//
//				@Override
//				public Sprite visit(OneFrameEffect effect) {
//					Sprite s = effect.sprite();
//					float tint = effect.getTint();
//					s.setColor(tint, tint, tint, tint);
//					s.setFlip(effect.getFlip(), false);
//					return s;
//				}
//
//				@Override
//				public Sprite visit(FlameEffect effect) {
//					return getFrameStopAtLastFrame(Assets.flamethrowerFlame, effect.stateTime());
//				}
//
//				@Override
//				public Sprite visit(FireballAnimationEffect effect) {
//					return getFrameLoop(Assets.topFireballLoop, effect.stateTime());
//				}
//
//				@Override
//				public Sprite visit(DiamondGlowEffect effect) {
//					Sprite s = null;
//					switch (effect.diamond.type) {
//					case COIN_5_1:
//						s = Assets.tokenGlow;
//						break;
//					case COIN_5_2:
//						s = Assets.diamondCyanGlow;
//						break;
//					case COIN_5_3:
//						s = Assets.diamondPurpleGlow;
//						break;
//					case COIN_5_4:
//						s = Assets.diamondGreenGlow;
//						break;
//					default:
//						Gdx.app.error("Error", "Diamond glow on a non-diamond collectable.");
//						break;
//					}
//					float tint = (float) (0.5 + (Math.sin(effect.stateTime() * 6) + 1) / 5);
//					s.setColor(tint, tint, tint, tint);
//					effect.position_.y = effect.diamond.position.y + getBounceY(effect.diamond.stateTime());
//					effect.position_.x = effect.diamond.position.x;
//					return s;
//				}
//				
//				@Override
//				public Sprite visit(PowerupGlow effect) {
//					Sprite s = effect.sprite();
//					if (effect.c.state() == Collectable.STATUS_IDLE) {
//						effect.position_.y = effect.c.position.y + getBounceY(effect.c.stateTime());
//					} else {
//						effect.position_.y = effect.c.position.y;
//					}
//					effect.position_.x = effect.c.position.x;
//					return s;
//				}
//
//				@Override
//				public Sprite visit(FireballStartEffect effect) {
//					effect.position_.y = effect.originalY + cam.position.y; 
//					Sprite $ = getFrameStopAtLastFrame(effect.getAnimation(), effect.stateTime());
//					$.setY($.getY() + world.camTarget);
//					return $;
//				}
//				
//				@Override
//				public Sprite visit(WarningSkullEffect effect) {
//					effect.position_.y = effect.originalY + cam.position.y; 
//					Sprite s = effect.sprite();
//					float tint = effect.getTint();
//					s.setColor(tint, tint, tint, tint);
//					s.setY(s.getY() + world.camTarget);
//					return s;
//				}
//				
//				@Override
//				public Sprite visit(WarningExclEffect effect) {
//					// TODO: merge with WarningSkullEffect.
//					effect.position_.y = effect.originalY + cam.position.y; 
//					Sprite s = effect.sprite();
//					float tint = effect.getTint();
//					s.setColor(tint, tint, tint, tint);
//					s.setY(s.getY() + world.camTarget);
//					return s;
//				}
//				
//				
//
//				@Override
//				public Sprite visit(PlayerShieldEffect effect) {
//					Sprite s = null;
//					switch (effect.shieldState()) {
//					case START:
//						s = getFrameLoopOnSecondAnim(Assets.playerShieldEffectStart, Assets.playerShieldEffect, effect. stateTime());
//						break;
//					case MIDDLE:
//						s = getFrameLoop(Assets.playerShieldEffect, effect.stateTime());
//						break;
//					case DIE:
//						s = getFrameStopAtLastFrame(Assets.playerShieldEffectEnd, effect.stateTime());
//						break;
//					case HIT:
//						s = getFrameLoopOnSecondAnim(Assets.playerShieldEffectHit, Assets.playerShieldEffect, effect. stateTime());
//						break;
//					}
//					return s;
//				}
//
//				@Override
//				public Sprite visit(PlayerMagnetEffect effect) {
//					Sprite s = getFrameLoop(Assets.playerMagnetEffect, effect.stateTime());
//					float tint = effect.getTint();
//					s.setColor(tint, tint, tint, tint);
//					return s; 
//				}
//
//				@Override
//				public Sprite visit(PlayerOnionSkinEffect effect) {
//					Sprite s = getPlayerFrame(effect.playerState,
//							effect.playerStateTime, effect.playerSide);
//					float tint = effect.getTint();
//					s.setColor(tint, tint, tint, tint);
//					return s;
//				}
//			});
//			s.setPosition(e.position_.x - s.getWidth()/2, e.position_.y - s.getHeight()/2);
//			s.setRotation(e.getRotation());
//			s.setScale(e.getScale());
//			s.setY(snapToY(s.getY()));
//			s.draw(batch);
//		}
//	}
//
//	private void renderFloor() {
//		drawCenterBottom(Assets.floor, 0, -90);
//		drawCenter(Assets.pillars_big.get(0), 0, -149);
//		drawCenter(Assets.pillars_end, 0, -260);
//	}
//
//	private void renderWalls() {
//		for (Wall wall : world.walls_) {
//			float y = wall.position.y;
//			TextureRegion keyFrame = null;
//			if (wall instanceof BurningWall) {
//				if (wall.status() == Wall.STATUS_ACTIVE) {
//					if (wall.stateTime() < BurningWall.TIME_WITHOUT_BURN) {
//						keyFrame = Assets.burningWall;
//					} else if (wall.stateTime() < BurningWall.TIME_START){
//						keyFrame = getFrameStopAtLastFrame(Assets.burningWallStart, wall.stateTime() - BurningWall.TIME_WITHOUT_BURN);
//					} else {
//						keyFrame = getFrameLoop(Assets.burningWallBurn, wall.stateTime() - BurningWall.TIME_START);
//					}
//				} else if (wall.status() == Wall.STATUS_INACTIVE) {
//					keyFrame = getFrameStopAtLastFrame(Assets.burningWallEnd, wall.stateTime());
//				}
//				y += 12;
//			} else if (wall instanceof FlamethrowerWall) {
//				if (wall.status() == Wall.STATUS_ACTIVE) {
//					keyFrame = Assets.flamethrower;
//				} else if (wall.status() == Wall.STATUS_INACTIVE) {
//					keyFrame = getFrameStopAtLastFrame(Assets.flamethrowerAll, wall.stateTime());
//				}
//				y += 12;
//			} else {
//				keyFrame = wall.isDual() ? keyFrame = Assets.walls2
//						.get(wall.graphic_) : Assets.walls1.get(wall.graphic_);  
//			}
//			
//			
//			if (wall.status() == Wall.STATUS_INACTIVE && !(wall instanceof BurningWall)) {
//				int index = (int) (wall.stateTime() * FPS);
//				if (index < wallBounceArray.size())
//				y +=  wallBounceArray.get(index);
//			}
//			drawCenter(keyFrame, wall.position.x, y);
//		}
//		
//	}
//	
//	private Array<Sprite> getCoinAnimation(Collectable.Type type) {
//		switch(type) {
//		case COIN_1_1: return Assets.coin1_1_land;
//		case COIN_1_2: return Assets.coin1_2_land;
//		case COIN_2_1: return Assets.coin2_1_land;
//		case COIN_2_2: return Assets.coin2_2_land;
//		case COIN_2_3: return Assets.coin2_3_land;
//		case COIN_3_1: return Assets.coin3_1_land;
//		case COIN_3_2: return Assets.coin3_2_land;
//		case COIN_3_3: return Assets.coin3_3_land;
//		case COIN_4_1: return Assets.coin4_1_land;
//		case COIN_4_2: return Assets.coin4_2_land;
//		case COIN_4_3: return Assets.coin4_3_land;
//		case COIN_5_1: return Assets.coin5_1_land;
//		case COIN_5_2: return Assets.coin5_2_land;
//		case COIN_5_3: return Assets.coin5_3_land;
//		case COIN_5_4: return Assets.coin5_4_land;
//		case POWERUP_MAGNET:
//		case POWERUP_SLOMO:
//		case POWERUP_SHIELD:
//		}
//		return null;
//	}
//	
//	private TextureRegion getCoinKeyFrame(Collectable.Type type) {
//		switch(type) {
//		case COIN_1_1: return Assets.coin1_1;
//		case COIN_1_2: return Assets.coin1_2;
//		case COIN_2_1: return Assets.coin2_1;
//		case COIN_2_2: return Assets.coin2_2;
//		case COIN_2_3: return Assets.coin2_3;
//		case COIN_3_1: return Assets.coin3_1;
//		case COIN_3_2: return Assets.coin3_2;
//		case COIN_3_3: return Assets.coin3_3;
//		case COIN_4_1: return Assets.coin4_1;
//		case COIN_4_2: return Assets.coin4_2;
//		case COIN_4_3: return Assets.coin4_3;
//		case COIN_5_1: return Assets.coin5_1;
//		case COIN_5_2: return Assets.coin5_2;
//		case COIN_5_3: return Assets.coin5_3;
//		case COIN_5_4: return Assets.coin5_4;
//		case POWERUP_MAGNET: return Assets.powerupMagnet;
//		case POWERUP_SLOMO: return Assets.powerupSlomo;
//		case POWERUP_SHIELD: return Assets.powerupShield;
//		}
//		return null;
//	}
//	
//	private void renderCoins() {
//		for (Collectable coin : world.collectables_) {
//			TextureRegion keyFrame = null;
//			if (coin.isPowerup() || coin.state() != Collectable.STATUS_IDLE) {
//				keyFrame = getCoinKeyFrame(coin.type);
//			} else {
//				keyFrame = getFrameStopAtLastFrame(getCoinAnimation(coin.type), coin.stateTime());
//			}
//			
//			float y = coin.position.y;
//			if (coin.state() == Collectable.STATUS_IDLE) {
//				y += getBounceY(coin.stateTime());
//			}
//			drawCenter(keyFrame, coin.position.x, y);
//		}
//	}
//	
//	private void renderGoldSacks() {
//		for (GoldSack gs : world.goldSacks_) {
//			Sprite s = null;
//			switch (gs.state()) {
//			case GoldSack.STATE_FALLING:
//				s = Assets.goldSackFalling;
//				break;
//			case GoldSack.STATE_GROUND:
//				s = getFrameStopAtLastFrame(Assets.goldSackLand, gs.stateTime());
//				break;
//			case GoldSack.STATE_PUMP:
//				s = getFrameStopAtLastFrame(Assets.goldSackPump, gs.stateTime());
//				break;
//			case GoldSack.STATE_EMPTY:
//				s = getFrameStopAtLastFrame(Assets.goldSackEnd, gs.stateTime());
//				break;
//			}
//			drawCenter(s, gs.position.x, gs.position.y);
//		}
//	}
//
//	private float getBounceY(float stateTime) {
//		int index = (int) (stateTime * FPS);
//		if (index < wallBounceArray.size())
//			return wallBounceArray.get(index);
//		return 0;
//	}
//
//	private void drawCenterWithTilt(TextureRegion keyFrame, Vector2 position, float xTilt, float yTilt) {
//		batch.draw(keyFrame, position.x - keyFrame.getRegionWidth()/2+ xTilt, position.y - keyFrame.getRegionHeight()/2 + yTilt);
//	}
//
//	private void drawWithTilt(TextureRegion keyFrame, Vector2 position,
//			float xTilt, float yTilt) {
//		batch.draw(keyFrame, position.x + xTilt, position.y + yTilt);
//	}
//
//	private void renderPlayer () {
//		Player player = world.player;
//		Sprite keyFrame = getPlayerFrame(player.state(), player.stateTime, player.side);
//		keyFrame.setColor(1,1,1,1);
//		drawCenter(keyFrame, player.position);
//	}
//	
//	private Sprite getPlayerFrame(int state, float stateTime, boolean side) {
//		Sprite keyFrame = null;
//		switch (state) {
//		case Player.STATE_IDLE:
//			keyFrame = getFrameLoop(Assets.playerIdle, stateTime);
//			keyFrame.setFlip(side, false);
//			break;
//		case Player.STATE_RUNNING:
//			float startTime = FRAME_TIME * Assets.playerRunStart.size / 3;
//			if (stateTime < startTime * Assets.playerRunStart.size) {
//				keyFrame = getFrameLoop(Assets.playerRunStart, stateTime); 
//			} else {
//				keyFrame = getFrameLoop(Assets.playerRun, stateTime - startTime);
//			}
//			keyFrame.setFlip(side, false);
//			break;
//		case Player.STATE_JUMPING:
//			keyFrame = getFrameLoop(Assets.playerJump, stateTime);
//			keyFrame.setFlip(side, false);
//			break;
//		case Player.STATE_LANDING:
//			keyFrame = getFrameStopAtLastFrame(Assets.playerLand, stateTime);
//			keyFrame.setFlip(side, false);
//			break;
//		case Player.STATE_DIE:
//		case Player.STATE_DEAD:
//			if (world.player.deathType == Player.DEATH_BY_BURN) {
//				keyFrame = getFrameDisappearAfterLastFrame(Assets.playerBurn, stateTime);
//			} else if (world.player.deathType == Player.DEATH_BY_CRUSH) {
//				keyFrame = getFrameDisappearAfterLastFrame(Assets.playerSquash, stateTime);
//			} else {
//				Gdx.app.log("ERROR", "Player is in death type: " + world.player.deathType);
//			}
//			keyFrame.setFlip(side, false);
//			break;
//		default:
//			Gdx.app.log("ERROR", "Player is in invalid state: " + state);
//		}
//		return keyFrame;
//	}
//	
//	private Sprite getFrameLoopOnSecondAnim(Array<Sprite> startAnim,
//			Array<Sprite> loopAnim, float stateTime) {
//		float startAnimTime = FRAME_TIME * startAnim.size;
//		if (stateTime < startAnimTime) {
//			return getFrameStopAtLastFrame(startAnim, stateTime); 
//		} else {
//			return getFrameLoop(loopAnim, stateTime - startAnimTime);
//		}
//	}
//	
//	private Sprite getFrameLoop(Array<Sprite> anim, float stateTime) {
//		return anim.get((int) (stateTime * FPS) % anim.size);
//	}
//	
//	public static Sprite getFrameStopAtLastFrame(Array<Sprite> anim, float stateTime) {
//		int frameIndex = (int) (stateTime * FPS);
//		frameIndex = Math.min(frameIndex, anim.size-1);
//		return anim.get(frameIndex);
//	}
//	
//	private Sprite getFrameDisappearAfterLastFrame(Array<Sprite> anim, float stateTime) {
//		int frameIndex = (int) (stateTime * FPS);
//		if (frameIndex > anim.size-1) {
//			return Assets.empty;
//		}
//		return anim.get(frameIndex);
//	}
//	
//	private void drawCenterBottom(TextureRegion keyFrame, float x, float y) {
//		drawCenter(keyFrame, x, y + keyFrame.getRegionHeight()/2);
//	}
//	
//	private void drawCenter(TextureRegion keyFrame, Vector2 position) {
//		drawCenter(keyFrame, position.x, position.y);
//	}
//	
	private void drawCenter(TextureRegion keyFrame, float x, float y) {
		batch.draw(keyFrame, x - keyFrame.getRegionWidth()/2, y - keyFrame.getRegionHeight()/2);
	}
//	
//	public float snapToY(float y) {
//		double fraction = FRUSTUM_WIDTH / Gdx.graphics.getWidth();
//		float newy = (float) (Math.floor(y / fraction) * fraction);
//		return newy;
//		
//	}
}
