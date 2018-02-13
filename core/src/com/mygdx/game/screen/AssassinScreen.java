package com.mygdx.game.screen;

import com.mygdx.game.Globals;
import com.mygdx.game.gameelements.AICharacter;
import com.mygdx.game.gameelements.Collideable;
import com.mygdx.game.gameelements.GameElement;
import com.mygdx.game.gameelements.GameParameterManager;
import com.mygdx.game.gameelements.player.PlayerCharacter;
import com.mygdx.game.ui.UISystem;

public class AssassinScreen extends GameScreen {

	private int aiCount;
	private int aiKilled;

	public AssassinScreen(boolean isFrameLimited, GameParameterManager parameterManager)
	{
		super(isFrameLimited, parameterManager);
		aiCount = 2;
		aiKilled = 0;
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		for (int i = 0; i < aiCount; i++) {
			createAICharacter();
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

	}

	@Override
	public void onAICharacterDied(AICharacter victim, PlayerCharacter killer) {
		aiKilled++;
		if(aiKilled == aiCount) {
			disableAllPlayers();
			UISystem.displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 6;
	}
}
