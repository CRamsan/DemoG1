package com.cramsan.demog1.subsystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cramsan.demog1.gameelements.GameElement;

import java.util.HashMap;

/**
 * Class that exposes the AssetManager as a singleton.
 */
public class SingleAssetManager implements IGameSubsystem{

    private static final String ASSET_SPRITE_SHEET = "animation_sheet.png";
    private static final String ASSET_LIGHT = "light.png";
    private static final String ASSET_MAIN_LIGHT = "main_light.png";
    private static final int ASSET_SPRITE_SHEET_ROWS = 2;
    private static final int ASSET_SPRITE_SHEET_COLUMNS = 6;
    private static final int ASSET_SPRITE_SHEET_SPRITE_WIDTH = 32;
    private static final int ASSET_SPRITE_SHEET_SPRITE_HEIGHT = 32;
    public static final float ANIMATION_DURATION = 0.5f;
    public static final int ANIMATION_COLUMNS = 3;
    public static final int ANIMATION_ROWS = 4;

    private HashMap<GameElement.TYPE, TextureRegion> typeToTextureMapping;
    private TextureRegion[][] spriteRegion;
    private Texture texture;

    private AssetManager manager;

    public SingleAssetManager() {
        manager = new AssetManager();
    }

    public Texture getLightTexture() {
        return new Texture(Gdx.files.internal(ASSET_LIGHT));
    }

    public Texture getSceneLightTexture() {
        return new Texture(Gdx.files.internal(ASSET_MAIN_LIGHT));
    }

    private TextureRegion getTextureRegionForType(GameElement.TYPE type) {
        if (typeToTextureMapping.containsKey(type))
            return typeToTextureMapping.get(type);

        TextureRegion textureRegion = null;
        switch (type) {
            case CHAR_STATUE:
                textureRegion = spriteRegion[0][5];
                break;
            case CHAR_HUMAN:
                textureRegion = spriteRegion[0][3];
                break;
            case FIRE:
                textureRegion = spriteRegion[0][0];
                break;
            case CHAR_BASEAI:
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
            case CHAR_RETICLE:
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
            default:
                throw new RuntimeException("Trying to load undefined sprite region");
        }
        typeToTextureMapping.put(type, textureRegion);
        return  textureRegion;
    }

    public void getPlayerTextures(GameElement.TYPE type, TextureAnimationReciever receiver) {
        TextureRegion textureRegion = getTextureRegionForType(type);

        TextureRegion[][] tmp = textureRegion.split(ASSET_SPRITE_SHEET_SPRITE_WIDTH,
                ASSET_SPRITE_SHEET_SPRITE_HEIGHT);

        Animation<TextureRegion> walkUpAnimation = null, walkRightAnimation = null, walkDownAnimation = null, walkLeftAnimation = null;
        for (int i = 0; i < ANIMATION_ROWS; i++) {
            TextureRegion[] walkFrames = new TextureRegion[ANIMATION_COLUMNS];
            for (int j = 0; j < ANIMATION_COLUMNS; j++) {
                walkFrames[j] = tmp[i][j];
            }
            switch (i) {
                case 0:
                    walkUpAnimation = new Animation<TextureRegion>(ANIMATION_DURATION, walkFrames);
                    break;
                case 1:
                    walkRightAnimation = new Animation<TextureRegion>(ANIMATION_DURATION, walkFrames);
                    break;
                case 2:
                    walkDownAnimation = new Animation<TextureRegion>(ANIMATION_DURATION, walkFrames);
                    break;
                case 3:
                    walkLeftAnimation = new Animation<TextureRegion>(ANIMATION_DURATION, walkFrames);
                    break;
            }
        }
        receiver.setAnimations(walkUpAnimation, walkRightAnimation, walkDownAnimation, walkLeftAnimation);
        receiver.setTextureSize(ASSET_SPRITE_SHEET_SPRITE_WIDTH, ASSET_SPRITE_SHEET_SPRITE_HEIGHT);
    }

    @Override
    public void InitSystem() {
        manager.load(ASSET_SPRITE_SHEET, Texture.class);
        manager.finishLoading();
        texture = manager.get(ASSET_SPRITE_SHEET);
        spriteRegion = TextureRegion.split(texture,
                texture.getWidth() / ASSET_SPRITE_SHEET_COLUMNS,
                texture.getHeight() / ASSET_SPRITE_SHEET_ROWS);
        typeToTextureMapping = new HashMap<GameElement.TYPE, TextureRegion>();
    }

    @Override
    public void InitScreen() {

    }

    @Override
    public void UnInitScreen() {

    }

    @Override
    public void UnInitSystem() {
        manager.unload(ASSET_SPRITE_SHEET);
        texture = null;
        spriteRegion = null;
        typeToTextureMapping = null;
    }

    public interface TextureAnimationReciever {
        void setAnimations(Animation<TextureRegion> walkUp, Animation<TextureRegion> walkRight, Animation<TextureRegion> walkDown, Animation<TextureRegion> walkLeft);
        void setTextureSize(int width, int height);
    }
}
