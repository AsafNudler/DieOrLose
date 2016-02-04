package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

public class PlayerExplode {
	
	Vector2 position;
	public float stateTime = - Player.TIME_BEFORE_EXPLODE;
	private boolean started;

	public PlayerExplode(Vector2 position) {
		this.position = position.cpy();
	}
	
	public void update(float deltaTime) {
		if (stateTime < 0 && stateTime + deltaTime > 0) {
			started = true;
		}
		stateTime += deltaTime;
	}
	
	public void render(SpriteBatch batch) {
		Sprite s = utils.getFrameUntilDone(Assets.playerExplode, stateTime, 30);
		if (s == null) return;
		utils.drawCenter(batch, s, position.x, position.y);
	}
	
	public boolean getStarted() {
		boolean $ = started;
		started = false;
		return $;
	}
	
	
}
