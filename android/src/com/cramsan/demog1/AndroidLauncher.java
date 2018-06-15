package com.cramsan.demog1;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.FixedResolutionStrategy;
import com.cramsan.demog1.Globals;
import com.cramsan.demog1.MyGdxGame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.resolutionStrategy = new FixedResolutionStrategy(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
		MyGdxGame game = new MyGdxGame();
		game.setUseFixedStep(true);
		initialize(game, config);
	}
}
