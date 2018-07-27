package com.cramsan.demog1.core.gameelements.player;

import com.badlogic.gdx.physics.box2d.World;
import com.cramsan.demog1.gameelements.BaseCharacter;
import com.cramsan.demog1.gameelements.CharacterEventListener;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.subsystems.SingleAssetManager;

public class MockCharacter extends BaseCharacter {

    public MockCharacter(TYPE type, CharacterEventListener listener, World gameWorld, SingleAssetManager assetManager, int rawSize) {
        super(type, listener, gameWorld, assetManager, rawSize);
    }

    @Override
    public void updateInputs() {

    }

    @Override
    public void onContact(GameElement collidable) {

    }
}
