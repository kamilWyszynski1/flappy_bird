package sample.models;

import javafx.animation.Timeline;

public class TimelineObserver implements Observer {
    private final TimelinesObservator timelinesObservator;
    private Timeline timeline;

    public TimelineObserver(TimelinesObservator timelinesObservator, Timeline timeline) {
        this.timelinesObservator = timelinesObservator;
        this.timeline = timeline;
    }

    @Override
    public void update() {
        if (timelinesObservator.isRunning())
            timeline.play();
        else
            timeline.stop();
    }
}
