package com.cramsan.demog1.core.gameelements.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cramsan.demog1.core.MockedGameTest;
import com.cramsan.demog1.gameelements.CharacterEventAdapter;
import com.cramsan.demog1.gameelements.Collidable;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;
import com.cramsan.demog1.gameelements.player.PlayerControllerAdapter;
import com.cramsan.demog1.subsystems.controller.DummyController;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerCharacterTest extends MockedGameTest {

    private boolean attackTriggered;
    private boolean pauseTriggered;
    private boolean collisionTriggered;

    @org.junit.Before
    public void setUp() throws Exception {
        attackTriggered = false;
        pauseTriggered = false ;
        collisionTriggered = false ;
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void updateInputs() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0);

        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, new CharacterEventAdapter() {
            public void onCharacterAttack(PlayerCharacter character) {
                attackTriggered = true;
            }
            public void onCharacterPause(PlayerCharacter character) {
                pauseTriggered = true;
            }
        }, world, gdxGame.getAssetManager());
        character.setController(controller);
        character.updateInputs();

        // Test the buttons
        controller.setButton(0, true);
        character.updateInputs();
        assertTrue(character.hasAttacked() && !character.hasPaused() && attackTriggered);

        attackTriggered = false;
        controller.clearInputs();
        controller.setButton(7, true);
        character.updateInputs();
        assertTrue(!character.hasAttacked() && character.hasPaused() && pauseTriggered);

        pauseTriggered = false;
        attackTriggered = false;
        controller.clearInputs();
        character.updateInputs();
        assertFalse(character.hasAttacked());
        assertFalse(character.hasPaused());
        assertFalse(attackTriggered);
        assertFalse(pauseTriggered);

        // Test axis
        float[] xAxis = {0, 0.2f, -0.5f, 1, -1};
        float[] yAxis = {1, 0.5f, -1, 0.1f, -0};
        controller.clearInputs();
        for (int i = 0; i < xAxis.length; i++) {
            float x = xAxis[i];
            float y = yAxis[i];
            controller.setAxis(0, x);
            controller.setAxis(1, y);
            character.updateInputs();
            assertTrue(character.getDx() == x && character.getDy() == (y * -1)); // The Y value is inverted so we account for that here
        }

        // Test killing a player
        controller.clearInputs();
        PlayerCharacter newCharacter = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, new CharacterEventAdapter(), world, gdxGame.getAssetManager());
        newCharacter.setController(controller);
        newCharacter.updateInputs();
        controller.setButton(0, true);
        controller.setButton(7, true);
        newCharacter.onKilled(character);
        newCharacter.updateInputs();
        assertFalse(character.hasAttacked());
        assertFalse(character.hasPaused());

        // Test disabling a player
        newCharacter = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, new CharacterEventAdapter(), world, gdxGame.getAssetManager());
        newCharacter.setController(controller);
        newCharacter.updateInputs();
        controller.setButton(0, true);
        controller.setButton(7, true);
        newCharacter.disableCharacter();
        newCharacter.updateInputs();
        assertFalse(character.hasAttacked());
        assertFalse(character.hasPaused());
    }

    @org.junit.Test
    public void update() {
    }

    @org.junit.Test
    public void onContact() {
        World world = new World(Vector2.Zero, true);
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, new CharacterEventAdapter() {
            public void onCharacterCollidableTouched(GameElement collidable, PlayerCharacter player) {
                collisionTriggered = true;
            }
        }, world, gdxGame.getAssetManager());
        PlayerCharacter otherCharacter = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, new CharacterEventAdapter(), world, gdxGame.getAssetManager());
        Collidable statue = new Collidable(new CharacterEventAdapter(), world, gdxGame.getAssetManager());

        character.onContact(otherCharacter);
        assertFalse(collisionTriggered);
        character.onContact(statue);
        assertTrue(collisionTriggered);
    }

    @org.junit.Test
    public void handleControllerInput() {
        World world = new World(Vector2.Zero, true);
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, new CharacterEventAdapter(), world, gdxGame.getAssetManager());
        character.handleControllerInput(PlayerControllerAdapter.AXIS.DX, 5);
        assertTrue(character.getDx() == 5);
        character.handleControllerInput(PlayerControllerAdapter.AXIS.DY, -3);
        assertTrue(character.getDy() == -3);
        character.handleControllerInput(PlayerControllerAdapter.INPUT.ATTACK, true);
        assertTrue(character.hasAttacked());
        character.handleControllerInput(PlayerControllerAdapter.INPUT.ATTACK, false);
        assertFalse(character.hasAttacked());
        character.handleControllerInput(PlayerControllerAdapter.INPUT.PAUSE, true);
        assertTrue(character.hasPaused());
        character.handleControllerInput(PlayerControllerAdapter.INPUT.PAUSE, false);
        assertFalse(character.hasPaused());
    }
}