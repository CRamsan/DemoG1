package com.cramsan.demog1.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cramsan.demog1.IGameStateListener;
import com.cramsan.demog1.MyGdxGame;
import com.cramsan.demog1.subsystems.ui.IUISystem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.Mockito;

import java.util.concurrent.Semaphore;

public class MockedGameTest {

    private static Application application;
    private static Semaphore sem = new Semaphore(0);;

    @BeforeClass
    public static void oneTimeSetUp() {
        Gdx.gl30 = Mockito.mock(GL30.class);
        Gdx.gl20 = Gdx.gl30;
        Gdx.gl = Gdx.gl20;

        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        IUISystem uiSystem = Mockito.mock(IUISystem.class);

        // Start the semaphore with 0 so we can stall until the listener
        // signals that we can move forward.

        MyGdxGame game = new MyGdxGame();
        game.setUseFixedStep(true);
        game.setSpriteBatch(batch);
        game.setUiSystem(uiSystem);
        game.setEnableGame(false);
        game.setEnableRender(false);
        game.setListener(new TestLifeCycleListener());

        application = new HeadlessApplication(game);
        try {
            waitThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
            closeAndCleanUp();
        }
    }

    @AfterClass
    public static void oneTimeTearDown() {
        closeAndCleanUp();
        try {
            waitThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void closeAndCleanUp() {
        application.exit();
        application = null;
    }
    public static void waitThread() throws InterruptedException {
        sem.acquire();
    }

    public static void signalThread() {
        sem.release();
    }

    static class TestLifeCycleListener implements IGameStateListener {

        @Override
        public void onGameCreated() {
            signalThread();
        }

        @Override
        public void onGameDestroyed() {
            signalThread();
        }
    }
}