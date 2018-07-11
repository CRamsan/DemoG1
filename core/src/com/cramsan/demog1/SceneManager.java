package com.cramsan.demog1;

import com.cramsan.demog1.screen.*;

public class SceneManager {

    private static MyGdxGame game;

    public static void setGame(MyGdxGame game) {
        if (game == null) {
            throw new RuntimeException("Game parameter is null");
        }
        if (SceneManager.game != null) {
            throw new RuntimeException("Game already set");
        }
        SceneManager.game = game;
    }

    public static MyGdxGame getGame() {
        return game;
    }

    public static void clearGame() {
        if (SceneManager.game == null) {
            throw new RuntimeException("Game was not set");
        }
        SceneManager.game = null;
    }

    private static GameParameterManager parameterManager;

    public static void startGameScreen(GameParameterManager newParameterManager) {
        if (game == null) {
            throw new RuntimeException("Game parameter is null");
        }

        if (parameterManager == null) {
            if (newParameterManager != null)
                parameterManager = newParameterManager;
            else
                throw new RuntimeException("GameParameterManager not provided.");
        } else if (newParameterManager != null) {
            throw new RuntimeException("GameParameterManager already set and cannot be overridden");
        }

        GameScreen screen;
        switch (parameterManager.getType()) {
            case ASSASSIN:
                screen = new AssassinScreen(parameterManager);
                break;
            case DEATH_RACE:
                screen = new DeathRaceScreen(parameterManager);
                break;
            case NINJA_PARTY:
                screen = new NinjaPartyScreen(parameterManager);
                break;
            case CATCH_A_THIEF:
                screen = new CatchAThiefScreen(parameterManager);
                break;
            case KNIGHTS_VS_THIEVES:
                screen = new KnightsVsNinjasScreen(parameterManager);
                break;
            default:
                throw new RuntimeException("Invalid game type");
        }
        game.setScreen(screen);
    }

    public static void startMainMenuScreen() {
        if (game == null) {
            throw new RuntimeException("Game parameter is null");
        }
        parameterManager = null;
        MainMenuScreen screen = new MainMenuScreen();
        game.setScreen(screen);
    }
}
