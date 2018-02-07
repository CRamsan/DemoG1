package com.mygdx.game.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Globals;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.gameelements.*;
import com.mygdx.game.gameelements.player.PlayerCharacter;
import com.mygdx.game.ui.PauseMenuEventListener;
import com.mygdx.game.ui.UISystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyGdxScreen extends MyGdxBaseScreen implements CharacterEventListener, PauseMenuEventListener {

	private List<BaseCharacter> characterList;
	private List<PlayerCharacter> playerList;
	private List<Collideable> collideableList;
	private Map<Integer, PlayerCharacter> playerCharacterMap;
	private GameParameterManager gameParameters;
	private ShapeRenderer debugRenderer;

	private boolean isPaused;
	private int statueCount;
	private int aiCount;

	public MyGdxScreen(boolean isFrameLimited, GameParameterManager parameterManager)
	{
		super(isFrameLimited);
		characterList = new ArrayList<BaseCharacter>();
		playerList = new ArrayList<PlayerCharacter>();
		collideableList = new ArrayList<Collideable>();
		debugRenderer = new ShapeRenderer();
		playerCharacterMap = new HashMap<Integer, PlayerCharacter>();
		gameParameters = parameterManager;
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
		UISystem.initPauseMenu(this);
		UISystem.initConfirmationMenu();
		UISystem.initEndGameMenu();
	}

	@Override
	protected void performCustomUpdate(float delta) {
		for (Collideable collideable : collideableList) {
			collideable.update(delta);
		}
		for (BaseCharacter character : characterList) {
			character.updateInputs();
		}

		if (isPaused)
			return;

		for (BaseCharacter character : characterList) {
			character.update(delta);
		}
		for (PlayerCharacter player : playerList) {
			if (player.hasMoved()) {
				for (Collideable collideable : collideableList) {
					if (player.getCenterPosition().dst(collideable.getCenterPosition()) < 1) {
						player.onStatueContact(collideable);
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
		for (GameElement character : characterList) {
			debugRenderer.rect(character.getX() * character.getWidth(), character.getY() * character.getHeight(), character.getWidth(), character.getHeight());
		}
		for (GameElement statue : collideableList) {
			debugRenderer.rect(statue.getX() * statue.getWidth(), statue.getY() * statue.getHeight(), statue.getWidth(), statue.getHeight());
		}
		debugRenderer.end();
	}

	@Override
	protected void performRenderSprites() {
		for (Collideable collideable : collideableList) {
			collideable.draw(batch);
		}
		for (GameElement charac : characterList) {
			charac.draw(batch);
		}
	}

	@Override
	protected void performRenderMap() {
		map.render(cam);
	}

	private void createPlayerCharacter(int index, PlayerController controller) {
		PlayerCharacter newChar = new PlayerCharacter(index, GameElement.TYPE.EARTH, this, map);
		newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
		newChar.setController(controller);
		characterList.add(newChar);
		playerList.add(newChar);
		playerCharacterMap.put(index, newChar);
	}

	private void createAICharacter() {
		GameElement.TYPE type = GameElement.TYPE.LIGHT;
		AICharacter newChar = new AICharacter(type, this, map);
		newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
		characterList.add(newChar);
	}

	private void createStatue() {
		Collideable newCollideable = new Collideable(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()), this);
		collideableList.add(newCollideable);
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
	public void onCharacterAttack(PlayerCharacter character) {
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
    public void onCharacterPause(PlayerCharacter character) {
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
	public void onControllerConnected(int port, PlayerController controller) {
		if (playerCharacterMap.containsKey(port)) {
			enablePlayerCharacter(port, controller);
		} else {
			createPlayerCharacter(port, controller);
		}
	}

	@Override
	public void onControllerDisconnected(int port, PlayerController controller) {
		if (playerCharacterMap.containsKey(port)) {
			disablePlayerCharacter(port);
		} else {
			throw new RuntimeException("Controller was not previously connected");
		}
	}
	private void enablePlayerCharacter(int port, PlayerController controller) {
		PlayerCharacter character = playerCharacterMap.get(port);
		character.setController(controller);
	}

	private void disablePlayerCharacter(int port) {
		PlayerCharacter character = playerCharacterMap.get(port);
		character.removeController();
	}

	@Override
	public void onCharacterCollideableTouched(int statueCount, PlayerCharacter player) {
		if (statueCount == this.statueCount) {
			for (PlayerCharacter closingPlayer : playerList) {
				closingPlayer.disableCharacter();
			}
			UISystem.displayEndGameMenu();
		}
	}

	@Override
	public void onCharacterDied(PlayerCharacter  victim, PlayerCharacter killer) {
		if (playerCharacterMap.remove(victim.getId()) != null) {
		}

	}

	protected int levelId() {
		return 1;
	}

	@Override
	public void onPauseMenuAppeared() {
	}

	@Override
	public void onPauseMenuDisappeared() {
		isPaused = false;
	}
}
