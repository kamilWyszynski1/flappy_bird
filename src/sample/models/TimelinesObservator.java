package sample.models;

import java.util.HashSet;
import java.util.Set;

public class TimelinesObservator implements Observable{
    private Set<Observer> observers = new HashSet<>();

    private boolean running = true;

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
        notifyObservers();
    }
}
