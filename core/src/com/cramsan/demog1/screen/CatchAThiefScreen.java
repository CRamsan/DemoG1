package com.cramsan.demog1.screen;

import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.gameelements.Collidable;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.GameParameterManager;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;
import com.cramsan.demog1.subsystems.AudioManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * There are two teams, snipers and thieves. Thieves will try 
 * to touch all the coins and snipers will try to kill all thieves.
 * A sniper can tag a character as innocent. Snipers only have a
 * limited amount of bullets.
 */
public class CatchAThiefScreen extends GameScreen {

	private int coinCount;
	private ArrayList<GameElement> removedQueue;
	private HashMap<PlayerCharacter, Set<Collidable>> scoreMap;

	public CatchAThiefScreen(GameParameterManager parameterManager)
	{
		super(parameterManager);
		coinCount = parameterManager.getGoal();
		setAiCount(10);
		removedQueue = new ArrayList<GameElement>();
		scoreMap = new HashMap<PlayerCharacter, Set<Collidable>>();
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		createAICharacters();
		for (int i = 0; i < coinCount; i++) {
			createCoin();
		}
		for (PlayerCharacter character : getPlayerList()) {
			if (character.getType() == GameElement.TYPE.CHAR_RETICLE)
				addLightSource(character);
		}
	}
	private void createCoin() {
		Vector2 position = getMap().getRandomNonSolidTile();
		Collidable newCollidable = new Collidable(this, getGameWorld(), getAssetManager());
		newCollidable.setTilePosition((int)(position.x * newCollidable.getWidth()), (int)(position.y * newCollidable.getHeight()));
		newCollidable.setScale(0.5f);
		addCollidable(newCollidable);
	}

	@Override
	protected void performCustomUpdate(float delta) {
		super.performCustomUpdate(delta);
		for (GameElement collidable : removedQueue)
		{
			removeCollidable(collidable);
		}
		removedQueue.clear();
	}

	@Override
	public void onCharacterCollidableTouched(Collidable collidable, PlayerCharacter player) {
		removedQueue.add(collidable);
		if (!scoreMap.containsKey(player)) {
			scoreMap.put(player, new HashSet<Collidable>());
		}

		Set<Collidable> statueSet = scoreMap.get(player);
		if (statueSet.contains(collidable))
			return;

		statueSet.add(collidable);
		getAudioManager().PlaySound(AudioManager.SOUND.BELL);

		if (statueSet.size() == this.coinCount) {
			disableAllPlayers();
			getUiSystem().displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 2;
	}
}
