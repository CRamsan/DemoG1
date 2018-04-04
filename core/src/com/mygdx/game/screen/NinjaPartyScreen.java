package com.mygdx.game.screen;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Globals;
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
		statueCount = parameterManager.getGoal();
		aiCount = 0;
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		for (int i = 0; i < aiCount; i++) {
			createAICharacter();
		}

		createStatues();
	}

	private void createStatues() {
		for (Vector2 pos : map.getStatueSpawner()) {
			Collideable newCollideable = new Collideable(pos.x, pos.y, this);
			addCollidable(newCollideable);
			addLightSource(newCollideable);
		}
	}

	protected void createAICharacter() {
		GameElement.TYPE type = GameElement.TYPE.CHAR_BASEAI;
		AICharacter newChar = new AICharacter(type, this, map);
		newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
		addAICharacter(newChar);
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
