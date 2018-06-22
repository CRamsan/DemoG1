package com.cramsan.demog1;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.cramsan.demog1.screen.BaseScreen;
import com.cramsan.demog1.subsystems.AudioManager;
import com.cramsan.demog1.subsystems.CallbackManager;
import com.cramsan.demog1.subsystems.IGameSubsystem;
import com.cramsan.demog1.subsystems.SingleAssetManager;
import com.cramsan.demog1.subsystems.ui.GameUISystem;
import com.cramsan.demog1.subsystems.ui.IUISystem;

import java.util.ArrayList;

public class MyGdxGame extends Game {

    public static final float FRAME_TIME = 1f/60f;

    private boolean useFixedStep;
    private boolean enableRender;
    private boolean enableGame;
    private SpriteBatch spriteBatch;
    private IUISystem uiSystem;
    private float timeBuffer;

    // Game subsystems. All this classes implement IGameSubsystem to make them easier to
    // manage and mock.
    private AudioManager audioManager;
    private CallbackManager callbackManager;
    private SingleAssetManager assetManager;

    private ArrayList<IGameSubsystem> subsystemList;

    private IGameStateListener listener;

    public MyGdxGame() {
        useFixedStep = true;
        enableGame = true;
        enableRender = true;
        spriteBatch = null;
        uiSystem = null;
        listener = null;
        timeBuffer = 0;
        subsystemList = new ArrayList<IGameSubsystem>();
    }

    @Override
    public void create() {

        SceneManager.setGame(this);

        if (getUiSystem() == null) {
            setUiSystem(new GameUISystem());
        }
        subsystemList.add(getUiSystem());

        if (getAudioManager() == null) {
            setAudioManager(new AudioManager());
        }
        subsystemList.add(getAudioManager());

        if (getCallbackManager() == null) {
            setCallbackManager(new CallbackManager());
        }
        subsystemList.add(getCallbackManager());

        if (getAssetManager() == null) {
            setAssetManager(new SingleAssetManager());
        }
        subsystemList.add(getAssetManager());

        for (IGameSubsystem subsystem : subsystemList) {
            subsystem.InitSystem();
        }

        // We don't need to mock Box2D so it is safe to be left here.
        Box2D.init();

        if (getSpriteBatch() == null) {
            setSpriteBatch(new SpriteBatch());
        }

        if (isEnableGame())
            SceneManager.startMainMenuScreen();

        if (getListener() != null)
            getListener().onGameCreated();
    }

    public void setScreen (BaseScreen newScreen) {
        // Check if there was an existing screen and if there was hide it.
        if (this.screen != null) {
            this.screen.hide();
            for (IGameSubsystem subsystem : subsystemList) {
                subsystem.UnInitScreen();
            }
        }

        // Prepare the new screen
        newScreen.setBatch(getSpriteBatch());
        newScreen.setAssetManager(getAssetManager());
        newScreen.setAudioManager(getAudioManager());
        newScreen.setCallbackManager(getCallbackManager());
        newScreen.setUiSystem(getUiSystem());
        newScreen.setRenderEnabled(isEnableRender());
        for (IGameSubsystem subsystem : subsystemList) {
            subsystem.InitScreen();
        }
        newScreen.ScreenInit();

        // Set the screen and show it.
        this.screen = newScreen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    @Override
    public void resume () {
        super.resume();
    }

    @Override
    public void render () {
        if (!isEnableRender() || screen == null)
            return;
        if (isUseFixedStep()) {
            timeBuffer += Gdx.graphics.getDeltaTime();
            while (timeBuffer > FRAME_TIME) {
                screen.render(FRAME_TIME);
                timeBuffer-=FRAME_TIME;
            }
        } else {
            screen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void pause () {
        super.pause();
    }

    @Override
    public void dispose () {
        super.dispose();
        for (IGameSubsystem subsystem : subsystemList) {
            subsystem.UnInitSystem();
        }

        SceneManager.clearGame();

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

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    public void setCallbackManager(CallbackManager callbackManager) {
        this.callbackManager = callbackManager;
    }

    public SingleAssetManager getAssetManager() {
        return assetManager;
    }

    public void setAssetManager(SingleAssetManager assetManager) {
        this.assetManager = assetManager;
    }
}
