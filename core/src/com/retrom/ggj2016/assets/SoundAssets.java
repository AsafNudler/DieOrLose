package com.retrom.ggj2016.assets;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundAssets {
	
	public static Music gameMusic;
	public static long musicid;
	
	public static Sound flamethrowerEnd;
	
	public static Sound opening;
	public static Sound lineComplete;
	public static Sound levelComplete;
	public static Sound bloodSlashes;
	public static Sound bloody_steps;
	public static Sound candlePick;
	public static Sound candlePlace;
	public static Sound playerDie;
	public static Sound exitAppear;
	public static Sound[] enemyHit;
	
	
	private static Random rand = new Random();

	public static void load() {
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music.wav"));
		gameMusic.setLooping(true);
//		music.setVolume(0.5f);
//		if (Settings.soundEnabled) music.play();
		
		opening = newSound("opening.wav");
		lineComplete = newSound("line_complete.wav");
		levelComplete = newSound("level_complete.wav");
		bloodSlashes = newSound("blood_slashes.wav");
		
		candlePick = newSound("collect_candle.wav");
		candlePlace = newSound("place_candle.wav");
		playerDie = newSound("player_die.wav");
		exitAppear = newSound("exit_appearing.wav");
		
//		blood_slashes = newSound("blood_slashes.wav");
//		bloody_steps = newSound("bloody_steps.wav");
		
//		playerJump = new Sound[] {newSound("player_jump_0a.wav"), newSound("player_jump_0b.wav")};
//		playerJumpIntense = new Sound[] {newSound("player_jump_1.wav"), newSound("player_jump_2.wav"), newSound("player_jump_3.wav"), newSound("player_jump_4.wav")};
//		
//				
//		shopClick = newSound("menus_shop_click.wav");
	}
	
	public static void playSound (Sound sound) {
//		if (!Settings.soundEnabled)
//			return 0;
		sound.play(1);
	}
	
	public static void playRandomSound(Sound[] sounds) {
		int index = rand.nextInt(sounds.length);
		playSound(sounds[index]);
	}
	
	private static Sound newSound(String filename) {
		return Gdx.audio.newSound(Gdx.files.internal(filename));
	}

	public static void loopSound(Sound sound) {
		sound.stop();
		sound.loop(1);
	}
	
	public static void stopSound(Sound sound) {
		sound.stop();
	}
	

	public static void startMusic() {
		gameMusic.stop();
		gameMusic.play();
	}

	public static void pauseMusic() {
		gameMusic.pause();
	}
	
	public static void resumeMusic() {
		gameMusic.play();
	}
	
	public static void resumeMusicAt(float position) {
		gameMusic.setPosition(position);
		gameMusic.play();
	}
}
