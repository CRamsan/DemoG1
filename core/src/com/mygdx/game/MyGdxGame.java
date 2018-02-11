package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.gameelements.GameParameterManager;
import com.mygdx.game.screen.*;

public class MyGdxGame extends Game {
    private static MyGdxGame ourInstance;
    private static GameParameterManager parameterManager;

    private static MyGdxGame getInstance() { return ourInstance; }

    private static boolean isFrameLimited() { return ourInstance.useFixedStep; }

    public static void startGameScreen(GameParameterManager parameterManager) {
        if (MyGdxGame.parameterManager == null) {
            if (parameterManager != null)
                MyGdxGame.parameterManager = parameterManager;
            else
                throw new RuntimeException("GameParameterManager not provided.");
        } else if (parameterManager != null)
            throw new RuntimeException("GameParameterManager already set and cannot be overriden");

        GameScreen screen;
        switch (MyGdxGame.parameterManager.getType()) {
            case ASSASSIN:
                screen = new AssassinScreen(isFrameLimited(), MyGdxGame.parameterManager);
                break;
            case DEATH_RACE:
                screen = new DeathRaceScreen(isFrameLimited(), MyGdxGame.parameterManager);
                break;
            case NINJA_PARTY:
                screen = new NinjaPartyScreen(isFrameLimited(), MyGdxGame.parameterManager);
                break;
            case CATCH_A_THIEF:
                screen = new CatchAThiefScreen(isFrameLimited(), MyGdxGame.parameterManager);
                break;
            case KNIGHTS_VS_THIEFS:
                screen = new KnightsVsThiefsScreen(isFrameLimited(), MyGdxGame.parameterManager);
                break;
            default:
                throw new RuntimeException("Invalid game type");
        }
        startGameScreen(screen);
    }

    public static void startMainMenuScreen() {
        MyGdxGame.parameterManager = null;
        MainMenuScreen screen = new MainMenuScreen(isFrameLimited());
        screen.ScreenInit();
        getInstance().setScreen(screen);
    }

    private static void startGameScreen(GameScreen screen) {
        screen.ScreenInit();
        getInstance().setScreen(screen);
    }

    private boolean useFixedStep;

    public MyGdxGame(boolean useFixedStep) {
        this.useFixedStep = useFixedStep;
    }

    @Override
    public void create() {
        ourInstance = this;
        startMainMenuScreen();
    }
}
