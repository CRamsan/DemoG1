package com.mygdx.game.controller;

/**
 * Implement this interface to recieve the ControllerManager connected/disconnected
 * events
 */
public interface ControllerConnectionListener {
    /**
     * Method called when a PlayerControllerAdapter is connected
     * @param port the assigned port number
     * @param controller the connected PlayerControllerAdapter
     */
    void onControllerConnected(int port, PlayerController controller);

    /**
     * Method called when a PlayerControllerAdapter is disconnected
     * @param port the assigned port number
     * @param controller the removed PlayerControllerAdapter
     */
    void onControllerDisconnected(int port, PlayerController controller);
}