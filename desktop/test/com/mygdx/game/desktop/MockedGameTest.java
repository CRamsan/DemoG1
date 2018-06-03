package com.mygdx.game.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockito.Mockito;

public class MockedGameTest {

    private static Application application;

    @BeforeClass
    public static void oneTimeSetUp() {
        Gdx.gl30 = Mockito.mock(GL30.class);
        Gdx.gl20 = Gdx.gl30;
        Gdx.gl = Gdx.gl20;

        SpriteBatch batch = Mockito.mock(SpriteBatch.class);

        MyGdxGame game = new MyGdxGame(true, batch);
        application = new HeadlessApplication(game);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        application.exit();
        application = null;
    }
}