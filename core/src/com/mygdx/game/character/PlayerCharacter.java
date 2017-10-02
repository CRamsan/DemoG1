package com.mygdx.game.character;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;

/**
 * This class will handle the controller interaction for a character
 */
public class PlayerCharacter extends BaseCharacter {

	private PlayerController controller;
	private boolean hasAttacked, hasPaused;
	private float dx, dy;

	public PlayerCharacter(TYPE type, CharacterEventListener listener) {
		super(type, listener);
		this.controller = new PlayerController(this);
	}
	
	@Override
	public void update() {
		super.update();
		if (isDead)
			return;
        if(hasAttacked){
            attack();
        }
        if(hasPaused){
            pause();
        }
		this.handleMovement(dx, dy);
        // After handling buttons, reset them back to neutral(false)
		// Do not do the same for axises since those will receive an
		// event once the axis goes back to normal.
        hasAttacked = false;
        hasPaused = false;
	}

	private void handleControllerInput(int buttonCode, boolean value) {
		switch (buttonCode) {
			case 0:
				hasAttacked = value;
				break;
			case 7:
				hasPaused = value;
				break;
		}
	}

	private void handleControllerInput(int axisCode, float value) {
		switch (axisCode) {
			case 0:
				dx = value;
				break;
			case 1:
				dy = value;
				break;
		}
	}

	public void setController(Controller controller) {
		this.controller.setController(controller);
	}

	public void removeController() {
		this.controller.removeController();
	}

	private class PlayerController extends ControllerAdapter {

		private Controller controller;
		private PlayerCharacter player;

		public PlayerController(PlayerCharacter player) {
			this.player = player;
		}

		public PlayerController(Controller controller, PlayerCharacter player) {
			this(player);
			if (controller == null)
				throw new RuntimeException("Controller is null");
			setController(controller);
		}

		public void setController(Controller controller) {
			if (controller == null)
				throw new RuntimeException("Passed controller is null");
			if (this.controller != null)
				throw new RuntimeException("Controller is already set");
			this.controller = controller;
			this.controller.addListener(this);
		}

		public void removeController() {
			if (controller == null)
				throw new RuntimeException("Controller is already null");
			this.controller.removeListener(this);
			this.controller = null;
		}

		@Override
		public boolean buttonDown(Controller controller, int buttonCode) {
			this.player.handleControllerInput(buttonCode, true);
			return false;
		}

		@Override
		public boolean buttonUp(Controller controller, int buttonCode) {
			this.player.handleControllerInput(buttonCode, false);
			return false;
		}

		@Override
		public boolean axisMoved(Controller controller, int axisCode, float value) {
			this.player.handleControllerInput(axisCode, value);
			return false;
		}
	}
}
