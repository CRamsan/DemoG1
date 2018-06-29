package com.cramsan.demog1.screen;

import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.gameelements.Collidable;
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
		Collidable newCollidable = new Collidable(this, getGameWorld(), getAssetManager());
		newCollidable.setTilePosition((int)(position.x * newCollidable.getWidth()), (int)(position.y * newCollidable.getHeight()));
		addCollidable(newCollidable);
	}

	@Override
	public void onCharacterCollidableTouched(GameElement collidable, PlayerCharacter player) {
		super.onCharacterCollidableTouched(collidable, player);
		/*
		if (collidableIndex == this.statueCount) {
			disableAllPlayers();
			getUiSystem().displayEndGameMenu();
		}
		*/
	}

	@Override
	protected int levelId() {
		return 2;
	}
}
