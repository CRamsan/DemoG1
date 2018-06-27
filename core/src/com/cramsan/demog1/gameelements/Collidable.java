package com.cramsan.demog1.gameelements;

import com.badlogic.gdx.physics.box2d.World;

/**
 * This class will handle collidable game elements. This classes do no have the capability
 * to move.
 */
public class Collidable extends GameElement {

    public Collidable(CharacterEventListener listener, World world) {
        super(TYPE.CHAR_STATUE, listener, world);
    }

    @Override
    public void onContact(GameElement Collidable) {

    }
}
