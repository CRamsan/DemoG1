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
    public static void initMainMenu() {
        ourInstance.initMainMenuInternal();
    }

    public static void initPauseMenu() {
        ourInstance.initPauseMenuInternal();
    }

    public static void initEndGameMenu() {
        ourInstance.initEndGameMenuInternal();
    }

    public static GetReadyMenuController initGetReadyMenu() {
        return ourInstance.initGetReadyMenuInternal();
    }

    public static void displayMainMenu() {
        ourInstance.displayMainMenuInternal();
    }

    public static void displayPauseMenu() {
        ourInstance.displayPauseMenuInternal();
    }

    public static void displayEndGameMenu() {
        ourInstance.displayEndGameMenuInternal();
    }

    public static void displayGetReadyMenu() {
        ourInstance.displayGetReadyMenuInternal();
    }

    public static void hideMenu() {
        ourInstance.hideMenuInternal();
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

    private Skin skin;
    private static final String SKIN_FILE_PATH = "uiskin.json";
    private ArrayList<Texture> loadedTextures;
    private boolean uiVisible;

    private Stage stage;
    private Actor mainMenu;
    private Actor getReadyMenu;
    private Actor endGameMenu;
    private Actor pauseMenu;

    private UISystem() {
        skin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
        loadedTextures = new ArrayList<Texture>();
        uiVisible = false;
    }

    private void initSingleStage() {
        if (stage != null)
            return;
        stage = new Stage(new StretchViewport(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT));
        stage.setDebugAll(true);
    }

    private void initMainMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");

        Table mainPane = UIToolKit.GenerateParentChildContainer(skin);
        UIToolKit.AddButtonToParentWithAction(mainPane,"Button 1", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                UISystem.displayGetReadyMenu();
            }
        });
        UIToolKit.AddButtonToParentWithAction(mainPane,"Button 2", skin, null);
        UIToolKit.AddButtonToParentWithAction(mainPane,"Button 3", skin, null);
        UIToolKit.AddActorToChild(mainPane, "Test text~", skin);
        mainMenu = mainPane;
        initSingleStage();
    }

    private void initPauseMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");

        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);

        UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Button 1", skin, null);
        UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Button 2", skin, null);
        UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Button 3", skin, null);
        UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Button 4", skin, null);

        pauseMenu = mainPane;
        initSingleStage();
    }

    private void initEndGameMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");

        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);

        UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Restart", skin, null);
        UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Back to Menu", skin, null);

        endGameMenu = mainPane;
        initSingleStage();
    }

    private GetReadyMenuController initGetReadyMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");

        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);
        Table containerPane = UIToolKit.GenerateHorizontalContainer(mainPane, skin);
        GetReadyMenuController controller = new GetReadyMenuController(containerPane, skin);

        UIToolKit.AddButtonToParentWithAction(mainPane,"Start", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MyGdxGame.startGameScreen();
            }
        });
        UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Back to Menu", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                UISystem.displayMainMenu();
            }
        });

        getReadyMenu = mainPane;
        initSingleStage();
        return controller;
    }

    public void displayMainMenuInternal() {
        stage.addActor(mainMenu);
        Gdx.input.setInputProcessor(stage);
        uiVisible = true;
    }

    public void displayPauseMenuInternal() {
        stage.addActor(pauseMenu);
        Gdx.input.setInputProcessor(stage);
        uiVisible = true;
    }

    public void displayEndGameMenuInternal() {
        stage.addActor(endGameMenu);
        Gdx.input.setInputProcessor(stage);
        uiVisible = true;
    }

    public void displayGetReadyMenuInternal() {
        stage.addActor(getReadyMenu);
        Gdx.input.setInputProcessor(stage);
        uiVisible = true;
    }

    public void render(float delta) {
        if (!uiVisible)
            return;
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height){
        if (!uiVisible)
            return;
        stage.getViewport().update(width, height, true);
    }

    public void hideMenuInternal () {
        uiVisible = false;
        stage.clear();
    }

    public void dispose () {
        stage.dispose();
        for (Texture texture : loadedTextures) {
            texture.dispose();
        }
        loadedTextures.clear();
    }
}
