package com.cramsan.demog1.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cramsan.demog1.MyGdxGame;
import com.cramsan.demog1.ui.IUISystem;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.Mockito;

public class MockedGameTest {

    private static Application application;

    @BeforeClass
    public static void oneTimeSetUp() {
        Gdx.gl30 = Mockito.mock(GL30.class);
        Gdx.gl20 = Gdx.gl30;
        Gdx.gl = Gdx.gl20;

        SpriteBatch batch = Mockito.mock(SpriteBatch.class);
        IUISystem uiSystem = Mockito.mock(IUISystem.class);

        MyGdxGame game = new MyGdxGame(true, batch, uiSystem, false, false);
        application = new HeadlessApplication(game);
        application.addLifecycleListener(new TestLifeCycleListener());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception thrown waiting for background thread");
        }
    }

    @AfterClass
    public static void oneTimeTearDown() {
        Runnable r = () -> {
            try {
                waitThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("Exception thrown while waiting");
            }
        };
        Thread backgroundTask = new Thread(r);
        backgroundTask.start();

        try {
            Thread.sleep(500);
            application.exit();
            backgroundTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception thrown waiting for background thread");
        }

        application = null;
    }

    public static void waitThread() throws InterruptedException {
        synchronized (application) {
            application.wait();
        }
    }

    public static void signalThread() {
        synchronized (application) {
            application.notify();
        }
    }

    static class TestLifeCycleListener implements LifecycleListener {

        @Override
        public void pause() {}

        @Override
        public void resume() {}

        @Override
        public void dispose() {
            signalThread();
        }
    }
}