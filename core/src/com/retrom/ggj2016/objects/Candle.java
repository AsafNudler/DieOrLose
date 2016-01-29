package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class Candle extends DynamicGameObject {
	
	private final static float SIZE = 40;
	
	private boolean onPlayer = false;
	
	private final Sprite sprite = Assets.candle.random();

	private final Player player;

	public Candle(float x, float y, Player player) {
		super(x, y, SIZE, SIZE);
		this.player = player;
		
		if (onPlayer) {
			position.x = player.position.x + 20;
			position.y = player.position.y;
		}
	}
	
	public void render(SpriteBatch batch) {
		utils.drawCenter(batch, sprite, position.x, position.y);
	}

	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	public boolean overPlayer() {
		if (onPlayer) return true;
		return position.y < player.position.y;
	}
}
