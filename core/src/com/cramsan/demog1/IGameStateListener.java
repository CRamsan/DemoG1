package com.cramsan.demog1;

/**
 * IGameListener allows the caller to listen for Game-wide events. This event are related to gameplay
 * or screen changes and should not be used for any porpose other than testing.
 */
public interface IGameStateListener {

    /**
     * Called when the create method from MyGdxGame has completed.
     */
    void onGameCreated();

    /**
     * Event called after the destroyed method is called.
     */
    void onGameDestroyed();

}
