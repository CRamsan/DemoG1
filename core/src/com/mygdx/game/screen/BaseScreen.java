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
    protected float illumination;
    protected Texture lightTexture;
    protected ArrayList<GameElement> lightSources;
    private FrameBuffer lightBuffer;
    private TextureRegion lightBufferRegion;

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
        //viewport.setUnitsPerPixel(1f/(Globals.TILE_SIZE * Globals.MAGNIFICATION));
        int portIndex = 0;
        ControllerManager.setControllerConnectionListener(this);
        for(PlayerController controller : ControllerManager.getConnectedControllers()) {
            onControllerConnected(portIndex, controller);
            portIndex++;
        }

        lightTexture = new Texture(Gdx.files.internal("light.png"));
        lightBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT, false);
        lightBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        lightBufferRegion = new TextureRegion(lightBuffer.getColorBufferTexture(),0,0, Globals.SCREEN_WIDTH,Globals.SCREEN_HEIGHT);
        lightBufferRegion.flip(false, false);

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

    protected void performLightingRender(){
        // start rendering to the lightBuffer
        lightBuffer.begin();
        // setup the right blending
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE);
        Gdx.gl.glEnable(GL30.GL_BLEND);
        // set the ambient color values, this is the "global" light of your scene
        // imagine it being the sun.  Usually the alpha value is just 1, and you change the darkness/brightness with the Red, Green and Blue values for best effect
        Gdx.gl.glClearColor(1f,0.0f,0.0f, illumination);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        // start rendering the lights to our spriteBatch
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        // set the color of your light (red,green,blue,alpha values)
        batch.setColor(1f, 1f, 1f, 1f);
        for (GameElement lightSource : lightSources) {
            Vector2 center = lightSource.getCenterPosition();
            // and render the sprite
            float origX = center.x * 32f - (lightTexture.getWidth() / 2f);
            float origY = center.y * 32f - (lightTexture.getHeight() / 2f);
            batch.draw(lightTexture, origX, origY, lightTexture.getWidth(),lightTexture.getHeight());
        }
        batch.end();
        lightBuffer.end();
        // now we render the lightBuffer to the default "frame buffer"
        // with the right blending !
        Gdx.gl.glBlendFunc(GL30.GL_DST_COLOR, GL30.GL_ZERO);
        batch.begin();
        batch.draw(lightBufferRegion, 0, 0, Globals.SCREEN_WIDTH, Globals.SCREEN_HEIGHT);
        batch.end();
    }

    protected abstract void performCustomUpdate();

    protected abstract void performCustomUpdate(float delta);

    protected abstract void performRenderMap();

    protected abstract void performRenderSprites();

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
