package com.retrom.ggj2016.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ggj2016.assets.Assets;
import com.retrom.ggj2016.objects.Player;

public class World {
	
	private Player player = new Player(); 
	
	public void update(float deltaTime) {
		player.update(deltaTime);
	}
	
	public void render(SpriteBatch batch) {
		player.render(batch);
	}
}
