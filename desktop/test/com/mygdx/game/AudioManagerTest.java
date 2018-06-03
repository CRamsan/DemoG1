package com.mygdx.game;

import com.mygdx.game.desktop.MockedGameTest;

import static org.junit.Assert.assertTrue;

public class AudioManagerTest extends MockedGameTest {

    @org.junit.Test(expected = NullPointerException.class)
    public void playMusicAfterUnload() {
        AudioManager.LoadAssets(0);
        AudioManager.PlayMusic();
        AudioManager.UnloadAssets();
        AudioManager.PlaySound(AudioManager.SOUND.BELL);
    }

    @org.junit.Test
    public void playMusicAfterLoad() {
        AudioManager.LoadAssets(0);
        AudioManager.PlayMusic();
        AudioManager.UnloadAssets();
    }

    @org.junit.Test
    public void playSoundAfterLoad() {
        AudioManager.LoadAssets(0);
        assertTrue(AudioManager.PlaySound(AudioManager.SOUND.BELL));
        AudioManager.UnloadAssets();
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void playSoundWithoutLoad() {
        AudioManager.PlaySound(AudioManager.SOUND.BELL);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void playMusicWithoutLoad() {
        AudioManager.PlayMusic();
    }
}