package com.mygdx.game.gameelements;

/**
 * This class will handle collidable game elements. This classes do no have the capability
 * to move.
 */
public class Collideable extends GameElement {
    public Collideable(float x, float y, CharacterEventListener listerner) {
        super(TYPE.STATUE, x, y, listerner);
    }
}
