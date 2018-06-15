package com.cramsan.demog1.core;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cramsan.demog1.MyGdxGame;
import com.cramsan.demog1.MyGdxGameManager;
import com.cramsan.demog1.gameelements.GameParameterManager;
import com.cramsan.demog1.screen.*;
import com.cramsan.demog1.ui.IUISystem;
import org.junit.Before;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class MyGdxGameManagerTest {

    @Before
    public void setUp() {
        MyGdxGameManager.clearInstance();
    }

    @org.junit.Test
    public void setInstanceTest() {
        assertNull(MyGdxGameManager.getInstance());
        MyGdxGame game = Mockito.mock(MyGdxGame.class);
        MyGdxGameManager.setInstance(game);
        assertNotNull(MyGdxGameManager.getInstance());
        assertEquals(game, MyGdxGameManager.getInstance());
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void setInstanceToNullTest() {
        MyGdxGameManager.setInstance(null);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void overrideParameterManagerTest() {
        MyGdxGame game = Mockito.mock(MyGdxGame.class);
        GameParameterManager parameterManager = GameParameterManager.parameterManagerForGameType(GameParameterManager.GameType.NINJA_PARTY);
        MyGdxGameManager.startGameScreen(parameterManager);
        MyGdxGameManager.startGameScreen(parameterManager);
    }


    @org.junit.Test
    public void startMainMenuScreenTest() {
        //Going to the main screen will reset the GameParameterManager.
        MyGdxGame game = new MyGdxGame();
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        game.setSpriteBatch(batch);
        IUISystem uiSystem = Mockito.mock(IUISystem.class);
        game.setUiSystem(uiSystem);
        MyGdxGameManager.setInstance(game);
        game.create();
        GameParameterManager parameterManager = GameParameterManager.parameterManagerForGameType(GameParameterManager.GameType.NINJA_PARTY);
        MyGdxGameManager.startGameScreen(parameterManager);
        assertEquals(parameterManager, MyGdxGameManager.getParameterManager());
        MyGdxGameManager.startMainMenuScreen();
        assertNull(MyGdxGameManager.getParameterManager());
        assertEquals(game.getScreen().getClass(), MainMenuScreen.class);
    }

    @org.junit.Test
    public void startGameScreenTest() {
        MyGdxGame game = new MyGdxGame();
        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        game.setSpriteBatch(batch);
        IUISystem uiSystem = Mockito.mock(IUISystem.class);
        game.setUiSystem(uiSystem);
        MyGdxGameManager.setInstance(game);
        game.create();

        // Test starting every game type
        GameParameterManager parameterManager;
        for (GameParameterManager.GameType type : GameParameterManager.GameType.values()) {
            parameterManager = GameParameterManager.parameterManagerForGameType(type);
            MyGdxGameManager.startGameScreen(parameterManager);
            assertEquals(parameterManager, MyGdxGameManager.getParameterManager());
            Class screenClass = null;
            switch (type) {
                case ASSASSIN:
                    screenClass = AssassinScreen.class;
                    break;
                case DEATH_RACE:
                    screenClass = DeathRaceScreen.class;
                    break;
                case NINJA_PARTY:
                    screenClass = NinjaPartyScreen.class;
                    break;
                case CATCH_A_THIEF:
                    screenClass = CatchAThiefScreen.class;
                    break;
                case KNIGHTS_VS_THIEFS:
                    screenClass = KnightsVsNinjasScreen.class;
                    break;
            }
            assertEquals(screenClass, game.getScreen().getClass());
            MyGdxGameManager.startMainMenuScreen();
        }
    }
}