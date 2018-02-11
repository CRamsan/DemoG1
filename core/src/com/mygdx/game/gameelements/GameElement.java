package com.mygdx.game.gameelements;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Globals;
import com.mygdx.game.SingleAssetManager;

/***
 * Base class to be used for all elements that need to be rendered on the screen.
 * This class will handle rendering, assets and position.
 * Any subclass should handle input and use the translate method to move this character.
 */
public abstract class GameElement
{
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
		EARTH, LIGHT, STATUE,
		MALE_VILLAGER, FEMALE_VILLAGER, TRADER,
		WIZARD, KNIGHT, PIRATE
	}

	protected TYPE type;
	protected boolean isDirty;
	private DIRECTION direction;

	protected float x, y, width, height, scale;
    protected float state;
	protected com.mygdx.game.gameelements.CharacterEventListener listener;
	protected boolean shouldRender;

	private AssetManager manager;
	private Animation<TextureRegion> walkUpAnimation;
	private Animation<TextureRegion> walkDownAnimation;
	private Animation<TextureRegion> walkLeftAnimation;
	private Animation<TextureRegion> walkRightAnimation;

	private static Texture texture;
    private static int instanceCount = 0;

    /***
     * Regular constructor. It will initialize the regular variables as well as call the init method to load textures
     * and generate animations.
     * @param type
     * @param x value in world coordinates
     * @param y value in world coordinates
     */
    public GameElement(TYPE type, float x, float y, com.mygdx.game.gameelements.CharacterEventListener listener) {
        this.manager = SingleAssetManager.getAssetManager();
        this.type = type;
        this.x = x;
        this.y = y;
        this.scale = 1f;
        this.listener = listener;
		this.direction = DIRECTION.values()[ Globals.rand.nextInt(4)];
		this.isDirty = false;
		init();
    }

    /***
     * Simple constructor that only takes a type as argument
     * @param type
     */
    public GameElement(TYPE type, com.mygdx.game.gameelements.CharacterEventListener listerner) {
        this(type, 0f, 0f, listerner);
    }

    /***
     * Private method that will handle lower level details of the object such as handling instance count and loading
     * assets. This should only be called by the constructor.
     */
    private void init() {
		if (GameElement.instanceCount == 0) {
			manager.load(Globals.ASSET_SPRITE_SHEET, Texture.class);
			manager.finishLoading();
		}
		if (type == null) {
			throw new RuntimeException("Type cannot be null");
		}

		GameElement.instanceCount++;
		texture = manager.get(Globals.ASSET_SPRITE_SHEET);

		TextureRegion[][] spriteRegion = TextureRegion.split(texture,
				texture.getWidth() / Globals.ASSET_SPRITE_SHEET_COLUMNS,
				texture.getHeight() / Globals.ASSET_SPRITE_SHEET_ROWS);
		TextureRegion textureRegion = null;
		switch (this.type) {
			case STATUE:
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
		width = Globals.ASSET_SPRITE_SHEET_SPRITE_WIDTH;
		height = Globals.ASSET_SPRITE_SHEET_SPRITE_HEIGHT;
		shouldRender = true;
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
        float xPos = x * Globals.ASSET_SPRITE_SHEET_SPRITE_WIDTH;
        float yPos = y * Globals.ASSET_SPRITE_SHEET_SPRITE_WIDTH;
        TextureRegion currentFrame = currentAnimation.getKeyFrame(state, true);
        batch.draw(currentFrame, xPos, yPos, width, height);
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
		GameElement.instanceCount--;
		if (GameElement.instanceCount == 0) {
            manager.unload(Globals.ASSET_SPRITE_SHEET);
		}
	}

	final public Vector2 getCenterPosition() {
	    return new Vector2(x + (scale/2f), y + (scale/2));
    }

    final public float getRadious() {
		return scale / 2f;
	}

	final public void setScale(float scale) {
		this.width = this.width * scale;
		this.height = this.height * scale;
		this.scale = scale;
	}

    final public float getX() {
		return x;
	}

	final public float getY() {
		return y;
	}

	final public float getHeight() {
		return height;
	}

	final public float getWidth() {
		return width;
	}

	final public TYPE getType() {
		return type;
	}

	final public void setType(TYPE type) {
		this.type = type;
	}
}
