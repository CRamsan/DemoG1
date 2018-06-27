package com.cramsan.demog1.subsystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

/**
 * Class that provides a simple API to handle sound and music
 */
public class AudioManager implements IGameSubsystem {

    public enum MUSIC {
        BG_1
    }

    public enum SOUND {
        ATTACK, BELL
    }

    private HashMap<MUSIC, Music> musicMap;
    private HashMap<SOUND, Sound> soundMap;
    private int level;

    @Override
    public void OnGameLoad() {
        musicMap = new HashMap<MUSIC, Music>();
        soundMap = new HashMap<SOUND, Sound>();
    }

    /**
     * Call this function when loading a scene.
     * This will load the required sounds as well as prepare the music playlist
     */
    @Override
    public void OnScreenLoad() {
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

    @Override
    public void OnLoopStart() {

    }

    @Override
    public void OnLoopEnd() {

    }


    /**
     * Call this function to unload any assets for the current scene
     */
    @Override
    public void OnScreenClose() {
        for (Sound sound : soundMap.values()) {
            sound.dispose();
        }
        for (Music music : musicMap.values()) {
            music.dispose();
        }
        soundMap.clear();
        musicMap.clear();
        soundMap = null;
        musicMap = null;
    }

    @Override
    public void OnGameClose() {

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private boolean PlaySound(SOUND sound) {
        return soundMap.get(sound).play() != -1;
    }

    private void PlayMusic() {
        musicMap.get(MUSIC.BG_1).play();
    }
}
