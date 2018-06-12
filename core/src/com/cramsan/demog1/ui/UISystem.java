package com.cramsan.demog1.ui;

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
import com.cramsan.demog1.MyGdxGame;
import com.cramsan.demog1.controller.ControllerManager;
import com.cramsan.demog1.gameelements.GameParameterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class provides static methods to configure UI components.
 */
public class UISystem {

    /**
     * Public API calls
     */
    public static void UISytemInit(float worldWidth, float worldHeight) {
        ourInstance.initInternal(worldWidth, worldHeight);
    }

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

    public static void injectUIEvent(IUISystem.UI_EVENTS event) {
        ourInstance.injectUIEventInternal(event);
    }

    public static void setUISystem(IUISystem uiSystem) {
        ourInstance = uiSystem;
    }

    /**
     * Private calls
     */

    private static IUISystem ourInstance;
}
