package com.cramsan.demog1.core;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cramsan.demog1.SingleAssetManager;
import com.cramsan.demog1.gameelements.GameElement;
import org.junit.Assert;

public class SingleAssetManagerTest extends MockedGameTest {

    @org.junit.Test
    public void getAssetManager() {
        Assert.assertNotNull(SingleAssetManager.getAssetManager());
    }

    @org.junit.Test
    public void getLightTexture() {
        Assert.assertNotNull(SingleAssetManager.getLightTexture());
    }

    @org.junit.Test
    public void getSceneLightTexture() {
        Assert.assertNotNull(SingleAssetManager.getSceneLightTexture());
    }

    @org.junit.Test
    public void getPlayerTextures() {
        DummyTextureReceiver reciever = new DummyTextureReceiver();
        SingleAssetManager.loadSprite();
        for (GameElement.TYPE type : GameElement.TYPE.values()) {
            SingleAssetManager.getPlayerTextures(type, reciever);
            Assert.assertTrue(reciever.isDataLoaded());
        }
        Assert.assertTrue(reciever.isDataLoaded());
        SingleAssetManager.unloadSprite();
    }

    @org.junit.Test
    public void loadSprite() {
        DummyTextureReceiver reciever = new DummyTextureReceiver();
        SingleAssetManager.loadSprite();
        SingleAssetManager.getPlayerTextures(GameElement.TYPE.CHAR_HUMAN, reciever);
        Assert.assertTrue(reciever.isDataLoaded());
        SingleAssetManager.unloadSprite();
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void getPlayerTextureBeforeLoad() {
        DummyTextureReceiver reciever = new DummyTextureReceiver();
        SingleAssetManager.getPlayerTextures(GameElement.TYPE.CHAR_HUMAN, reciever);
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void unloadSprite() {
        DummyTextureReceiver reciever = new DummyTextureReceiver();
        SingleAssetManager.loadSprite();
        SingleAssetManager.getPlayerTextures(GameElement.TYPE.CHAR_HUMAN, reciever);
        Assert.assertTrue(reciever.isDataLoaded());
        SingleAssetManager.unloadSprite();
        SingleAssetManager.getPlayerTextures(GameElement.TYPE.CHAR_HUMAN, reciever);
    }

    public class DummyTextureReceiver implements SingleAssetManager.TextureAnimationReciever {

        public boolean animationSet, textureSizeSet = false;

        @Override
        public void setAnimations(Animation<TextureRegion> walkUp, Animation<TextureRegion> walkRight, Animation<TextureRegion> walkDown, Animation<TextureRegion> walkLeft) {
            animationSet = (walkUp != null)  && (walkDown != null) && (walkLeft != null) && (walkRight != null);
        }

        @Override
        public void setTextureSize(int width, int height) {
            textureSizeSet = width > 0 && height > 0;
        }

        public boolean isDataLoaded(){
            return animationSet && textureSizeSet;
        }
    }
}