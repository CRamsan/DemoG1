package com.cramsan.demog1.core.subsystem.map;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cramsan.demog1.core.MockedGameTest;
import com.cramsan.demog1.subsystems.map.TiledGameMap;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class TiledGameMapTest extends MockedGameTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void getRandomNonSolidTileTest() {
        TiledGameMap gameMap = new TiledGameMap(gdxGame.getSpriteBatch());
        gdxGame.setGameMap(gameMap);
        World gameWorld = new World(Vector2.Zero, true);
        gameMap.setGameWorld(gameWorld);

        assertNull(gameMap.getRandomNonSolidTile());

        gameMap.OnGameLoad();
        gameMap.OnScreenLoad();
        for (int i = 0; i < 1000; i++) {
            Vector2 vec = gameMap.getRandomNonSolidTile();
            assertFalse(gameMap.isTileSolid((int)vec.x, (int)vec.y));
        }
    }

    @org.junit.Test
    public void getStatueSpawnerTest() {
        TiledGameMap gameMap = new TiledGameMap(gdxGame.getSpriteBatch());
        gdxGame.setGameMap(gameMap);
        World gameWorld = new World(Vector2.Zero, true);
        gameMap.setGameWorld(gameWorld);
        gameMap.OnGameLoad();
        gameMap.OnScreenLoad();
        ArrayList<Vector2> spawnList = gameMap.getStatueSpawner();
        for (Vector2 vector : spawnList) {
            assertFalse(gameMap.isTileSolid((int)vector.x, (int)vector.y));
        }
    }
}