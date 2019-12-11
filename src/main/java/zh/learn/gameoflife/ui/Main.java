package zh.learn.gameoflife.ui;

import com.google.common.collect.ImmutableList;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import zh.learn.gameoflife.backend.Cell;
import zh.learn.gameoflife.backend.World;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    private static final Insets PADDING = new Insets(5.0);
    private static final int MAX_GRID_WIDTH = 75;
    private static final int MAX_GRID_HEIGHT = 40;
    private static final Color BUTTON_ICON_COLOR = Color.GAINSBORO;
    private static final double BUTTON_SIZE = 27.0;

    private World world;

    private TextField widthFld;
    private TextField heightFld;
    private GridPane grid;
    private HBox initBox;
    private HBox controlBox;

    private Button startBtn;
    private Button nextStateBtn;
    private Button playPauseBtn;
    private BorderPane root;

    private BooleanProperty isInInitMode = new SimpleBooleanProperty(true);
    private BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private Observable<Long> nextStateEmitter = Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(JavaFxScheduler.platform());
    private Disposable nextStateSubscription;

    @Override
    public void start(Stage stage) {
        widthFld = getNumericField(3, MAX_GRID_WIDTH);
        heightFld = getNumericField(3, MAX_GRID_HEIGHT);
        startBtn = new Button("Start");
        initBox = getInitBox();

        nextStateBtn = getNextStateBtn();
        playPauseBtn = getPlayPauseBtn();
        controlBox = getControlBox();

        grid = getGrid();

        renderCells();
        bindGrid();
        bindStartBtn();
        bindNextStateBtn();
        bindPlayPauseBtn();

        root = renderRoot();

        stage.setScene(new Scene(root, 400.0, 300.0));
        stage.setTitle("Game of Life");
        stage.show();
    }

    private TextField getNumericField(int initialValue, int max) {
        TextField tf = new TextField(String.valueOf(initialValue));
        tf.setPrefColumnCount(3);
        tf.textProperty().addListener((prop, oldValue, newValue) -> {
            String value = newValue.matches("\\d*")
                    ? newValue
                    : newValue.replaceAll("[\\D]", "");
            if (Objects.equals(value, ""))
                return;
            int num = Integer.parseInt(value);
            if (num < 2)
                tf.setText("2");
            else if (num > max)
                tf.setText(oldValue);
            else
                tf.setText(value);
        });
        return tf;
    }

    private HBox getInitBox() {
        HBox initBox = new HBox(5.0);
        initBox.setPadding(PADDING);
        initBox.setAlignment(Pos.CENTER);
        initBox.getChildren().addAll(new Label("Width"), widthFld, new Label("Height"), heightFld, startBtn);
        return initBox;
    }

    private Button getNextStateBtn() {
        Path nextStepIcon = getNextStepIcon();
        Button button = new Button(null, nextStepIcon);
        button.setTooltip(new Tooltip("Next state"));
        button.setMaxSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setMinSize(BUTTON_SIZE, BUTTON_SIZE);
        return button;
    }

    private static Path getNextStepIcon() {
        Path nextStepIcon = new Path(new MoveTo(0.0, 0.0),
                new LineTo(0.0, 20.0),
                new LineTo(15.0, 10.0),
                new ClosePath(),
                new MoveTo(18.0, 1.0),
                new ArcTo(1.0, 1.0, 0.0, 20.0, 1.0, false, true),
                new LineTo(20.0, 19.0),
                new ArcTo(1.0, 1.0, 0.0, 18.0, 19.0, false, true),
                new ClosePath());
        nextStepIcon.setFill(BUTTON_ICON_COLOR);
        return nextStepIcon;
    }

    private Button getPlayPauseBtn() {
        Button button = new Button(null, getPlayIcon());
        button.setTooltip(new Tooltip("Play"));
        button.setMaxSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setMinSize(BUTTON_SIZE, BUTTON_SIZE);
        return button;
    }

    private static Path getPlayIcon() {
        Path playIcon = new Path(new MoveTo(0.0, 0.0),
                new LineTo(0.0, 20.0),
                new LineTo(20.0, 10.0),
                new ClosePath());
        playIcon.setFill(BUTTON_ICON_COLOR);
        return playIcon;
    }

    private static Path getPauseIcon() {
        Path pauseIcon = new Path(new MoveTo(5.0, 1.0),
                new ArcTo(1.0, 1.0, 0.0, 7.0, 1.0, false, true),
                new LineTo(7.0, 19.0),
                new ArcTo(1.0, 1.0, 0.0, 5.0, 19.0, false, true),
                new ClosePath(),
                new MoveTo(13.0, 1.0),
                new ArcTo(1.0, 1.0, 0.0, 15.0, 1.0, false, true),
                new LineTo(15.0, 19.0),
                new ArcTo(1.0, 1.0, 0.0, 13.0, 19.0, false, true),
                new ClosePath());
        pauseIcon.setFill(BUTTON_ICON_COLOR);
        return pauseIcon;
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
                boolean isAlive = world == null
                        ? false
                        : world.getState().contains(new Cell(x, y));
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
    }

    private void bindStartBtn() {
        startBtn.setOnAction(event -> {
            isInInitMode.set(false);
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
            world = new World(initialState);
        });
    }

    private void bindNextStateBtn() {
        nextStateBtn.setOnAction(event -> {
            isPlaying.set(false);
            renderNextState();
        });
    }

    private void bindPlayPauseBtn() {
        playPauseBtn.setOnAction(event -> isPlaying.set(!isPlaying.get()));
        isPlaying.addListener((prop, oldValue, newValue) -> {
            if (isPlaying.get()) {
                playPauseBtn.setGraphic(getPauseIcon());
                playPauseBtn.getTooltip().setText("Play");
                nextStateSubscription = nextStateEmitter.subscribe(tick -> renderNextState());
            } else {
                playPauseBtn.setGraphic(getPlayIcon());
                playPauseBtn.getTooltip().setText("Pause");
                if (nextStateSubscription != null && !nextStateSubscription.isDisposed()) {
                    nextStateSubscription.dispose();
                }
            }
        });
    }

    private void renderNextState() {
        World nextWorld = world.getNextWorld();
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

        if (minX < 0 || minY < 0) {
            Set<Cell> nextState = new HashSet<>();
            for (Cell cell : nextWorld.getState()) {
                int x = minX < 0 ? cell.getX() - minX : cell.getX();
                int y = minY < 0 ? cell.getY() - minY : cell.getY();
                nextState.add(new Cell(x, y));
            }
            world = new World(nextState);
        } else {
            world = nextWorld;
        }

        int realWidth = maxX - minX + 1;
        int realHeight = maxY - minY + 1;
        int requiredWidth = Integer.max(realWidth, Integer.parseInt(widthFld.getText()));
        int requiredHeight = Integer.max(realHeight, Integer.parseInt(heightFld.getText()));
        int width = Integer.min(requiredWidth, MAX_GRID_WIDTH);
        int height = Integer.min(requiredHeight, MAX_GRID_HEIGHT);
        widthFld.setText(String.valueOf(width));
        heightFld.setText(String.valueOf(height));

        renderCells();
    }

    private BorderPane renderRoot() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
        root.setBottom(initBox);
        root.setCenter(grid);
        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
