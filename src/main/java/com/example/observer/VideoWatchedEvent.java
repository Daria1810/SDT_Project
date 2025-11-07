package com.example.observer;

//event emitted when a user watches a piece of content

public class VideoWatchedEvent extends Event {
    private Long contentId;
    private int progressSeconds;
    private boolean completed;
    
    public VideoWatchedEvent(Long userId, Long contentId, int progressSeconds, boolean completed) {
        super(userId);
        this.contentId = contentId;
        this.progressSeconds = progressSeconds;
        this.completed = completed;
    }
    
    public Long getContentId() { return contentId; }
    public int getProgressSeconds() { return progressSeconds; }
    public boolean isCompleted() { return completed; }

    @Override
    public String getEventType() {
        return "VIDEO_WATCHED";
    }
}
