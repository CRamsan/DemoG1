package com.mygdx.game.gameelements;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;

import java.util.HashSet;
import java.util.Set;

/**
 * This class will handle the controller interaction for a character
 */
public class PlayerCharacter extends BaseCharacter {

	private PlayerController controller;
	private boolean hasAttacked, hasPaused;
	private Set<Statue> statueSet;
	private int Id;
	private float dx, dy;
	private boolean isEventBased;

	public PlayerCharacter(int Id, TYPE type, CharacterEventListener listener, GameStateManager manager) {
		super(type, listener, manager);
		this.controller = new PlayerController(this);
		this.statueSet = new HashSet<Statue>();
		this.Id = Id;
		this.isEventBased = true;
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		if (isDead)
			return;
		if (!isEventBased) {
			controller.poll();
		}
        if(hasAttacked){
            attack();
        }
        if(hasPaused){
            pause();
        }

		this.handleMovement(dx, dy, delta);
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

	@Override
	public void onKilled(PlayerCharacter killer) {
		super.onKilled(killer);
		this.listener.onPlayerDied(this, killer);
	}

	public void onStatueContact(Statue statue) {
		if (!statueSet.contains(statue)){
			statueSet.add(statue);
			listener.onNewStatueTouched(statueSet.size(), this);
		}
	}

	public int getId() {
		return Id;
	}

	/***
	 * This function will send an event to the listener that this character has attacked.
	 */
	protected void attack() {
		this.listener.attack(this);
	}

	/***
	 * Signal to the listener that this character has send a pause event.
	 */
	protected void pause() { this.listener.pause(this);}

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
			if (isEventBased)
				this.controller.addListener(this);
			else
				throw new RuntimeException("Polling is not implemented yet");
		}

		public void removeController() {
			if (controller == null)
				throw new RuntimeException("Controller is already null");
			if (isEventBased)
				this.controller.removeListener(this);
			else
				throw new RuntimeException("Polling is not implemented yet");
			this.controller = null;
		}

		public void poll() {
			if (isEventBased)
				throw new RuntimeException("This method should not be called when using events");
			else
				throw new RuntimeException("Polling is not implemented yet");
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
