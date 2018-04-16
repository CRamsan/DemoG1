package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Globals;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.controller.ControllerManager;
import com.mygdx.game.gameelements.GameParameterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides static methods to configure UI components.
 */
public class UISystem {

    public enum UI_EVENTS { UP, DOWN, LEFT, RIGHT, SELECT, NOOP }

    private static final String TEXT_LABEL_NINJA_PARTY = "Ninja Party";
    private static final String TEXT_LABEL_CATCH_A_THIEF = "Catch A Thief";
    private static final String TEXT_LABEL_KNIGHTS_VS_NINJAS = "Knights vs Ninjas";
    private static final String TEXT_LABEL_DEATH_RACE = "Death Race";
    private static final String TEXT_LABEL_ASSASSIN = "Assassin";
    private static final String TEXT_LABEL_START = "Start";
    private static final String TEXT_LABEL_BACK = "Back";

    private static final String ASSET_SKIN_FILE = "uiskin.json";

    /**
     * Public API calls
     */
    public static void initMainMenu() {
        ourInstance.initMainMenuInternal();
    }

    public static void initPauseMenu(PauseMenuEventListener pauseMenuListener) {
        ourInstance.initPauseMenuInternal(pauseMenuListener);
    }

    public static void initConfirmationMenu() {
        ourInstance.initConfirmationMenuInternal();
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

    public static void displayConfirmationMenu() {
        ourInstance.displayConfirmationMenuInternal();
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

    public static void injectUIEvent(UISystem.UI_EVENTS event) {
        ourInstance.injectUIEventInternal(event);
    }

    /**
     * Private calls
     */

    private static UISystem ourInstance = new UISystem();

    private Skin skin;
    private ArrayList<Texture> loadedTextures;
    private HashMap<Button, HashMap<UISystem.UI_EVENTS, Button>> sequenceMap;
    private HashMap<Actor, Button> defaultSelectionMap;

    private PauseMenuEventListener pauseMenuListener;
    private GameParameterManager gameParams;
    private GetReadyMenuController getReadyMenuController;

    private boolean uiVisible;
    private Button selected;
    private InputProcessor stashedProcessor;    // When the UI displays it will register itself as the InputProcessor.
                                                // In case there is another IP already in place, such as a keyboard controller,
                                                // then lets stash it until the UI is dismissed so we can restore it.
    private Stage stage;
    private Actor mainMenu;
    private Actor getReadyMenu;
    private Actor endGameMenu;
    private Actor confirmationMenu;
    private Actor pauseMenu;

    private UISystem() {
        skin = new Skin(Gdx.files.internal(ASSET_SKIN_FILE));
        loadedTextures = new ArrayList<Texture>();
        uiVisible = false;
    }

    private void initSingleStage() {
        if (stage != null)
            return;
        stage = new Stage(new StretchViewport(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT));
        //stage = new Stage(new ScreenViewport());
        sequenceMap = new HashMap<Button, HashMap<UISystem.UI_EVENTS, Button>>();
        defaultSelectionMap = new HashMap<Actor, Button>();
        stage.setDebugAll(true);
    }

    private void initMainMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");
        initSingleStage();

        Table mainPane = UIToolKit.GenerateParentChildContainer(skin);
        Label childLabel = UIToolKit.AddActorToChild(mainPane, "", skin);
        Button firstButton = UIToolKit.AddButtonToParentWithAction(mainPane,TEXT_LABEL_NINJA_PARTY, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                gameParams = GameParameterManager.createNinjaPartyManager();
                UISystem.displayGetReadyMenu();
            }
        }, sequenceMap);

        Button secondButton = UIToolKit.AddButtonToParentWithAction(mainPane, TEXT_LABEL_CATCH_A_THIEF, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                gameParams = GameParameterManager.createCatchAThiefManager();
                UISystem.displayGetReadyMenu();
            }
        }, sequenceMap);
        Button thirdButton = UIToolKit.AddButtonToParentWithAction(mainPane, TEXT_LABEL_KNIGHTS_VS_NINJAS, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                gameParams = GameParameterManager.createKnightsVsThiefsManager();
                UISystem.displayGetReadyMenu();
            }
        }, sequenceMap);
        Button fourthButton = UIToolKit.AddButtonToParentWithAction(mainPane, TEXT_LABEL_DEATH_RACE, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                gameParams = GameParameterManager.createDeathRaceManager();
                UISystem.displayGetReadyMenu();
            }
        }, sequenceMap);
        Button fifthButton = UIToolKit.AddButtonToParentWithAction(mainPane, TEXT_LABEL_ASSASSIN, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                gameParams = GameParameterManager.createAssassinsManager();
                UISystem.displayGetReadyMenu();
            }
        }, sequenceMap);

        UIToolKit.LinkUpAndDown(firstButton, secondButton, sequenceMap);
        UIToolKit.LinkUpAndDown(secondButton, thirdButton, sequenceMap);
        UIToolKit.LinkUpAndDown(thirdButton, fourthButton, sequenceMap);
        UIToolKit.LinkUpAndDown(fourthButton, fifthButton, sequenceMap);

        mainMenu = mainPane;
        setDefaultSelection(mainPane, firstButton);
    }

    private void initPauseMenuInternal(PauseMenuEventListener listener) {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");
        initSingleStage();

        this.pauseMenuListener = listener;
        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);
        Button button1 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "Resume", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                pauseMenuListener.onPauseMenuDisappeared();
            }
        }, sequenceMap);
        Button button2 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "Quit", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.displayConfirmationMenu();
            }
        }, sequenceMap);
        UIToolKit.LinkUpAndDown(button1, button2, sequenceMap);

        pauseMenu = mainPane;
        setDefaultSelection(pauseMenu, button1);
    }

    private void initConfirmationMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");
        initSingleStage();

        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);
        Button button1 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "No", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                UISystem.displayPauseMenu();
            }
        }, sequenceMap);
        Button button2 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "Yes", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                MyGdxGame.startMainMenuScreen();
            }
        }, sequenceMap);
        UIToolKit.LinkUpAndDown(button1, button2, sequenceMap);

        confirmationMenu = mainPane;
        setDefaultSelection(confirmationMenu, button1);
    }

    private void initEndGameMenuInternal() {
        if (loadedTextures.size() != 0)
            throw new RuntimeException("Unload textures before loading more");
        initSingleStage();
        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);

        Button  button1 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "Restart", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                // Since we are restarting the game with the same parameters we can pass null as the
                // GameParameterManager. This will cause the Game object to reuse the previously set instance
                MyGdxGame.startGameScreen(null);
            }
        }, sequenceMap);
        Button  button2 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "Back to Menu", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                MyGdxGame.startMainMenuScreen();
            }
        }, sequenceMap);

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
        getReadyMenuController = new GetReadyMenuController(containerPane, skin);
        Button  buttonStart = UIToolKit.AddButtonToParentWithAction(mainPane,TEXT_LABEL_START, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (getReadyMenuController.enoughPlayers()) {
                    UISystem.hideMenu();
                    MyGdxGame.startGameScreen(gameParams);
                }
            }
        }, sequenceMap);
        Button  buttonBack = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, TEXT_LABEL_BACK, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                UISystem.displayMainMenu();
            }
        }, sequenceMap);
        UIToolKit.LinkUpAndDown(buttonStart, buttonBack, sequenceMap);

        getReadyMenu = mainPane;
        setDefaultSelection(getReadyMenu, buttonStart);
        return getReadyMenuController;
    }

    public void displayMainMenuInternal() {
        setActorAsVisible(mainMenu);
    }

    public void displayPauseMenuInternal() {
        setActorAsVisible(pauseMenu);
        pauseMenuListener.onPauseMenuAppeared();
    }

    public void displayEndGameMenuInternal() {
        setActorAsVisible(endGameMenu);
    }

    public void displayGetReadyMenuInternal() {
        getReadyMenuController.updateGameParams(gameParams);
        setActorAsVisible(getReadyMenu);
    }

    private void displayConfirmationMenuInternal() {
        setActorAsVisible(confirmationMenu);
    }


    public void render(float delta) {
        if (!uiVisible)
            return;
        stage.act(delta);
        ControllerManager.getInstance().update(delta);
        processInput();
        stage.draw();
    }

    private void processInput() {
        List<ControllerManager.ControllerEventTuple> tupleList = ControllerManager.getInstance().getUIEvents();
        for (ControllerManager.ControllerEventTuple tuple : tupleList) {
            UISystem.UI_EVENTS event = tuple.event;
            handleEvent(event);
        }
    }

    private void handleEvent(UISystem.UI_EVENTS event) {
        if (event == UISystem.UI_EVENTS.NOOP)
            return;

        if (event == UISystem.UI_EVENTS.DOWN || event == UISystem.UI_EVENTS.UP ||
                event == UISystem.UI_EVENTS.LEFT || event == UISystem.UI_EVENTS.RIGHT) {
            Button newSelected = sequenceMap.get(selected).get(event);
            if (newSelected != null)
                setSelected(newSelected);
        } else if (event == UISystem.UI_EVENTS.SELECT) {
            selected.getClickListener().clicked(null, 0, 0);
        }
    }

    /**
     * This method should only be used for tests.
     * @param event
     */
    private void injectUIEventInternal(UISystem.UI_EVENTS event) {
        handleEvent(event);
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
        stashedProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(stage);
        uiVisible = true;
        Button defaultSelection = defaultSelectionMap.get(actor);
        setSelected(defaultSelection);
    }

    private void setDefaultSelection(Actor actor, Button button) {
        defaultSelectionMap.put(actor, button);
    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    public void hideMenuInternal () {
        uiVisible = false;
        if (stashedProcessor != null)
            Gdx.input.setInputProcessor(stashedProcessor);
        stashedProcessor = null;
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
