package com.mygdx.game.screen;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gameelements.Collideable;
import com.mygdx.game.gameelements.GameParameterManager;
import com.mygdx.game.gameelements.player.PlayerCharacter;
import com.mygdx.game.ui.UISystem;

/**
 * There are two teams, one with players and NPCs walking and running
 * towards the finish line. The other team is of snipers that can shoot
 * the characters. Players have the option to run, which NPCs will never do.
 */
public class DeathRaceScreen extends GameScreen {

	private int statueCount;

	public DeathRaceScreen(boolean isFrameLimited, GameParameterManager parameterManager)
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
		Collideable newCollideable = new Collideable((int)position.x, (int)position.y, this);
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
