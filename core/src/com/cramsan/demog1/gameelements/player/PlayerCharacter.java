package com.cramsan.demog1.gameelements.player;

import com.badlogic.gdx.physics.box2d.World;
import com.cramsan.demog1.AudioManager;
import com.cramsan.demog1.map.TiledGameMap;
import com.cramsan.demog1.controller.PlayerController;
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
	private Set<GameElement> collideableSet;
	private int Id;
	private float dx, dy;
	private boolean isEventBased;

	public PlayerCharacter(int Id, TYPE type, CharacterEventListener listener, TiledGameMap map,
						   World gameWorld) {
		super(type, listener, map, gameWorld);
		this.controller = new PlayerControllerAdapter(this);
		this.collideableSet = new HashSet<GameElement>();
		this.Id = Id;
		this.isEventBased = false;
	}

	@Override
	public void updateInputs() {
		if (isDead || !isRunning)
			return;

		if (!isEventBased) {
			controller.poll();
		}
		if(hasAttacked){
			attack();
		}
		if(hasPaused){
			onCharacterPause();
		}
		// After handling buttons, reset them back to neutral(false)
		// Do not do the same for axises since those will receive an
		// event once the axis goes back to normal.
		hasAttacked = false;
		hasPaused = false;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (isDead)
			return;

		this.handleMovement(dx, dy, delta);
	}

	@Override
	public void onContact(GameElement collideable) {
		if (collideable.getType() != TYPE.CHAR_STATUE)
			return;

		if (!collideableSet.contains(collideable)){
			collideableSet.add(collideable);
			listener.onCharacterCollideableTouched(collideable, collideableSet.size(), this);
			AudioManager.PlaySound(AudioManager.SOUND.BELL);
		}
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

	/***
	 * This function will send an event to the listener that this character has attacked.
	 */
	protected void attack() {
		AudioManager.PlaySound(AudioManager.SOUND.ATTACK);
		this.listener.onCharacterAttack(this);
	}

	/***
	 * Signal to the listener that this character has send a pause event.
	 */
	protected void onCharacterPause() { this.listener.onCharacterPause(this);}

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
}
