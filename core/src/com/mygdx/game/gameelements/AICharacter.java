package com.mygdx.game.gameelements;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Globals;
import com.mygdx.game.map.TiledGameMap;
import com.mygdx.game.gameelements.player.PlayerCharacter;

/**
 * This class will handle the AI movement.
 */
public class AICharacter extends BaseCharacter
{	
	private Vector2 lastMovementVector;
	private int aiState;
	
	public AICharacter(TYPE type, CharacterEventListener listerner, TiledGameMap map, World gameWorld) {
		super(type, listerner, map, gameWorld);
        lastMovementVector = new Vector2();
        aiState = 0;
	}

	@Override
	public void updateInputs() {
		if (isDead)
			return;
		aiState++;
		// Every 15 frames there is a chance that we will decide a new movement vector
		if (aiState % 15 == 0
				&& Globals.rand.nextInt() % 800 < 1) {
			lastMovementVector = new Vector2((float)((Globals.rand.nextFloat() * 2) - 1)/ 3f,
					(float)((Globals.rand.nextFloat() * 2) - 1)/ 3f);
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (isDead)
			return;
		this.handleMovement(lastMovementVector.x, lastMovementVector.y, delta);
	}

	@Override
	public void onContact(GameElement collideable) {

	}

	@Override
	public void onKilled(PlayerCharacter killer) {
		super.onKilled(killer);
		this.listener.onAICharacterDied(this, killer);
	}
}
