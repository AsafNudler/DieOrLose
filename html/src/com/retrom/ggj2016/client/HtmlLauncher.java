package com.retrom.ggj2016.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.retrom.ggj2016.ggj2016;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(800, 800);
	}

	@Override
	public ApplicationListener createApplicationListener() {
		return new ggj2016();
	}
}