package com.cramsan.demog1.gameelements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.cramsan.demog1.Globals;
import com.cramsan.demog1.subsystems.SingleAssetManager;

/***
 * Base class to be used for all elements that need to be rendered on the screen.
 * This class will handle rendering, assets and position.
 * Any subclass should handle input and use the translate method to move this character.
 */
public abstract class GameElement implements SingleAssetManager.TextureAnimationReceiver {

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
	private float scale;
    protected float state;
	protected com.cramsan.demog1.gameelements.CharacterEventListener listener;
	protected Body body;
	private World gameWorld;

	private Animation<TextureRegion> walkUpAnimation;
	private Animation<TextureRegion> walkDownAnimation;
	private Animation<TextureRegion> walkLeftAnimation;
	private Animation<TextureRegion> walkRightAnimation;

    /***
     * Regular constructor. It will initialize the regular variables as well as call the init method to load textures
     * and generate animations.
     * @param type
     */
    public GameElement(TYPE type, com.cramsan.demog1.gameelements.CharacterEventListener listener, World gameWorld, SingleAssetManager assetManager) {
        this.type = type;
        this.x = 0;
        this.y = 0;
        this.scale = 1f;
        this.listener = listener;
		this.direction = DIRECTION.values()[ Globals.rand.nextInt(4)];
		this.isDirty = false;
		this.gameWorld = gameWorld;
		this.state = 0;
		if (assetManager != null)
			assetManager.getPlayerTextures(type, this);
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

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x + (width/2), y + (height/2)));
		body = gameWorld.createBody(bodyDef);
		body.setUserData(this);
		CircleShape circle = new CircleShape();
		circle.setRadius(width/2);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		if (type == TYPE.CHAR_STATUE) {
			fixtureDef.isSensor = true;
			fixtureDef.filter.categoryBits = GameCollision.Statue;
            fixtureDef.filter.maskBits = GameCollision.Player;
		} else {
            fixtureDef.filter.categoryBits = GameCollision.Player;
            fixtureDef.filter.maskBits = GameCollision.Obstacle | GameCollision.Statue;
		}
		fixtureDef.density = 1f;
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 0.0f;
		body.createFixture(fixtureDef);
		circle.dispose();
	}

	public void destroyBody() {
    	gameWorld.destroyBody(body);
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
	public void setCenterPosition(float x, float y) {
		this.x = x - (width/2);
		this.y = y - (height/2);
		this.body.setTransform(x, y, 0);
	}

	public void setTilePosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.body.setTransform(x + (width/2), y + (height/2), 0);
	}

	public abstract void onContact(GameElement collidable);

    /***
     * This method should be called when this object will be not used anymore. If all instances of this object
     * are unloaded, then the loaded assets will be disposed as well.
     */
	public void unload() {

	}

	final public Vector2 getCenterPosition() {
	    return new Vector2(x + (scale/2f), y + (scale/2));
    }

    final public float getRadius() {
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
