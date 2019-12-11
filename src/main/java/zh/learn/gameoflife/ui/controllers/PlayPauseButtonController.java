package zh.learn.gameoflife.ui.controllers;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.control.Button;
import zh.learn.gameoflife.ui.factories.ButtonIconFactory;

import java.util.concurrent.TimeUnit;

public class PlayPauseButtonController {
    private final Button playPauseBtn;
    private ReadOnlyBooleanProperty isInInitMode;
    private final BooleanProperty isPlaying;
    private final Runnable renderNextState;
    
    private Observable<Long> nextStateEmitter = Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(JavaFxScheduler.platform());
    private Disposable nextStateSubscription;

    public PlayPauseButtonController(
            Button playPauseBtn,
            ReadOnlyBooleanProperty isInInitMode,
            BooleanProperty isPlaying,
            Runnable renderNextState
    ) {
        this.playPauseBtn = playPauseBtn;
        this.isInInitMode = isInInitMode;
        this.isPlaying = isPlaying;
        this.renderNextState = renderNextState;
    }

    public void bind() {
        playPauseBtn.setOnAction(event -> isPlaying.set(!isPlaying.get()));
        isPlaying.addListener((prop, oldValue, newValue) -> {
            if (isPlaying.get()) {
                playPauseBtn.setGraphic(ButtonIconFactory.getPauseIcon());
                playPauseBtn.getTooltip().setText("Play");
                nextStateSubscription = nextStateEmitter.subscribe(tick -> renderNextState.run());
            } else {
                playPauseBtn.setGraphic(ButtonIconFactory.getPlayIcon());
                playPauseBtn.getTooltip().setText("Pause");
                if (nextStateSubscription != null && !nextStateSubscription.isDisposed()) {
                    nextStateSubscription.dispose();
                }
            }
        });
        isInInitMode.addListener((prop, oldValue, newValue) -> {
            if (newValue) {
                isPlaying.set(falsegit);
            }
        });
    }
}
