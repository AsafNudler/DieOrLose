package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class Candle extends DynamicGameObject {
	
	private final static float SIZE = 40;
	
	public boolean taken = false;
	
	private final Sprite sprite = Assets.candle.random();

	private final Player player;

	public Candle(float x, float y, Player player) {
		super(x, y, SIZE, SIZE);
		this.player = player;
		
		System.out.println("bb="+bounds);
		
	}
	
	public void render(SpriteBatch batch) {
		utils.drawCenter(batch, sprite, position.x, position.y);
	}

	public void update(float deltaTime) {
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
