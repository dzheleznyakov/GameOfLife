package zh.learn.gameoflife.ui.factories;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Path;

public class ButtonFactory {
    static final double BUTTON_SIZE = 27.0;

    public static Button getNextStateBtn() {
        Path nextStepIcon = ButtonIconFactory.getNextStepIcon();
        Button button = new Button(null, nextStepIcon);
        button.setTooltip(new Tooltip("Next state"));
        button.setMaxSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setMinSize(BUTTON_SIZE, BUTTON_SIZE);
        return button;
    }

    public static Button getPlayPauseBtn() {
        Button button = new Button(null, ButtonIconFactory.getPlayIcon());
        button.setTooltip(new Tooltip("Play"));
        button.setMaxSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setMinSize(BUTTON_SIZE, BUTTON_SIZE);
        return button;
    }
}
