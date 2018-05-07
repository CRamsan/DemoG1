package com.mygdx.game.gameelements;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Globals;
import com.mygdx.game.SingleAssetManager;

/***
 * Base class to be used for all elements that need to be rendered on the screen.
 * This class will handle rendering, assets and position.
 * Any subclass should handle input and use the translate method to move this character.
 */
public abstract class GameElement implements SingleAssetManager.TextureAnimationReciever {

	protected enum DIRECTION {
		LEFT, RIGHT, UP, DOWN
	}


	protected enum AXIS {
		X, Y
	}

	/**
	 * This needs to be moved out of here when we have a way to load all different
	 * resources.
	 */
	public enum TYPE {
		FIRE, WATER, PLANT,
		CHAR_HUMAN, CHAR_BASEAI, CHAR_STATUE,
		MALE_VILLAGER, FEMALE_VILLAGER, TRADER,
		WIZARD, KNIGHT, CHAR_RETICLE
	}

	protected TYPE type;
	protected boolean isDirty;
	private DIRECTION direction;

	protected float x, y;
	protected int width, height;
	protected float scale;
    protected float state;
	protected com.mygdx.game.gameelements.CharacterEventListener listener;
	protected boolean shouldRender;

	private Animation<TextureRegion> walkUpAnimation;
	private Animation<TextureRegion> walkDownAnimation;
	private Animation<TextureRegion> walkLeftAnimation;
	private Animation<TextureRegion> walkRightAnimation;

    /***
     * Regular constructor. It will initialize the regular variables as well as call the init method to load textures
     * and generate animations.
     * @param type
     */
    public GameElement(TYPE type, com.mygdx.game.gameelements.CharacterEventListener listener) {
        this.type = type;
        this.x = 0;
        this.y = 0;
        this.scale = 1f;
        this.listener = listener;
		this.direction = DIRECTION.values()[ Globals.rand.nextInt(4)];
		this.isDirty = false;
		init();
    }

    /***
     * Private method that will handle lower level details of the object such as handling instance count and loading
     * assets. This should only be called by the constructor.
     */
    private void init() {
		if (type == null) {
			throw new RuntimeException("Type cannot be null");
		}

		// Signal to the AssetManager that we are going to need to load textures
		SingleAssetManager.loadSprite();

		SingleAssetManager.getPlayerTextures(type, this);

		state = 0;
		shouldRender = true;
	}

	@Override
	public void setAnimations(Animation<TextureRegion> walkUp, Animation<TextureRegion> walkRight, Animation<TextureRegion> walkDown, Animation<TextureRegion> walkLeft) {
    	this.walkDownAnimation = walkDown;
    	this.walkUpAnimation = walkUp;
    	this.walkLeftAnimation = walkLeft;
    	this.walkRightAnimation = walkRight;
	}

	@Override
	public void setTextureSize(int width, int height) {
    	this.width = width;
    	this.height = height;
	}

	/***
     * Call this method to update the state of the object. This method will handle inputs and updating sny state.
     */
    public void update(float delta) {
    }

    /***
     * Draw method to render the images on the screen. It takes a SpriteBatch as a parameter.
     * @param batch
     */
    public void draw(SpriteBatch batch) {
        if (!isDirty) {
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
        batch.draw(currentFrame, x, y, width, height);
    }

    /***
	 * Helper method to determine the new direction
     * @param dx
     * @param dy
     */
	protected void updateDirection(float dx, float dy) {
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
     * This helper method will transport the character to the provided coordinates in world space. The direction will
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
		SingleAssetManager.unloadSprite();
	}

	final public Vector2 getCenterPosition() {
	    return new Vector2(x + (scale/2f), y + (scale/2));
    }

    final public float getRadious() {
		return scale / 2f;
	}

	final public void setScale(float scale) {
		this.width = (int)(this.width * scale);
		this.height = (int)(this.height * scale);
		this.scale = scale;
	}

    final public float getX() {
		return x;
	}

	final public float getY() {
		return y;
	}

	final public int getHeight() {
		return height;
	}

	final public int getWidth() {
		return width;
	}

	final public TYPE getType() {
		return type;
	}

	final public void setType(TYPE type) {
		this.type = type;
	}
}
