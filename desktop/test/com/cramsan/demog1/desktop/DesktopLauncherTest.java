package com.cramsan.demog1.desktop;

import com.badlogic.gdx.Gdx;
import com.cramsan.demog1.IGameStateListener;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.Semaphore;

public class DesktopLauncherTest {

    private static Semaphore sem = new Semaphore(0);

    @org.junit.Before
    public void setUp() {
        new Thread(() -> {
            try {
                sem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("Exception thrown while acquiring semaphore");
            }
            Gdx.app.exit();
        }).start();
    }

    @Test(timeout=5000)
    public void main() {
        DesktopLauncher launcher = new DesktopLauncher();
        launcher.startApplication(new TestLifeCycleListener());
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception thrown while acquiring semaphore");
        }
    }

    @After
    public void tearDown() { }

    static class TestLifeCycleListener implements IGameStateListener {

        @Override
        public void onGameCreated() {
            sem.release();
        }

        @Override
        public void onGameDestroyed() {
            sem.release();
        }
    }
}