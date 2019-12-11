package zh.learn.gameoflife.ui.controllers;

import com.google.common.collect.ImmutableList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import zh.learn.gameoflife.backend.Cell;
import zh.learn.gameoflife.backend.World;
import zh.learn.gameoflife.ui.UiCell;

public class StartButtonController {
    private final Button startBtn;
    private final BooleanProperty isInInitMode;
    private final BorderPane root;
    private final HBox controlBox;
    private final GridPane grid;
    private final ObjectProperty<World> world;

    public StartButtonController(
            Button startBtn,
            BooleanProperty isInInitMode,
            BorderPane root,
            HBox controlBox,
            GridPane grid,
            ObjectProperty<World> world
    ) {
        this.startBtn = startBtn;
        this.isInInitMode = isInInitMode;
        this.root = root;
        this.controlBox = controlBox;
        this.grid = grid;
        this.world = world;
    }

    public void bindStartBtn() {
        startBtn.setOnAction(event -> {
            isInInitMode.set(false);
            root.setTop(null);
            root.setBottom(controlBox);
            ImmutableList<Cell> initialState = grid.getChildren().stream()
                    .map(UiCell.class::cast)
                    .filter(UiCell::isAlive)
                    .map(uiCell -> {
                        int x = uiCell.getXCoord();
                        int y = uiCell.getYCoord();
                        return new Cell(x, y);
                    })
                    .collect(ImmutableList.toImmutableList());
            world.set(new World(initialState));
        });
    }
}
