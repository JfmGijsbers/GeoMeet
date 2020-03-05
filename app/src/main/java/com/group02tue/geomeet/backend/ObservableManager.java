package com.group02tue.geomeet.backend;

import androidx.core.util.Consumer;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public abstract class ObservableManager<T extends EventListener> {
    private final List<T> listeners = new ArrayList<>();

    /**
     * Notify all listeners about a certain happening.
     * @param action What to execute for each listener
     */
    protected void notifyListeners(Consumer<T> action) {
        synchronized (listeners) {
            for (T listener : listeners) {
                action.accept(listener);
            }
        }
    }

    /**
     * Adds a new event listener
     * @param listener Listener to add
     */
    public void addListener(T listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Removes an event listener.
     * @param listener Listener to remove
     */
    public void removeListener(T listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
}
