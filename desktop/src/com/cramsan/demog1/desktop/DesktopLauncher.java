package com.cramsan.demog1.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.cramsan.demog1.MyGdxGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		startApplication();
	}

	public static void startApplication(Lwjgl3ApplicationConfiguration configuration, boolean useFixedStep) {
		new Lwjgl3Application(new MyGdxGame(useFixedStep), configuration);
	}

	public static void startApplication() {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		//config.setWindowedMode(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
		//config.setResizable(false);
		startApplication(configuration, true);
	}

	public static void stopApplication() {
		Gdx.app.exit();
	}
}
