package com.cramsan.demog1.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.cramsan.demog1.subsystems.controller.ControllerConnectionListener;
import com.cramsan.demog1.subsystems.controller.PlayerController;
import com.cramsan.demog1.gameelements.AICharacter;
import com.cramsan.demog1.gameelements.BaseCharacter;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.subsystems.ui.GetReadyMenuController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Class to manage the main menu screen. It will initialize the UI components and any objects needed or the background.
 */
public class MainMenuScreen extends BaseScreen implements Screen, ControllerConnectionListener {

	private List<BaseCharacter> characterList;
	private HashMap<Integer, PlayerController> preInitControllerMap;
	private GetReadyMenuController getReadyMenuController;
	private boolean hasInitCompleted;
	private boolean hasGetReadyMenuController;

	public MainMenuScreen() {
		super();
		hasInitCompleted = false;
		characterList = new ArrayList<BaseCharacter>();
		preInitControllerMap = new LinkedHashMap<Integer, PlayerController>();
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		for (int i = 0; i < 1; i++) {
			GameElement.TYPE type = GameElement.TYPE.FEMALE_VILLAGER;
			AICharacter newChar = new AICharacter(type, null, getGameWorld(), getAssetManager(), getMap().getTileWidth());
			Vector2 position = getMap().getRandomNonSolidTile();
			newChar.setTilePosition((int) (position.x * newChar.getWidth()), (int)(position.y * newChar.getHeight()));
			characterList.add(newChar);
		}
		getUiSystem().initMainMenu();
		getReadyMenuController = getUiSystem().initGetReadyMenu();
		// The getReadyMenuController can be null if we are using a mocked UISystem.
		// In that case lets set a flag to ignore it.
		hasGetReadyMenuController = getReadyMenuController != null;
		getUiSystem().displayMainMenu();

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
	protected void performCustomUpdate(float delta) {
		if (hasGetReadyMenuController)
			getReadyMenuController.update(delta);

		for (GameElement character : characterList) {
			character.update(delta);
		}
	}

	@Override
	protected void performRenderSprites() {
		for (GameElement charac : characterList) {
			charac.draw(getBatch());
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
			if (hasGetReadyMenuController)
				getReadyMenuController.addPlayer(controller);
		} else {
			preInitControllerMap.put(port, controller);
		}
	}


	@Override
	public void onControllerDisconnected(int port, PlayerController controller) {
		if (hasGetReadyMenuController)
			getReadyMenuController.removePlayer(controller);
	}

	protected int levelId() {
			return 0;
	}
}
