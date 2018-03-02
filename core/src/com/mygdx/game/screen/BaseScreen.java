package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AudioManager;
import com.mygdx.game.Globals;
import com.mygdx.game.TiledGameMap;
import com.mygdx.game.controller.ControllerConnectionListener;
import com.mygdx.game.controller.ControllerManager;
import com.mygdx.game.controller.PlayerController;
import com.mygdx.game.gameelements.GameElement;
import com.mygdx.game.ui.UISystem;

import java.util.ArrayList;

/**
 * Base class to handle all code shared across all scenes. This class will configure the camera, the background map
 * as well as calling the update method.
 */
public abstract class BaseScreen implements Screen, ControllerConnectionListener {

    protected OrthographicCamera cam;
    protected SpriteBatch batch;
    protected Viewport viewport;
    protected ShapeRenderer shapeRenderer;

    protected FPSLogger logger;
    protected float timeBuffer;
    protected boolean useFixedStep;

    protected TiledGameMap map;
    protected ArrayList<GameElement> lightSources;
	protected float illumination = 0f;
	
    public BaseScreen(boolean useFixedStep)
    {
        batch = new SpriteBatch();
        timeBuffer = 0;
        cam = new OrthographicCamera(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
        //viewport = new ScreenViewport(cam);
        viewport = new StretchViewport(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT, cam);
        logger = new FPSLogger();
        this.useFixedStep = useFixedStep;
        map = new TiledGameMap();
        shapeRenderer = new ShapeRenderer();
        lightSources = new ArrayList<GameElement>();
        illumination = 0f;
    }

    // This method will be called to configure objects. This is used to decouple the object initialization
    // From their configuration in the game world.
    public void ScreenInit() {
        cam.setToOrtho(false);
        cam.update();
        int portIndex = 0;
        ControllerManager.setControllerConnectionListener(this);
        for(PlayerController controller : ControllerManager.getConnectedControllers()) {
            onControllerConnected(portIndex, controller);
            portIndex++;
        }

        cam.position.set(Globals.ASSET_SPRITE_SHEET_SPRITE_WIDTH * map.getWidth()/2f,
                Globals.ASSET_SPRITE_SHEET_SPRITE_WIDTH * map.getHeight()/2f, 1);

	    AudioManager.LoadAssets(levelId());
        AudioManager.PlayMusic();
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
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        performRenderMap();

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        performRenderSprites();
        batch.end();

        performLightingRender();
        Gdx.gl.glDisable(GL30.GL_BLEND);

        UISystem.draw(delta);
    }

	/**
	 * Implement logic here that specific to each implementation of
	 * this class. This method will use the provided time delta for 
	 * the step. This update method is called before each frame.
	 */
    protected abstract void performCustomUpdate(float delta);

	/**
	 * Implement logic here that specific to each implementation of
	 * this class. This method will use the default time step defined
	 * in Globals.FRAME_TIME.
	 */
    protected abstract void performCustomUpdate();

	/**
	 * First render call
	 * Implement logic here to draw into the background. This 
	 * will be mostly used to dran the map. But it can be used to 
	 * also render anything else behind the sprites.
	 */	
    protected abstract void performRenderMap();

	/**
	 * Second render call
	 * Implement logic here to draw sprites and most other game 
	 * elements. 
	 */	
    protected abstract void performRenderSprites();

	/**
	 * Third render call
	 * Implement logic here to draw the lighting and post processing.
	 * Anything rendered here will not affect the UI since that will 
	 * happen last.
	 */
	protected void performLightingRender(){
		// Configure the light in the scene

        for (GameElement lightSource : lightSources) {
            Vector2 center = lightSource.getCenterPosition();
			// Light the scene using this light source
        }

		// If using a light buffer, make sure to write the lighting 
		// back to the main buffer.
    }
	
	/**
	 * This method must be implemented as a way to identify differnt 
	 * child classes. It's use is still not well defined.
	 */
    protected abstract int levelId();

    @Override
    public void dispose() {
        UISystem.disposeMenu();
	    AudioManager.UnloadAssets();
    }

    public void setIllumination(float illumination) {
        if (illumination > 1 || illumination < 0)
            throw new RuntimeException("Illumination value should be between 0 and 1, got: " + illumination);
        this.illumination = 1f - illumination;
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
