package com.mygdx.game.gameelements;

public class Collideable extends GameElement {
    public Collideable(float x, float y, CharacterEventListener listerner) {
        super(TYPE.DARK, x, y, listerner);
    }
}
