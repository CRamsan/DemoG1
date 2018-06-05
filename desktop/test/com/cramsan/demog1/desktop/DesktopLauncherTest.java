package com.cramsan.demog1.desktop;

import org.junit.After;
import org.junit.Test;

public class DesktopLauncherTest {

    @Test(timeout=8000)
    public void main() {
        Runnable r = () -> {
            try {
                Thread.sleep(4500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("Exception thrown while waiting");
            }
            DesktopLauncher.stopApplication();
        };
        Thread backgroundTask = new Thread(r);
        backgroundTask.start();
        DesktopLauncher.main(null);
        try {
            backgroundTask.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception thrown waiting for background thread");
        }
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(1000);
    }
}