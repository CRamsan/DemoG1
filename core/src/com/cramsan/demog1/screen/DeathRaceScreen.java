package com.cramsan.demog1.screen;

import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.gameelements.Collidable;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.gameelements.GameParameterManager;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;

/**
 * There are two teams, one with players and NPCs walking and running
 * towards the finish line. The other team is of snipers that can shoot
 * the characters. Players have the option to run, which NPCs will never do.
 */
public class DeathRaceScreen extends GameScreen {

	private int statueCount;

	public DeathRaceScreen(GameParameterManager parameterManager)
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
	public void onCharacterCollidableTouched(GameElement collidable, int collidableIndex, PlayerCharacter player) {
		if (collidableIndex == this.statueCount) {
			disableAllPlayers();
			getUiSystem().displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 2;
	}
}
