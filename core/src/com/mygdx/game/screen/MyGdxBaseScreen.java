package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AudioManager;
import com.mygdx.game.Globals;
import com.mygdx.game.TiledGameMap;
import com.mygdx.game.controller.ControllerConnectionListener;
import com.mygdx.game.controller.ControllerManager;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.ui.UISystem;

/**
 * Base class to handle all code shared across all scenes. This class will confgure the camera, the background map
 * as well as calling the update method.
 */
public abstract class MyGdxBaseScreen implements Screen, ControllerConnectionListener {

    protected OrthographicCamera cam;
    protected SpriteBatch batch;
    //protected ScreenViewport viewport;
    protected Viewport viewport;

    protected FPSLogger logger;
    protected float timeBuffer;
    protected boolean useFixedStep;

    protected TiledGameMap map;

    public MyGdxBaseScreen(boolean useFixedStep)
    {
        batch = new SpriteBatch();
        timeBuffer = 0;
        cam = new OrthographicCamera(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
        viewport = new ScreenViewport(cam);
        //viewport = new StretchViewport(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT, cam);
        logger = new FPSLogger();
        this.useFixedStep = useFixedStep;
        map = new TiledGameMap();
    }

    // This method will be called to configure objects. This is used to decouple the object initialization
    // From their configuration in the game world.
    public void ScreenInit() {
        cam.setToOrtho(false);
        cam.update();
        //viewport.setUnitsPerPixel(1f/(Globals.TILE_SIZE * Globals.MAGNIFICATION));
        int portIndex = 0;
        ControllerManager.setControllerConnectionListener(this);
        for(PlayerController controller : ControllerManager.getConnectedControllers()) {
            onControllerConnected(portIndex, controller);
            portIndex++;
        }
        cam.position.set(map.getWidth()/2f, map.getHeight()/2f, 1);
	    AudioManager.LoadAssets(levelId());
    }

    @Override
    public final void render(float delta) {
        if (useFixedStep) {
            timeBuffer += Gdx.graphics.getDeltaTime();
            while (timeBuffer > Globals.FRAME_TIME) {
                performCustomUpdate();
                performRender();
                timeBuffer-=Globals.FRAME_TIME;
            }
        } else {
            performCustomUpdate(delta);
            performRender(delta);
        }
        logger.log();
    }

    /**
     * This method will render the scene always after a fixed time step
     */
    private final void performRender() {
        performRender(Globals.FRAME_TIME);
    }

    /**
     * This method will render the scene always after a fixed time step
     */
    private final void performRender(float delta) {
        viewport.apply();
        cam.update();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        performRenderMap();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        performRenderSprites();
        batch.end();
        performDebugRender();
        UISystem.draw(delta);
    }

    protected abstract void performCustomUpdate();

    protected abstract void performCustomUpdate(float delta);

    protected abstract void performRenderMap();

    protected abstract void performRenderSprites();

    protected abstract void performDebugRender();

    protected abstract int levelId();

    @Override
    public void dispose() {
        UISystem.disposeMenu();
	    AudioManager.UnloadAssets();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        UISystem.resizeUI(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
