package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class Book extends GameObject {
	
	private final static float SIZE = 40;
	
	private float stateTime = 0;

	private Player player;

	private boolean destroyed;
	
	public Book(float x, float y, Player player) {
		super(x, y, SIZE , SIZE);
		this.player = player;
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
		if (player.candle != null) {
			stateTime = 0;
		}
	}
	
	public void render(SpriteBatch batch) {
		float alpha = 1;
		if (destroyed) alpha = Math.max(0, 1 - stateTime * 3);
		Assets.book.setAlpha(alpha);
		utils.drawCenter(batch, Assets.book, position.x, position.y);
		if (player.candle == null) {
			Sprite glowSprite = Assets.bookGlow;
			float glowAlpha = (float) ((Math.sin(stateTime * 6) + 1)/4+0.5f) * Math.min(1,stateTime*3);
			if (destroyed) glowAlpha = Math.max(0, 1 - stateTime * 3);
			glowSprite.setAlpha(glowAlpha);
			utils.drawCenter(batch, glowSprite, position.x, position.y);
		}
	}
	
	public void destroy() {
		if (destroyed) return;
		destroyed = true;
		stateTime = 0;
	}
	
}
