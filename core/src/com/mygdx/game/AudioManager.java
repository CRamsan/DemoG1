package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/* 
 * Singleton class that provides a simple API to handle sound and music
 */
public class AudioManager {

    /*
     * Call this function when loading a scene and provide the level Id int
     * This will load the required sounds as well as preperat the music playlist
     */
    public void LoadAssets(int level)
    {
    }
    
    /*
     * Call this function to unload any assets for the current scene
     */
    public void UnloadAssets() 
    {
    }

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
