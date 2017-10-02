package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;

public class SingleAssetManager {
    private static SingleAssetManager ourInstance = new SingleAssetManager();

    public static AssetManager getAssetManager() {
        return ourInstance.getManager();
    }

    private AssetManager manager;

    private SingleAssetManager() {
        manager = new AssetManager();
    }

    private AssetManager getManager() {
        return manager;
    }
}
