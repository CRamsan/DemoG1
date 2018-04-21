package com.mygdx.game.screen;

import com.badlogic.gdx.math.Vector2;
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
		for (PlayerCharacter character : playerList) {
			if (character.getType() == GameElement.TYPE.CHAR_RETICLE)
				addLightSource(character);
		}
	}
	private void createCoin() {
		Vector2 position = this.map.getRandomNonSolidTile();
		Collideable newCollideable = new Collideable((int)position.x, (int)position.y, this);
		newCollideable.setScale(0.5f);
		addCollidable(newCollideable);
	}

	protected void createAICharacter() {
		AICharacter newChar = new AICharacter(GameElement.TYPE.CHAR_BASEAI, this, map);
		Vector2 position = this.map.getRandomNonSolidTile();
		newChar.setPosition((int)position.x, (int)position.y);
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
