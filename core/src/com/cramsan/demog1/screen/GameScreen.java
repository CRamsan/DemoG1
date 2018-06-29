package com.cramsan.demog1.screen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.utils.Array;
import com.cramsan.demog1.gameelements.*;
import com.cramsan.demog1.gameelements.player.PlayerCharacter;
import com.cramsan.demog1.subsystems.AudioManager;
import com.cramsan.demog1.subsystems.controller.PlayerController;
import com.cramsan.demog1.subsystems.ui.PauseMenuEventListener;

import java.util.*;

public abstract class GameScreen extends BaseScreen implements CharacterEventListener, PauseMenuEventListener {

	public static final float POISON_TIME = 5f;

	private Map<Integer, PlayerCharacter> playerCharacterMap;
	private PlayerCharacter pauseCaller;
	private GameParameterManager parameterManager;
	private List<PlayerCharacter> playerList;
	private int aiCount;

	public GameScreen(GameParameterManager parameterManager)
	{
		super();
		playerList = new ArrayList<PlayerCharacter>();
		playerCharacterMap = new HashMap<Integer, PlayerCharacter>();
		this.parameterManager = parameterManager;
	}

	@Override
	public void ScreenInit() {
		super.ScreenInit();
		getUiSystem().initPauseMenu(this);
		getUiSystem().initConfirmationMenu();
		getUiSystem().initEndGameMenu();
		setIllumination(0.01f);
	}

	protected void createAICharacters() {
		for (int i = 0; i < aiCount; i++) {
			GameElement.TYPE type = GameElement.TYPE.CHAR_BASEAI;
			AICharacter newChar = new AICharacter(type, this, getGameWorld(), getAssetManager());
			Vector2 charPos = getMap().getRandomNonSolidTile();
			newChar.setTilePosition((int) (charPos.x * newChar.getWidth()), (int)(charPos.y * newChar.getHeight()));
			addCharacter(newChar);
		}
	}

	@Override
	protected void performCustomUpdate(float delta) {

		if(!isRunning())
			return;

		Iterator<BaseCharacter> baseCharacterIterator = getCharacterIterator();
		while(baseCharacterIterator.hasNext()) {
			BaseCharacter baseCharacter = baseCharacterIterator.next();
			baseCharacter.updateInputs();
		}

		Iterator<GameElement> gameElementIterator = getCollidableIterator();
		while(gameElementIterator.hasNext()) {
			GameElement collidable = gameElementIterator.next();
			collidable.update(delta);
		}

		baseCharacterIterator = getCharacterIterator();
		while(baseCharacterIterator.hasNext()) {
			BaseCharacter baseCharacter = baseCharacterIterator.next();
			baseCharacter.update(delta);
		}

		Array<Body> bodies = new Array<Body>();
		getGameWorld().getBodies(bodies);

		for (Body b : bodies) {
			// Get the body's user data - in this example, our user
			// data is an instance of the Entity class
			if (b.getType() == BodyDef.BodyType.StaticBody)
				continue;
			GameElement e = (GameElement) b.getUserData();
			if (e != null) {
				e.setCenterPosition(b.getPosition().x, b.getPosition().y);
			}
		}
	/*
		for (PlayerCharacter player : playerList) {
			// Ignore anything that is not CHAR_HUMAN, for example a type reticle will not trigger a collision
			if (player.getType() != GameElement.TYPE.CHAR_HUMAN)
				continue;
			if (player.hasMoved()) {
				for (Collidable collidable : collidableList) {
					if (player.getCenterPosition().dst(collidable.getCenterPosition()) < (player.getRadius() + collidable.getRadius())) {
						player.onContact(collidable);
					}
				}
			}
		}
	*/
	}

	private void createPlayerCharacter(int index, PlayerController controller, GameElement.TYPE type) {
		PlayerCharacter newChar = new PlayerCharacter(index, type, this, getGameWorld(), getAssetManager());
		if (type == GameElement.TYPE.CHAR_HUMAN) {

        } else if(type == GameElement.TYPE.CHAR_RETICLE){

        } else {
		    throw new RuntimeException("Type not supported for PlayerCharacter" + type);
        }
		Vector2 characterPos = getMap().getRandomNonSolidTile();
        newChar.setTilePosition((int) (characterPos.x * newChar.getWidth()), (int)(characterPos.y * newChar.getHeight()));
		newChar.setController(controller);
		addCharacter(newChar);
		playerList.add(newChar);
		playerCharacterMap.put(index, newChar);
		if (!playerFound) {
			playerFound = true;
		}
	}

	private boolean playerFound;

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
	public void onCharacterAttack(final PlayerCharacter character) {
		getAudioManager().PlaySound(AudioManager.SOUND.ATTACK);
		getGameWorld().QueryAABB(new QueryCallback() {
			@Override
			public boolean reportFixture(Fixture fixture) {
				if (fixture.getFilterData().categoryBits != GameCollision.Player)
					return true;
				BaseCharacter otherElem = (BaseCharacter)fixture.getBody().getUserData();
				if (otherElem == null || character.equals(otherElem))
					return true;
				if (otherElem.getType() == GameElement.TYPE.CHAR_RETICLE) {
					return true;
				}
				otherElem.onKilled(character);
				return true;
			}
		}, character.getCenterPosition().x- character.attackRadius(),
				character.getCenterPosition().y - character.attackRadius(),
				character.getCenterPosition().x + character.attackRadius(),
				character.getCenterPosition().y + character.attackRadius());
	}

    @Override
    public void onCharacterPause(PlayerCharacter character) {
	    if (!isRunning())
        {
        	if (pauseCaller == character) {
				getUiSystem().hideMenu();
			}
        }
        else {
			getUiSystem().displayPauseMenu();
			setRunning(false);
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
		Iterator<BaseCharacter> characterIterator = getCharacterIterator();
		while (characterIterator.hasNext()) {
			BaseCharacter character = characterIterator.next();
			character.disableCharacter();
		}
	}

	@Override
	public void onCharacterCollidableTouched(GameElement collidable, PlayerCharacter player) {
		getAudioManager().PlaySound(AudioManager.SOUND.BELL);
	}

	@Override
	public void onPlayerCharacterDied(PlayerCharacter  victim, PlayerCharacter killer) {
		playerCharacterMap.remove(victim.getId());
		playerList.remove(victim);
		if (playerList.size() == 1) {
			disableAllPlayers();
			getUiSystem().displayEndGameMenu();
		}
	}

	@Override
	public void onAICharacterDied(AICharacter victim, PlayerCharacter killer) {

	}

	public List<PlayerCharacter> getPlayerList() {
		return playerList;
	}

	public int getAiCount() {
		return aiCount;
	}

	public void setAiCount(int aiCount) {
		this.aiCount = aiCount;
	}

	protected abstract int levelId();

	@Override
	public void onPauseMenuAppeared() {
	}

	@Override
	public void onPauseMenuDisappeared() {
		setRunning(true);
		pauseCaller = null;
	}
}
