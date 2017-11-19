package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;

import java.util.Random;

public class Globals {
    public static final String ASSET_SPRITE_SHEET = "animation_sheet.png";
    public static final int ASSET_SPRITE_SHEET_ROWS = 2;
    public static final int ASSET_SPRITE_SHEET_COLUMNS = 6;
    public static final int ASSET_SPRITE_SHEET_SPRITE_WIDTH = 16;
    public static final int ASSET_SPRITE_SHEET_SPRITE_HEIGHT = 16;
    public static final int TILE_SIZE = 16;
    public static final int MAGNIFICATION = 4;
    public static final int ANIMATION_COLUMNS = 3;
    public static final int ANIMATION_ROWS = 4;
    public static final float ANIMATION_DURATION = 0.5f;
    public static final String ASSET_TMX_MAP = "town.tmx";
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final float FRAME_TIME = 1f/60f;

    public static Random rand = new Random();

    public enum UI_EVENTS { UP, DOWN, LEFT, RIGHT, SELECT, NOOP }
}
