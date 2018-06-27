package com.cramsan.demog1.screen;

import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.gameelements.Collidable;
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
			GameElement newCollidable = new Collidable(this, getGameWorld());
			newCollidable.init(getAssetManager());
			newCollidable.setTilePosition((int)(pos.x * newCollidable.getWidth()), (int)(pos.y * newCollidable.getHeight()));
			addCollidable(newCollidable);
			addLightSource(newCollidable);
			statueCount++;
		}
	}

	@Override
	public void onCharacterCollidableTouched(GameElement collidable, int collidableIndex, PlayerCharacter player) {
		if (collidableIndex == this.statueCount) {
			disableAllPlayers();
			getUiSystem().displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 1;
	}
}
