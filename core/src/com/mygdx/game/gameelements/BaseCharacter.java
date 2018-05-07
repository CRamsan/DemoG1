package com.mygdx.game.gameelements;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Globals;
import com.mygdx.game.TiledGameMap;
import com.mygdx.game.gameelements.player.PlayerCharacter;

/**
 * This class will handle all common logic required by characters, either NPCs or human players.
 */
public abstract class BaseCharacter extends GameElement {

	protected boolean isDead;
	protected boolean isRunning;
    private TiledGameMap map;

    public BaseCharacter(TYPE type, CharacterEventListener listener, TiledGameMap map) {
        super(type, listener);
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
    protected void handleMovement(float dx, float dy, float delta, boolean ignoreCollision){
        if (isDead || !isRunning)
            return;
        Vector2 movement = new Vector2(dx, dy);
        if (movement.len() == 0) {
            return;
        }

        movement = movement.scl(delta * 50f);
        float absX = Math.abs(movement.x);
        float absY = Math.abs(movement.y);

        boolean processVectors = (absX == absY) ? Globals.rand.nextBoolean() : false;

        if (absX > absY || processVectors) {
            movement.x = testVectorMovement(movement.x, AXIS.X, ignoreCollision);
            this.x += movement.x;
            if (absY != 0) {
                movement.y = testVectorMovement(movement.y, AXIS.Y, ignoreCollision);
                this.y += movement.y;
            }
        } else {
            movement.y = testVectorMovement(movement.y, AXIS.Y, ignoreCollision);
            this.y += movement.y;
            if (absX != 0) {
                movement.x = testVectorMovement(movement.x, AXIS.X, ignoreCollision);
                this.x += movement.x;
            }
        }

        isDirty = true;
        state += (movement.len());
        updateDirection(movement.x, movement.y);
    }

    /**
     * Private method that will return the distance that this character can move on the provided axis.
     * This method will take in consideration collisions.
     * @param value The distance the character wants to move
     * @param axis The expected axis that wants to be tested
     * @return The resulting distance that this character can move
     */
    private float testVectorMovement(float value, AXIS axis, boolean ignoreWalls) {
        return 0f;
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
