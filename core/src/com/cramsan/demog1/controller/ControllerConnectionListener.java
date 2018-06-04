package com.cramsan.demog1.controller;

/**
 * Implement this interface to receive the ControllerManager connected/disconnected
 * events. This interface will be implemented by Scenes to receive the notification
 * from the Controller Manager.
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