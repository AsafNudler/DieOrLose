package com.retrom.ggj2016.assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
	
	public static Sprite bg;
	
	public static Sprite playerHead;
	public static Sprite playerFront;
	public static Sprite playerSide;
	public static Sprite playerBack;
	public static Sprite enemy;
	public static Sprite target;
	public static Sprite taken;
	
	public static void init() {
		TextureAtlas basicSheet = new TextureAtlas("basic.txt");
		
		bg = basicSheet.createSprite("bg");
		
		playerHead = basicSheet.createSprite("Player_head_front");
		playerFront = basicSheet.createSprite("Player_body_front");
		playerSide = basicSheet.createSprite("Player_body_side");
		playerBack = basicSheet.createSprite("Player_body_back");
		
		enemy = basicSheet.createSprite("enemy");
		target = basicSheet.createSprite("target");
		taken = basicSheet.createSprite("taken");
	}
}
