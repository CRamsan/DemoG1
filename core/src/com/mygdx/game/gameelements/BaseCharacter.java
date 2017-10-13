package com.mygdx.game.gameelements;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Globals;

public abstract class BaseCharacter extends GameElement {

    protected GameStateManager manager;
    private Controller controller;
    private float walkSpeed;

    public BaseCharacter(TYPE type, CharacterEventListener listerner, GameStateManager manager) {
        super(type, listerner);
        if (type == TYPE.DARK)
            throw new RuntimeException("Characters cannot be of this type");
        this.walkSpeed = 2f;
        this.isDead = false;
        this.manager = manager;
    }

    /***
     * Call this method to update the state of the object. This method will handle inputs and updating sny state.
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        isDirty = false;
    }

    /***
     * Receive input as two components of one vector. The resulting vector should not be greater than 1.
     * @param dx
     * @param dy
     */
    protected void handleMovement(float dx, float dy, float delta){
        if (isDead)
            return;
        Vector2 movement = new Vector2(dx, dy);
        if (movement.len() > 1) {
            //movement = movement.nor();
        } else if (movement.len() == 0) {
            return;
        }

        movement = movement.scl(delta * walkSpeed);
        float absX = Math.abs(movement.x);
        float absY = Math.abs(movement.y);

        boolean processVectors = (absX == absY) ? Globals.rand.nextBoolean() : false;

        if (absX > absY || processVectors) {
            movement.x = handleVectorMovement(movement.x, AXIS.X);
            this.x += movement.x;
            if (absY != 0) {
                movement.y = handleVectorMovement(movement.y, AXIS.Y);
                this.y += movement.y;
            }
        } else if (absX < absY){
            movement.y = handleVectorMovement(movement.y, AXIS.Y);
            this.y += movement.y;
            if (absX != 0) {
                movement.x = handleVectorMovement(movement.x, AXIS.X);
                this.x += movement.x;
            }
        }

        isDirty = true;
        state += (movement.len());
        updateDirection(movement.x, movement.y);
    }

    private float handleVectorMovement(float value, AXIS axis) {
        if (value == 0)
            throw new RuntimeException("Vector has value of 0");
        int endXStart, endXEnd, endYStart, endYEnd;
        if (value > 0) {
            if (axis == AXIS.X){
                endYStart = (int)Math.floor(this.y);
                endYEnd = (int)Math.floor(this.y + getHeight());
                if (Math.floor(this.y + getHeight()) == endYEnd) {
                    endYEnd--;
                }
                endXEnd = (int)Math.floor(this.x + getWidth() + value);
                if (manager.isSolid(endXEnd, endYStart) || manager.isSolid(endXEnd, endYEnd)) {
                    return (float)Math.ceil(this.x + getWidth()) - (this.x + getWidth());
                } else {
                    return value;
                }
            } else {
                endXStart = (int)Math.floor(this.x);
                endXEnd = (int)Math.floor(this.x + getWidth());
                endYEnd = (int)Math.floor(this.y + getHeight() + value);
                if (endXEnd == Math.floor(this.x + getWidth())) {
                    endXEnd--;
                }
                if (manager.isSolid(endXStart, endYEnd) || manager.isSolid(endXEnd, endYEnd)) {
                    return (float)Math.ceil(this.y + getHeight()) - (this.y + getHeight());
                } else {
                    return value;
                }
            }
        } else {
            if (axis == AXIS.X){
                endYStart = (int)Math.floor(this.y);
                endYEnd = (int)Math.floor(this.y + getHeight());
                endXStart = (int)Math.floor(this.x + value);
                if (endYEnd == Math.floor(this.y + getHeight())) {
                    endYEnd--;
                }
                if (manager.isSolid(endXStart, endYStart) || manager.isSolid(endXStart, endYEnd)) {
                    return - (this.x) - (float)Math.floor(this.x);
                } else {
                    return value;
                }
            } else {
                endXStart = (int)Math.floor(this.x);
                endXEnd = (int)Math.floor(this.x + getWidth());
                endYEnd = (int)Math.floor(this.y + value);
                if (endXEnd == Math.floor(this.x + getWidth())) {
                    endXEnd--;
                }
                if (manager.isSolid(endXStart, endYEnd) || manager.isSolid(endXEnd, endYEnd)) {
                    return - (this.y) - (float)Math.floor(this.y);
                } else {
                    return value;
                }
            }
        }
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
