package com.example.observer;

/**
 * Receives notifications when events occur.
 */
public interface EventObserver {
    /**
     * Called when an event occurs.
     * @param event the event that occurred
     */
    void update(Event event);
    
    /**
     * @return human-friendly name of this observer
     */
    String getObserverName();
}
