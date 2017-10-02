package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ControllerManager;
import com.mygdx.game.Globals;
import com.mygdx.game.ui.UISystem;

public abstract class MyGdxBaseScreen implements Screen, ControllerManager.ControllerConnectionListener {

    protected OrthographicCamera cam;
    protected SpriteBatch batch;
    protected ScreenViewport viewport;

    protected float timeBuffer = 0;

    public MyGdxBaseScreen()
    {
        batch = new SpriteBatch();
        timeBuffer = 0;
        cam = new OrthographicCamera();
        viewport = new ScreenViewport(cam);
    }

    // This method will be called to configure objects. This is used to decouple the object initialization
    // From their configuration in the game world.
    public void ScreenInit() {
        cam.setToOrtho(false);
        cam.update();
        viewport.setUnitsPerPixel(1f/(Globals.TILE_SIZE * Globals.MAGNIFICATION));
        int portIndex = 0;
        ControllerManager.setControllerConnectionListener(this);
        for(Controller controller : ControllerManager.getConnectedControllers()) {
            onControllerConnected(portIndex, controller);
            portIndex++;
        }
    }

    @Override
    public final void render(float delta) {
        timeBuffer += Gdx.graphics.getDeltaTime();
        while (timeBuffer > Globals.FRAME_TIME) {
            performCustomUpdate();
            performRender();
            timeBuffer-=Globals.FRAME_TIME;
        }
    }

    /**
     * This method will render the scene always after a fixed time step
     */
    private final void performRender() {
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
        UISystem.draw(Globals.FRAME_TIME);
    }

    protected abstract void performCustomUpdate();

    protected abstract void performRenderMap();

    protected abstract void performRenderSprites();

    protected abstract void performDebugRender();

    @Override
    public void dispose() {
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
