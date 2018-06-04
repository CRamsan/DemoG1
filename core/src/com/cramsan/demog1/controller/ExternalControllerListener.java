package com.cramsan.demog1.controller;

/**
 * This interface is used to expose the events from the PlayerController. This interface
 * is usually used when the events are received by the ControllerListener and then
 * passed to this interface.
 */
public interface ExternalControllerListener {

	boolean buttonDown (PlayerController controller, int buttonCode);

	boolean buttonUp (PlayerController controller, int buttonCode);

	boolean axisMoved (PlayerController controller, int axisCode, float value);
}
