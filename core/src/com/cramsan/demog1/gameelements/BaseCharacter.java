package com.cramsan.demog1.gameelements;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cramsan.demog1.subsystems.map.TiledGameMap;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;

/**
 * This class will handle all common logic required by characters, either NPCs or human players.
 */
public abstract class BaseCharacter extends GameElement {

	protected boolean isDead;
	protected boolean isRunning;
    private TiledGameMap map;

    public BaseCharacter(TYPE type, CharacterEventListener listener, TiledGameMap map,
                         World gameWorld) {
        super(type, listener, gameWorld);
        if (type == TYPE.CHAR_STATUE)
            throw new RuntimeException("Characters cannot be of this type");
        this.isDead = false;
		this.isRunning = true;
		this.map = map;
    }

    /***
     * Call this method to handle inputs. This will not change the state of the character.
     */
    public abstract void updateInputs();

    /***
     * Call this method to update the state of the object. This method will not handle inputs, only updating the state.
     */
    @Override
    public void update(float delta) {
        super.update(delta);
		if (!isRunning)
			return;
        isDirty = false;
    }

    /***
     * Receive input as two components of one vector. The resulting vector should not be greater than 1.
     * @param dx
     * @param dy
     */
    protected void handleMovement(float dx, float dy, float delta){
        Vector2 movement = new Vector2(dx, dy);

        if (isDead || !isRunning || movement.len() == 0)
        {
            this.body.setLinearVelocity(Vector2.Zero);
            return;
        }
        state += (delta * movement.len());
        movement = movement.scl(delta * 2000f);
        this.body.setLinearVelocity(movement);
        isDirty = true;
        updateDirection(movement.x, movement.y);
    }

	public void disableCharacter() {
		isRunning = false;
	}
	
    /**
     * This method will return if the Character has moved.
     */
    public boolean hasMoved() {
        return isDirty;
    }

    /***
     * Callback to be called when this character is killed.
     */
    public void onKilled(PlayerCharacter killer) {
        this.isDead = true;
    }
}
