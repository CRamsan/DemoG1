package com.mygdx.game.gameelements;

/***
 * This interface should be implemented by the object that wants to receive the events from the Character.
 * This will be an instance of whatever is handling all the Characters. A common use of this interface is to detect
 * when a character attacks, the listener should handle the event and determine if another character was killed.
 */
public interface CharacterEventListener {
    void onCharacterAttack(PlayerCharacter character);
    void onCharacterPause(PlayerCharacter character);
    void onCharacterCollideableTouched(int statueCount, PlayerCharacter player);
    void onCharacterDied(PlayerCharacter victim, PlayerCharacter killer);
}
