package com.mygdx.game.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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

	public NinjaPartyScreen(boolean isFrameLimited, SpriteBatch spriteBatch, GameParameterManager parameterManager)
	{
		super(isFrameLimited, spriteBatch, parameterManager);
		aiCount = 0;
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
			GameElement newCollideable = new Collideable(this, gameWorld);
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
			UISystem.displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 1;
	}
}
