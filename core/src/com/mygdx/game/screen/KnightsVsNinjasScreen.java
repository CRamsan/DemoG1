package com.mygdx.game.screen;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gameelements.Collideable;
import com.mygdx.game.gameelements.GameParameterManager;
import com.mygdx.game.gameelements.player.PlayerCharacter;
import com.mygdx.game.ui.UISystem;

/**
 * There is a time limit in this game. There are three NPCs that 
 * are the king, queen and princess. Players will be either a 
 * ninja or a knight. Knights have to protect the royal family 
 * that the ninjas are trying to kill. Knights cannot be killed
 * but they are slower than the ninjas.
 */
public class KnightsVsNinjasScreen extends GameScreen {

	private int statueCount;

	public KnightsVsNinjasScreen(boolean isFrameLimited, GameParameterManager parameterManager)
	{
		super(isFrameLimited, parameterManager);
		statueCount = 4;
		aiCount = 10;
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
		Vector2 position = this.map.getRandomNonSolidTile();
		Collideable newCollideable = new Collideable(this, gameWorld);
		newCollideable.setTilePosition((int)(position.x * newCollideable.getWidth()), (int)(position.y * newCollideable.getHeight()));
		addCollidable(newCollideable);
	}

	@Override
	public void onCharacterCollideableTouched(Collideable collideable, int collideableIndex, PlayerCharacter player) {
		if (collideableIndex == this.statueCount) {
			disableAllPlayers();
			UISystem.displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 2;
	}
}
