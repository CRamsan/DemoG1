package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Globals;
import com.mygdx.game.TiledGameMap;
import com.mygdx.game.character.AICharacter;
import com.mygdx.game.character.BaseCharacter;
import com.mygdx.game.character.CharacterEventListener;
import com.mygdx.game.character.PlayerCharacter;
import com.mygdx.game.ui.UISystem;

import java.util.*;

public class MyGdxScreen extends MyGdxBaseScreen implements CharacterEventListener {

	private List<BaseCharacter> charactersList;
	private Map<Integer, PlayerCharacter> playerCharacterMap;
	private TiledGameMap map;
	private ShapeRenderer debugRenderer;

	private boolean isPaused;

	public MyGdxScreen()
	{
		super();
		charactersList = new ArrayList<BaseCharacter>();
		playerCharacterMap = new HashMap<Integer, PlayerCharacter>();
		map = new TiledGameMap();
		debugRenderer = new ShapeRenderer();
		isPaused = false;
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		float viewHeight = map.getHeight();
		float halfViewSize = (viewHeight / 2f);
		cam.position.set(halfViewSize, halfViewSize, 1);
		for (int i = 0; i < 10; i++) {
			createAICharacter();
		}
	}

	@Override
	protected void performCustomUpdate() {
		for (BaseCharacter character : charactersList) {
			character.update();
		}
	}

	@Override
	protected void performDebugRender() {
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);
		debugRenderer.setColor(Color.YELLOW);
		debugRenderer.rect(0, 0, map.getWidth(), map.getHeight());
		for (BaseCharacter character : charactersList) {
			debugRenderer.rect(character.getX(), character.getY(), 1, 1);
		}
		debugRenderer.end();
	}

	@Override
	protected void performRenderSprites() {
		for (BaseCharacter charac : charactersList) {
			charac.draw(batch);
		}
	}

	@Override
	protected void performRenderMap() {
		map.render(cam);
	}

	private void createPlayerCharacter(int index, Controller controller) {
		PlayerCharacter newChar = new PlayerCharacter(BaseCharacter.TYPE.EARTH, this);
		newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
		newChar.setController(controller);
		charactersList.add(newChar);
		playerCharacterMap.put(index, newChar);
	}

	private void createAICharacter() {
		BaseCharacter.TYPE type = BaseCharacter.TYPE.values()[Globals.rand.nextInt(BaseCharacter.TYPE.values().length)];
		AICharacter newChar = new AICharacter(type, this);
		newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
		charactersList.add(newChar);
	}

	@Override
	public void dispose()
	{
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
	public void attack(BaseCharacter character) {
	    for (BaseCharacter otherCharacter : charactersList) {
	        if (character.equals(otherCharacter))
	            continue;
	        if (character.getCenterPosition().dst(otherCharacter.getCenterPosition()) < 0.5) {
	            otherCharacter.onKilled();
	            break;
            }
        }
	}

    @Override
    public void pause(BaseCharacter character) {
	    if (isPaused)
        {
            UISystem.disposeMenu();
            isPaused = false;
        }
        else {
            UISystem.displayMainMenu();
            isPaused = true;
        }
    }

	@Override
	public void onControllerConnected(int port, Controller controller) {
		if (playerCharacterMap.containsKey(port)) {
			enablePlayerCharacter(port, controller);
		} else {
			createPlayerCharacter(port, controller);
		}
	}

	@Override
	public void onControllerDisconnected(int port, Controller controller) {
		if (playerCharacterMap.containsKey(port)) {
			disablePlayerCharacter(port);
		} else {
			throw new RuntimeException("Controller was not previously connected");
		}
	}
	private void enablePlayerCharacter(int port, Controller controller) {
		PlayerCharacter character = playerCharacterMap.get(port);
		character.setController(controller);
	}

	private void disablePlayerCharacter(int port) {
		PlayerCharacter character = playerCharacterMap.get(port);
		character.removeController();
	}
}
