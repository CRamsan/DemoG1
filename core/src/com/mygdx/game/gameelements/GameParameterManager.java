package com.mygdx.game.gameelements;

import java.util.HashMap;

/**
 * This class will define the winning condition for a game.
 */
public class GameParameterManager {

    public enum GameType{
        NINJA_PARTY,
        CATCH_A_THIEF,
        KNIGHTS_VS_THIEFS,
        DEATH_RACE,
        ASSASSIN
    }

    private GameType type;
    private int goal;
    private HashMap<Integer, GameElement.TYPE> playerTypeMap;
    private boolean allowTeamChange;

    private GameParameterManager() {
        playerTypeMap = new HashMap<Integer, GameElement.TYPE>();
        for (int i = 0; i < 4; i++) {
            playerTypeMap.put(i, GameElement.TYPE.CHAR_HUMAN);
        }
    }

    public static GameParameterManager createNinjaPartyManager() {
        GameParameterManager manager = new GameParameterManager();
        manager.setType(GameType.NINJA_PARTY);
        manager.setGoal(4);
        manager.setAllowTeamChange(false);
        return manager;
    }

    public static GameParameterManager createCatchAThiefManager() {
        GameParameterManager manager = new GameParameterManager();
        manager.setType(GameType.CATCH_A_THIEF);
        manager.setGoal(40);
        manager.setAllowTeamChange(true);
        return manager;
    }

    public static GameParameterManager createKnightsVsThiefsManager() {
        GameParameterManager manager = new GameParameterManager();
        manager.setType(GameType.KNIGHTS_VS_THIEFS);
        manager.setGoal(4);
        manager.setAllowTeamChange(true);
        return manager;
    }

    public static GameParameterManager createDeathRaceManager() {
        GameParameterManager manager = new GameParameterManager();
        manager.setType(GameType.DEATH_RACE);
        manager.setGoal(4);
        manager.setAllowTeamChange(false);
        return manager;
    }

    public static GameParameterManager createAssassinsManager() {
        GameParameterManager manager = new GameParameterManager();
        manager.setType(GameType.ASSASSIN);
        manager.setGoal(4);
        manager.setAllowTeamChange(true);
        return manager;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public GameType getType() {
        return this.type;
    }

    public int getGoal() {
        return this.goal;
    }

    public GameElement.TYPE getTypeForPlayer(int index) {
        return playerTypeMap.get(index);
    }

    public void setTypeForPlayer(int index, GameElement.TYPE type){
        playerTypeMap.put(index, type);
    }

    public boolean allowTeamChange() {
        return allowTeamChange;
    }

    public void setAllowTeamChange(boolean allowTeamChange) {
        this.allowTeamChange = allowTeamChange;
    }

}