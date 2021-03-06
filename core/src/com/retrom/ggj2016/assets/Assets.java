package com.retrom.ggj2016.assets;

import com.badlogic.gdx.Gdx;
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
	public static Sprite lifeBarEyes;
	public static Sprite lifeBarFlare;

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

	public static Array<Sprite> dust;

	public static Sprite book;
	public static Sprite bookGlow;

	public static Sprite page;
	
	public static Sprite endNote;
	public static Sprite endNoteGlow;

	public static Sprite stageNumberBg;
	
	public static Array<Sprite> stageNumberNum;
	public static Sprite stageNumberPentagram;
	
	public static Sprite hatchUX;

	public static Array<Sprite> bones;
	
	public static void init() {
		TextureAtlas basicSheet = new TextureAtlas(Gdx.files.internal("img/basic.txt"));
		
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
		lifeBarEyes = basicSheet.createSprite("hud_healthbar_lowhealthglow");
		lifeBarFlare = basicSheet.createSprite("hud_healthbar_flare");
		
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
		
		book = basicSheet.createSprite("items_book");
		bookGlow = basicSheet.createSprite("items_book_glow");
		
		stageNumberBg = basicSheet.createSprite("hud_stagenumber_slot");
		stageNumberNum = basicSheet.createSprites("hud_stagenumber");
		stageNumberPentagram = basicSheet.createSprite("hud_stagenumber_final"); 
		
		hatchUX = basicSheet.createSprite("floorhatch_UX");
		
		bones = basicSheet.createSprites("Player_deadbones");
		
		TextureAtlas bgSheet = new TextureAtlas("img/bg.txt");
		bg = bgSheet.createSprite("bg");
		page = bgSheet.createSprite("items_book_UX");
		
		TextureAtlas noteSheet = new TextureAtlas("img/note.txt");
		endNote = noteSheet.createSprite("endingtext");
		endNoteGlow = noteSheet.createSprite("endingtext_glow");
		
		TextureAtlas explodeSheet = new TextureAtlas("img/explode.txt");
		playerExplode = explodeSheet.createSprites("blood_explotion");
		
		TextureAtlas titleSheet = new TextureAtlas("img/title.txt");
		logo = titleSheet.createSprite("title");
		
		TextureAtlas dustSheet1 = new TextureAtlas("img/dust1.txt");
		TextureAtlas dustSheet2 = new TextureAtlas("img/dust2.txt");
		TextureAtlas dustSheet3 = new TextureAtlas("img/dust3.txt");
		TextureAtlas dustSheet4 = new TextureAtlas("img/dust4.txt");
		dust = dustSheet1.createSprites("altar_dustwave");
		dust.addAll(dustSheet2.createSprites("altar_dustwave"));
		dust.addAll(dustSheet3.createSprites("altar_dustwave"));
		dust.addAll(dustSheet4.createSprites("altar_dustwave"));
	}
}
