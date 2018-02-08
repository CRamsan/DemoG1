package com.mygdx.game;

import java.util.Random;

public class Globals {
    public static final String ASSET_SPRITE_SHEET = "animation_sheet.png";
    public static final String ASSET_SKIN_FILE = "uiskin.json";
    public static final int ASSET_SPRITE_SHEET_ROWS = 2;
    public static final int ASSET_SPRITE_SHEET_COLUMNS = 6;
    public static final int ASSET_SPRITE_SHEET_SPRITE_WIDTH = 32;
    public static final int ASSET_SPRITE_SHEET_SPRITE_HEIGHT = 32;
    public static final int TILE_SIZE = 1;
    public static final int MAGNIFICATION = 4;
    public static final int ANIMATION_COLUMNS = 3;
    public static final int ANIMATION_ROWS = 4;
    public static final float UI_WAIT = .2f;
    public static final float ANIMATION_DURATION = 0.5f;
    public static final String ASSET_TMX_MAP = "town.tmx";
    public static final int SCREEN_WIDTH = 640;
    public static final int SCREEN_HEIGHT = 480;
    public static final float FRAME_TIME = 1f/60f;

    public static final String TEXT_LABEL_NINJA_PARTY = "Ninja Party";
    public static final String TEXT_LABEL_CATCH_A_THIEF = "Catch A Thief";
    public static final String TEXT_LABEL_KNIGHTS_VS_NINJAS = "Knights vs Ninjas";
    public static final String TEXT_LABEL_DEATH_RACE = "Death Race";
    public static final String TEXT_LABEL_ASSASSIN = "Assassin";
    public static final String TEXT_LABEL_START = "Start";
    public static final String TEXT_LABEL_BACK = "Back";

    public static Random rand = new Random();

    public enum UI_EVENTS { UP, DOWN, LEFT, RIGHT, SELECT, NOOP }
}
