package com.retrom.ggj2016.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.assets.SoundAssets;
import com.retrom.ggj2016.objects.GameObject;
import com.retrom.ggj2016.utils.BatchUtils;
import com.retrom.ggj2016.utils.utils;

public class Altar extends GameObject {
	
	float stateTime = 0;
	boolean shown = false;
	boolean reallyShown = false;
	
	float timeBeforeShow = 0.5f;
	
	private static float ALTAR_COLLIDER = 60f;
	private int level;
	public float master_alpha = 0;

	public Altar(int level) {
		super(0, 0, ALTAR_COLLIDER, ALTAR_COLLIDER);
		this.level = level;
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
		if (!shown) return;
		if (shown && timeBeforeShow > 0) {
			timeBeforeShow -= deltaTime;
			return;
		}
		reallyShow();
	}
	
	public void render(SpriteBatch batch) {
		if (!reallyShown) {
			return;
		}
		
		float dustTime = stateTime - 0.6f;
		Sprite ds = utils.getFrameUntilDone(Assets.dust, dustTime, 30);
		if (ds != null) {
			BatchUtils.setBlendFuncScreen(batch);
			utils.drawCenter(batch, ds, 0, 0);
			BatchUtils.setBlendFuncNormal(batch);
		}
		
		
		master_alpha = Math.min(1, stateTime * 3);
		float rotation;
		{
			float t = Math.min(1, Math.max(0, stateTime * 2));
			rotation = - 15f * (1 - t)*(1 - t);
//			Math.min(0, (stateTime * 80 - 60));
		}
		Sprite base = Assets.altar;
		base.setAlpha(master_alpha);
		base.setRotation(rotation);
		utils.drawCenter(batch, base, 0, 0);
		{
			Sprite s = Assets.altar_glow;
			float a = Math.min(1, Math.max(0, stateTime * 3 - 0.66f));
			s.setAlpha(a * master_alpha);
			s.setRotation(rotation);
			utils.drawCenter(batch, s, 0, 0);
			if (stateTime > 1f) {
				s.setAlpha(master_alpha * (float) (0.4f * (-Math.cos((stateTime - 1f)* 5) + 1)));
				s.setRotation(rotation);
				utils.drawCenter(batch, s, 0, 0);
			}
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
	}
	
	private void reallyShow() {
		if (reallyShown) {
			return;
		}
		reallyShown = true;
		SoundAssets.playSound(SoundAssets.exitAppear);
		stateTime = 0;
	}

}
