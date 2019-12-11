package zh.learn.gameoflife.ui.factories;

import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class ButtonIconFactory {
    static final Color BUTTON_ICON_COLOR = Color.GAINSBORO;

    static Path getNextStepIcon() {
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

    public static Path getPlayIcon() {
        Path playIcon = new Path(new MoveTo(0.0, 0.0),
                new LineTo(0.0, 20.0),
                new LineTo(20.0, 10.0),
                new ClosePath());
        playIcon.setFill(BUTTON_ICON_COLOR);
        return playIcon;
    }

    public static Path getPauseIcon() {
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
}
