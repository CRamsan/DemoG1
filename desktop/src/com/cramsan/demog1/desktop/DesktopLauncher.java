package com.cramsan.demog1.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.cramsan.demog1.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		//config.setWindowedMode(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
		//config.setResizable(false);
		new Lwjgl3Application(new MyGdxGame(true), config);
	}
}