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
	public static Array<Sprite> enemy;
	public static Sprite taken;
	public static Sprite logo;
	public static Sprite playerBodyKnife;
	
	public static Array<Sprite> playerWalkFront;
	public static Array<Sprite> playerWalkBack;

	public static Sprite centerGlow;

	public static Sprite targetWithCandle;
	public static Sprite targetNoCandle;
	
	public static Array<Sprite> blood;
	public static Array<Sprite> smallBlood;

	public static Array<Sprite> candle;
	public static Array<Sprite> candleGlow;

	public static Sprite candlePointNo;
	public static Sprite candlePointOff;
	public static Sprite candlePointOn;
	public static Sprite candlePointGlow;
	public static Sprite candlePointUI;
	public static Sprite candleBlank;
	
	public static Sprite lifeBarOver;
	public static Sprite lifeBarBg;

	public static Array<Sprite> enemyFire;
	
	public static Array<Sprite> slash;

	public static Sprite knifeFlare;
	public static Sprite heart;

	public static Array<Sprite> enemyFollower;
	public static Array<Sprite> enemyFollowerFire;
	public static Sprite enemyFollowerEyes;
	
	public static Sprite altar;
	public static Sprite altar_glow;
	public static Sprite altar_ux;

	public static Array<Sprite> playerExplode;

	public static Sprite playerHeadDies;
	public static Sprite playerBodyDies;

	public static Array<Sprite> playerComplete;
	
	public static Array<Sprite> fireParticles;

	public static Array<Sprite> candleFire;

	public static void init() {
		TextureAtlas basicSheet = new TextureAtlas("basic.txt");
		
		playerHead = basicSheet.createSprite("Player_head_front");
		playerHeadBack = basicSheet.createSprite("Player_head_back");
		playerFront = basicSheet.createSprite("Player_body_front");
		playerSide = basicSheet.createSprite("Player_body_side");
		playerBack = basicSheet.createSprite("Player_body_back");
		playerBodyKnife = basicSheet.createSprite("Player_body_front_knife");
		playerComplete = basicSheet.createSprites("Player_head_levelcomplete");
		
		enemy = basicSheet.createSprites("enemies_roamer_frame");
		enemyFire = basicSheet.createSprites("enemies_fire");
		taken = basicSheet.createSprite("taken");
		
		centerGlow = basicSheet.createSprite("center_glow");
		
		blood = basicSheet.createSprites("blood");
		smallBlood = basicSheet.createSprites("blood_small");
		
		candle = basicSheet.createSprites("items_candle");
		candleGlow = basicSheet.createSprites("items_candle_glow");
		
		candlePointNo = basicSheet.createSprite("items_candleslot_empty");
		candlePointOff = basicSheet.createSprite("items_candleslot_full");
		candlePointOn = basicSheet.createSprite("items_candleslot_light");
		candlePointGlow = basicSheet.createSprite("items_candleslot_light_glow");
		candlePointUI = basicSheet.createSprite("items_candleslot_UX");
		candleBlank = basicSheet.createSprite("items_candleslot_nocandle");
		
		candleFire = basicSheet.createSprites("candle_fire");
		
		lifeBarOver = basicSheet.createSprite("hud_healthbar");
		lifeBarBg = basicSheet.createSprite("hud_healthbar_bg");
		
		playerWalkFront = basicSheet.createSprites("Player_body_front_walking");
		playerWalkBack = basicSheet.createSprites("Player_body_back_walking");
		
		slash = basicSheet.createSprites("blood_slash");
		knifeFlare = basicSheet.createSprite("Player_body_front_knife_knifeflare");
		heart = basicSheet.createSprite("heart_icon");
		
		enemyFollower = basicSheet.createSprites("enemies_stalker_frame");
		enemyFollowerFire = basicSheet.createSprites("enemies_fire_green");
		enemyFollowerEyes = basicSheet.createSprite("enemies_stalker_eyesglow");
		
		altar = basicSheet.createSprite("exitaltar_base");
		altar_glow = basicSheet.createSprite("exitaltar_base_glow");
		altar_ux = basicSheet.createSprite("exitaltar_base_UX");
		
		playerHeadDies = basicSheet.createSprite("Player_head_front_die");
		playerBodyDies = basicSheet.createSprite("Player_body_front_die");
		
		fireParticles = basicSheet.createSprites("particles_burnpiece");
		
		TextureAtlas bgSheet = new TextureAtlas("bg.txt");
		bg = bgSheet.createSprite("bg");
		
		TextureAtlas explodeSheet = new TextureAtlas("explode.txt");
		playerExplode = explodeSheet.createSprites("blood_explotion");
		
		TextureAtlas titleSheet = new TextureAtlas("title.txt");
		logo = titleSheet.createSprite("title");
	}
}
