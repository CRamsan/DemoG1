package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.gameelements.GameParameterManager;
import com.mygdx.game.screen.MainMenuScreen;
import com.mygdx.game.screen.MyGdxBaseScreen;
import com.mygdx.game.screen.MyGdxScreen;

public class MyGdxGame extends Game {
    private static MyGdxGame ourInstance;
    private static GameParameterManager parameterManager;

    private static MyGdxGame getInstance() { return ourInstance; }

    private static boolean isFrameLimited() { return ourInstance.isFrameLimited; }

    public static void startGameScreen(GameParameterManager parameterManager) {
        if (MyGdxGame.parameterManager == null) {
            if (parameterManager != null)
                MyGdxGame.parameterManager = parameterManager;
            else
                throw new RuntimeException("GameParameterManager not provided.");
        } else if (parameterManager != null)
            throw new RuntimeException("GameParameterManager already set and cannot be overriden");

        startScreen(new MyGdxScreen(isFrameLimited(), parameterManager) );
    }

    public static void startMainMenuScreen() {
        MyGdxGame.parameterManager = null;
        startScreen(new MainMenuScreen(isFrameLimited()));
    }

    private static void startScreen(MyGdxBaseScreen screen) {
        screen.ScreenInit();
        getInstance().setScreen(screen);
    }

    private boolean isFrameLimited;

    public MyGdxGame(boolean isFrameLimited) {
        this.isFrameLimited = isFrameLimited;
    }

    @Override
    public void create() {
        ourInstance = this;
        startMainMenuScreen();
    }
}
