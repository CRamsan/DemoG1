package com.mygdx.game.gameelements;

/**
 * This class will handle collidable game elements. This classes do no have the capability
 * to move.
 */
public class Collideable extends GameElement {

    public Collideable(CharacterEventListener listener) {
        super(TYPE.CHAR_STATUE, listener);
    }
}
