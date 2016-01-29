package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.utils;

public class CandlePoint extends GameObject {
	
	public boolean hasCandle = false;

	public CandlePoint(float x, float y) {
		super(x, y, 40, 40);
	}
	
	public void putCandle() {
		hasCandle = true;
	}
	
	public void render(SpriteBatch batch) {
		Sprite s = hasCandle ? Assets.targetWithCandle : Assets.targetNoCandle;
		
		utils.drawCenter(batch, s, position.x, position.y);
	}
	
}
