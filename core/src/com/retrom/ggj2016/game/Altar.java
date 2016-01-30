package com.retrom.ggj2016.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.objects.GameObject;
import com.retrom.ggj2016.utils.utils;

public class Altar extends GameObject {
	
	float stateTime = 0;
	boolean shown = false;
	
	private static float ALTAR_COLLIDER = 60f;
	private int level;
	public float master_alpha = 0;

	public Altar(int level) {
		super(0, 0, ALTAR_COLLIDER, ALTAR_COLLIDER);
		this.level = level;
		System.out.println("level="+level);
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
	}
	
	public void render(SpriteBatch batch) {
		if (!shown) {
			return;
		}
		Sprite base = Assets.altar;
		base.setAlpha(master_alpha);
		utils.drawCenter(batch, base, 0, 0);
		{
			Sprite s = Assets.altar_glow;
			s.setAlpha(1 * master_alpha);
			utils.drawCenter(batch, s, 0, 0);
			s.setAlpha(master_alpha * (float) (0.4f * (Math.sin(stateTime* 5) + 1)));
			utils.drawCenter(batch, s, 0, 0);
		}
		if (level == 0) {
			Sprite s = Assets.altar_ux;
			float diff = (float) Math.abs(Math.sin(stateTime * 3));
			s.setScale(0.8f + 0.2f * diff);
			s.setAlpha(1 * master_alpha);
			utils.drawCenter(batch, s, 0, 0);
		}
		
	}

	public void show() {
		if (shown) {
			return;
		}
		shown = true;
		stateTime = 0;
	}

}
