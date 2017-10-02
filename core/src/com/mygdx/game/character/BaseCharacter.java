package com.mygdx.game.character;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Globals;
import com.mygdx.game.SingleAssetManager;

/***
 * Base class to be used for all characters, either NPCs or human players. This class will handle rendering, assets,
 * position. Any subclass should handle input and use the translate method to move this character.
 */
public abstract class BaseCharacter
{
	protected enum DIRECTION {
		LEFT, RIGHT, UP, DOWN
	}

	public enum TYPE {
		FIRE, WATER, PLANT,
		EARTH, LIGHT, DARK,
		MALE_VILLAGER, FEMALE_VILLAGER, TRADER,
		WIZARD, KNIGHT, PIRATE
	}

	protected TYPE type;

    private float walkSpeed;
	private float x, y;
    private AssetManager manager;
    private Animation<TextureRegion> walkUpAnimation;
    private Animation<TextureRegion> walkDownAnimation;
    private Animation<TextureRegion> walkLeftAnimation;
    private Animation<TextureRegion> walkRightAnimation;
    private float state;
    private boolean hasMoved;
    private DIRECTION direction;
    protected boolean isDead;
    private Controller controller;
    private CharacterEventListener listerner;

    private static Texture texture;
    private static int instanceCount = 0;

    /***
     * Regular constructor. It will initialize the regular variables as well as call the init method to load textures
     * and generate animations.
     * @param type
     * @param x value in world coordinates
     * @param y value in world coordinates
     */
    public BaseCharacter (TYPE type, float x, float y, CharacterEventListener listerner) {
        this.manager = SingleAssetManager.getAssetManager();
        this.type = type;
        this.walkSpeed = 0.05f;
        this.isDead = false;
        this.x = x;
        this.y = y;
        this.direction = DIRECTION.values()[ Globals.rand.nextInt(4)];
        this.listerner = listerner;
        this.hasMoved = false;
        init();
    }

    /***
     * Simple constructor that only takes a type as argument
     * @param type
     */
    public BaseCharacter (TYPE type, CharacterEventListener listerner) {
        this(type, 0f, 0f, listerner);
    }

    /***
     * Private method that will handle lower level details of the object such as handling instance count and loading
     * assets. This should only be called by the constructor.
     */
    private void init() {
		if (BaseCharacter.instanceCount == 0) {
			manager.load(Globals.ASSET_SPRITE_SHEET, Texture.class);
			manager.finishLoading();
		}
		BaseCharacter.instanceCount++;
		texture = manager.get(Globals.ASSET_SPRITE_SHEET);

		TextureRegion[][] spriteRegion = TextureRegion.split(texture,
				texture.getWidth() / Globals.ASSET_SPRITE_SHEET_COLUMNS,
				texture.getHeight() / Globals.ASSET_SPRITE_SHEET_ROWS);
		TextureRegion textureRegion = null;
		switch (this.type) {
			case DARK:
				textureRegion = spriteRegion[0][5];
				break;
			case EARTH:
				textureRegion = spriteRegion[0][3];
				break;
			case FIRE:
				textureRegion = spriteRegion[0][0];
				break;
			case LIGHT:
				textureRegion = spriteRegion[0][4];
				break;
			case PLANT:
				textureRegion = spriteRegion[0][2];
				break;
			case WATER:
				textureRegion = spriteRegion[0][1];
				break;
			case KNIGHT:
				textureRegion = spriteRegion[1][4];
				break;
			case PIRATE:
				textureRegion = spriteRegion[1][5];
				break;
			case TRADER:
				textureRegion = spriteRegion[1][2];
				break;
			case WIZARD:
				textureRegion = spriteRegion[1][3];
				break;
			case MALE_VILLAGER:
				textureRegion = spriteRegion[1][0];
				break;
			case FEMALE_VILLAGER:
				textureRegion = spriteRegion[1][1];
				break;
		}
		TextureRegion[][] tmp = textureRegion.split(Globals.ASSET_SPRITE_SHEET_SPRITE_WIDTH,
				Globals.ASSET_SPRITE_SHEET_SPRITE_HEIGHT);

		for (int i = 0; i < Globals.ANIMATION_ROWS; i++) {
            TextureRegion[] walkFrames = new TextureRegion[Globals.ANIMATION_COLUMNS];
            for (int j = 0; j < Globals.ANIMATION_COLUMNS; j++) {
				walkFrames[j] = tmp[i][j];
			}
			switch (i) {
				case 0:
					walkUpAnimation = new Animation<TextureRegion>(Globals.ANIMATION_DURATION, walkFrames);
					break;
				case 1:
					walkRightAnimation = new Animation<TextureRegion>(Globals.ANIMATION_DURATION, walkFrames);
					break;
				case 2:
					walkDownAnimation = new Animation<TextureRegion>(Globals.ANIMATION_DURATION, walkFrames);
					break;
				case 3:
					walkLeftAnimation = new Animation<TextureRegion>(Globals.ANIMATION_DURATION, walkFrames);
					break;
			}
		}
		state = 0;
	}

    /***
     * Call this method to update the state of the object. This method will handle inputs and updating sny state.
     */
    public void update() {
        hasMoved = false;
    }

    /***
     * Draw method to render the images on the screen. It takes a SpriteBatch as a parameter.
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        if (isDead)
            return;
        if (!hasMoved) {
        	state = 0;
		}
        Animation<TextureRegion> currentAnimation = null;
        switch (direction){
            case UP:
                currentAnimation = walkUpAnimation;
                break;
            case DOWN:
                currentAnimation = walkDownAnimation;
                break;
            case LEFT:
                currentAnimation = walkLeftAnimation;
                break;
            case RIGHT:
                currentAnimation = walkRightAnimation;
                break;
        }
        TextureRegion currentFrame = currentAnimation.getKeyFrame(state, true);
        batch.draw(currentFrame, x, y, 1, 1);
    }

    /***
     * Receive input as two components of one vector. The resulting vector should not be greater than 1.
     * @param dx
     * @param dy
     */
    protected void handleMovement(float dx, float dy){
    	if (isDead)
    		return;
    	Vector2 movement = new Vector2(dx, dy);
        if (movement.len() > 1) {
			movement = movement.nor();
		} else if (movement.len() == 0) {
			return;
		}
		hasMoved = true;
		state += movement.len();
		movement.scl(walkSpeed);
        translate(movement.x, movement.y);
    }

    /***
     * Helper method to move the character by the provided values on world space coordinates. This method will also
     * handle changing the direction that character is facing.
     * @param dx
     * @param dy
     */
	private void translate(float dx, float dy) {
		this.x += dx;
		this.y += dy;
		float absDx = Math.abs(dx);
		float absDy = Math.abs(dy);
		if (absDx == absDy)
		    return;
		if (absDx > absDy) {
		    if (dx > 0)
		        this.direction = DIRECTION.RIGHT;
            else
                this.direction = DIRECTION.LEFT;
        } else {
		    if (dy > 0)
                this.direction = DIRECTION.UP;
	        else
                this.direction = DIRECTION.DOWN;
        }
	}

    /***
     * This helper methos will transport the character to the provided coordinates in world space. The direction will
     * not be affected by this call.
     * @param x
     * @param y
     */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

    /***
     * This method should be called when this object will be not used anymore. If all instances of this object
     * are unloaded, then the loaded assets will be disposed as well.
     */
	public void unload() {
		BaseCharacter.instanceCount--;
		if (BaseCharacter.instanceCount == 0) {
            manager.unload(Globals.ASSET_SPRITE_SHEET);
		}
	}

	final public Vector2 getCenterPosition() {
	    return new Vector2(x + 0.5f, y + 0.5f);
    }

    final public float getX() {
		return x;
	}

	final public float getY() {
		return y;
	}

    /***
     * Callback to be called when this character is killed.
     */
	public void onKilled() {
	    this.isDead = true;
    }

    /***
     * This function will send an event to the listener that this character has attacked.
     */
    protected void attack() {
	    this.listerner.attack(this);
    }

    /***
     * Signal to the listener that this character has send a pause event.
     */
    protected void pause() { this.listerner.pause(this);}
}
