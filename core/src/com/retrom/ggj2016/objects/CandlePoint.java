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
	private final Player player;
	private float stateTime = 0;

	public CandlePoint(float x, float y, Player player) {
		super(x, y, 20, 20);
		this.player = player;
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
		
		if (state == State.NOCANDLE && player.candle != null) {
			utils.drawCenter(batch, Assets.candlePointUI, position.x, (float) (position.y + 50 + 5 * Math.sin(stateTime * 6)));
		}
	}

	public void update(float deltaTime) {
		stateTime += deltaTime;
	}
	
	public void putCandle() {
		state = State.OFF;
	}
	
	public void turnOnCandle() {
		state = State.ON;
	}
	
}
