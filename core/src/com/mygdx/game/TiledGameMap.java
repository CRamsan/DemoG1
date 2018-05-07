package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;

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

	public TiledGameMap() {
		map = new TmxMapLoader().load(ASSET_TMX_MAP);
		renderer = new OrthogonalTiledMapRenderer(map);
		width = map.getProperties().get("width", Integer.class);
		height = map.getProperties().get("height", Integer.class);
		tileWidth = map.getProperties().get("tilewidth", Integer.class);
		tileHeight = map.getProperties().get("tileheight", Integer.class);
		collisionMap = new boolean[width][height];
		//The collision layer should be located on the first layer
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(2);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
			    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
			    if (cell == null)
			        continue;
				MapProperties props = cell.getTile().getProperties();
				if (props != null) {
					if (props.containsKey("Solid") && props.get("Solid", Boolean.class)) {
						collisionMap[x][y] = true;
					}
				}
			}
		}
	}
	
	public void render (OrthographicCamera camera) {
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
