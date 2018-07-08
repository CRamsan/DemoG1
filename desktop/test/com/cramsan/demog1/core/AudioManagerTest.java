package com.cramsan.demog1.core;

import com.cramsan.demog1.subsystems.AudioManager;

public class AudioManagerTest extends MockedGameTest {

    @org.junit.Test(expected = NullPointerException.class)
    public void playSoundWithoutLoad() {
        AudioManager manager = new AudioManager();
        manager.PlaySound(AudioManager.SOUND.ATTACK);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void playMusicWithoutLoad() {
        AudioManager manager = new AudioManager();
        manager.PlayMusic();
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void playSoundAfterGameLoad() {
        AudioManager manager = new AudioManager();
        manager.OnGameLoad();
        manager.PlaySound(AudioManager.SOUND.ATTACK);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void playMusicAfterGameLoad() {
        AudioManager manager = new AudioManager();
        manager.OnGameLoad();
        manager.PlayMusic();
    }

    @org.junit.Test
    public void playSoundAfterScreenLoad() {
        AudioManager manager = new AudioManager();
        manager.OnGameLoad();
        manager.OnScreenLoad();
        manager.PlaySound(AudioManager.SOUND.ATTACK);
    }

    @org.junit.Test
    public void playMusicAfterScreenLoad() {
        AudioManager manager = new AudioManager();
        manager.OnGameLoad();
        manager.OnScreenLoad();
        manager.PlayMusic();
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void playSoundAfterScreenUnload() {
        AudioManager manager = new AudioManager();
        manager.OnGameLoad();
        manager.OnScreenLoad();
        manager.OnScreenClose();
        manager.PlaySound(AudioManager.SOUND.ATTACK);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void playMusicAfterScreenUnload() {
        AudioManager manager = new AudioManager();
        manager.OnGameLoad();
        manager.OnScreenLoad();
        manager.OnScreenClose();
        manager.PlayMusic();
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void playSoundAfterGameUnload() {
        AudioManager manager = new AudioManager();
        manager.OnGameLoad();
        manager.OnScreenLoad();
        manager.OnScreenClose();
        manager.OnGameClose();
        manager.PlaySound(AudioManager.SOUND.ATTACK);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void playMusicAfterGameUnload() {
        AudioManager manager = new AudioManager();
        manager.OnGameLoad();
        manager.OnScreenLoad();
        manager.OnScreenClose();
        manager.OnGameClose();
        manager.PlayMusic();
    }
}