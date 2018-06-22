package com.cramsan.demog1.screen;

import com.cramsan.demog1.subsystems.CallbackManager;
import com.cramsan.demog1.gameelements.*;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;

/**
 * There are two teams, assasins and snipers. The assasins are
 * trying to kill all NPCs while snipers are trying to kill 
 * all assasins. 
 */
public class AssassinScreen extends GameScreen {

	private int aiKilled;

	public AssassinScreen(GameParameterManager parameterManager)
	{
		super(parameterManager);
		setAiCount(10);
		aiKilled = 0;
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		createAICharacters();
	}

	@Override
	public void onCharacterCollideableTouched(GameElement collideable, int collideableIndex, PlayerCharacter player) {

	}

	@Override
	public void onCharacterAttack(final PlayerCharacter character) {
	    for (final BaseCharacter otherCharacter : getCharacterList()) {
	        if (character.equals(otherCharacter))
	            continue;
			
	        if (otherCharacter.getType() == GameElement.TYPE.CHAR_RETICLE) {
	        	// Ignore collisions with other Snipers
	        	continue;
			}
			
			// TODO: If the attacker is coming from an assasin, then ignore hits to other assasins

	        if (character.getCenterPosition().dst(otherCharacter.getCenterPosition()) < 0.5) {
				getCallbackManager().registerEventFromNow(POISON_TIME, new CallbackManager.ExecutioBlockInterface() {
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
		if(aiKilled == getAiCount()) {
			disableAllPlayers();
			getUiSystem().displayEndGameMenu();
		}
	}

	@Override
	protected int levelId() {
		return 6;
	}
}
