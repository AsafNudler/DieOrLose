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
}
