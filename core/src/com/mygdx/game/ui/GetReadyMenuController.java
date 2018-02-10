package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.gameelements.GameParameterManager;

import java.util.HashMap;

/**
 * This controller will allow users to add or remove players before starting a game.
 */
public class GetReadyMenuController {

    private Table contentTable;
    private Skin skin;
    private HashMap<Integer, Label> tableMap;
    private int players;

    public GetReadyMenuController(Table contentTable, Skin skin){
        this.contentTable = contentTable;
        this.skin = skin;
        tableMap = new HashMap<Integer, Label>();
        players = 0;
        for (int i = 0; i < 4; i++) {
            Label descriptionLabel = new Label("Not Connected", skin);
            descriptionLabel.setWrap(true);
            contentTable.add(descriptionLabel).width(UIToolKit.INTERNAL_WIDTH).pad(UIToolKit.DIALOG_PAD);
            tableMap.put(i, descriptionLabel);
        }
        Label descriptionLabel = new Label("Press ?? to change teams", skin);
        descriptionLabel.setWrap(true);
        contentTable.add(descriptionLabel).width(UIToolKit.INTERNAL_WIDTH).pad(UIToolKit.DIALOG_PAD);
        tableMap.put(4, descriptionLabel);
    }

    public void updateState(GameParameterManager parameterManager) {
        // TODO Move this label to it's appropriate location
        Label labelView = tableMap.get(4);
        labelView.setText(parameterManager.allowTeamChange() ? "Press ?? to change teams" : "-");
    }

    public boolean enoughPlayers() {
        return players >= 2;
    }

    public void addPlayer(String label, int number) {
        Label labelView = tableMap.get(number);
        labelView.setText(label);
        players++;
    }

    public void removePlayer(int number) {
        Label labelView = tableMap.get(number);
        labelView.setText("Not Connected");
        players--;
    }
}