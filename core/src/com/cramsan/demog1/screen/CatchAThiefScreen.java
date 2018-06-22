package com.cramsan.demog1.screen;

import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.gameelements.Collideable;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.gameelements.GameParameterManager;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;

import java.util.ArrayList;

/**
 * There are two teams, snipers and thieves. Thieves will try 
 * to touch all the coins and snipers will try to kill all thieves.
 * A sniper can tag a character as innocent. Snipers only have a
 * limited ammount of bullets.
 */
public class CatchAThiefScreen extends GameScreen {

	private int countCount;
	private ArrayList<GameElement> removedQueue;

	public CatchAThiefScreen(GameParameterManager parameterManager)
	{
		super(parameterManager);
		countCount = parameterManager.getGoal();
		setAiCount(10);
		removedQueue = new ArrayList<GameElement>();
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		createAICharacters();
		for (int i = 0; i < countCount; i++) {
			createCoin();
		}
		for (PlayerCharacter character : getPlayerList()) {
			if (character.getType() == GameElement.TYPE.CHAR_RETICLE)
				addLightSource(character);
		}
	}
	private void createCoin() {
		Vector2 position = getMap().getRandomNonSolidTile();
		Collideable newCollideable = new Collideable(this, getGameWorld());
		newCollideable.setTilePosition((int)(position.x * newCollideable.getWidth()), (int)(position.y * newCollideable.getHeight()));
		newCollideable.setScale(0.5f);
		addCollidable(newCollideable);
	}

	@Override
	protected void performCustomUpdate(float delta) {
		super.performCustomUpdate(delta);
		for (GameElement collideable : removedQueue)
		{
			removeCollidable(collideable);
		}
		removedQueue.clear();
	}

	@Override
	public void onCharacterCollideableTouched(GameElement collideable, int collideableIndex, PlayerCharacter player) {
		removedQueue.add(collideable);
		if (collideableIndex == this.countCount) {
			disableAllPlayers();
			getUiSystem().displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 2;
	}
}
