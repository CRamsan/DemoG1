package com.mygdx.game.gameelements;

/**
 * This class will define the winning condition for a game.
 */
public class GameParameterManager {

    public enum GameType{
        HUNT_STATUE,
        THIEF_HUNT,
        KILLER_HUNT
    }

    private GameType type;
    private int goal;

    public static GameParameterManager createStatueHuntManager() {
        GameParameterManager manager = new GameParameterManager();
        manager.setType(GameType.HUNT_STATUE);
        manager.setGoal(4);
        return manager;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }
}