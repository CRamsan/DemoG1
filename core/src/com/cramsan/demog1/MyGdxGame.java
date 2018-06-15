package com.cramsan.demog1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.cramsan.demog1.ui.GameUISystem;
import com.cramsan.demog1.ui.IUISystem;
import com.cramsan.demog1.ui.UISystem;

public class MyGdxGame extends Game {

    private boolean useFixedStep;
    private boolean enableRender;
    private boolean enableGame;
    private SpriteBatch spriteBatch;
    private IUISystem uiSystem;
    private IGameStateListener listener;

    public MyGdxGame() {
        useFixedStep = true;
        enableGame = true;
        enableRender = true;
        spriteBatch = null;
        uiSystem = null;
        listener = null;
    }

    @Override
    public void create() {
        if (this.getSpriteBatch() == null) {
            setSpriteBatch(new SpriteBatch());
        }
        if (getUiSystem() == null) {
            UISystem.setUISystem(new GameUISystem());
        } else {
            UISystem.setUISystem(getUiSystem());
        }
        SingleAssetManager.initSingleAssetManager();
        Box2D.init();
        if (isEnableGame())
            MyGdxGameManager.startMainMenuScreen();

        if (getListener() != null)
            getListener().onGameCreated();
    }

    @Override
    public void resume () {
        super.resume();
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void pause () {
        super.pause();
    }

    @Override
    public void dispose () {
        super.dispose();
        SingleAssetManager.unInitSingleAssetManager();
        if (getListener() != null)
            getListener().onGameDestroyed();
    }

    public boolean isUseFixedStep() {
        return useFixedStep;
    }

    public void setUseFixedStep(boolean useFixedStep) {
        this.useFixedStep = useFixedStep;
    }

    public boolean isEnableRender() {
        return enableRender;
    }

    public void setEnableRender(boolean enableRender) {
        this.enableRender = enableRender;
    }

    public boolean isEnableGame() {
        return enableGame;
    }

    public void setEnableGame(boolean enableGame) {
        this.enableGame = enableGame;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public IUISystem getUiSystem() {
        return uiSystem;
    }

    public void setUiSystem(IUISystem uiSystem) {
        this.uiSystem = uiSystem;
    }

    public IGameStateListener getListener() {
        return listener;
    }

    public void setListener(IGameStateListener listener) {
        this.listener = listener;
    }
}
