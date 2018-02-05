package com.mygdx.game.gameelements.player;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.controller.ExternalControllerListener;
import com.mygdx.game.controller.PlayerController;

public class PlayerControllerAdapter implements ExternalControllerListener, ControllerListener {

	private PlayerController controller;
	private PlayerControllerAdapterInterface player;

	public PlayerControllerAdapter(PlayerControllerAdapterInterface player) {
		this.player = player;
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
			if (value != 0)
				axisMoved(controller, axis, value);
		}

		for(int code = 0; code < 10; code++)
		{
			isPressed = controller.getButton(code);
		}
	}

	@Override
	public boolean buttonDown(PlayerController controller, int buttonCode) {
		this.player.handleControllerInput(buttonCode, true);
		return false;
	}

	@Override
	public boolean buttonUp(PlayerController controller, int buttonCode) {
		this.player.handleControllerInput(buttonCode, false);
		return false;
	}

	@Override
	public boolean axisMoved(PlayerController controller, int axisCode, float value) {
		float modifier = 1f;
		if (axisCode == 1)
			modifier = -1f;

		this.player.handleControllerInput(axisCode, value * modifier);
		return false;
	}

	// This methods will receive the calls from the Controller and pass them to the
	// ExternalControllerListerner
	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		return this.buttonDown(this.controller, buttonCode);
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		return this.buttonUp(this.controller, buttonCode);
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return this.axisMoved(this.controller, axisCode, value);
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
}