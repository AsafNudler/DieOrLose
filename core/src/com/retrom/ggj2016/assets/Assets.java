package com.retrom.ggj2016.assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class Assets {
	
	public static Sprite bg;
	
	public static Sprite playerHead;
	public static Sprite playerFront;
	public static Sprite playerSide;
	public static Sprite playerBack;
	public static Sprite enemy;
	public static Sprite taken;
	public static Sprite logo;

	public static Sprite centerGlow;

	public static Sprite targetWithCandle;
	public static Sprite targetNoCandle;
	
	public static Array<Sprite> blood;
	public static Array<Sprite> smallBlood;
	
	public static void init() {
		TextureAtlas basicSheet = new TextureAtlas("basic.txt");
		
		bg = basicSheet.createSprite("bg");
		
		playerHead = basicSheet.createSprite("Player_head_front");
		playerFront = basicSheet.createSprite("Player_body_front");
		playerSide = basicSheet.createSprite("Player_body_side");
		playerBack = basicSheet.createSprite("Player_body_back");
		
		enemy = basicSheet.createSprite("enemy");
		taken = basicSheet.createSprite("taken");
		
		logo = basicSheet.createSprite("title");
		
		centerGlow = basicSheet.createSprite("center_glow");
		
		blood = basicSheet.createSprites("blood");
		smallBlood = basicSheet.createSprites("blood_small");
	}
}
