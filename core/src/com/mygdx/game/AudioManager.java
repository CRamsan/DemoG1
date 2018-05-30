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
        ATTACK, BELL
    }

    /**
     * Call this function when loading a scene and provide the level Id int
     * This will load the required sounds as well as prepare the music playlist
     */
    public static void LoadAssets(int level)
    {
        musicMap = new HashMap<MUSIC, Music>();
        soundMap = new HashMap<SOUND, Sound>();
        for (SOUND enumSound : SOUND.values()) {
            String filePath = null;
            switch (enumSound) {
                case ATTACK:
                     filePath = "knife-slash.ogg";
                    break;
                case BELL:
                    filePath = "bell.wav";
                    break;
            }
            if (filePath == null) {
                throw new RuntimeException("Sound file path was not initialized");
            }
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
            soundMap.put(enumSound, sound);
        }
        for (MUSIC enumMusic : MUSIC.values()) {
            String filePath = null;
            switch (enumMusic) {
                case BG_1:
                    filePath = "bg_music.wav";
                    break;
            }
            if (filePath == null) {
                throw new RuntimeException("Music file path was not initialized");
            }
            Music music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
            musicMap.put(enumMusic, music);
        }
    }
    
    /**
     * Call this function to unload any assets for the current scene
     */
    public static void UnloadAssets()
    {
        for (Sound sound : soundMap.values()) {
            sound.dispose();
        }
        for (Music music : musicMap.values()) {
            music.dispose();
        }
        soundMap = null;
        musicMap = null;
    }

    public static boolean PlaySound(SOUND sound) {
        return ourInstance.PlaySoundInternal(sound);
    }

    public static void PlayMusic() {
        ourInstance.PlayMusicInternal();
    }

    private static AudioManager ourInstance = new AudioManager();

    private boolean PlaySoundInternal(SOUND sound) {
        return soundMap.get(sound).play() != -1;
    }

    private void PlayMusicInternal() {
        musicMap.get(MUSIC.BG_1).play();
    }
}
