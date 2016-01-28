package com.retrom.ggj2016.assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	
	public static Sprite player;
	
	public static void init() {
		TextureAtlas basicSheet = new TextureAtlas("basic.txt");
		player = basicSheet.createSprite("player");
	}
}
