package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.*;
import com.mygdx.game.Globals;
import com.mygdx.game.MyGdxGame;

import java.util.ArrayList;

public class UISystem {

    /**
     * Public API calls
     */
    public static void displayMainMenu() {
        ourInstance.displayMainMenuInternal();
    }

    public static void disposeMenu() {
        ourInstance.dispose();
    }

    public static void draw(float delta) {
        ourInstance.render(delta);
    }

    public static void resizeUI(int width, int height) {
        ourInstance.resize(width, height);
    }

    /**
     * Private calls
     */

    private static UISystem ourInstance = new UISystem();

    private Stage stage;
    private Skin skin;
    private static final String SKIN_FILE_PATH = "uiskin.json";
    private ArrayList<Texture> loadedTextures;
    private boolean uiVisible;

    private UISystem() {
        skin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
        loadedTextures = new ArrayList<Texture>();
        stage = new Stage(new StretchViewport(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT));
        uiVisible = false;
    }

    public void displayMainMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");

        Table table = new Table();
        table.setFillParent(true);
        TextButton startGameButton = new TextButton("Button 1", skin);
        startGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                MyGdxGame.startGameScreen();
            }
        });
        table.add(startGameButton);

        TextButton closeGameButton = new TextButton("Button 2", skin);
        closeGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        table.add(closeGameButton);
        table.setDebug(true); // This is optional, but enables debug lines for tables.

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        uiVisible = true;
    }

    public void render(float delta) {
        if (!uiVisible)
            return;
        //stage.getViewport().apply();
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height){
        if (!uiVisible)
            return;
        stage.getViewport().update(width, height, true);
    }

    public void dispose () {
        uiVisible = false;
        stage.dispose();
        for (Texture texture : loadedTextures) {
            texture.dispose();
        }
        loadedTextures.clear();
    }
}
