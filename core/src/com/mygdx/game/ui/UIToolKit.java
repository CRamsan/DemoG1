package com.mygdx.game.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.game.MyGdxGame;

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

    public static void AddButtonToParentWithAction(Table root, String label, Skin skin, EventListener listener) {
        TextButton startGameButton = new TextButton(label, skin);
        if (listener != null)
            startGameButton.addListener(listener);
        Table parentTable = (Table) root.getChildren().get(0);
        parentTable.add(startGameButton).pad(30).row();
    }

    public static void AddActorToChild(Table root, String label, Skin skin) {
        Label descriptionLabel = new Label(label, skin);
        descriptionLabel.setWrap(true);
        Table contentTable = (Table) root.getChildren().get(1);
        contentTable.add(descriptionLabel).width(200);
    }

    public static void AddButtonToSinglePaneWithAction(Table root, String label, Skin skin, EventListener listener) {
        TextButton startGameButton = new TextButton(label, skin);
        if (listener != null)
            startGameButton.addListener(listener);
        Table contentTable = (Table) root.getChildren().get(0);
        contentTable.add(startGameButton).pad(30).row();
    }
}
