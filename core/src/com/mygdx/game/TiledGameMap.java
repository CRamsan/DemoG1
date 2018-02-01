package com.mygdx.game;

import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.graphics.*;

import static com.mygdx.game.Globals.ASSET_TMX_MAP;

/**
 * This class will handle the tiled map. Both rendering and interaction with the tiled
 * map will be done here.
 */
public class TiledGameMap
{
	private TiledMap map;
	private TiledMapRenderer renderer;
	private boolean[][] collisionMap;
	
	public TiledGameMap() {
		map = new TmxMapLoader().load(ASSET_TMX_MAP);

		// TODO Make this a global constant or a runtime value
		renderer = new OrthogonalTiledMapRenderer(map);
		int columns = this.getWidth();
		int rows = this.getHeight();
		collisionMap = new boolean[columns][rows];
		//The collision layer should be located on the first layer
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);

		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < columns; x++) {
				MapProperties props = layer.getCell(x, y).getTile().getProperties();
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
		return map.getProperties().get("width", Integer.class);
	}
	
	public int getHeight() {
		return map.getProperties().get("height", Integer.class);
	}

	public boolean isTileSolid(int x, int y) {
		if (x < 0 || y < 0 || x >= collisionMap.length || y >= collisionMap[0].length)
			return true;
		return collisionMap[x][y];
	}
}
