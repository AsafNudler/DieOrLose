package com.retrom.ggj2016.assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class Assets {
	
	public static Sprite bg;
	
	public static Sprite playerHead;
	public static Sprite playerHeadBack;
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

	public static Array<Sprite> candle;

	public static Sprite candlePointNo;
	public static Sprite candlePointOff;
	public static Sprite candlePointOn;
	public static Sprite candlePointGlow;
	
	public static Sprite lifeBarOver;
	public static Sprite lifeBarBg;
	
	public static void init() {
		TextureAtlas basicSheet = new TextureAtlas("basic.txt");
		
		bg = basicSheet.createSprite("bg");
		
		playerHead = basicSheet.createSprite("Player_head_front");
		playerHeadBack = basicSheet.createSprite("Player_head_back");
		playerFront = basicSheet.createSprite("Player_body_front");
		playerSide = basicSheet.createSprite("Player_body_side");
		playerBack = basicSheet.createSprite("Player_body_back");
		
		enemy = basicSheet.createSprite("enemies_roamer_frame1");
		taken = basicSheet.createSprite("taken");
		
		logo = basicSheet.createSprite("title");
		
		centerGlow = basicSheet.createSprite("center_glow");
		
		blood = basicSheet.createSprites("blood");
		smallBlood = basicSheet.createSprites("blood_small");
		
		candle = basicSheet.createSprites("items_candle");
		
		candlePointNo = basicSheet.createSprite("items_candleslot_empty");
		candlePointOff = basicSheet.createSprite("items_candleslot_full");
		candlePointOn = basicSheet.createSprite("items_candleslot_light");
		candlePointGlow = basicSheet.createSprite("items_candleslot_light_glow");
		
		lifeBarOver = basicSheet.createSprite("hud_healthbar");
		lifeBarBg = basicSheet.createSprite("hud_healthbar_bg");
	}
}
