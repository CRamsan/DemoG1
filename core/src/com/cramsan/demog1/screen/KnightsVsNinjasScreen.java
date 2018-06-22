package com.cramsan.demog1.screen;

import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.gameelements.Collideable;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.gameelements.GameParameterManager;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;

/**
 * There is a time limit in this game. There are three NPCs that 
 * are the king, queen and princess. Players will be either a 
 * ninja or a knight. Knights have to protect the royal family 
 * that the ninjas are trying to kill. Knights cannot be killed
 * but they are slower than the ninjas.
 */
public class KnightsVsNinjasScreen extends GameScreen {

	private int statueCount;

	public KnightsVsNinjasScreen(GameParameterManager parameterManager)
	{
		super(parameterManager);
		statueCount = 4;
		setAiCount(10);
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		createAICharacters();
		for (int i = 0; i < statueCount; i++) {
			createStatue();
		}
	}
	private void createStatue() {
		Vector2 position = getMap().getRandomNonSolidTile();
		Collideable newCollideable = new Collideable(this, getGameWorld());
		newCollideable.setTilePosition((int)(position.x * newCollideable.getWidth()), (int)(position.y * newCollideable.getHeight()));
		addCollidable(newCollideable);
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
		return 2;
	}
}
