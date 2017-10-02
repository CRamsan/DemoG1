package com.mygdx.game;

import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.graphics.*;

import static com.mygdx.game.Globals.ASSET_TMX_MAP;

public class TiledGameMap extends TiledMap
{
	private TiledMap map;
	private TiledMapRenderer renderer;
	private boolean[][] collisionMap;
	
	public TiledGameMap() {
		map = new TmxMapLoader().load(ASSET_TMX_MAP);

		// TODO Make this a global constant or a runtime value
		renderer = new OrthogonalTiledMapRenderer(map, 1/(float)Globals.TILE_SIZE);
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

	public boolean isSolid(int x, int y) {
		return collisionMap[x][y];
	}
}
