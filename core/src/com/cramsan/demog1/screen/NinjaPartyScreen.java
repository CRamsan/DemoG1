package com.cramsan.demog1.screen;

import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.gameelements.Collideable;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.gameelements.GameParameterManager;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;

/**
 * There are two winning conditions. Either touch the five statues or
 * be the last player alive. You have one smoke bomb that you can use.
 */
public class NinjaPartyScreen extends GameScreen {

	private int statueCount;

	public NinjaPartyScreen(GameParameterManager parameterManager)
	{
		super(parameterManager);
		setAiCount(10);
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		createAICharacters();
		createStatues();
	}

	private void createStatues() {
		statueCount = 0;
		for (Vector2 pos : getMap().getStatueSpawner()) {
			GameElement newCollideable = new Collideable(this, getGameWorld());
			newCollideable.setTilePosition((int)(pos.x * newCollideable.getWidth()), (int)(pos.y * newCollideable.getHeight()));
			addCollidable(newCollideable);
			addLightSource(newCollideable);
			statueCount++;
		}
	}

	@Override
	public void onCharacterCollideableTouched(GameElement collideable, int collideableIndex, PlayerCharacter player) {
		if (collideableIndex == this.statueCount) {
			disableAllPlayers();
			getUiSystem().displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 1;
	}
}
