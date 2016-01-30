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

	private final Player player;
	
	private float stateTime = 0;

	public Candle(float x, float y, Player player) {
		super(x, y, SIZE, SIZE);
		this.player = player;
		
		int spriteindex = Math.random() > 0.5 ? 0 : 1;
		 sprite = Assets.candle.get(spriteindex);
		 glowSprite = Assets.candleGlow.get(spriteindex);
		
	}
	
	public void render(SpriteBatch batch) {
		utils.drawCenter(batch, sprite, position.x, position.y);
		if (player.candle == null) {
			glowSprite.setAlpha((float) ((Math.sin(stateTime * 6) + 1)/4+0.5f));
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
