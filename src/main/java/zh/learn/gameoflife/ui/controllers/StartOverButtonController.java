package zh.learn.gameoflife.ui.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import zh.learn.gameoflife.backend.World;

import java.util.HashSet;

public class StartOverButtonController {
    private final Button startOverButton;
    private BooleanProperty isInInitMode;
    private BorderPane root;
    private HBox initBox;
    private Label msgLbl;
    private ObjectProperty<World> worldProperty;

    public StartOverButtonController(
            Button startOverBtn,
            BooleanProperty isInInitMode,
            BorderPane root,
            HBox initBox,
            Label msgLbl,
            ObjectProperty<World> worldProperty
    ) {
        this.startOverButton = startOverBtn;
        this.isInInitMode = isInInitMode;
        this.root = root;
        this.initBox = initBox;
        this.msgLbl = msgLbl;
        this.worldProperty = worldProperty;
    }

    public void bind() {
        startOverButton.setOnAction(event -> {
            isInInitMode.set(true);
            root.setTop(msgLbl);
            root.setBottom(initBox);
            worldProperty.set(new World(new HashSet<>()));
        });
    }
}
