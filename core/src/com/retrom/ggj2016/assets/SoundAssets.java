package com.retrom.ggj2016.assets;

import java.util.Random;

import javax.xml.stream.events.StartDocument;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundAssets {
	
	public static boolean soundEnabled = true;
	
	public static Sound gameMusic;
	public static long musicid = 0;
	
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
	
	public static Sound levelCompleteFinal;
	public static Sound hatchOpen;
	
	public static Sound bookOpen;
	public static Sound bookClose;
	
	public static Sound bloodSteps;
	public static long bloodStepsId = 0;
	
	private static Sound heartBeat;
	private static long heartBeatId = 0;
	
	private static Random rand = new Random();

	public static void load() {
		gameMusic = Gdx.audio.newSound(Gdx.files.internal("sound/music.wav"));
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
		
		bookOpen = newSound("book_open.wav");
		bookClose = newSound("book_close.wav");
		
		bloodSteps = newSound("bloody_steps.wav");
		heartBeat = newSound("player_abouttodieloop.wav");
		
		enemyHit = new Sound[] { newSound("hit_enemies_1.wav"),
				newSound("hit_enemies_2.wav"),
				newSound("hit_enemies_3.wav"),
				newSound("hit_enemies_4.wav"),
				newSound("hit_enemies_5.wav") };
		
		levelCompleteFinal = newSound("level_final_complete.wav");
		hatchOpen = newSound("level_final_floorhatch.wav");
	}
	
	public static void pauseMusic() {
		gameMusic.pause();
	}
	public static void resumeMusic() {
		if (!soundEnabled) return;
		if (musicid == 0) {
			startMusic();
		} else {
			gameMusic.resume();
		}
	}
	
	public static void toggleSound() {
		if (soundEnabled) {
			soundEnabled = false;
			stopBloodSteps();
			stopHeartBeat();
			pauseMusic();
			opening.stop();
		} else {
			soundEnabled = true;
			resumeMusic();
			startHeartBeat();
			playBloodSteps();
		}
	}
	
	public static void playSound (Sound sound) {
		sound.play(1);
		if (!soundEnabled)
			sound.pause();
	}
	
	public static void playRandomSound(Sound[] sounds) {
		int index = rand.nextInt(sounds.length);
		playSound(sounds[index]);
	}
	
	private static Sound newSound(String filename) {
		return Gdx.audio.newSound(Gdx.files.internal("sound/"+filename));
	}

	public static void loopSound(Sound sound) {
		if (!soundEnabled) return;
		sound.stop();
		sound.loop(1);
	}
	
	public static void stopSound(Sound sound) {
		sound.stop();
	}
	
	public static void startMusic() {
		gameMusic.stop();
		if (!soundEnabled) return;
		musicid = gameMusic.play();
		gameMusic.setLooping(musicid, true);
	}
	
	public static void stopMusic() {
		gameMusic.stop();
	}

	public static void stopBloodSteps() {
		bloodSteps.stop();
	}
	
	public static void playBloodSteps() {
		if (!soundEnabled) return;
		bloodSteps.stop();
		bloodStepsId = bloodSteps.play();
		bloodSteps.setLooping(bloodStepsId, true);
	}
	
	public static void setBloodStepsVolume(float volume) {
		bloodSteps.setVolume(bloodStepsId, volume);
	}

	public static void startHeartBeat() {
		if (!soundEnabled) return;
		stopHeartBeat();
		heartBeatId = heartBeat.play();
		heartBeat.setLooping(heartBeatId, true);
	}
	public static void stopHeartBeat() {
		heartBeat.stop();
	}
}
