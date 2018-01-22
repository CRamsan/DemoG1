package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.game.Globals;

import java.util.HashMap;

/**
 * This class provides helped methods to create UI elements. Here is where the UI layout is defined.
 */
public class UIToolKit {
    public static Table GenerateParentChildContainer(Skin skin) {
        Table childTable = new Table(skin);
        Table parentTable = new Table(skin);

        childTable.setBackground("dialog");
        parentTable.setBackground("dialog");

        Table mainPane = new Table();
        mainPane.setFillParent(true);
        mainPane.add(parentTable).width(400).pad(10);
        mainPane.add(childTable).width(400).pad(10).fillY();

        return mainPane;
    }

    public static Table GenerateSinglePaneContainer(Skin skin) {
        Table contentTable = new Table(skin);
        contentTable.setBackground("dialog");

        Table mainPane = new Table();
        mainPane.setFillParent(true);
        mainPane.add(contentTable).width(400).pad(10);

        return mainPane;
    }

    public static Table GenerateHorizontalContainer (Table root, Skin skin) {
        Table containerTable = new Table(skin);
        containerTable.setBackground("dialog");

        Table parentTable = (Table) root.getChildren().get(0);
        parentTable.add(containerTable).pad(30).row();

        return containerTable;
    }

    public static Button AddButtonToParentWithAction(Table root, String label, Skin skin, EventListener listener, HashMap<Button, HashMap<Globals.UI_EVENTS, Button>> sequenceMap) {
        TextButton startGameButton = new TextButton(label, skin);
        if (listener != null) {
            startGameButton.addListener(listener);
        } else {

        }
        Table parentTable = (Table) root.getChildren().get(0);
        parentTable.add(startGameButton).pad(30).row();
        sequenceMap.put(startGameButton, new HashMap<Globals.UI_EVENTS, Button>());
        return startGameButton;
    }

    public static Label AddActorToChild(Table root, String label, Skin skin) {
        Label descriptionLabel = new Label(label, skin);
        descriptionLabel.setWrap(true);
        Table contentTable = (Table) root.getChildren().get(1);
        contentTable.add(descriptionLabel).width(200);
        return descriptionLabel;
    }

    public static Button AddButtonToSinglePaneWithAction(Table root, String label, Skin skin,
                                                         EventListener listener,
                                                         HashMap<Button, HashMap<Globals.UI_EVENTS, Button>> sequenceMap) {
        TextButton startGameButton = new TextButton(label, skin);
        if (listener != null)
            startGameButton.addListener(listener);
        Table contentTable = (Table) root.getChildren().get(0);
        contentTable.add(startGameButton).pad(30).row();
        sequenceMap.put(startGameButton, new HashMap<Globals.UI_EVENTS, Button>());
        return startGameButton;
    }

    public static void LinkUpAndDown(Button up, Button down, HashMap<Button, HashMap<Globals.UI_EVENTS, Button>> sequenceMap) {
        sequenceMap.get(up).put(Globals.UI_EVENTS.DOWN, down);
        sequenceMap.get(down).put(Globals.UI_EVENTS.UP, up);
    }

    public static void LinkLeftAndRight(Button left, Button right, HashMap<Button, HashMap<Globals.UI_EVENTS, Button>> sequenceMap) {
        sequenceMap.get(left).put(Globals.UI_EVENTS.RIGHT, right);
        sequenceMap.get(right).put(Globals.UI_EVENTS.LEFT, left);
    }
}
