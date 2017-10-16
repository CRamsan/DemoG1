package com.mygdx.game.screen;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Globals;
import com.mygdx.game.TiledGameMap;
import com.mygdx.game.gameelements.*;
import com.mygdx.game.ui.UISystem;

import java.util.*;

public class MyGdxScreen extends MyGdxBaseScreen implements CharacterEventListener, GameStateManager {

	private List<BaseCharacter> characterList;
	private List<PlayerCharacter> playerList;
	private List<Statue> statueList;
	private Map<Integer, PlayerCharacter> playerCharacterMap;
	private ShapeRenderer debugRenderer;

	private boolean isPaused;
	private int statueCount;
	private int aiCount;

	public MyGdxScreen(boolean isFrameLimited)
	{
		super(isFrameLimited);
		characterList = new ArrayList<BaseCharacter>();
		playerList = new ArrayList<PlayerCharacter>();
		statueList = new ArrayList<Statue>();
		playerCharacterMap = new HashMap<Integer, PlayerCharacter>();
		debugRenderer = new ShapeRenderer();
		isPaused = false;
		statueCount = 4;
		aiCount = 10;
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		for (int i = 0; i < aiCount; i++) {
			createAICharacter();
		}
		for (int i = 0; i < statueCount; i++) {
			createStatue();
		}
		UISystem.initPauseMenu();
	}

	@Override
	protected void performCustomUpdate(float delta) {
		for (Statue statue : statueList) {
			statue.update(delta);
		}
		for (GameElement character : characterList) {
			character.update(delta);
		}
		for (PlayerCharacter player : playerList) {
			if (player.hasMoved()) {
				for (Statue statue : statueList) {
					if (player.getCenterPosition().dst(statue.getCenterPosition()) < 1) {
						player.onStatueContact(statue);
					}
				}
			}
		}
	}

	@Override
	protected void performCustomUpdate() {
		performCustomUpdate(Globals.FRAME_TIME);
	}

	@Override
	protected void performDebugRender() {
		debugRenderer.setProjectionMatrix(cam.combined);
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);
		debugRenderer.setColor(Color.YELLOW);
		debugRenderer.rect(0, 0, map.getWidth(), map.getHeight());
		for (GameElement character : characterList) {
			debugRenderer.rect(character.getX(), character.getY(), 1, 1);
		}
		for (GameElement statue : statueList) {
			debugRenderer.rect(statue.getX(), statue.getY(), 1, 1);
		}
		debugRenderer.end();
	}

	@Override
	protected void performRenderSprites() {
		for (Statue statue : statueList) {
			statue.draw(batch);
		}
		for (GameElement charac : characterList) {
			charac.draw(batch);
		}
	}

	@Override
	protected void performRenderMap() {
		map.render(cam);
	}

	private void createPlayerCharacter(int index, Controller controller) {
		PlayerCharacter newChar = new PlayerCharacter(index, GameElement.TYPE.EARTH, this, this);
		newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
		newChar.setController(controller);
		characterList.add(newChar);
		playerList.add(newChar);
		playerCharacterMap.put(index, newChar);
	}

	private void createAICharacter() {
		GameElement.TYPE type = GameElement.TYPE.LIGHT;
		AICharacter newChar = new AICharacter(type, this, this);
		newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
		characterList.add(newChar);
	}

	private void createStatue() {
		Statue newStatue = new Statue(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()), this);
		statueList.add(newStatue);
	}

	@Override
	public void dispose()
	{
		super.dispose();
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
	public void attack(PlayerCharacter character) {
	    for (BaseCharacter otherCharacter : characterList) {
	        if (character.equals(otherCharacter))
	            continue;
	        if (character.getCenterPosition().dst(otherCharacter.getCenterPosition()) < 0.5) {
	            otherCharacter.onKilled(character);
	            break;
            }
        }
	}

    @Override
    public void pause(PlayerCharacter character) {
	    if (isPaused)
        {
            UISystem.hideMenu();
            isPaused = false;
        }
        else {
            UISystem.displayPauseMenu();
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

	@Override
	public boolean isSolid(int x, int y) {
		return this.map.isSolid(x, y);
	}

	@Override
	public void onNewStatueTouched(int statueCount, PlayerCharacter player) {
		if (statueCount == this.statueCount) {

		}
	}

	@Override
	public void onPlayerDied(PlayerCharacter  victim, PlayerCharacter killer) {
		if (playerCharacterMap.remove(victim.getId()) != null) {
		}

	}
}
