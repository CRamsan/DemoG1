package com.cramsan.demog1.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen implements Screen {
    private SpriteBatch batch;
    private Texture texture;
    private float minLoadTime;
    private IResourcesLoaded onComplete;
    private boolean loadCompleted;
    private float counter;

    public interface IResourcesLoaded {
        void onResourcesLoaded();
    }

    public SplashScreen(float minTime, IResourcesLoaded onComplete) {
        this.minLoadTime = minTime;
        this.onComplete = onComplete;
        this.counter = 0;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("android-mod.png"));
    }

    @Override
    public void render(float delta) {
        if (isLoadCompleted() && counter > minLoadTime) {
            onComplete.onResourcesLoaded();
        } else {
            counter += delta;
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.
            batch.begin();
            batch.draw(texture, 10, 10, 10, 10);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {

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

    @Override
    public void dispose() {

    }

    public boolean isLoadCompleted() {
        return loadCompleted;
    }

    public void setLoadCompleted(boolean loadCompleted) {
        this.loadCompleted = loadCompleted;
    }
}
