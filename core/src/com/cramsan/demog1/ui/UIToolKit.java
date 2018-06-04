package com.cramsan.demog1.ui;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.HashMap;

/**
 * This class provides helped methods to create UI elements. Here is where the UI layout is defined.
 */
public class UIToolKit {

    public static int DIALOG_WIDTH = 200;
    public static int INTERNAL_WIDTH = 40;
    public static int DIALOG_PAD = 10;
    public static int DIALOG_TABLE_PAD = 5;
    public static int DIALOG_CHILD_WIDTH = 0;

    public static Table GenerateParentChildContainer(Skin skin) {
        Table childTable = new Table(skin);
        Table parentTable = new Table(skin);

        childTable.setBackground("window");
        parentTable.setBackground("window");

        Table mainPane = new Table();
        mainPane.setFillParent(true);
        mainPane.add(parentTable).width(DIALOG_WIDTH).pad(DIALOG_PAD);
        mainPane.add(childTable).width(DIALOG_WIDTH).pad(DIALOG_PAD).fillY();

        return mainPane;
    }

    public static Table GenerateSinglePaneContainer(Skin skin) {
        Table contentTable = new Table(skin);
        contentTable.setBackground("window");

        Table mainPane = new Table();
        mainPane.setFillParent(true);
        mainPane.add(contentTable).width(DIALOG_WIDTH).pad(DIALOG_PAD);

        return mainPane;
    }

    public static Table GenerateHorizontalContainer (Table root, Skin skin) {
        Table containerTable = new Table(skin);
        containerTable.setBackground("window");

        Table parentTable = (Table) root.getChildren().get(0);
        parentTable.add(containerTable).pad(DIALOG_TABLE_PAD).row();

        return containerTable;
    }

    public static Button AddButtonToParentWithAction(Table root, String label, Skin skin, EventListener listener, HashMap<Button, HashMap<UISystem.UI_EVENTS, Button>> sequenceMap) {
        TextButton startGameButton = new TextButton(label, skin);
        if (listener != null) {
            startGameButton.addListener(listener);
        } else {

        }
        Table parentTable = (Table) root.getChildren().get(0);
        parentTable.add(startGameButton).pad(DIALOG_TABLE_PAD).row();
        sequenceMap.put(startGameButton, new HashMap<UISystem.UI_EVENTS, Button>());
        return startGameButton;
    }

    public static Label AddActorToChild(Table root, String label, Skin skin) {
        Label descriptionLabel = new Label(label, skin);
        descriptionLabel.setWrap(true);
        Table contentTable = (Table) root.getChildren().get(1);
        contentTable.add(descriptionLabel).width(DIALOG_CHILD_WIDTH);
        return descriptionLabel;
    }

    public static Button AddButtonToSinglePaneWithAction(Table root, String label, Skin skin,
                                                         EventListener listener,
                                                         HashMap<Button, HashMap<UISystem.UI_EVENTS, Button>> sequenceMap) {
        TextButton startGameButton = new TextButton(label, skin);
        if (listener != null)
            startGameButton.addListener(listener);
        Table contentTable = (Table) root.getChildren().get(0);
        contentTable.add(startGameButton).pad(DIALOG_TABLE_PAD).row();
        sequenceMap.put(startGameButton, new HashMap<UISystem.UI_EVENTS, Button>());
        return startGameButton;
    }

    public static void LinkUpAndDown(Button up, Button down, HashMap<Button, HashMap<UISystem.UI_EVENTS, Button>> sequenceMap) {
        sequenceMap.get(up).put(UISystem.UI_EVENTS.DOWN, down);
        sequenceMap.get(down).put(UISystem.UI_EVENTS.UP, up);
    }

    public static void LinkLeftAndRight(Button left, Button right, HashMap<Button, HashMap<UISystem.UI_EVENTS, Button>> sequenceMap) {
        sequenceMap.get(left).put(UISystem.UI_EVENTS.RIGHT, right);
        sequenceMap.get(right).put(UISystem.UI_EVENTS.LEFT, left);
    }
}
