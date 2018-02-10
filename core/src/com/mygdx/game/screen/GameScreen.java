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

public abstract class GameScreen extends BaseScreen implements CharacterEventListener, PauseMenuEventListener {

	private List<BaseCharacter> characterList;
	private List<PlayerCharacter> playerList;
	private List<Collideable> collideableList;
	private Map<Integer, PlayerCharacter> playerCharacterMap;
	private ShapeRenderer debugRenderer;
	private PlayerCharacter pauseCaller;
	private GameParameterManager parameterManager;

	private boolean isPaused;

	public GameScreen(boolean isFrameLimited, GameParameterManager parameterManager)
	{
		super(isFrameLimited);
		characterList = new ArrayList<BaseCharacter>();
		playerList = new ArrayList<PlayerCharacter>();
		collideableList = new ArrayList<Collideable>();
		debugRenderer = new ShapeRenderer();
		playerCharacterMap = new HashMap<Integer, PlayerCharacter>();
		isPaused = false;
		this.parameterManager = parameterManager;
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
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
					if (player.getCenterPosition().dst(collideable.getCenterPosition()) < (player.getRadious() + collideable.getRadious())) {
						player.onCollideableContact(collideable);
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
			debugRenderer.rect(character.getX() * Globals.ASSET_SPRITE_SHEET_SPRITE_HEIGHT, character.getY() * Globals.ASSET_SPRITE_SHEET_SPRITE_HEIGHT, character.getWidth(), character.getHeight());
		}
		for (GameElement statue : collideableList) {
			debugRenderer.rect(statue.getX() * Globals.ASSET_SPRITE_SHEET_SPRITE_HEIGHT, statue.getY() * Globals.ASSET_SPRITE_SHEET_SPRITE_HEIGHT, statue.getWidth(), statue.getHeight());
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

	protected void createPlayerCharacter(int index, PlayerController controller, GameElement.TYPE type) {
		PlayerCharacter newChar = new PlayerCharacter(index, type, this, map);
		if (type == GameElement.TYPE.EARTH) {

        } else if(type == GameElement.TYPE.PIRATE){

        } else {
		    throw new RuntimeException("Type not supported for PlayerCharacter" + type);
        }
		newChar.setPosition(Globals.rand.nextInt(this.map.getWidth()), Globals.rand.nextInt(this.map.getHeight()));
		newChar.setController(controller);
		characterList.add(newChar);
		playerList.add(newChar);
		playerCharacterMap.put(index, newChar);
	}

	protected void addAICharacter(AICharacter character) {
		characterList.add(character);
	}

	protected void addCollidable(Collideable collideable) {
		collideableList.add(collideable);
	}

	protected void removeCollidable(Collideable collideable) {
		collideableList.remove(collideable);
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
	        if (otherCharacter.getType() == GameElement.TYPE.PIRATE) {
	        	// Ignore collisions with other Snipers
	        	continue;
			}
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
        	if (pauseCaller == character) {
				UISystem.hideMenu();
				isPaused = false;
			}
        }
        else {
            UISystem.displayPauseMenu();
            isPaused = true;
            pauseCaller = character;
        }
    }

	@Override
	public void onControllerConnected(int port, PlayerController controller) {
		GameElement.TYPE type = parameterManager.getTypeForPlayer(port);
		if (playerCharacterMap.containsKey(port)) {
			enablePlayerCharacter(port, controller);
		} else {
			createPlayerCharacter(port, controller, type);
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

	protected void disableAllPlayers() {
		for (BaseCharacter closingPlayer : characterList) {
			closingPlayer.disableCharacter();
		}
	}

	@Override
	public abstract void onCharacterCollideableTouched(Collideable collideable, int collideableIndex, PlayerCharacter player);

	@Override
	public void onCharacterDied(PlayerCharacter  victim, PlayerCharacter killer) {
		victim.disableCharacter();
		playerCharacterMap.remove(victim.getId());
		playerList.remove(victim);
		if (playerList.size() == 1) {
			disableAllPlayers();
			UISystem.displayEndGameMenu();
		}
	}

	protected abstract int levelId();

	@Override
	public void onPauseMenuAppeared() {
	}

	@Override
	public void onPauseMenuDisappeared() {
		isPaused = false;
		pauseCaller = null;
	}
}
