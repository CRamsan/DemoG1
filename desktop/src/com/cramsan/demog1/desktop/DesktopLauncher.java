package com.cramsan.demog1.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.cramsan.demog1.IGameStateListener;
import com.cramsan.demog1.MyGdxGame;
import com.cramsan.demog1.MyGdxGameManager;

public class DesktopLauncher {

	private static MyGdxGame game;

	public static void main (String[] arg) {
		startApplication(null);
	}

	public static void startApplication(IGameStateListener listener) {
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		//config.setWindowedMode(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
		//config.setResizable(false);
		game = new MyGdxGame();
		game.setUseFixedStep(true);
		if (listener != null) {
			game.setListener(listener);
		}
		MyGdxGameManager.setInstance(game);
		new Lwjgl3Application(game, configuration);

	}

	public static void stopApplication() {
		Gdx.app.exit();
	}
}
