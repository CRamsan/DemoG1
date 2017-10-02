package com.mygdx.game.character;

/***
 * This interface should be implemented by the object that wants to recieve the events from the Character.
 * This will be an instance of whatever is handling all the Characters. A common use of this interface is to detect
 * when a character attacks, the listener should handle the event and determine if another character was killed.
 */
public interface CharacterEventListener {
    public void attack(BaseCharacter character);
    public void pause(BaseCharacter character);
}
