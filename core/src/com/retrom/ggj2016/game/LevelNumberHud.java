package com.retrom.ggj2016.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

public class LevelNumberHud {
	private int level;
	
	private static final float Y = 480;

	public LevelNumberHud(int level) {
		this.level = level;
	}
	
	public void render(SpriteBatch batch) {
		if (level == 0) return;
		BatchUtils.setBlendFuncNormal(batch);
		utils.drawCenter(batch, Assets.stageNumberBg, 0, Y);
		Sprite numberSprite = Assets.stageNumberNum.get(level-1);
		utils.drawCenter(batch, numberSprite, 0, Y - 20);
	}
}
