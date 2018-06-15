package com.cramsan.demog1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cramsan.demog1.gameelements.GameParameterManager;
import com.cramsan.demog1.screen.*;

public class MyGdxGameManager {

    private static MyGdxGame ourInstance;
    private static GameParameterManager parameterManager;

    private static MyGdxGame getInstance() { return ourInstance; }

    public static void setInstance(MyGdxGame game) { ourInstance = game; }

    public static GameParameterManager getParameterManager() { return parameterManager; }

    public static void setParameterManager(GameParameterManager parameterManager) { MyGdxGameManager.parameterManager = parameterManager; }

    public static void startGameScreen(GameParameterManager parameterManager) {
        if (getInstance() == null) {
            throw new RuntimeException("Game instance is not net");
        }
        if (getParameterManager() == null) {
            throw new RuntimeException("GameParameterManager instance is not net");
        }

        if (MyGdxGameManager.getParameterManager() == null) {
            if (parameterManager != null)
                MyGdxGameManager.setParameterManager(parameterManager);
            else
                throw new RuntimeException("GameParameterManager not provided.");
        } else if (parameterManager != null)
            throw new RuntimeException("GameParameterManager already set and cannot be overriden");

        GameParameterManager manager = MyGdxGameManager.getParameterManager();

        GameScreen screen;
        switch (MyGdxGameManager.getParameterManager().getType()) {
            case ASSASSIN:
                screen = new AssassinScreen(manager);
                break;
            case DEATH_RACE:
                screen = new DeathRaceScreen(manager);
                break;
            case NINJA_PARTY:
                screen = new NinjaPartyScreen(manager);
                break;
            case CATCH_A_THIEF:
                screen = new CatchAThiefScreen(manager);
                break;
            case KNIGHTS_VS_THIEFS:
                screen = new KnightsVsNinjasScreen(manager);
                break;
            default:
                throw new RuntimeException("Invalid game type");
        }
        startGameScreen(screen);
    }

    public static void startMainMenuScreen() {
        if (getInstance() == null) {
            throw new RuntimeException("Game instance is not net");
        }

        MyGdxGameManager.setParameterManager(null);
        MainMenuScreen screen = new MainMenuScreen();
        screen.setBatch(ourInstance.getSpriteBatch());
        screen.setUseFixedStep(ourInstance.isUseFixedStep());
        screen.setRenderEnabled(ourInstance.isEnableRender());
        screen.ScreenInit();
        getInstance().setScreen(screen);
    }

    private static void startGameScreen(GameScreen screen) {
        if (getInstance() == null) {
            throw new RuntimeException("Game instance is not net");
        }
        screen.setBatch(ourInstance.getSpriteBatch());
        screen.setUseFixedStep(ourInstance.isUseFixedStep());
        screen.setRenderEnabled(ourInstance.isEnableRender());
        screen.ScreenInit();
        getInstance().setScreen(screen);
    }
}
