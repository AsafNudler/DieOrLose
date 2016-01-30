package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

public class CandlePoint extends GameObject {
	
	public enum State {
		NOCANDLE,
		OFF,
		ON;
	}
	
	public State state = State.NOCANDLE;

	public CandlePoint(float x, float y) {
		super(x, y, 20, 20);
	}
	
	public void render(SpriteBatch batch) {
		Sprite s = null;
		switch (state) {
		case NOCANDLE:
			s = Assets.candlePointNo;
			break;
		case OFF:
			s = Assets.candlePointOff;
			break;
		case ON:
			s = Assets.candlePointOn;
			break;
		
		}
		utils.drawCenter(batch, s, position.x, position.y);
		
		if (state == State.ON) {
			BatchUtils.setBlendFuncAdd(batch);
			utils.drawCenter(batch, Assets.candlePointGlow, position.x, position.y);
			BatchUtils.setBlendFuncNormal(batch);
		}
	}

	public void update(float deltaTime) {
		// TODO Auto-generated method stub
	}
	
	public void putCandle() {
		state = State.OFF;
	}
	
	public void turnOnCandle() {
		state = State.ON;
	}
	
}
