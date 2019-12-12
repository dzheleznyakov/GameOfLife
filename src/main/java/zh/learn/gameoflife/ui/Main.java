package zh.learn.gameoflife.ui;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import zh.learn.gameoflife.backend.Cell;
import zh.learn.gameoflife.backend.World;
import zh.learn.gameoflife.ui.controllers.NextStateButtonController;
import zh.learn.gameoflife.ui.controllers.PlayPauseButtonController;
import zh.learn.gameoflife.ui.controllers.StartButtonController;
import zh.learn.gameoflife.ui.controllers.StartOverButtonController;
import zh.learn.gameoflife.ui.factories.ButtonFactory;
import zh.learn.gameoflife.ui.factories.TextFieldFactory;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Main extends Application {
    private static final Insets PADDING = new Insets(5.0);
    private static final int MAX_GRID_WIDTH = 75;
    private static final int MAX_GRID_HEIGHT = 40;

    private ObjectProperty<World> worldProperty = new SimpleObjectProperty<>();

    private TextField widthFld;
    private TextField heightFld;
    private GridPane grid;
    private HBox initBox;
    private HBox controlBox;

    private Label msgLbl;

    private Button startBtn;
    private Button nextStateBtn;
    private Button playPauseBtn;
    private Button startOverBtn;

    private BorderPane root;

    private BooleanProperty isInInitMode = new SimpleBooleanProperty(true);
    private BooleanProperty isPlaying = new SimpleBooleanProperty(false);

    private PlayPauseButtonController playPauseButtonController;
    private NextStateButtonController nextStateButtonController;
    private StartButtonController startButtonController;
    private StartOverButtonController startOverButtonController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        widthFld = TextFieldFactory.getNumericField(3, MAX_GRID_WIDTH);
        heightFld = TextFieldFactory.getNumericField(3, MAX_GRID_HEIGHT);
        startBtn = new Button("Start");
        initBox = getInitBox();

        nextStateBtn = ButtonFactory.getNextStateBtn();
        playPauseBtn = ButtonFactory.getPlayPauseBtn();
        controlBox = getControlBox();

        startOverBtn = new Button("Start Over");
        BorderPane.setAlignment(startOverBtn, Pos.CENTER);
        BorderPane.setMargin(startOverBtn, PADDING);

        grid = getGrid();
        root = getRoot();

        renderCells();
        bindGrid();

        startButtonController = new StartButtonController(startBtn, isInInitMode, root, controlBox, startOverBtn, grid, worldProperty);
        startButtonController.bind();

        nextStateButtonController = new NextStateButtonController(nextStateBtn, isPlaying, this::renderNextState);
        nextStateButtonController.bind();

        playPauseButtonController = new PlayPauseButtonController(playPauseBtn, isInInitMode, isPlaying, this::renderNextState);
        playPauseButtonController.bind();

        startOverButtonController = new StartOverButtonController(startOverBtn, isInInitMode, root, initBox, msgLbl, worldProperty);
        startOverButtonController.bind();

        stage.setScene(new Scene(root, 400.0, 300.0));
        stage.setTitle("Game of Life");
        stage.show();
    }

    private HBox getInitBox() {
        HBox initBox = new HBox(5.0);
        initBox.setPadding(PADDING);
        initBox.setAlignment(Pos.CENTER);
        initBox.getChildren().addAll(new Label("Width"), widthFld, new Label("Height"), heightFld, startBtn);
        return initBox;
    }

    private HBox getControlBox() {
        HBox controlBox = new HBox(5.0);
        controlBox.setPadding(PADDING);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.getChildren().addAll(nextStateBtn, playPauseBtn);
        return controlBox;
    }

    private GridPane getGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(2.0);
        grid.setVgap(2.0);
        grid.setPadding(PADDING);
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    private void renderCells() {
        grid.getChildren().clear();
        int width = Integer.parseInt(widthFld.getText());
        int height = Integer.parseInt(heightFld.getText());
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                boolean isAlive = worldProperty.get() == null
                        ? false
                        : worldProperty.get().getState().contains(new Cell(x, y));
                grid.add(new UiCell(isInInitMode, isAlive), x, y);
            }
        }
    }

    private void bindGrid() {
        widthFld.textProperty().addListener((prop, oldValue, newValue) -> {
            if (!Objects.equals(newValue, "")) renderCells();
        });
        heightFld.textProperty().addListener((prop, oldValue, newValue) -> {
            if (!Objects.equals(newValue, "")) renderCells();
        });
        worldProperty.addListener((prop, oldValue, newValue) -> {
            if (newValue != null) {
                renderCells();
            }
        });
    }

    private void renderNextState() {
        World nextWorld = worldProperty.get().getNextWorld();
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Cell cell : nextWorld.getState()) {
            minX = Integer.min(minX, cell.getX());
            maxX = Integer.max(maxX, cell.getX());
            minY = Integer.min(minY, cell.getY());
            maxY = Integer.max(maxY, cell.getY());
        }

        int realWidth = maxX - minX + 1;
        int realHeight = maxY - minY + 1;
        int requiredWidth = Integer.max(realWidth, Integer.parseInt(widthFld.getText()));
        int requiredHeight = Integer.max(realHeight, Integer.parseInt(heightFld.getText()));
        int width = Integer.min(requiredWidth, MAX_GRID_WIDTH);
        int height = Integer.min(requiredHeight, MAX_GRID_HEIGHT);
        widthFld.setText(String.valueOf(width));
        heightFld.setText(String.valueOf(height));

        if (minX < 0 || minY < 0) {
            Set<Cell> nextState = new HashSet<>();
            for (Cell cell : nextWorld.getState()) {
                int x = minX < 0 ? cell.getX() - minX : cell.getX();
                int y = minY < 0 ? cell.getY() - minY : cell.getY();
                nextState.add(new Cell(x, y));
            }
            worldProperty.set(new World(nextState));
        } else {
            worldProperty.set(nextWorld);
        }
    }

    private BorderPane getRoot() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
        msgLbl = new Label("Click on a cell to set it alive");
        BorderPane.setAlignment(msgLbl, Pos.CENTER);
        BorderPane.setMargin(msgLbl, PADDING);
        root.setTop(msgLbl);
        root.setBottom(initBox);
        root.setCenter(grid);
        return root;
    }
}
