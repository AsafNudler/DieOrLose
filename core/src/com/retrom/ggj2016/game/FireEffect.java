package com.retrom.ggj2016.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.retrom.ggj2016.assets.Assets;

/**
 * Created by Asaf on 30/01/2016.
 */
public class FireEffect {
    public Vector2 pos;
    public Sprite ass = Assets.fireParticles.random();
    public float ySpeed;
    public float xCurrElement;
    public float alpha = 0;
    public int frames = 0;
    public float scale = (float)Math.random() * 0.5f + 0.5f;
    public float rotationRate = (float) (Math.random() * 18 - 9);
	public float lifetime = (float) (Math.random() * 2 + 1);
	public float amplitude = (float) (Math.random() * 9);
	public float phase = (float) (Math.random() * Math.PI * 2);
}
