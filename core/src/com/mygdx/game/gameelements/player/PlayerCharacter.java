package com.mygdx.game.gameelements.player;
import com.mygdx.game.AudioManager;
import com.mygdx.game.TiledGameMap;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.gameelements.BaseCharacter;
import com.mygdx.game.gameelements.CharacterEventListener;
import com.mygdx.game.gameelements.Collideable;

import java.util.HashSet;
import java.util.Set;

/**
 * This class will handle the controller interaction for a character
 */
public class PlayerCharacter extends BaseCharacter implements PlayerControllerAdapterInterface {

	private PlayerControllerAdapter controller;
	private boolean hasAttacked, hasPaused;
	private Set<Collideable> collideableSet;
	private int Id;
	private float dx, dy;
	private boolean isEventBased;

	public PlayerCharacter(int Id, TYPE type, CharacterEventListener listener, TiledGameMap map) {
		super(type, listener, map);
		this.controller = new PlayerControllerAdapter(this);
		this.collideableSet = new HashSet<Collideable>();
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

	static int counter;

	public void handleControllerInput(int buttonCode, boolean value) {
		switch (buttonCode) {
			case 0:
				counter++;
				if (counter % 2 == 0)
					hasAttacked = value;
				else
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

	public void onCollideableContact(Collideable collideable) {
		if (!collideableSet.contains(collideable)){
			collideableSet.add(collideable);
			listener.onCharacterCollideableTouched(collideable, collideableSet.size(), this);
			AudioManager.PlaySound(AudioManager.SOUND.BELL);
		}
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
}
