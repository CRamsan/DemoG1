package com.cramsan.demog1.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.cramsan.demog1.Globals;
import com.cramsan.demog1.gameelements.GameCollision;

import java.util.ArrayList;

/**
 * This class will handle the tiled map. Both rendering and interaction with the tiled
 * map will be done here.
 */
public class TiledGameMap
{
	private static final String ASSET_TMX_MAP = "town.tmx";

	private TiledMap map;
	private TiledMapRenderer renderer;
	private boolean[][] collisionMap;
	private int width, height, tileWidth, tileHeight;

	public TiledGameMap(World gameWorld, SpriteBatch batch) {
		map = new TmxMapLoader().load(ASSET_TMX_MAP);
		renderer = new OrthogonalTiledMapRenderer(map, batch);
		width = map.getProperties().get("width", Integer.class);
		height = map.getProperties().get("height", Integer.class);
		tileWidth = map.getProperties().get("tilewidth", Integer.class);
		tileHeight = map.getProperties().get("tileheight", Integer.class);
		collisionMap = new boolean[width][height];
		//The collision layer should be located on the first layer
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(2);

		for (int y = 0; y < width; y++) {
			for (int x = 0; x < height; x++) {
			    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
			    if (cell == null)
			        continue;
				MapProperties props = cell.getTile().getProperties();
				if (props != null) {
					if (props.containsKey("Solid") && props.get("Solid", Boolean.class)) {
						BodyDef groundBodyDef = new BodyDef();
						groundBodyDef.type = BodyDef.BodyType.StaticBody;
						groundBodyDef.position.set(new Vector2((x * tileWidth) + (tileWidth/2), (y * tileHeight) + (tileHeight/2)));
						Body groundBody = gameWorld.createBody(groundBodyDef);
						PolygonShape groundBox = new PolygonShape();
						groundBox.setAsBox(tileWidth/2, tileHeight/2);
						FixtureDef fixtureDef = new FixtureDef();
						fixtureDef.shape = groundBox;
						fixtureDef.filter.categoryBits = GameCollision.Obstacle;
						fixtureDef.filter.maskBits = GameCollision.Player;
						groundBody.createFixture(fixtureDef);
						groundBox.dispose();
					}
				}
			}
		}
		// Build the external walls
		float vRad = (height / 2f) * tileHeight;
		float hRad = (width / 2f) * tileWidth;
		PolygonShape groundBox = new PolygonShape();
		BodyDef groundBodyDef = new BodyDef();
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = groundBox;
		fixtureDef.filter.categoryBits = GameCollision.Obstacle;
		fixtureDef.filter.maskBits = GameCollision.Player;

		// Bottom wall
		groundBodyDef.position.set(new Vector2(hRad, vRad * -1));
		Body groundBody = gameWorld.createBody(groundBodyDef);
		groundBox.setAsBox(hRad, vRad);
		groundBody.createFixture(fixtureDef);

		// Right wall
		groundBodyDef.position.set(new Vector2(hRad * 3, vRad));
		groundBody = gameWorld.createBody(groundBodyDef);
		groundBox.setAsBox(hRad, vRad);
		groundBody.createFixture(fixtureDef);

		// LEft wall
		groundBodyDef.position.set(new Vector2(hRad * -1, vRad));
		groundBody = gameWorld.createBody(groundBodyDef);
		groundBox.setAsBox(hRad, vRad);
		groundBody.createFixture(fixtureDef);

		// Top wall
		groundBodyDef.position.set(new Vector2(hRad, vRad * 3));
		groundBody = gameWorld.createBody(groundBodyDef);
		groundBox.setAsBox(hRad, vRad);
		groundBody.createFixture(fixtureDef);

		groundBox.dispose();
	}

	public void performUpdate(float delta) {

	}

	public void render (OrthographicCamera camera, float delta) {
		renderer.setView(camera);
		renderer.render();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public boolean isTileOutOfBounds(int x, int y) {
		return (x < 0 || y < 0 || y >= height || x >= width);
	}

	public boolean isTileSolid(int x, int y) {
		return collisionMap[x][y];
	}

	public ArrayList<Vector2> getStatueSpawner() {
		ArrayList<Vector2> spawnList = new ArrayList<Vector2>();
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
		int columns = this.getWidth();
		int rows = this.getHeight();
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				TiledMapTileLayer.Cell cell = layer.getCell(x, y);
				if (cell == null)
					continue;
				spawnList.add(new Vector2(x, y));
			}
		}
		return spawnList;
	}

	public Vector2 getRandomNonSolidTile() {
		// Dangerous loop with risk of running forever
		while (true) {
			int posX = Globals.rand.nextInt(this.getWidth());
			int posY = Globals.rand.nextInt(this.getHeight());
			if (this.isTileSolid(posX, posY)) {
				continue;
			}
			return new Vector2(posX, posY);
		}
	}
}
