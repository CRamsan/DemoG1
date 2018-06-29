package com.cramsan.demog1.gameelements.player;

import com.badlogic.gdx.physics.box2d.World;
import com.cramsan.demog1.subsystems.AudioManager;
import com.cramsan.demog1.subsystems.SingleAssetManager;
import com.cramsan.demog1.subsystems.controller.PlayerController;
import com.cramsan.demog1.gameelements.BaseCharacter;
import com.cramsan.demog1.gameelements.CharacterEventListener;
import com.cramsan.demog1.gameelements.GameElement;

import java.util.HashSet;
import java.util.Set;

/**
 * This class will handle the controller interaction for a character
 */
public class PlayerCharacter extends BaseCharacter implements PlayerControllerAdapterInterface {

	private PlayerControllerAdapter controller;
	private boolean hasAttacked, hasPaused;
	private int Id;
	private float dx;
	private float dy;
	private boolean isEventBased;

	public PlayerCharacter(int Id, TYPE type, CharacterEventListener listener,
						   World gameWorld, SingleAssetManager assetManager) {
		super(type, listener, gameWorld, assetManager);
		this.controller = new PlayerControllerAdapter(this);
		this.Id = Id;
		this.isEventBased = false;
	}

	@Override
	public void updateInputs() {
		hasAttacked = false;
		hasPaused = false;

		if (isDead || !isRunning)
			return;

		if (!isEventBased) {
			controller.poll();
		}
		if(hasAttacked){
			this.listener.onCharacterAttack(this);
		}
		if(hasPaused){
			this.listener.onCharacterPause(this);
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (isDead)
			return;

		this.handleMovement(dx, dy, delta);
	}

	@Override
	public void onContact(GameElement collidable) {
		if (collidable.getType() != TYPE.CHAR_STATUE)
			return;

		listener.onCharacterCollidableTouched(collidable, this);
	}

	public void handleControllerInput(int buttonCode, boolean value) {
		switch (buttonCode) {
			case 0:
				hasAttacked = value;
				break;
			case 7:
				hasPaused = value;
				break;
		}
	}

	public void handleControllerInput(int axisCode, float value) {
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
		this.listener.onPlayerCharacterDied(this, killer);
	}

	public int getId() {
		return Id;
	}

	public void setController(PlayerController controller) {
		this.controller.setController(controller);
		this.isEventBased = controller.supportsEvents();
	}

	public void removeController() {
		this.controller.removeController();
	}

	public float attackRadius() {
		return width / 2;
	}

	public boolean hasAttacked() {
		return hasAttacked;
	}

	public boolean hasPaused() {
		return hasPaused;
	}

	public float getDx() {
		return dx;
	}

	public float getDy() {
		return dy;
	}

}