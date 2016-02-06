package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

public class Candle extends DynamicGameObject {
	
	private final static float SIZE = 40;
	
	public boolean taken = false;
	
	private final Sprite sprite;
	private final Sprite glowSprite;
	
	private final float groundRotation = (float) (Math.random() * 100 - 50);

	private final Player player;
	
	private float stateTime = 0;

	private int level;

	public Candle(float x, float y, Player player, int level) {
		super(x, y, SIZE, SIZE);
		this.player = player;
		this.level = level;
		
		int spriteindex = Math.random() > 0.5 ? 0 : 1;
		 sprite = Assets.candle.get(spriteindex);
		 glowSprite = Assets.candleGlow.get(spriteindex);
		
	}
	
	public void render(SpriteBatch batch) {
		float rotation;
		if (player.candle != null) {
			stateTime = 0;
		}
		if (player.candle != this) {
			rotation = groundRotation;
		} else {
			rotation = 0;
		}
		
		sprite.setRotation(rotation);
		utils.drawCenter(batch, sprite, position.x, position.y);
		if (player.candle == null && level == 0) {
			glowSprite.setAlpha((float) ((Math.sin(stateTime * 6) + 1)/4+0.5f) * Math.min(1,stateTime*3));
			glowSprite.setRotation(rotation);
			utils.drawCenter(batch, glowSprite, position.x, position.y);
		}
	}

	public void update(float deltaTime) {
		stateTime += deltaTime;
		if (taken) {
			position.x = player.position.x + 24;
			position.y = player.position.y+ 5;
		}
	}

	public boolean overPlayer() {
		if (taken) return true;
		return position.y < player.position.y;
	}
}
