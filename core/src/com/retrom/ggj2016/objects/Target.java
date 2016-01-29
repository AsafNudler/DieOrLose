package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class Target extends GameObject {
	private boolean isTaken;
	
	private static final float TARGET_SIZE = 40;
	
	public Target(float x, float y) {
		super(x, y, TARGET_SIZE, TARGET_SIZE);
	}
	
	public void take() {
		isTaken = true;
	}
	
	public boolean taken() {
		return isTaken;
	}

	
	public void render(SpriteBatch batch) {
		Sprite s = !isTaken ? Assets.target : Assets.taken;
		utils.drawCenter(batch, s, position.x, position.y);
	}
}
