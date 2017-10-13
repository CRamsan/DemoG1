package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.screen.MainMenuScreen;
import com.mygdx.game.screen.MyGdxBaseScreen;
import com.mygdx.game.screen.MyGdxScreen;

public class MyGdxGame extends Game {
    private static MyGdxGame ourInstance;

    private static MyGdxGame getInstance() { return ourInstance; }

    private static boolean isFrameLimited() { return ourInstance.isFrameLimited; }

    public static void startGameScreen() {
        startScreen(new MyGdxScreen(isFrameLimited()));
    }

    public static void startMainMenuScreen() {
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
