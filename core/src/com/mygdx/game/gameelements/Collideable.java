package com.mygdx.game.gameelements;

import com.badlogic.gdx.physics.box2d.World;

/**
 * This class will handle collidable game elements. This classes do no have the capability
 * to move.
 */
public class Collideable extends GameElement {

    public Collideable(CharacterEventListener listerner, World world) {
        super(TYPE.CHAR_STATUE, listerner, world);
    }

    @Override
    public void onContact(GameElement collideable) {

    }
}
