package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

/**
 * Singleton class that provides a simple API to handle sound and music
 */
public class AudioManager {

    private static HashMap<MUSIC, Music> musicMap;
    private static HashMap<SOUND, Sound> soundMap;

    public enum MUSIC {
        BG_1
    }

    public enum SOUND {
        ATTACK
    }

    /**
     * Call this function when loading a scene and provide the level Id int
     * This will load the required sounds as well as preperat the music playlist
     */
    public static void LoadAssets(int level)
    {
        musicMap = new HashMap<MUSIC, Music>();
        soundMap = new HashMap<SOUND, Sound>();
        Music music = Gdx.audio.newMusic(Gdx.files.internal("bg_music.wav"));
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("knife-slash.ogg"));
        musicMap.put(MUSIC.BG_1, music);
        soundMap.put(SOUND.ATTACK, sound);
    }
    
    /**
     * Call this function to unload any assets for the current scene
     */
    public static void UnloadAssets()
    {
        soundMap.get(SOUND.ATTACK).dispose();
        musicMap.get(MUSIC.BG_1).dispose();
        soundMap = null;
        musicMap = null;
    }

    public static void PlaySound(SOUND sound) {
        ourInstance.PlaySoundInternal();
    }

    public static void PlayMusic() {
        ourInstance.PlayMusicInternal();
    }

    private static AudioManager ourInstance = new AudioManager();

    private void PlaySoundInternal() {
        soundMap.get(SOUND.ATTACK).play();
    }

    private void PlayMusicInternal() {
        musicMap.get(MUSIC.BG_1).play();
    }
}
