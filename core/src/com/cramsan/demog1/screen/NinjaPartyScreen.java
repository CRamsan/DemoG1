package com.cramsan.demog1.screen;

import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.gameelements.Collidable;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.GameParameterManager;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;
import com.cramsan.demog1.subsystems.AudioManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * There are two winning conditions. Either touch the five statues or
 * be the last player alive. You have one smoke bomb that you can use.
 */
public class NinjaPartyScreen extends GameScreen {

	private int statueCount;
	private HashMap<PlayerCharacter, Set<Collidable>> scoreMap;

	public NinjaPartyScreen(GameParameterManager parameterManager)
	{
		super(parameterManager);
		setAiCount(10);
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		createAICharacters();
		createStatues();
		setAiCount(10);
		scoreMap = new HashMap<PlayerCharacter, Set<Collidable>>();
	}

	private void createStatues() {
		statueCount = 0;
		for (Vector2 pos : getMap().getStatueSpawner()) {
			GameElement newCollidable = new Collidable(this, getGameWorld(), getAssetManager());
			newCollidable.setTilePosition((int)(pos.x * newCollidable.getWidth()), (int)(pos.y * newCollidable.getHeight()));
			addCollidable(newCollidable);
			addLightSource(newCollidable);
			statueCount++;
		}
	}

	@Override
	public void onCharacterCollidableTouched(Collidable collidable, PlayerCharacter player) {
		if (!scoreMap.containsKey(player)) {
			scoreMap.put(player, new HashSet<Collidable>());
		}

		Set<Collidable> statueSet = scoreMap.get(player);
		if (statueSet.contains(collidable))
			return;

		statueSet.add(collidable);
		getAudioManager().PlaySound(AudioManager.SOUND.BELL);

		if (statueSet.size() == this.statueCount) {
			disableAllPlayers();
			getUiSystem().displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 1;
	}
}
