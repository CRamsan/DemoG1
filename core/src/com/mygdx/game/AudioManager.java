package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Singleton class that provides a simple API to handle sound and music
 */
public class AudioManager {

    public enum MUSIC {

    }

    public enum SOUND {

    }

    /**
     * Call this function when loading a scene and provide the level Id int
     * This will load the required sounds as well as preperat the music playlist
     */
    public static void LoadAssets(int level)
    {
    }
    
    /**
     * Call this function to unload any assets for the current scene
     */
    public static void UnloadAssets()
    {
    }

    public static void PlaySound(SOUND sound) {
        ourInstance.PlaySoundInternal();
    }

    public static void PlayMusic() {
        ourInstance.PlayMusicInternal();
    }

    private static AudioManager ourInstance = new AudioManager();

    private void PlaySoundInternal() {
        Gdx.app.log(this.getClass().toString(), "Sound");
    }

    private void PlayMusicInternal() {
        Gdx.app.log(this.getClass().toString(), "Music");
    }
}
