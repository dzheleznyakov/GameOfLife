package zh.learn.gameoflife.ui.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Button;

public class NextStateButtonController {
    private final Button nextStateBtn;
    private final BooleanProperty isPlaying;
    private final Runnable renderNextState;

    public NextStateButtonController(Button nextStateBtn, BooleanProperty isPlaying, Runnable renderNextState) {
        this.nextStateBtn = nextStateBtn;
        this.isPlaying = isPlaying;
        this.renderNextState = renderNextState;
    }

    public void bind() {
        nextStateBtn.setOnAction(event -> {
            isPlaying.set(false);
            renderNextState.run();
        });
    }
}
