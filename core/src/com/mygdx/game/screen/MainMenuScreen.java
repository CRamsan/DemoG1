package com.mygdx.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.mygdx.game.ControllerManager;
import com.mygdx.game.Globals;
import com.mygdx.game.gameelements.*;
import com.mygdx.game.ui.UISystem;

import java.util.ArrayList;
import java.util.List;

public class MainMenuScreen extends MyGdxBaseScreen implements Screen, ControllerManager.ControllerConnectionListener, GameStateManager {

	private List<BaseCharacter> characterList;

	public MainMenuScreen(boolean isFrameLimited)
	{
		super(isFrameLimited);
		characterList = new ArrayList<BaseCharacter>();
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		for (int i = 0; i < 20; i++) {
			GameElement.TYPE type = GameElement.TYPE.FEMALE_VILLAGER;
			AICharacter newChar = new AICharacter(type, null, this);
			newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
			characterList.add(newChar);
		}
		UISystem.displayMainMenu();
	}

	@Override
	protected void performCustomUpdate() {
		performCustomUpdate(Globals.FRAME_TIME);
	}

	@Override
	protected void performCustomUpdate(float delta) {
		for (GameElement character : characterList) {
			character.update(delta);
		}
	}

	@Override
	protected void performDebugRender() {

	}

	@Override
	protected void performRenderMap() {
		map.render(cam);
	}

	@Override
	protected void performRenderSprites() {
		for (GameElement charac : characterList) {
			charac.draw(batch);
		}
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

	@Override
	public boolean isSolid(int x, int y) {
		return this.map.isSolid(x, y);
	}
}
