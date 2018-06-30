package com.cramsan.demog1.gameelements.player;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.cramsan.demog1.subsystems.controller.ExternalControllerListener;
import com.cramsan.demog1.subsystems.controller.PlayerController;

import java.util.HashMap;

public class PlayerControllerAdapter implements ExternalControllerListener, ControllerListener {

	private PlayerController controller;
	private PlayerControllerAdapterInterface player;
	private HashMap<Integer, Boolean> buttonStateMap;

	public enum INPUT {
		ATTACK,
		PAUSE,
		NOOP
	}

	public enum AXIS {
        DX,
        DY,
		NOOP
    }

	public PlayerControllerAdapter(PlayerControllerAdapterInterface player) {
		this.player = player;
		this.buttonStateMap = new HashMap<Integer, Boolean>();
		for(int code = 0; code < 10; code++)
		{
			buttonStateMap.put(code, false);
		}
	}

	public void setController(PlayerController controller) {
		if (controller == null)
			throw new RuntimeException("Passed controller is null");
		if (this.controller != null)
			throw new RuntimeException("Controller is already set");
		this.controller = controller;
		if (controller.supportsEvents())
			this.controller.addListener(this);
	}

	public void removeController() {
		if (controller == null)
			throw new RuntimeException("Controller is already null");
		if (controller.supportsEvents())
			this.controller.removeListener(this);
		this.controller = null;
	}

	public void poll() {
		if (controller.supportsEvents())
			throw new RuntimeException("This method should not be called when using events");

		float value;
		boolean isPressed;
		for(int axis = 0; axis < 2; axis++)
		{
			value = controller.getAxis(axis);
			axisMoved(controller, axis, value);
		}

		for(int code = 0; code < 8; code++)
		{
			isPressed = controller.getButton(code);
			if (isPressed) {
				// Check if the button was already pressed.
				// Only call the callback if the button was not
				// previously pressed.
				if (!buttonStateMap.get(code)) {
					buttonDown(controller, code);
					buttonStateMap.put(code, true);
				}
			} else {
				// Same thing. Only call buttonUp if the
				// button was previously pressed.
				if (buttonStateMap.get(code)) {
					buttonUp(controller, code);
					buttonStateMap.put(code, false);
				}
			}
		}
	}

	@Override
	public boolean buttonDown(PlayerController controller, INPUT buttonCode) {
		this.player.handleControllerInput(buttonCode, true);
		return false;
	}

	@Override
	public boolean buttonUp(PlayerController controller, INPUT buttonCode) {
		this.player.handleControllerInput(buttonCode, false);
		return false;
	}

	@Override
	public boolean axisMoved(PlayerController controller, AXIS axisCode, float value) {
		this.player.handleControllerInput(axisCode, value);
		return false;
	}

	// This methods will receive the calls from the Controller and pass them to the
	// ExternalControllerListener
	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		return this.buttonDown(this.controller, getInput(buttonCode));
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		return this.buttonUp(this.controller, getInput(buttonCode));
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		float modifier = 1f;
		if (axisCode == 1)
			modifier = -1f;
		return this.axisMoved(this.controller, getAxis(axisCode), value * modifier);
	}

	@Override
	public void connected(Controller controller) {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public void disconnected(Controller controller) {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		throw new RuntimeException("Not Implemented");
	}

	private INPUT getInput(int buttonCode) {
		switch (buttonCode) {
			case 0:
				return INPUT.ATTACK;
			case 7:
				return INPUT.PAUSE;
			default:
					return INPUT.NOOP;
		}
	}

	private AXIS getAxis(int axis) {
		switch (axis) {
			case 0:
				return AXIS.DX;
			case 1:
				return AXIS.DY;
			default:
				return AXIS.NOOP;
		}
	}
}