package com.mygdx.game.gameelements.player;

public interface PlayerControllerAdapterInterface {
	void handleControllerInput(int buttonCode, boolean value);
	void handleControllerInput(int axisCode, float value);
}
