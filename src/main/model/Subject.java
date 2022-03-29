package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    List<Observer> observers;

    public Subject() {
        observers = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: if o is not already in observers, add o to observers
    public void addObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    // MODIFIES: this
    // EFFECTS: if o is in observers, remove o from observers
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    // EFFECTS: calls update method on each observer in observers
    public void notifyObservers(ProductivityEntry curr, ProductivityEntry prev) {
        for (Observer o : observers) {
            o.update(curr, prev);
        }
    }
}
