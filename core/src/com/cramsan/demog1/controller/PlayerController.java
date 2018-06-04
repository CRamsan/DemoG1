package com.cramsan.demog1.controller;

import com.badlogic.gdx.controllers.Controller;

/**
 * Interface that complements the Controller interface by adding two methods:
 *  supportsEvents and getControllerIndex.This interface will be the public interface
 *  so the game logic does not have to interact directly with Controller objects
 */
public interface PlayerController extends Controller {

    /**
     * Some controllers can produce events while other need to be polled for their state.
     * This method will expose the capabilities of the controller implementation.
     * @return true is the caller can register to listen for events. If false then the
     * caller will need to poll the state on every frame.
     */
    boolean supportsEvents();

    /**
     * Each PlayerControllerAdapter will have an index. This will represents the player number.
     * @return the index of the controller
     */
    int getControllerIndex();
}