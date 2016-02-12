package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class Hatch extends GameObject {
	
	private float stateTime = 0;

	public Hatch() {
		super(-357, 361, 40, 40);
	}
	
	public void render(SpriteBatch batch) {
		Sprite s = Assets.hatchUX;
		s.setAlpha((float) ((Math.sin(stateTime * 6) + 1)/4+0.5f) * Math.min(1,stateTime*3));
		utils.drawCenter(batch, s, position.x, position.y);
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
	}
}
