package com.cramsan.demog1.subsystems.controller;

import com.cramsan.demog1.gameelements.player.PlayerControllerAdapter;

/**
 * This interface is used to expose the events from the PlayerController. This interface
 * is usually used when the events are received by the ControllerListener and then
 * passed to this interface.
 */
public interface ExternalControllerListener {

	boolean buttonDown (PlayerController controller, PlayerControllerAdapter.INPUT buttonCode);

	boolean buttonUp (PlayerController controller, PlayerControllerAdapter.INPUT buttonCode);

	boolean axisMoved (PlayerController controller, PlayerControllerAdapter.AXIS axisCode, float value);
}
