package com.cramsan.demog1.gameelements;

import com.cramsan.demog1.gameelements.player.PlayerCharacter;

public class CharacterEventAdapter implements CharacterEventListener{
    public void onCharacterAttack(PlayerCharacter character) {}
    public void onCharacterPause(PlayerCharacter character) {}
    public void onCharacterCollidableTouched(Collidable collidable, PlayerCharacter player) {}
    public void onPlayerCharacterDied(PlayerCharacter victim, PlayerCharacter killer) {}
    public void onAICharacterDied(AICharacter victim, PlayerCharacter killer) {}
}
