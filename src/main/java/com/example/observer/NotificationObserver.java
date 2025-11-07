package com.example.observer;

/**
 * sends user-facing notifications for interesting events.
 */
public class NotificationObserver implements EventObserver {
    
    @Override
    public void update(Event event) {
        if (event instanceof VideoWatchedEvent) {
            VideoWatchedEvent videoEvent = (VideoWatchedEvent) event;
            if (videoEvent.isCompleted()) {
                sendCompletionNotification(videoEvent);
            }
        } else if (event instanceof ContentRatedEvent) {
            sendRatingThankYou((ContentRatedEvent) event);
        }
    }
    
    private void sendCompletionNotification(VideoWatchedEvent event) {
        //in real system, would send email/push notification
        System.out.println("[NotificationObserver] Sent completion notification to user " + event.getUserId() + 
                          " for content " + event.getContentId());
    }
    
    private void sendRatingThankYou(ContentRatedEvent event) {
        System.out.println("[NotificationObserver] Sent thank you message to user " + event.getUserId() + 
                          " for rating content " + event.getContentId());
    }
    
    @Override
    public String getObserverName() {
        return "NotificationObserver";
    }
}
