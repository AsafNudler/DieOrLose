package com.retrom.ggj2016.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class utils {
	public static void drawCenter(SpriteBatch batch, Sprite keyFrame, float x, float y) {
		batch.draw(keyFrame, x - keyFrame.getRegionWidth()/2, y - keyFrame.getRegionHeight()/2);
	}
}
