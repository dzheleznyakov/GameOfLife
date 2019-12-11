package zh.learn.gameoflife.ui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UiCell extends Rectangle {
    private static final double SIZE = 15.0;

    private final ReadOnlyBooleanProperty isInInitMode;

    private final BooleanProperty alive = new SimpleBooleanProperty();
    private final EventHandler<MouseEvent> onClickHandler = event -> setAlive(!isAlive());

    public UiCell(ReadOnlyBooleanProperty isInInitMode, boolean alive) {
        super(SIZE, SIZE);

        this.isInInitMode = isInInitMode;

        layoutView(alive);
        addListeners();
    }

    private void layoutView(boolean alive) {
        setAlive(alive);
        setStroke(Color.BLACK);
        setFill(getColor(alive));
    }

    private void addListeners() {
        alive.addListener((prop, oldValue, newValue) -> setFill(getColor(newValue)));
        if (isInInitMode.get()) {
            setOnMouseClicked(onClickHandler);
        }
        isInInitMode.addListener((prop, oldValue, newValue) -> {
            if (newValue) setOnMouseClicked(onClickHandler);
            else setOnMouseClicked(null);
        });
    }

    private Color getColor(Boolean newValue) {
        return newValue ? getRandomColor() : Color.LIGHTGRAY;
    }

    private Color getRandomColor() {
        double r = Math.random();
        if (r < 0.75)
            return Color.AQUAMARINE;
        else if (r < 0.85)
            return Color.AQUA;
        else if (r < 0.95)
            return Color.MEDIUMAQUAMARINE;
        return Color.LIGHTGOLDENRODYELLOW;
    }

    public boolean isAlive() {
        return alive.get();
    }

    public void setAlive(boolean alive) {
        this.alive.set(alive);
    }

    public int getXCoord() {
        return GridPane.getColumnIndex(this);
    }

    public int getYCoord() {
        return GridPane.getRowIndex(this);
    }
}
