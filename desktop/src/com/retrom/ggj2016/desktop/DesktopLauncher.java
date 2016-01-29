package com.retrom.ggj2016.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.retrom.ggj2016.GGJ2016;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = config.height = 800;
		new LwjglApplication(new GGJ2016(), config);
	}
}
