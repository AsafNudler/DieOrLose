package com.retrom.ggj2016.utils;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BatchUtils {

	private BatchUtils() {}
	
	public static void setBlendFuncNormal(SpriteBatch batch) {
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void setBlendFuncAdd(SpriteBatch batch) {
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
	}

	public static void setBlendFuncScreen(SpriteBatch batch) {
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_COLOR);
	}
}
