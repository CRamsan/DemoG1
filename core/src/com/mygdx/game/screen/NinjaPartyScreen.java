package com.mygdx.game.screen;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.gameelements.AICharacter;
import com.mygdx.game.gameelements.Collideable;
import com.mygdx.game.gameelements.GameElement;
import com.mygdx.game.gameelements.GameParameterManager;
import com.mygdx.game.gameelements.player.PlayerCharacter;
import com.mygdx.game.ui.UISystem;

/**
 * There are two winning conditions. Either touch the five statues or
 * be the last player alive. You have one smoke bomb that you can use.
 */
public class NinjaPartyScreen extends GameScreen {

	private int statueCount;
	private int aiCount;

	public NinjaPartyScreen(boolean isFrameLimited, GameParameterManager parameterManager)
	{
		super(isFrameLimited, parameterManager);
		aiCount = 100;
		statueCount = 0;
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		createAICharacters();
		createStatues();
	}

	private void createStatues() {
		for (Vector2 pos : map.getStatueSpawner()) {
			Collideable newCollideable = new Collideable(pos.x, pos.y, this);
			addCollidable(newCollideable);
			addLightSource(newCollideable);
			statueCount++;
		}
	}

	protected void createAICharacters() {
		GameElement.TYPE type = GameElement.TYPE.CHAR_BASEAI;
		int counter = 0;
		while (true) {
			AICharacter newChar = new AICharacter(type, this, map);
			Vector2 characterPos = this.map.getRandomNonSolidTile();
			newChar.setPosition((int)characterPos.x, (int)characterPos.y);
			addAICharacter(newChar);
			counter++;
			if (counter >= aiCount)
				break;
		}
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
		return 1;
	}
}
