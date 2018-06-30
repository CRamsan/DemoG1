package com.cramsan.demog1.gameelements.player;

public interface PlayerControllerAdapterInterface {
	void handleControllerInput(PlayerControllerAdapter.INPUT inputCode, boolean value);
	void handleControllerInput(PlayerControllerAdapter.AXIS axisCode, float value);
}
