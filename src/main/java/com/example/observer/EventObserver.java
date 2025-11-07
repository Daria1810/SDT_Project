package com.example.observer;

//receives notifications when events occur

public interface EventObserver {
    /**
     * 
     * @param event 
     */
    void update(Event event);
    
    /**
     * @return 
     */
    String getObserverName();
}
