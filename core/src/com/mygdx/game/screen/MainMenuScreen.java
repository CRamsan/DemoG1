package com.mygdx.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.mygdx.game.ControllerManager;
import com.mygdx.game.ui.UISystem;

public class MainMenuScreen extends MyGdxBaseScreen implements Screen, ControllerManager.ControllerConnectionListener {

	public MainMenuScreen()
	{
		super();
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		UISystem.displayMainMenu();
	}

	@Override
	protected void performCustomUpdate() {

	}

	@Override
	protected void performDebugRender() {

	}

	@Override
	protected void performRenderMap() {
		// There is no map in this screen
	}

	@Override
	protected void performRenderSprites() {
		// There are no sprites in this screen
	}

	@Override
	public void dispose()
	{
		UISystem.disposeMenu();
	}

	@Override
	public void show() {

	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
    }

	@Override
	public void hide() {

	}

	@Override
	public void onControllerConnected(int port, Controller controller) {

	}


	@Override
	public void onControllerDisconnected(int port, Controller controller) {

	}
}
