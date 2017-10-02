package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.character.BaseCharacter;
import com.mygdx.game.screen.MainMenuScreen;
import com.mygdx.game.screen.MyGdxBaseScreen;
import com.mygdx.game.screen.MyGdxScreen;

public class MyGdxGame extends Game {
    private static MyGdxGame ourInstance;

    private static MyGdxGame getInstance() { return ourInstance; }

    public static void startGameScreen() {
        startScreen(new MyGdxScreen());
    }

    public static void startMainMenuScreen() {
        startScreen(new MainMenuScreen());
    }

    private static void startScreen(MyGdxBaseScreen screen) {
        screen.ScreenInit();
        getInstance().setScreen(screen);
    }

    @Override
    public void create() {
        ourInstance = this;
        startMainMenuScreen();
    }
}
