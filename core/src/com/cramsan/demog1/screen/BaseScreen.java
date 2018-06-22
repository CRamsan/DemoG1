package com.cramsan.demog1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cramsan.demog1.Globals;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.subsystems.AudioManager;
import com.cramsan.demog1.subsystems.CallbackManager;
import com.cramsan.demog1.subsystems.SingleAssetManager;
import com.cramsan.demog1.subsystems.controller.ControllerConnectionListener;
import com.cramsan.demog1.subsystems.controller.ControllerManager;
import com.cramsan.demog1.subsystems.controller.PlayerController;
import com.cramsan.demog1.subsystems.map.TiledGameMap;
import com.cramsan.demog1.subsystems.ui.IUISystem;

import java.util.ArrayList;

/**
 * Base class to handle all code shared across all scenes. This class will configure the camera, the background map
 * as well as calling the update method.
 */
public abstract class BaseScreen implements Screen, ControllerConnectionListener, ContactListener {

    private OrthographicCamera cam;
    private SpriteBatch batch;
    private Viewport viewport;

    private boolean renderEnabled;
    private CallbackManager callbackManager;
    private AudioManager audioManager;
    private SingleAssetManager assetManager;
    private IUISystem uiSystem;

    private TiledGameMap map;
    private World gameWorld;
    private Box2DDebugRenderer debugRenderer;

    private ArrayList<GameElement> lightSources;
    private float illumination;
    private Texture mainLightTexture;
    private Texture lightTexture;
    private FrameBuffer lightBuffer;

    public BaseScreen()
    {
        cam = new OrthographicCamera(Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
        viewport = new StretchViewport(cam.viewportWidth, cam.viewportHeight, cam);
        gameWorld = new World(Vector2.Zero, true);
        gameWorld.setContactListener(this);
        map = new TiledGameMap(gameWorld);
        debugRenderer = new Box2DDebugRenderer();
        lightSources = new ArrayList<GameElement>();
        illumination = 0f;
		callbackManager = new CallbackManager();
    }

    // This method will be called to configure objects. This is used to decouple the object initialization
    // From their configuration in the game world.
    public void ScreenInit() {
        map.setBatch(getBatch());
        map.TimedGameMapInit();

        cam.setToOrtho(false);
        cam.update();
        int portIndex = 0;
        ControllerManager.setControllerConnectionListener(this);
        for(PlayerController controller : ControllerManager.getConnectedControllers()) {
            onControllerConnected(portIndex, controller);
            portIndex++;
        }

        cam.position.set(map.getTileWidth() * map.getWidth()/2f,map.getTileHeight() * map.getHeight()/2f, 1);

        if (map.getHeight() < map.getWidth())
        {
            viewport.setWorldHeight((map.getWidth() * map.getTileWidth()) * (viewport.getWorldHeight() / viewport.getWorldWidth()));
            viewport.setWorldWidth(map.getWidth() * map.getTileWidth());
        }
        else
        {
            viewport.setWorldWidth((map.getHeight() * map.getTileHeight()) * (viewport.getWorldWidth() / viewport.getWorldHeight()));
            viewport.setWorldHeight(map.getHeight() * map.getTileHeight());
        }

        getUiSystem().resize((int)viewport.getWorldWidth(), (int)viewport.getWorldHeight());
        // TODO: Fix this ASAP!
	    //AudioManager.LoadAssets(levelId());
        //AudioManager.PlayMusic();

        lightTexture = getAssetManager().getLightTexture();
        mainLightTexture = getAssetManager().getSceneLightTexture();
        lightBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight(), false);
    }

    @Override
    public final void render(float delta) {
        performUpdate(delta);
        performRender(delta);
    }

    /**
     * This method will render the scene always after a fixed time step
     */
    private void performRender(float delta) {
        if (!renderEnabled)
            return;

        viewport.apply();
        cam.update();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        performRenderMap(delta);

        getBatch().setProjectionMatrix(cam.combined);
        getBatch().begin();
        performRenderSprites();
        getBatch().end();

        performLightingRender();
        debugRenderer.render(gameWorld, cam.combined);
        getUiSystem().render(delta);
    }

	/**
	 * Implement logic here that is shared for all child classes of BaseScreen.
	 * You can use this method to update the callback manager and other objects
	 * that are tied to the life time of a screen.
	 */
    private void performUpdate(float delta) {
		callbackManager.update(delta);
		map.performUpdate(delta);
		performCustomUpdate(delta);
        gameWorld.step(delta, 6, 2);
	}

	/**
	 * Implement logic here that specific to each implementation of
	 * this class. This method will use the provided time delta for 
	 * the step. This update method is called before each frame.
	 */
    protected abstract void performCustomUpdate(float delta);

	/**
	 * First render call
	 * Implement logic here to draw into the background. This 
	 * will be mostly used to draw the map. But it can be used to
	 * also render anything else behind the sprites.
	 */	
    private void performRenderMap(float delta) {
        map.render(cam, delta);
    }

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
	private void performLightingRender(){
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
        getBatch().begin();
        // set the color of your light (red,green,blue,alpha values)
        getBatch().setColor(1f, 1f, 1f, 1f);
        getBatch().draw(mainLightTexture, 0, 0, map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
        for (GameElement lightSource : lightSources) {
            Vector2 center = lightSource.getCenterPosition();
            // and render the sprite
            float spriteh = lightTexture.getHeight() * 0.7f;
            float spritew = lightTexture.getWidth() * 0.7f;
            float origX = (center.x) - (spriteh);
            float origY = (center.y) - (spritew);

            getBatch().draw(lightTexture, origX, origY, spritew * 2, spriteh * 2);
        }
        getBatch().end();
        lightBuffer.end();

        // now we render the lightBuffer to the default "frame buffer"
        // with the right blending !
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_ZERO, GL30.GL_SRC_COLOR);
        getBatch().enableBlending();
        getBatch().setBlendFunction(GL30.GL_ZERO, GL30.GL_SRC_COLOR);
        getBatch().begin();
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_ZERO, GL30.GL_SRC_COLOR);
        getBatch().draw(lightBuffer.getColorBufferTexture(),
                cam.position.x - ((viewport.getWorldWidth()/2f) * cam.zoom),
                cam.position.y + ((viewport.getWorldHeight()/2f) * cam.zoom),
                viewport.getWorldWidth() * cam.zoom,
                -viewport.getWorldHeight() * cam.zoom);

        getBatch().end();
        getBatch().setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
	}

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void beginContact(Contact contact) {
        GameElement elem1 = (GameElement)contact.getFixtureA().getBody().getUserData();
        GameElement elem2 = (GameElement)contact.getFixtureB().getBody().getUserData();

        if (elem1 == null || elem2 == null)
            return;

        elem2.onContact(elem1);
        elem1.onContact(elem2);
    }

    public void preSolve (Contact contact, Manifold oldManifold)
    {}

    public void postSolve (Contact contact, ContactImpulse impulse)
    {}

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
        //UISystem.disposeMenu();
	    //AudioManager.UnloadAssets();
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
        //UISystem.resizeUI(width, height);
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

    public boolean isRenderEnabled() {
        return renderEnabled;
    }

    public void setRenderEnabled(boolean renderEnabled) {
        this.renderEnabled = renderEnabled;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
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

    public IUISystem getUiSystem() {
        return this.uiSystem;
    }

    public void setUiSystem(IUISystem uiSystem) {
        this.uiSystem = uiSystem;
    }

    public TiledGameMap getMap() {
        return map;
    }

    public void setMap(TiledGameMap map) {
        this.map = map;
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public void setGameWorld(World gameWorld) {
        this.gameWorld = gameWorld;
    }

    public Box2DDebugRenderer getDebugRenderer() {
        return debugRenderer;
    }

    public void setDebugRenderer(Box2DDebugRenderer debugRenderer) {
        this.debugRenderer = debugRenderer;
    }
}
