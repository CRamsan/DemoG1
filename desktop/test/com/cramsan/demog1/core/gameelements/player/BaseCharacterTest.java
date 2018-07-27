package com.cramsan.demog1.core.gameelements.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cramsan.demog1.core.MockedGameTest;
import com.cramsan.demog1.gameelements.CharacterEventAdapter;
import com.cramsan.demog1.gameelements.CharacterEventListener;
import com.cramsan.demog1.gameelements.Collidable;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;
import com.cramsan.demog1.gameelements.player.PlayerControllerAdapter;
import com.cramsan.demog1.subsystems.SingleAssetManager;
import com.cramsan.demog1.subsystems.controller.DummyController;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BaseCharacterTest extends MockedGameTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void changeScaleDimensionsTest() {
        World world = new World(Vector2.Zero, true);
        float rawSize = 32;
        MockCharacter character = new MockCharacter(GameElement.TYPE.CHAR_BASEAI, null, world, gdxGame.getAssetManager(), (int)rawSize);
        assertTrue(character.getScale() == 1);
        assertTrue(character.getWidth() == rawSize);
        assertTrue(character.getHeight() == rawSize);
        character.setScale(2);
        assertTrue(character.getScale() == 2);
        assertTrue(character.getWidth() == (rawSize * 2));
        assertTrue(character.getHeight() == (rawSize * 2));
        character.setScale(0.1f);
        assertTrue(character.getScale() == 0.1f);
        assertTrue(character.getWidth() == (3));
        assertTrue(character.getHeight() == (3));
    }

    @org.junit.Test
    public void changeScaleMaintainCenterTest() {
        World world = new World(Vector2.Zero, true);
        int posX = 10;
        int posY = 25;
        float rawSize = 32;
        MockCharacter character = new MockCharacter(GameElement.TYPE.CHAR_BASEAI, null, world, gdxGame.getAssetManager(), (int)rawSize);
        character.setCenterPosition(posX, posY);
        assertTrue(character.getCenterPosition().x == posX);
        assertTrue(character.getCenterPosition().y == posY);
        character.setScale(2);
        assertTrue(character.getCenterPosition().x == posX);
        assertTrue(character.getCenterPosition().y == posY);
        character.setScale(0.1f);
        assertTrue(character.getCenterPosition().x == posX);
        assertTrue(character.getCenterPosition().y == posY);
    }
}