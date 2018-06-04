package com.cramsan.demog1.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.controller.ControllerConnectionListener;
import com.cramsan.demog1.controller.PlayerController;
import com.cramsan.demog1.gameelements.AICharacter;
import com.cramsan.demog1.gameelements.BaseCharacter;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.ui.GetReadyMenuController;
import com.cramsan.demog1.ui.UISystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Class to manage the main manu screen. It will initialize the UI components and any objects needed or the background.
 */
public class MainMenuScreen extends BaseScreen implements Screen, ControllerConnectionListener {

	private List<BaseCharacter> characterList;
	private HashMap<Integer, PlayerController> preInitControllerMap;
	private GetReadyMenuController getReadyMenuController;
	private boolean hasInitCompleted;

	public MainMenuScreen(boolean isFrameLimited, SpriteBatch spriteBatch) {
		super(isFrameLimited, spriteBatch);
		hasInitCompleted = false;
		characterList = new ArrayList<BaseCharacter>();
		preInitControllerMap = new LinkedHashMap<Integer, PlayerController>();
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		for (int i = 0; i < 1; i++) {
			GameElement.TYPE type = GameElement.TYPE.FEMALE_VILLAGER;
			AICharacter newChar = new AICharacter(type, null, map, gameWorld);
			Vector2 position = this.map.getRandomNonSolidTile();
			newChar.setTilePosition((int) (position.x * newChar.getWidth()), (int)(position.y * newChar.getHeight()));
			characterList.add(newChar);
		}
		UISystem.initMainMenu();
		getReadyMenuController = UISystem.initGetReadyMenu();
		UISystem.displayMainMenu();

		// Now  call onControllerConnected for all the controllers in the preInitMap.
		// This will add the controllers to the GetReadyManager and then clear the mapping
		// since it is not needed anymore.
		hasInitCompleted = true;
		for (Integer port : preInitControllerMap.keySet()) {
			PlayerController controller = preInitControllerMap.get(port);
			onControllerConnected(port, controller);
		}
		preInitControllerMap.clear();
	}

	@Override
	protected void performCustomUpdate() {
		performCustomUpdate(FRAME_TIME);
	}

	@Override
	protected void performCustomUpdate(float delta) {
		getReadyMenuController.update(delta);
		for (GameElement character : characterList) {
			character.update(delta);
		}
	}

	@Override
	protected void performRenderMap(float delta) {
		map.render(cam, delta);
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
	public void onControllerConnected(int port, PlayerController controller) {
		if (hasInitCompleted) {
			getReadyMenuController.addPlayer(controller);
		} else {
			preInitControllerMap.put(port, controller);
		}
	}


	@Override
	public void onControllerDisconnected(int port, PlayerController controller) {
		getReadyMenuController.removePlayer(controller);
	}

	protected int levelId() {
			return 0;
	}
}