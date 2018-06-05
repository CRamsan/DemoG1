package com.cramsan.demog1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.cramsan.demog1.gameelements.GameParameterManager;
import com.cramsan.demog1.screen.*;

public class MyGdxGame extends Game {
    private static MyGdxGame ourInstance;
    private static GameParameterManager parameterManager;

    private static MyGdxGame getInstance() { return ourInstance; }

    private static boolean isFrameLimited() { return ourInstance.useFixedStep; }

    private static SpriteBatch getSpriteBatch() { return ourInstance.spriteBatch; }

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
                screen = new AssassinScreen(isFrameLimited(), getSpriteBatch(), MyGdxGame.parameterManager);
                break;
            case DEATH_RACE:
                screen = new DeathRaceScreen(isFrameLimited(), getSpriteBatch(), MyGdxGame.parameterManager);
                break;
            case NINJA_PARTY:
                screen = new NinjaPartyScreen(isFrameLimited(), getSpriteBatch(), MyGdxGame.parameterManager);
                break;
            case CATCH_A_THIEF:
                screen = new CatchAThiefScreen(isFrameLimited(), getSpriteBatch(), MyGdxGame.parameterManager);
                break;
            case KNIGHTS_VS_THIEFS:
                screen = new KnightsVsNinjasScreen(isFrameLimited(), getSpriteBatch(), MyGdxGame.parameterManager);
                break;
            default:
                throw new RuntimeException("Invalid game type");
        }
        startGameScreen(screen);
    }

    public static void startMainMenuScreen() {
        MyGdxGame.parameterManager = null;
        MainMenuScreen screen = new MainMenuScreen(isFrameLimited(), getSpriteBatch());
        screen.ScreenInit();
        getInstance().setScreen(screen);
    }

    private static void startGameScreen(GameScreen screen) {
        screen.ScreenInit();
        getInstance().setScreen(screen);
    }

    private boolean useFixedStep;
    private SpriteBatch spriteBatch;

    public MyGdxGame(boolean useFixedStep) {
        this(useFixedStep, null);
    }

    public MyGdxGame(boolean useFixedStep, SpriteBatch spriteBatch) {
        this.useFixedStep = useFixedStep;
        this.spriteBatch = spriteBatch;
    }

    @Override
    public void create() {
        ourInstance = this;
        if (this.spriteBatch == null) {
            spriteBatch = new SpriteBatch();
        }
        SingleAssetManager.initSingleAssetManager();
        Box2D.init();
        startMainMenuScreen();
    }

    @Override
    public void dispose () {
        SingleAssetManager.unInitSingleAssetManager();
    }
}
