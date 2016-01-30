package com.retrom.ggj2016.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

public class CandlePoint extends GameObject {
	
	public enum State {
		BLANK,
		NOCANDLE,
		OFF,
		ON;
	}
	
	public State state = State.NOCANDLE;
	private final Player player;
	private float stateTime = 0;
	private int level;

	public CandlePoint(float x, float y, Player player, int level, boolean withCandle) {
		super(x, y, 20, 20);
		this.player = player;
		this.level = level;
		if (!withCandle) {
			state = State.BLANK;
		}
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
		case BLANK:
			s = Assets.candleBlank;
			break;
		
		}
		utils.drawCenter(batch, s, position.x, position.y);
		
		if (state == State.ON) {
			BatchUtils.setBlendFuncAdd(batch);
			utils.drawCenter(batch, Assets.candlePointGlow, position.x, position.y);
			BatchUtils.setBlendFuncNormal(batch);
		}
		
		if (state == State.NOCANDLE && player.candle != null && level <= 0) {
			utils.drawCenter(batch, Assets.candlePointUI, position.x, (float) (position.y + 50 + 5 * Math.sin(stateTime * 6)));
		}
	}

	public void update(float deltaTime) {
		stateTime += deltaTime;
	}
	
	public void putCandle() {
		if (state != State.BLANK)
			state = State.OFF;
	}
	
	public void turnOnCandle() {
		if (state != State.BLANK)
			state = State.ON;
	}
	
}
