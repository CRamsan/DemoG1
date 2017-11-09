package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class GetReadyMenuController {

    private Table contentTable;
    private Skin skin;
    private HashMap<Integer, Label> tableMap;
    private boolean isVisible;

    public GetReadyMenuController(Table contentTable, Skin skin){
        this.contentTable = contentTable;
        this.skin = skin;
        tableMap = new HashMap<Integer, Label>();
        for (int i = 0; i < 4; i++) {
            Label descriptionLabel = new Label("Not Connected", skin);
            descriptionLabel.setWrap(true);
            contentTable.add(descriptionLabel).width(200).pad(10);
            tableMap.put(i, descriptionLabel);
        }
    }

    public void addPlayer(String label, int number) {
        Label labelView = tableMap.get(number);
        labelView.setText(label);
    }

    public void removePlayer(int number) {
        Label labelView = tableMap.get(number);
        labelView.setText("Not Connected");
    }
}