package com.mygdx.game.screen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
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

	public static final float POISON_TIME = 5f;

	private Map<Integer, PlayerCharacter> playerCharacterMap;
	private PlayerCharacter pauseCaller;
	private GameParameterManager parameterManager;

	private boolean isPaused;

	protected List<BaseCharacter> characterList;
	protected List<PlayerCharacter> playerList;
	protected List<Collideable> collideableList;	
	protected int aiCount;

	public GameScreen(boolean isFrameLimited, GameParameterManager parameterManager)
	{
		super(isFrameLimited);
		characterList = new ArrayList<BaseCharacter>();
		playerList = new ArrayList<PlayerCharacter>();
		collideableList = new ArrayList<Collideable>();
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
		setIllumination(0.01f);
	}

	protected void createAICharacters() {
		for (int i = 0; i < aiCount; i++) {
			GameElement.TYPE type = GameElement.TYPE.CHAR_BASEAI;
			AICharacter newChar = new AICharacter(type, this, map, gameWorld);
			Vector2 charPos = this.map.getRandomNonSolidTile();
			newChar.setTilePosition((int) (charPos.x * newChar.getWidth()), (int)(charPos.y * newChar.getHeight()));
			addAICharacter(newChar);
		}
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

		Array<Body> bodies = new Array<Body>();
		gameWorld.getBodies(bodies);

		for (Body b : bodies) {
			// Get the body's user data - in this example, our user
			// data is an instance of the Entity class
			if (b.getType() == BodyDef.BodyType.StaticBody)
				continue;
			GameElement e = (GameElement) b.getUserData();
			if (e != null) {
				e.setCenterPosition((int)b.getPosition().x, (int)b.getPosition().y);
			}
		}
	/*
		for (PlayerCharacter player : playerList) {
			// Ignore anything that is not CHAR_HUMAN, for example a type reticle will not trigger a collision
			if (player.getType() != GameElement.TYPE.CHAR_HUMAN)
				continue;
			if (player.hasMoved()) {
				for (Collideable collideable : collideableList) {
					if (player.getCenterPosition().dst(collideable.getCenterPosition()) < (player.getRadious() + collideable.getRadious())) {
						player.onCollideableContact(collideable);
					}
				}
			}
		}
	*/
	}

	@Override
	protected void performCustomUpdate() {
		performCustomUpdate(FRAME_TIME);
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
	protected void performRenderMap(float delta) {
		map.render(cam, delta);
	}

	protected void createPlayerCharacter(int index, PlayerController controller, GameElement.TYPE type) {
		PlayerCharacter newChar = new PlayerCharacter(index, type, this, map, gameWorld);
		if (type == GameElement.TYPE.CHAR_HUMAN) {

        } else if(type == GameElement.TYPE.CHAR_RETICLE){

        } else {
		    throw new RuntimeException("Type not supported for PlayerCharacter" + type);
        }
		Vector2 characterPos = this.map.getRandomNonSolidTile();
        newChar.setTilePosition((int) (characterPos.x * newChar.getWidth()), (int)(characterPos.y * newChar.getHeight()));
		newChar.setController(controller);
		characterList.add(newChar);
		playerList.add(newChar);
		playerCharacterMap.put(index, newChar);
		if (!playerFound) {
			playerFound = true;
		}
	}

	private boolean playerFound;

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
	        if (otherCharacter.getType() == GameElement.TYPE.CHAR_RETICLE) {
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
		character.disableCharacter();
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
	public void onPlayerCharacterDied(PlayerCharacter  victim, PlayerCharacter killer) {
		victim.disableCharacter();
		playerCharacterMap.remove(victim.getId());
		playerList.remove(victim);
		if (playerList.size() == 1) {
			disableAllPlayers();
			UISystem.displayEndGameMenu();
		}
	}

	@Override
	public void onAICharacterDied(AICharacter victim, PlayerCharacter killer) {

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
