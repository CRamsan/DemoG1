package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

    public void PlaySound() {
        ourInstance.PlaySoundInternal();
    }

    public void PlayMusic() {
        ourInstance.PlayMusicInternal();
    }

    private static AudioManager ourInstance = new AudioManager();

    private void PlaySoundInternal() {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("data/mysound.mp3"));
        sound.play();
        sound.dispose();
    }

    private void PlayMusicInternal() {

    }
}
