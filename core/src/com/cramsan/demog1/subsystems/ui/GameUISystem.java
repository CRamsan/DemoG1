package com.cramsan.demog1.subsystems.ui;

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
import com.cramsan.demog1.SceneManager;
import com.cramsan.demog1.subsystems.controller.ControllerManager;
import com.cramsan.demog1.gameelements.GameParameterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cramsan.demog1.Globals.SCREEN_HEIGHT;
import static com.cramsan.demog1.Globals.SCREEN_WIDTH;

/**
 * This class provides static methods to configure UI components.
 */
public class GameUISystem implements IUISystem {

    private static final String TEXT_LABEL_NINJA_PARTY = "Ninja Party";
    private static final String TEXT_LABEL_CATCH_A_THIEF = "Catch A Thief";
    private static final String TEXT_LABEL_KNIGHTS_VS_NINJAS = "Knights vs Ninjas";
    private static final String TEXT_LABEL_DEATH_RACE = "Death Race";
    private static final String TEXT_LABEL_ASSASSIN = "Assassin";
    private static final String TEXT_LABEL_START = "Start";
    private static final String TEXT_LABEL_BACK = "Back";
    private static final String ASSET_SKIN_FILE = "uiskin.json";

    private Skin skin;
    private HashMap<Button, HashMap<UI_EVENTS, Button>> sequenceMap;
    private HashMap<Actor, Button> defaultSelectionMap;

    private PauseMenuEventListener pauseMenuListener;
    private GameParameterManager gameParams;
    private GetReadyMenuController getReadyMenuController;
    private ControllerManager controllerManager;

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

    public GameUISystem() {
        uiVisible = false;
    }

    @Override
    public void initMainMenu() {
        Table mainPane = UIToolKit.GenerateParentChildContainer(skin);
        Label childLabel = UIToolKit.AddActorToChild(mainPane, "", skin);
        Button firstButton = UIToolKit.AddButtonToParentWithAction(mainPane,TEXT_LABEL_NINJA_PARTY, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMenu();
                gameParams = GameParameterManager.parameterManagerForGameType(GameParameterManager.GameType.NINJA_PARTY);
                displayGetReadyMenu();
            }
        }, sequenceMap);

        Button secondButton = UIToolKit.AddButtonToParentWithAction(mainPane, TEXT_LABEL_CATCH_A_THIEF, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMenu();
                gameParams = GameParameterManager.parameterManagerForGameType(GameParameterManager.GameType.CATCH_A_THIEF);
                displayGetReadyMenu();
            }
        }, sequenceMap);
        Button thirdButton = UIToolKit.AddButtonToParentWithAction(mainPane, TEXT_LABEL_KNIGHTS_VS_NINJAS, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMenu();
                gameParams = GameParameterManager.parameterManagerForGameType(GameParameterManager.GameType.KNIGHTS_VS_THIEVES);
                displayGetReadyMenu();
            }
        }, sequenceMap);
        /*
        Button fourthButton = UIToolKit.AddButtonToParentWithAction(mainPane, TEXT_LABEL_DEATH_RACE, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UISystem.hideMenu();
                gameParams = GameParameterManager.createDeathRaceManager();
                UISystem.displayGetReadyMenu();
            }
        }, sequenceMap);
        */
        Button fifthButton = UIToolKit.AddButtonToParentWithAction(mainPane, TEXT_LABEL_ASSASSIN, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMenu();
                gameParams = GameParameterManager.parameterManagerForGameType(GameParameterManager.GameType.ASSASSIN);
                displayGetReadyMenu();
            }
        }, sequenceMap);

        UIToolKit.LinkUpAndDown(firstButton, secondButton, sequenceMap);
        UIToolKit.LinkUpAndDown(secondButton, thirdButton, sequenceMap);
        // Death Race removed
        UIToolKit.LinkUpAndDown(thirdButton, fifthButton, sequenceMap);

        mainMenu = mainPane;
        setDefaultSelection(mainPane, firstButton);
    }

    @Override
    public void initPauseMenu(PauseMenuEventListener listener) {
        this.pauseMenuListener = listener;
        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);
        Button button1 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "Resume", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMenu();
                pauseMenuListener.onPauseMenuDisappeared();
            }
        }, sequenceMap);
        Button button2 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "Quit", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                displayConfirmationMenu();
            }
        }, sequenceMap);
        UIToolKit.LinkUpAndDown(button1, button2, sequenceMap);

        pauseMenu = mainPane;
        setDefaultSelection(pauseMenu, button1);
    }

    @Override
    public void initConfirmationMenu() {
        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);
        Button button1 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "No", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMenu();
                displayPauseMenu();
            }
        }, sequenceMap);
        Button button2 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "Yes", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMenu();
                SceneManager.startMainMenuScreen();
            }
        }, sequenceMap);
        UIToolKit.LinkUpAndDown(button1, button2, sequenceMap);

        confirmationMenu = mainPane;
        setDefaultSelection(confirmationMenu, button1);
    }

    @Override
    public void initEndGameMenu() {
        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);

        Button  button1 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "Restart", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMenu();
                // Since we are restarting the game with the same parameters we can pass null as the
                // GameParameterManager. This will cause the Game object to reuse the previously set instance
                SceneManager.startGameScreen(null);
            }
        }, sequenceMap);
        Button  button2 = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, "Back to Menu", skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMenu();
                SceneManager.startMainMenuScreen();
            }
        }, sequenceMap);

        UIToolKit.LinkUpAndDown(button1, button2, sequenceMap);

        endGameMenu = mainPane;
        setDefaultSelection(endGameMenu, button1);
    }

    @Override
    public GetReadyMenuController initGetReadyMenu() {
        Table mainPane = UIToolKit.GenerateSinglePaneContainer(skin);
        Table containerPane = UIToolKit.GenerateHorizontalContainer(mainPane, skin);
        getReadyMenuController = new GetReadyMenuController(containerPane, skin);
        Button  buttonStart = UIToolKit.AddButtonToParentWithAction(mainPane,TEXT_LABEL_START, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (getReadyMenuController.enoughPlayers()) {
                    hideMenu();
                    SceneManager.startGameScreen(gameParams);
                }
            }
        }, sequenceMap);
        Button  buttonBack = UIToolKit.AddButtonToSinglePaneWithAction(mainPane, TEXT_LABEL_BACK, skin, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hideMenu();
                displayMainMenu();
            }
        }, sequenceMap);
        UIToolKit.LinkUpAndDown(buttonStart, buttonBack, sequenceMap);

        getReadyMenu = mainPane;
        setDefaultSelection(getReadyMenu, buttonStart);
        return getReadyMenuController;
    }

    @Override
    public void displayMainMenu() {
        setActorAsVisible(mainMenu);
    }

    @Override
    public void displayPauseMenu() {
        setActorAsVisible(pauseMenu);
        pauseMenuListener.onPauseMenuAppeared();
    }

    @Override
    public void displayEndGameMenu() {
        setActorAsVisible(endGameMenu);
    }

    @Override
    public void displayGetReadyMenu() {
        getReadyMenuController.updateGameParams(gameParams);
        setActorAsVisible(getReadyMenu);
    }

    public void displayConfirmationMenu() {
        setActorAsVisible(confirmationMenu);
    }


    @Override
    public void render(float delta) {
        if (!uiVisible)
            return;
        stage.act(delta);
        controllerManager.update(delta);
        processInput();
        stage.draw();
    }

    private void processInput() {
        List<ControllerManager.ControllerEventTuple> tupleList = controllerManager.getUIEvents();
        for (ControllerManager.ControllerEventTuple tuple : tupleList) {
            UI_EVENTS event = tuple.event;
            handleEvent(event);
        }
    }

    private void handleEvent(UI_EVENTS event) {
        if (event == UI_EVENTS.NOOP)
            return;

        if (event == UI_EVENTS.DOWN || event == UI_EVENTS.UP ||
                event == UI_EVENTS.LEFT || event == UI_EVENTS.RIGHT) {
            Button newSelected = sequenceMap.get(selected).get(event);
            if (newSelected != null)
                setSelected(newSelected);
        } else if (event == UI_EVENTS.SELECT) {
            selected.getClickListener().clicked(null, 0, 0);
        }
    }

    /**
     * This method should only be used for tests.
     * @param event
     */
    public void injectUIEvent(UI_EVENTS event) {
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

    @Override
    public void resize(int width, int height){
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hideMenu() {
        uiVisible = false;
        if (stashedProcessor != null)
            Gdx.input.setInputProcessor(stashedProcessor);
        stashedProcessor = null;
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        sequenceMap.clear();
    }

    @Override
    public void OnGameLoad() {
        skin = new Skin(Gdx.files.internal(ASSET_SKIN_FILE));
        stage = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT)); // TODO: Remove this dependency, these values should be provided as a parameter
        sequenceMap = new HashMap<Button, HashMap<UI_EVENTS, Button>>();
        defaultSelectionMap = new HashMap<Actor, Button>();
        stage.setDebugAll(true);
    }

    @Override
    public void OnScreenLoad() {
    }

    @Override
    public void OnLoopStart() {

    }

    @Override
    public void OnLoopEnd() {

    }

    @Override
    public void OnScreenClose() {

    }

    @Override
    public void OnGameClose() {

    }

    @Override
    public void setControllerManager(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }
}
