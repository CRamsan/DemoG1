package com.cramsan.demog1.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.cramsan.demog1.Globals;
import com.cramsan.demog1.controller.ControllerManager;
import com.cramsan.demog1.controller.PlayerController;
import com.cramsan.demog1.gameelements.GameElement;
import com.cramsan.demog1.gameelements.GameParameterManager;

import java.util.HashMap;
import java.util.List;

/**
 * This controller will allow users to add or remove players before starting a game.
 */
public class GetReadyMenuController {

    private Table contentTable;
    private Skin skin;
    private HashMap<Integer, Label> tableMap;
    private int players;
    private GameParameterManager parameterManager;
    private boolean allowTeamChange;

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

    public void update(float delta) {
        if (!allowTeamChange)
            return;

        List<ControllerManager.ControllerEventTuple> tupleList = ControllerManager.getInstance().getUIEvents();
        for (ControllerManager.ControllerEventTuple tuple : tupleList) {
            IUISystem.UI_EVENTS event = tuple.event;
            handleGetReadyEvent(tuple.index, event);
        }
    }

    private void handleGetReadyEvent(int index, IUISystem.UI_EVENTS event) {
        if (event == IUISystem.UI_EVENTS.RIGHT || event == IUISystem.UI_EVENTS.LEFT) {
            this.parameterManager.setTypeForPlayer(index,
                    parameterManager.getTypeForPlayer(index) == GameElement.TYPE.CHAR_HUMAN ?
                            GameElement.TYPE.CHAR_RETICLE :
                            GameElement.TYPE.CHAR_HUMAN);
            Label playerDescription = tableMap.get(index);
            playerDescription.setText(parameterManager.getTypeForPlayer(index).name());
        }
    }

    public void updateGameParams(GameParameterManager parameterManager) {
        // TODO Move this label to it's appropriate location
        Label labelView = tableMap.get(4);
        labelView.setText(parameterManager.allowTeamChange() ? "Press ?? to change teams" : "-");
        this.parameterManager = parameterManager;
        this.allowTeamChange = parameterManager.allowTeamChange();
    }

    public boolean enoughPlayers() {
        return players >= 2;
    }

    public void addPlayer(PlayerController controller) {
        Label labelView = tableMap.get(controller.getControllerIndex());
        labelView.setText(controller.getName());
        players++;
    }

    public void removePlayer(PlayerController controller) {
        Label labelView = tableMap.get(controller.getControllerIndex());
        labelView.setText("Not Connected");
        players--;
    }
}