package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.AudioManager;
import com.mygdx.game.CallbackManager;
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

    protected float timeBuffer;
    protected boolean useFixedStep;
	protected CallbackManager callbackManager;
	
    protected TiledGameMap map;

    private ArrayList<GameElement> lightSources;
    private float illumination;
    private Texture mainLightTexture;
    private Texture lightTexture;
    private FrameBuffer lightBuffer;

    public BaseScreen(boolean useFixedStep)
    {
        batch = new SpriteBatch();
        timeBuffer = 0;
        cam = new OrthographicCamera(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
        viewport = new StretchViewport(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT, cam);
        this.useFixedStep = useFixedStep;
        map = new TiledGameMap();
        shapeRenderer = new ShapeRenderer();
        lightSources = new ArrayList<GameElement>();
        illumination = 0f;
		callbackManager = new CallbackManager();
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

        cam.position.set(Globals.ASSET_SPRITE_SHEET_SPRITE_WIDTH * map.getWidth()/2f,Globals.ASSET_SPRITE_SHEET_SPRITE_WIDTH * map.getHeight()/2f, 1);

        if (map.getHeight() < map.getWidth())
        {
            viewport.setWorldWidth(map.getWidth() * 32f);
            viewport.setWorldHeight((map.getWidth() * 32f) * ((float)Globals.SCREEN_HEIGHT / (float)Globals.SCREEN_WIDTH));
        }
        else
        {
            viewport.setWorldHeight(map.getHeight() * 32f);
            viewport.setWorldWidth((map.getHeight() * 32f) * ((float)Globals.SCREEN_WIDTH / (float)Globals.SCREEN_HEIGHT));
        }

	    AudioManager.LoadAssets(levelId());
        AudioManager.PlayMusic();

        lightTexture = new Texture(Gdx.files.internal(Globals.ASSET_LIGHT));
        mainLightTexture = new Texture(Gdx.files.internal(Globals.ASSET_MAIN_LIGHT));
        lightBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int)viewport.getWorldWidth(), (int)viewport.getWorldHeight(), false);
    }

    @Override
    public final void render(float delta) {
        if (useFixedStep) {
            timeBuffer += Gdx.graphics.getDeltaTime();
            while (timeBuffer > Globals.FRAME_TIME) {
                performUpdate();
                performRender();
                timeBuffer-=Globals.FRAME_TIME;
            }
        } else {
            performUpdate(delta);
            performRender(delta);
        }
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

        UISystem.draw(delta);
    }

	/**
	 * Implement logic here that is shared for all child classes of BaseScreen.
	 * You can use this method to update the callback manager and other objects
	 * that are tied to the life time of a screen.
	 */
    private final void performUpdate(float delta) {
		callbackManager.update(delta);
		performCustomUpdate(delta);
	}
	
    private final void performUpdate() {
		performUpdate(Globals.FRAME_TIME);
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
        // start rendering to the lightBuffer
        lightBuffer.begin();

        //Set the blending
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL30.GL_BLEND);

        // set the ambient color values, this is the "global" light of your scene
        // imagine it being the sun.  Usually the alpha value is just 1, and you change the darkness/brightness with the Red, Green and Blue values for best effect
        Gdx.gl.glClearColor(0.0f,0.0f,0.0f, illumination);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        // start rendering the lights to our spriteBatch
        batch.begin();
        // set the color of your light (red,green,blue,alpha values)
        batch.setColor(1f, 1f, 1f, 1f);
        batch.draw(mainLightTexture, 0, 0, map.getWidth() * 32, map.getHeight() * 32);
        for (GameElement lightSource : lightSources) {
            Vector2 center = lightSource.getCenterPosition();
            // and render the sprite
            float spriteh = lightTexture.getHeight() * 0.7f;
            float spritew = lightTexture.getWidth() * 0.7f;
            float origX = (center.x * 32f) - (spriteh);
            float origY = (center.y * 32f) - (spritew);

            batch.draw(lightTexture, origX, origY, spritew * 2, spriteh * 2);
        }
        batch.end();
        lightBuffer.end();

        // now we render the lightBuffer to the default "frame buffer"
        // with the right blending !
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_ZERO, GL30.GL_SRC_COLOR);
        batch.enableBlending();
        batch.setBlendFunction(GL30.GL_ZERO, GL30.GL_SRC_COLOR);
        batch.begin();
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_ZERO, GL30.GL_SRC_COLOR);
        batch.draw(lightBuffer.getColorBufferTexture(),
                cam.position.x - ((viewport.getWorldWidth()/2f) * cam.zoom),
                cam.position.y + ((viewport.getWorldHeight()/2f) * cam.zoom),
                viewport.getWorldWidth() * cam.zoom,
                -viewport.getWorldHeight() * cam.zoom);

        batch.end();
        batch.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	/**
	 * This method must be implemented as a way to identify differnt 
	 * child classes. It's use is still not well defined.
	 */
    protected abstract int levelId();

    protected void addLightSource(GameElement newSourceElement){
        if (!this.lightSources.contains(newSourceElement))
            this.lightSources.add(newSourceElement);
    }

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
