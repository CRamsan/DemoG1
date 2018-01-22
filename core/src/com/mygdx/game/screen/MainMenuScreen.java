package com.mygdx.game.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.mygdx.game.ControllerManager;
import com.mygdx.game.Globals;
import com.mygdx.game.gameelements.*;
import com.mygdx.game.ui.GetReadyMenuController;
import com.mygdx.game.ui.UISystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Class to manage the main manu screen. It will initialize the UI components and any objects needed or the background.
 */
public class MainMenuScreen extends MyGdxBaseScreen implements Screen, ControllerManager.ControllerConnectionListener {

	private List<BaseCharacter> characterList;
	private HashMap<Integer, Controller> preInitControllerMap;
	private GetReadyMenuController getReadyMenuController;
	private boolean hasInitCompleted;

	public MainMenuScreen(boolean isFrameLimited) {
		super(isFrameLimited);
		hasInitCompleted = false;
		characterList = new ArrayList<BaseCharacter>();
		preInitControllerMap = new LinkedHashMap<Integer, Controller>();
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		for (int i = 0; i < 20; i++) {
			GameElement.TYPE type = GameElement.TYPE.FEMALE_VILLAGER;
			AICharacter newChar = new AICharacter(type, null);
			newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
			characterList.add(newChar);
		}
		UISystem.initMainMenu();
		getReadyMenuController = UISystem.initGetReadyMenu();
		UISystem.displayMainMenu();
		hasInitCompleted = true;
		for (Integer port : preInitControllerMap.keySet()) {
			Controller controller = preInitControllerMap.get(port);
			onControllerConnected(port, controller);
		}
		preInitControllerMap.clear();
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
	public void dispose() {
		super.dispose();
	}

	@Override
	public void show() {

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {

	}

	@Override
	public void onControllerConnected(int port, Controller controller) {
		if (hasInitCompleted) {
			getReadyMenuController.addPlayer(controller.getName(), port);
		} else {
			preInitControllerMap.put(port, controller);
		}
	}


	@Override
	public void onControllerDisconnected(int port, Controller controller) {
		getReadyMenuController.removePlayer(port);
	}

	protected int levelId() {
			return 0;
	}
}
