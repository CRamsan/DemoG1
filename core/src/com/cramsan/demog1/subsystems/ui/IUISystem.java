package com.cramsan.demog1.subsystems.ui;

import com.cramsan.demog1.subsystems.IGameSubsystem;
import com.cramsan.demog1.subsystems.controller.ControllerManager;

public interface IUISystem extends IGameSubsystem {

    void displayMainMenu();

    void displayPauseMenu();

    void displayEndGameMenu();

    void displayGetReadyMenu();

    void render(float delta);

    void resize(int width, int height);

    void hideMenu();

    void dispose();

    void initMainMenu();

    void initPauseMenu(PauseMenuEventListener pauseMenuListener);

    void initConfirmationMenu();

    void initEndGameMenu();

    GetReadyMenuController initGetReadyMenu();

    void displayConfirmationMenu();

    void injectUIEvent(UI_EVENTS event);

    void setControllerManager(ControllerManager controllerManager);

    enum UI_EVENTS { UP, DOWN, LEFT, RIGHT, SELECT, NOOP }
}
