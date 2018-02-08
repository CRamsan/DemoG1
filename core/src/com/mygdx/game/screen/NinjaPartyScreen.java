package com.mygdx.game.screen;

import com.mygdx.game.Globals;
import com.mygdx.game.gameelements.AICharacter;
import com.mygdx.game.gameelements.Collideable;
import com.mygdx.game.gameelements.GameElement;
import com.mygdx.game.gameelements.GameParameterManager;
import com.mygdx.game.gameelements.player.PlayerCharacter;
import com.mygdx.game.ui.UISystem;

public class NinjaPartyScreen extends GameScreen {

	private int statueCount;
	private int aiCount;

	public NinjaPartyScreen(boolean isFrameLimited, GameParameterManager parameterManager)
	{
		super(isFrameLimited);
		statueCount = parameterManager.getGoal();
		aiCount = 10;
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		for (int i = 0; i < aiCount; i++) {
			createAICharacter();
		}
		for (int i = 0; i < statueCount; i++) {
			createStatue();
		}
	}

	private void createStatue() {
		Collideable newCollideable = new Collideable(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()), this);
		addCollidable(newCollideable);
	}

	protected void createAICharacter() {
		GameElement.TYPE type = GameElement.TYPE.LIGHT;
		AICharacter newChar = new AICharacter(type, this, map);
		newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
		addAICharacter(newChar);
	}

	@Override
	public void onCharacterCollideableTouched(int collideableIndex, PlayerCharacter player) {
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
