package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.*;
import com.mygdx.game.ControllerManager;
import com.mygdx.game.Globals;
import com.mygdx.game.MyGdxGame;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class provides static methods to configure UI components.
 */
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

    private static void displayGetReadyMenu() {
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
    private ArrayList<Texture> loadedTextures;
    private HashMap<Button, HashMap<Globals.UI_EVENTS, Button>> sequenceMap;
    private HashMap<Actor, Button> defaultSelectionMap;

    private boolean uiVisible;
    private Button selected;
    private float waitBuffer;
    private boolean blockInput;

    private Stage stage;
    private Actor mainMenu;
    private Actor getReadyMenu;
    private Actor endGameMenu;
    private Actor pauseMenu;

    private UISystem() {
        skin = new Skin(Gdx.files.internal(Globals.ASSET_SKIN_FILE));
        loadedTextures = new ArrayList<Texture>();
        uiVisible = false;
    }

    private void initSingleStage() {
        if (stage != null)
            return;
        stage = new Stage(new StretchViewport(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT));
        sequenceMap = new HashMap<Button, HashMap<Globals.UI_EVENTS, Button>>();
        defaultSelectionMap = new HashMap<Actor, Button>();
        stage.setDebugAll(true);
    }

    private void initMainMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");
        initSingleStage();

        Table mainPane = UIToolKit.GenerateParentChildContainer(skin);
        Label childLabel = UIToolKit.AddActorToChild(mainPane, "", skin);
        Button firstButton = UIToolKit.AddButtonToParentWithAction(mainPane,Globals.TEXT_LABEL_NINJA_PARTY, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                UISystem.displayGetReadyMenu();
            }
        }, sequenceMap);

        Button secondButton = UIToolKit.AddButtonToParentWithAction(mainPane,Globals.TEXT_LABEL_CATCH_A_THIEF, skin, null, sequenceMap);
        Button thirdButton = UIToolKit.AddButtonToParentWithAction(mainPane, Globals.TEXT_LABEL_KNIGHTS_VS_NINJAS, skin, null, sequenceMap);
        Button fourthButton = UIToolKit.AddButtonToParentWithAction(mainPane, Globals.TEXT_LABEL_DEATH_RACE, skin, null, sequenceMap);
        Button fifthButton = UIToolKit.AddButtonToParentWithAction(mainPane, Globals.TEXT_LABEL_ASSASSIN, skin, null, sequenceMap);

        UIToolKit.LinkUpAndDown(firstButton, secondButton, sequenceMap);
        UIToolKit.LinkUpAndDown(secondButton, thirdButton, sequenceMap);
        UIToolKit.LinkUpAndDown(thirdButton, fourthButton, sequenceMap);
        UIToolKit.LinkUpAndDown(fourthButton, fifthButton, sequenceMap);

        mainMenu = mainPane;
        setDefaultSelection(mainPane, firstButton);
    }

    private void initPauseMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");
        initSingleStage();

        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);
        Button button1 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Button 1", skin, null, sequenceMap);
        Button button2 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Button 2", skin, null, sequenceMap);
        Button button3 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Button 3", skin, null, sequenceMap);
        Button button4 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Button 4", skin, null, sequenceMap );
        UIToolKit.LinkUpAndDown(button1, button2, sequenceMap);
        UIToolKit.LinkUpAndDown(button2, button3, sequenceMap);
        UIToolKit.LinkUpAndDown(button3, button4, sequenceMap);

        pauseMenu = mainPane;
        setDefaultSelection(pauseMenu, button1);
    }

    private void initEndGameMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");
        initSingleStage();
        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);

        Button  button1 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Restart", skin, null, sequenceMap);
        Button  button2 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane,"Back to Menu", skin, null, sequenceMap);

        UIToolKit.LinkUpAndDown(button1, button2, sequenceMap);

        endGameMenu = mainPane;
        setDefaultSelection(endGameMenu, button1);
    }

    private GetReadyMenuController initGetReadyMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");
        initSingleStage();

        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);
        Table containerPane = UIToolKit.GenerateHorizontalContainer(mainPane, skin);
        final GetReadyMenuController controller = new GetReadyMenuController(containerPane, skin);
        Button  buttonStart = UIToolKit.AddButtonToParentWithAction(mainPane,Globals.TEXT_LABEL_START, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (controller.enoughPlayers()) {
                    UISystem.hideMenu();
                    MyGdxGame.startGameScreen();
                }
            }
        }, sequenceMap);
        Button  buttonBack = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, Globals.TEXT_LABEL_BACK, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                UISystem.displayMainMenu();
            }
        }, sequenceMap);
        UIToolKit.LinkUpAndDown(buttonStart, buttonBack, sequenceMap);

        getReadyMenu = mainPane;
        setDefaultSelection(getReadyMenu, buttonStart);
        return controller;
    }

    public void displayMainMenuInternal() {
        setActorAsVisible(mainMenu);
    }

    public void displayPauseMenuInternal() {
        setActorAsVisible(pauseMenu);
    }

    public void displayEndGameMenuInternal() {
        setActorAsVisible(endGameMenu);
    }

    public void displayGetReadyMenuInternal() {
        setActorAsVisible(getReadyMenu);
    }

    public void render(float delta) {
        if (!uiVisible)
            return;
        stage.act(delta);
        processInput(delta);
        stage.draw();
    }

    private void processInput(float delta) {
        Globals.UI_EVENTS event = ControllerManager.getInstance().getNextUIEvent();
        if (blockInput) {
            // There was a UI event that is blocking other events
            // If the new event is NOOP then the event was released.
            // Otherwise wait for the timeout
            if (event == Globals.UI_EVENTS.NOOP) {
                blockInput = false;
            } else {
                // Wait until we reach the timeout.
                waitBuffer += delta;
                if (waitBuffer >= Globals.UI_WAIT) {
                    blockInput = false;
                    waitBuffer = 0;
                } else {
                    return;
                }
            }
        }

        if (event == Globals.UI_EVENTS.DOWN || event == Globals.UI_EVENTS.UP ||
                event == Globals.UI_EVENTS.LEFT || event == Globals.UI_EVENTS.RIGHT) {
            Button newSelected = sequenceMap.get(selected).get(event);
            if (newSelected != null)
                setSelected(newSelected);
            blockInput = true;
            waitBuffer = 0;
        } else if (event == Globals.UI_EVENTS.SELECT) {
            selected.getClickListener().clicked(null, 0, 0);
            blockInput = true;
        }
    }

    private void setSelected(Button actor) {
        if (selected != null) {
            selected.setColor(Color.WHITE);
            selected.setScale(1);
        }
        selected = actor;
        selected.setScale(1.2f);
        selected.setColor(Color.RED);
    }

    private void setActorAsVisible(Actor actor) {
        stage.addActor(actor);
        Gdx.input.setInputProcessor(stage);
        uiVisible = true;
        Button defaultSelection = defaultSelectionMap.get(actor);
        setSelected(defaultSelection);
    }

    private void setDefaultSelection(Actor actor, Button button) {
        defaultSelectionMap.put(actor, button);
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
        sequenceMap.clear();
    }
}
