package com.example.factory;

import com.example.model.Content;

/**
 * Creates content instances without exposing construction details.
 */
public interface ContentFactory {
    /**
     * Build a content object.
     * @return a Content instance (Movie or TVSeries)
     */
    Content createContent();
}
