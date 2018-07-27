package com.cramsan.demog1.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.cramsan.demog1.IGameStateListener;
import com.cramsan.demog1.MyGdxGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		new DesktopLauncher().startApplication(null);
	}

	public void startApplication(IGameStateListener listener) {
		MyGdxGame game = new MyGdxGame();
		Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
		//configuration.setWindowedMode(426, 240);
		//config.setResizable(false);
		if (listener != null) {
			game.setListener(listener);
		}
		game.setEnableBox2dRender(true);
		new Lwjgl3Application(game, configuration);
	}
}
