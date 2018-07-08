package com.cramsan.demog1.gameelements.player;

import com.badlogic.gdx.physics.box2d.World;
import com.cramsan.demog1.subsystems.SingleAssetManager;
import com.cramsan.demog1.subsystems.controller.PlayerController;
import com.cramsan.demog1.gameelements.BaseCharacter;
import com.cramsan.demog1.gameelements.CharacterEventListener;
import com.cramsan.demog1.gameelements.GameElement;

/**
 * This class will handle the controller interaction for a character
 */
public class PlayerCharacter extends BaseCharacter implements PlayerControllerAdapterInterface {

	private PlayerControllerAdapter controller;
	private boolean willAttack;
	private boolean willPause;
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
		if (isDead || !isRunning) {
			// If dead or running then ignore any changes
			willAttack = false;
			willPause = false;
			return;
		}

		if (!isEventBased) {
			controller.poll();
		}
		if(willAttack){
			this.listener.onCharacterAttack(this);
			willAttack = false;
		}
		if(willPause){
			this.listener.onCharacterPause(this);
			willPause = false;
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

	public void handleControllerInput(PlayerControllerAdapter.INPUT inputCode, boolean value) {
		switch (inputCode) {
			case ATTACK:
				willAttack = value;
				break;
			case PAUSE:
				willPause = value;
				break;
		}
	}

	public void handleControllerInput(PlayerControllerAdapter.AXIS axisCode, float value) {
		switch (axisCode) {
			case DX:
				dx = value;
				break;
			case DY:
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

	public float getDx() {
		return dx;
	}

	public float getDy() {
		return dy;
	}

	public boolean isWillAttack() {
		return willAttack;
	}

	public boolean isWillPause() {
		return willPause;
	}
}