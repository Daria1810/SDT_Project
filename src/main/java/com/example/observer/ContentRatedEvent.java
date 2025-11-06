package com.example.observer;

/**
 * Event emitted when a user rates content.
 */
public class ContentRatedEvent extends Event {
    private Long contentId;
    private int rating;
    
    public ContentRatedEvent(Long userId, Long contentId, int rating) {
        super(userId);
        this.contentId = contentId;
        this.rating = rating;
    }
    
    public Long getContentId() { return contentId; }
    public int getRating() { return rating; }

    @Override
    public String getEventType() {
        return "CONTENT_RATED";
    }
}
