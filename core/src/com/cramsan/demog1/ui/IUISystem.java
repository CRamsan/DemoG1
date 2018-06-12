package com.cramsan.demog1.ui;

public interface IUISystem {

    void initInternal(float worldWidth, float worldHeight);

    void displayMainMenuInternal();

    void displayPauseMenuInternal();

    void displayEndGameMenuInternal();

    void displayGetReadyMenuInternal();

    void render(float delta);

    void resize(int width, int height);

    void hideMenuInternal();

    void dispose();

    void initMainMenuInternal();

    void initPauseMenuInternal(PauseMenuEventListener pauseMenuListener);

    void initConfirmationMenuInternal();

    void initEndGameMenuInternal();

    GetReadyMenuController initGetReadyMenuInternal();

    void displayConfirmationMenuInternal();

    void injectUIEventInternal(UI_EVENTS event);

    enum UI_EVENTS { UP, DOWN, LEFT, RIGHT, SELECT, NOOP }
}
