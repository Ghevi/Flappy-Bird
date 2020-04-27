package com.ghevi.flappybird.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ghevi.flappybird.GameMain;

import helpers.Gameinfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = (int) Gameinfo.WIDTH;
		config.height = (int) Gameinfo.HEIGHT;

		new LwjglApplication(new GameMain(), config);
	}
}
