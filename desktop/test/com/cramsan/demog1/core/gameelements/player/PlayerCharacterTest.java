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
    private CharacterEventAdapter eventAdapter;

    @org.junit.Before
    public void setUp() throws Exception {
        attackTriggered = false;
        pauseTriggered = false;
        collisionTriggered = false;
        eventAdapter = new CharacterEventAdapter() {
            public void onCharacterAttack(PlayerCharacter character) {
                attackTriggered = true;
            }
            public void onCharacterPause(PlayerCharacter character) {
                pauseTriggered = true;
            }
        };
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void updateInputsEventBasedAttack() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0, true);
        assertTrue(controller.supportsEvents());
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, eventAdapter, world, gdxGame.getAssetManager());
        character.setController(controller);
        character.updateInputs();

        controller.buttonDown(null, 0);
        character.updateInputs();
        assertTrue(!pauseTriggered && attackTriggered);
    }

    @org.junit.Test
    public void updateInputsEventBasedPause() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0, true);
        assertTrue(controller.supportsEvents());
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, eventAdapter, world, gdxGame.getAssetManager());
        character.setController(controller);
        character.updateInputs();

        controller.buttonDown(null, 7);
        character.updateInputs();
        assertTrue(!attackTriggered && pauseTriggered);
    }

    @org.junit.Test
    public void updateInputsEventBasedTestAxis() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0, true);
        assertTrue(controller.supportsEvents());
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, eventAdapter, world, gdxGame.getAssetManager());
        character.setController(controller);
        character.updateInputs();

        float[] xAxis = {0, 0.2f, -0.5f, 1, -1};
        float[] yAxis = {1, 0.5f, -1, 0.1f, -0};
        for (int i = 0; i < xAxis.length; i++) {
            float x = xAxis[i];
            float y = yAxis[i];
            controller.axisMoved(null, 0, x);
            controller.axisMoved(null, 1, y);
            character.updateInputs();
            assertTrue(character.getDx() == x && character.getDy() == (y * -1)); // The Y value is inverted so we account for that here
        }
    }

    @org.junit.Test
    public void updateInputsEventBasedKillOtherPlayer() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0, true);
        assertTrue(controller.supportsEvents());
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, null, world, gdxGame.getAssetManager());

        PlayerCharacter newCharacter = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, eventAdapter, world, gdxGame.getAssetManager());
        newCharacter.setController(controller);
        newCharacter.updateInputs();
        controller.buttonDown(null, 0);
        controller.buttonDown(null, 7);
        newCharacter.onKilled(character);
        newCharacter.updateInputs();
        assertFalse(attackTriggered);
        assertFalse(pauseTriggered);
    }

    @org.junit.Test
    public void updateInputsEventBasedDisablePlayer() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0, true);
        assertTrue(controller.supportsEvents());
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, eventAdapter, world, gdxGame.getAssetManager());
        character.setController(controller);
        character.updateInputs();

        controller.buttonDown(null, 0);
        controller.buttonDown(null, 7);
        character.disableCharacter();
        character.updateInputs();
        assertFalse(attackTriggered);
        assertFalse(pauseTriggered);
    }

    @org.junit.Test
    public void updateInputsPollBasedAttack() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0);
        assertFalse(controller.supportsEvents());
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, eventAdapter, world, gdxGame.getAssetManager());
        character.setController(controller);
        character.updateInputs();

        controller.setButton(0, true);
        character.updateInputs();
        assertTrue(!pauseTriggered && attackTriggered);
    }

    @org.junit.Test
    public void updateInputsPollBasedPause() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0);
        assertFalse(controller.supportsEvents());
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, eventAdapter, world, gdxGame.getAssetManager());
        character.setController(controller);
        character.updateInputs();

        controller.setButton(7, true);
        character.updateInputs();
        assertTrue(!attackTriggered && pauseTriggered);
    }

    @org.junit.Test
    public void updateInputsPollBasedTestAxis() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0);
        assertFalse(controller.supportsEvents());
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, eventAdapter, world, gdxGame.getAssetManager());
        character.setController(controller);
        character.updateInputs();

        float[] xAxis = {0, 0.2f, -0.5f, 1, -1};
        float[] yAxis = {1, 0.5f, -1, 0.1f, -0};
        for (int i = 0; i < xAxis.length; i++) {
            float x = xAxis[i];
            float y = yAxis[i];
            controller.setAxis(0, x);
            controller.setAxis(1, y);
            character.updateInputs();
            assertTrue(character.getDx() == x && character.getDy() == (y * -1)); // The Y value is inverted so we account for that here
        }
    }

    @org.junit.Test
    public void updateInputsPollBasedKillOtherPlayer() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0);
        assertFalse(controller.supportsEvents());
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, eventAdapter, world, gdxGame.getAssetManager());

        PlayerCharacter newCharacter = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, new CharacterEventAdapter(), world, gdxGame.getAssetManager());
        newCharacter.setController(controller);
        newCharacter.updateInputs();
        controller.setButton(0, true);
        controller.setButton(7, true);
        newCharacter.onKilled(character);
        newCharacter.updateInputs();
        assertFalse(attackTriggered);
        assertFalse(pauseTriggered);
    }

    @org.junit.Test
    public void updateInputsPollBasedDisablePlayer() {
        World world = new World(Vector2.Zero, true);
        DummyController controller = new DummyController(0);
        assertFalse(controller.supportsEvents());
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, eventAdapter, world, gdxGame.getAssetManager());
        character.setController(controller);
        character.updateInputs();

        controller.setButton(0, true);
        controller.setButton(7, true);
        character.disableCharacter();
        character.updateInputs();
        assertFalse(attackTriggered);
        assertFalse(pauseTriggered);
    }

    @org.junit.Test
    public void onContact() {
        World world = new World(Vector2.Zero, true);
        PlayerCharacter character = new PlayerCharacter(0, GameElement.TYPE.CHAR_HUMAN, new CharacterEventAdapter() {
            public void onCharacterCollidableTouched(Collidable collidable, PlayerCharacter player) {
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
        assertTrue(character.isWillAttack());
        character.handleControllerInput(PlayerControllerAdapter.INPUT.ATTACK, false);
        assertFalse(character.isWillAttack());
        character.handleControllerInput(PlayerControllerAdapter.INPUT.PAUSE, true);
        assertTrue(character.isWillPause());
        character.handleControllerInput(PlayerControllerAdapter.INPUT.PAUSE, false);
        assertFalse(character.isWillPause());
    }
}