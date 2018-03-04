package com.mygdx.game.screen;

import com.mygdx.game.Globals;
import com.mygdx.game.gameelements.AICharacter;
import com.mygdx.game.gameelements.Collideable;
import com.mygdx.game.gameelements.GameElement;
import com.mygdx.game.gameelements.GameParameterManager;
import com.mygdx.game.gameelements.player.PlayerCharacter;
import com.mygdx.game.ui.UISystem;

import java.util.ArrayList;

/**
 * There are two teams, snipers and thieves. Thieves will try 
 * to touch all the coins and snipers will try to kill all thieves.
 * A sniper can tag a character as innocent. Snipers only have a
 * limited ammount of bullets.
 */
public class CatchAThiefScreen extends GameScreen {

	private int countCount;
	private int aiCount;
	private ArrayList<Collideable> removedQueue;

	public CatchAThiefScreen(boolean isFrameLimited, GameParameterManager parameterManager)
	{
		super(isFrameLimited, parameterManager);
		countCount = parameterManager.getGoal();
		aiCount = 10;
		removedQueue = new ArrayList<Collideable>();
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		for (int i = 0; i < aiCount; i++) {
			createAICharacter();
		}
		for (int i = 0; i < countCount; i++) {
			createCoin();
		}
	}
	private void createCoin() {
		Collideable newCollideable = new Collideable(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()), this);
		newCollideable.setScale(0.2f);
		addCollidable(newCollideable);
	}

	protected void createAICharacter() {
		GameElement.TYPE type = GameElement.TYPE.CHAR_BASEAI;
		AICharacter newChar = new AICharacter(type, this, map);
		newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
		addAICharacter(newChar);
	}

	@Override
	protected void performCustomUpdate(float delta) {
		super.performCustomUpdate(delta);
		for (Collideable collideable : removedQueue)
		{
			removeCollidable(collideable);
		}
		removedQueue.clear();
	}

	@Override
	public void onCharacterCollideableTouched(Collideable collideable, int collideableIndex, PlayerCharacter player) {
		removedQueue.add(collideable);
		if (collideableIndex == this.countCount) {
			disableAllPlayers();
			UISystem.displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 2;
	}
}
