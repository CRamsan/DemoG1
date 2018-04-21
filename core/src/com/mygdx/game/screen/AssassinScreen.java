package com.mygdx.game.screen;

import com.mygdx.game.CallbackManager;
import com.mygdx.game.gameelements.*;
import com.mygdx.game.gameelements.player.PlayerCharacter;
import com.mygdx.game.ui.UISystem;

/**
 * There are two teams, assasins and snipers. The assasins are
 * trying to kill all NPCs while snipers are trying to kill 
 * all assasins. 
 */
public class AssassinScreen extends GameScreen {

	private int aiKilled;

	public AssassinScreen(boolean isFrameLimited, GameParameterManager parameterManager)
	{
		super(isFrameLimited, parameterManager);
		aiCount = 10;
		aiKilled = 0;
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		createAICharacters();
	}

	@Override
	public void onCharacterCollideableTouched(Collideable collideable, int collideableIndex, PlayerCharacter player) {

	}

	@Override
	public void onCharacterAttack(final PlayerCharacter character) {
	    for (final BaseCharacter otherCharacter : characterList) {
	        if (character.equals(otherCharacter))
	            continue;
			
	        if (otherCharacter.getType() == GameElement.TYPE.CHAR_RETICLE) {
	        	// Ignore collisions with other Snipers
	        	continue;
			}
			
			// TODO: If the attacker is coming from an assasin, then ignore hits to other assasins

	        if (character.getCenterPosition().dst(otherCharacter.getCenterPosition()) < 0.5) {
				callbackManager.registerEventFromNow(POISON_TIME, new CallbackManager.ExecutioBlockInterface() {
						@Override
						public void execute()
						{
							otherCharacter.onKilled(character);							
						}
				});
	            break;
            }
        }
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
