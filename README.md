# StreamFlix - Video Streaming Platform

## Team Members

1. **BILCIURESCU ELENA-ALINA** - 1241EA
2. **SOLOMON MIRUNA-MARIA** - 1241EA
3. **TOMA DARIA-MARIA** - 1241EA

---

## Project Description

**StreamFlix** is a video streaming platform similar to Netflix that allows users to browse, watch, and rate video content. The platform supports user authentication, multiple subscription tiers, content management, personalized recommendations, and real-time notifications.

### Core Features

**User Management**
- User registration and authentication with JWT tokens
- Multiple user profiles per account (family sharing)
- Three subscription tiers: Basic (SD, 1 device), Standard (HD, 2 devices), Premium (4K, 4 devices)

**Content Catalog**
- Two content types: Movies and TV Series
- Each content item includes: title, description, genre, release year, duration/episodes, and average rating
- Browse by genre, search by title, view trending content

**Video Streaming**
- Stream videos with quality adjustment based on subscription tier
- Continue watching from last position across devices
- Track watch history and progress

**Recommendation System**
- Personalized recommendations based on watch history and ratings
- Different algorithms for new users vs. returning users
- Genre-based suggestions and trending content

**Rating System**
- Users rate content (1-5 stars)
- Average ratings displayed for each content item
- Ratings influence future recommendations

**Notification System**
- Email notifications for new content in favorite genres
- Subscription renewal reminders
- Weekly personalized recommendation digests

### Technical Architecture

**Backend**: Spring Boot (Java 17+) with Maven for dependency management

**Database**: 
- PostgreSQL for persistent data (users, content, ratings, subscriptions)
- Redis for caching frequently accessed data and session management

**Security**: Spring Security with JWT-based authentication

**API**: RESTful APIs with JSON responses

**Storage**: AWS S3 or local file storage for video files

### Data Models

**User**: id, email, password (hashed), subscriptionTier, createdAt

**Profile**: id, userId, name, avatarUrl, preferences

**Content**: id, title, description, genre, type (Movie/Series), releaseYear, rating, viewCount

**Movie** (extends Content): duration

**TVSeries** (extends Content): seasons, episodesList

**Rating**: userId, contentId, rating (1-5), timestamp

**WatchHistory**: userId, contentId, progress, lastWatched

### Implementation Milestones

**Milestone 1**: Project description and design pattern identification

**Milestone 2**: UML diagrams and proof-of-concept implementation demonstrating all design patterns

**Milestone 3**: Architecture analysis (Monolithic vs. Microservices vs. Event-Driven)

**Milestone 4**: Full microservices implementation with at least 3 services (User Service, Content Service, Streaming Service), inter-service communication, and CI/CD pipeline

**Milestone 5**: Message Queue + CI/CD Pipeline

---

## Design Patterns

### 1. Factory Method (Creational Pattern)

**Purpose**: Create different types of content objects (Movie vs. TVSeries) without coupling the creation logic to specific classes.

**Problem It Solves**: 
Our platform supports multiple content types (Movies and TV Series) that share common properties but have distinct characteristics. Movies have a single duration, while TV Series have seasons and episodes. Without the Factory Method pattern, we would need conditional statements (if-else or switch) scattered throughout the codebase wherever content objects are created, violating the Open/Closed Principle.

**Implementation**:
- `ContentFactory` interface with `createContent()` method
- `MovieFactory` creates Movie objects with duration and releaseYear
- `TVSeriesFactory` creates TVSeries objects with seasons and episodes
- `ContentService` uses appropriate factory based on content type when admins upload content or users browse the catalog

**Why Simpler Alternatives Fail**:

*Alternative 1: Direct instantiation with conditionals*
```
if (type == "MOVIE") {
    content = new Movie(...)
} else if (type == "TV_SERIES") {
    content = new TVSeries(...)
}
```
**Problems**: This conditional logic would be repeated in multiple places (upload service, browse service, recommendation service). Adding a new content type (e.g., Documentaries) requires finding and modifying all these locations. Violates the Open/Closed Principle and DRY principle, making maintenance difficult.

*Alternative 2: Single Content class with type field*
```
Content content = new Content()
content.setType("MOVIE")
content.setDuration(120) // only for movies
content.setSeasons(null) // not applicable
```
**Problems**: Leads to objects with many null fields depending on type. Cannot leverage polymorphism for type-specific behavior. Requires type checking before accessing properties. No compile-time type safety. All content operations need to check the type field first.

**Advantages**:
- **Extensibility**: Adding new content types (Documentaries, Podcasts) requires only creating a new factory, no changes to existing code
- **Single Responsibility**: Each factory handles creation logic for one content type
- **Type Safety**: Compiler ensures correct types are created
- **Clean Code**: Eliminates conditional logic for object creation
- **Testability**: Each factory can be unit tested independently

---

### 2. Strategy (Behavioral Pattern)

**Purpose**: Define a family of recommendation algorithms and make them interchangeable, allowing the algorithm to vary independently from clients that use it.

**Problem It Solves**:
StreamFlix needs different recommendation algorithms for different user contexts. New users with no watch history should see trending content, while returning users should get personalized recommendations based on their viewing patterns and ratings. The algorithm must be selected dynamically at runtime based on user state. Without the Strategy pattern, we would have a monolithic recommendation method with complex nested conditionals that becomes unmaintainable as we add more algorithms.

**Implementation**:
- `RecommendationStrategy` interface with `recommend(User user, int limit)` method
- `TrendingStrategy`: Returns most-watched content from the last 7 days (for new users)
- `HistoryBasedStrategy`: Analyzes user's watch history and recommends content from similar genres
- `RatingBasedStrategy`: Recommends content similar to user's highly-rated items (4-5 stars)
- `RecommendationService` holds a strategy reference and selects appropriate strategy based on user profile

**Why Simpler Alternatives Fail**:

*Alternative 1: Single method with conditional logic*
```
getRecommendations(User user) {
    if (user.isNew()) {
        // 50 lines: query trending content
    } else if (user.hasRatings()) {
        // 80 lines: analyze ratings, find similar content
    } else if (user.hasHistory()) {
        // 100 lines: analyze watch history, genre preferences
    }
}
```
**Problems**: Method becomes 200+ lines long, violating Single Responsibility Principle. Cannot test individual algorithms in isolation. Adding a new algorithm (e.g., collaborative filtering based on similar users) requires modifying this already complex method. All algorithms are loaded and compiled together even though only one executes. Impossible for different developers to work on different algorithms simultaneously.

*Alternative 2: Separate methods for each algorithm*
```
getTrendingRecommendations(user)
getHistoryRecommendations(user)  
getRatingRecommendations(user)
```
**Problems**: Calling code still needs conditional logic to select the right method. Cannot switch algorithms at runtime without changing the calling code. Difficult to combine algorithms (e.g., 70% history-based + 30% trending). Cannot easily implement A/B testing with different algorithms. Each method duplicates similar data retrieval logic.

**Advantages**:
- **Runtime Flexibility**: Algorithm selection based on user state without code changes
- **Open/Closed Principle**: New algorithms added without modifying existing code
- **Testability**: Each strategy tested independently with mock data
- **Clean Code**: Eliminates complex conditional logic
- **Easy A/B Testing**: Switch algorithms for different user segments to compare effectiveness
- **Parallel Development**: Different team members work on different strategies simultaneously

---

### 3. Observer (Behavioral Pattern)

**Purpose**: Define a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.

**Problem It Solves**:
When important events occur in StreamFlix (user watches content, new content added, subscription changes), multiple independent components need to react. For example, when a user watches a video: (1) WatchHistory must save progress, (2) RecommendationEngine must update preferences, (3) Analytics must record view count, and (4) Achievement system must check milestones. Without the Observer pattern, the VideoService would be tightly coupled to all these services, requiring direct method calls to each one. This creates a rigid architecture where adding a new service (e.g., StreakTracker for daily watching streaks) requires modifying VideoService.

**Implementation**:
- `Event` classes: `VideoWatchedEvent`, `ContentAddedEvent`, `ContentRatedEvent`
- `EventObserver` interface with `update(Event event)` method
- Concrete observers:
  - `WatchHistoryObserver`: Saves viewing progress and timestamps
  - `RecommendationObserver`: Updates user preference data for future recommendations
  - `AnalyticsObserver`: Records view counts and viewing duration statistics
  - `NotificationObserver`: Sends email/push notifications for relevant events
- `VideoService` and `ContentService` maintain observer lists and notify all observers when events occur

**Why Simpler Alternatives Fail**:

*Alternative 1: Direct coupling with method calls*
```
VideoService {
    recordWatchEvent(user, video) {
        watchHistoryService.saveProgress(...)
        recommendationService.updatePreferences(...)
        analyticsService.recordView(...)
        achievementService.checkMilestones(...)
        notificationService.notify(...)
    }
}
```
**Problems**: VideoService depends on and must know about 5+ different services. Any change to observer services (add/remove/modify) requires changing VideoService. If one service fails (e.g., notification service down), the entire operation may fail. All operations execute synchronously, blocking video playback start. Adding a new observer (e.g., SocialSharingService) requires modifying VideoService code. Testing requires mocking all dependent services.

*Alternative 2: Callback functions*
```
recordWatchEvent(user, video, callback1, callback2, callback3, ...) {
    callback1()
    callback2()
    callback3()
}
```
**Problems**: Number of callbacks grows unmanageably with each new feature. No clear structure or contract for what callbacks do. Difficult to add/remove callbacks dynamically. Memory leaks if callbacks not properly cleaned up. No type safety for callback parameters. Callback execution order is unclear.

**Advantages**:
- **Loose Coupling**: VideoService doesn't know about specific observers, only publishes events
- **Easy Extension**: Add new observers (e.g., StreakTracker) without modifying event publishers
- **Dynamic Registration**: Observers can subscribe/unsubscribe at runtime
- **Asynchronous Processing**: Observers process events in background threads without blocking
- **Fault Isolation**: One observer's failure doesn't affect others or the main operation
- **Testability**: Publishers and observers tested independently

---

### 4. Singleton (Creational Pattern)

**Purpose**: Ensure a class has only one instance and provide a global point of access to it.

**Problem It Solves**:
StreamFlix requires certain resources to exist as a single instance throughout the application lifecycle. The database connection pool should have exactly one instance managing a fixed number of connections (e.g., 20 connections). If multiple connection pool instances exist, we would have 60 connections (3 pools × 20) instead of 20, wasting database resources and potentially exceeding database limits. Similarly, the configuration manager should load application settings once and provide consistent values to all services. The cache manager should maintain one cache instance so that all services benefit from the same cached data, maximizing cache hit rates.

**Implementation**:
- `DatabaseConnectionPool`: Singleton managing 20 PostgreSQL connections with connection reuse
- `ConfigurationManager`: Singleton loading settings from application.properties and environment variables
- `CacheManager`: Singleton managing Redis cache for user profiles, content metadata, and trending data
- Each singleton uses double-checked locking for thread-safe lazy initialization
- Private constructor prevents external instantiation

**Why Simpler Alternatives Fail**:

*Alternative 1: Multiple instances created freely*
```
UserService: 
    connectionPool = new DatabaseConnectionPool() // 20 connections

ContentService:
    connectionPool = new DatabaseConnectionPool() // another 20 connections
    
VideoService:
    connectionPool = new DatabaseConnectionPool() // another 20 connections
```
**Problems**: Three separate connection pools create 60 total connections when only 20 are needed. Wastes database resources and memory. Pools operate independently without coordination. May exceed database connection limit (e.g., 50 connections max). Each pool maintains its own connection lifecycle, reducing efficiency. Defeats the entire purpose of connection pooling which is to share a limited resource efficiently.

*Alternative 2: Pass instance through constructor chain*
```
main() {
    pool = new DatabaseConnectionPool()
    config = new ConfigurationManager()
    cache = new CacheManager()
    
    userService = new UserService(pool, config, cache)
    contentService = new ContentService(pool, config, cache)
    videoService = new VideoService(pool, config, cache)
}
```
**Problems**: Every class needs these three parameters in constructor. Must manually thread instances through entire object graph. Prone to accidentally creating second instance elsewhere. As application grows, constructor parameter lists become unwieldy. Refactoring becomes difficult when dependencies change.

*Alternative 3: Static class with static methods*
```
public static class DatabaseConnectionPool {
    public static Connection getConnection() { ... }
}
```
**Problems**: Cannot control initialization timing (created at class load, even if never used). Cannot implement interfaces or extend classes. Difficult to create mock implementations for testing. No encapsulation of instance state. Cannot inject dependencies. Violates object-oriented design principles.

**Advantages**:
- **Guaranteed Single Instance**: Only one pool/cache/config exists, preventing resource duplication
- **Global Access**: Any service can access without passing references through constructors
- **Lazy Initialization**: Created only when first accessed, not at application startup
- **Resource Efficiency**: 20 database connections shared across all services instead of 60+
- **Consistent State**: All services see the same configuration values and cached data
- **Thread Safety**: Double-checked locking ensures safe concurrent access
- **Memory Efficiency**: One cache instance with deduplicated data instead of multiple caches

---

## Pattern Integration

These four patterns work together cohesively in StreamFlix:

1. **Factory Method creates content objects** → **Observer notifies** interested services about new content
2. **Strategy selects recommendation algorithm** based on user data → **Observer updates** user preference data when content is watched
3. **Singleton provides database connections** → **Factory Method uses** those connections to persist created content objects
4. **Observer events trigger** cache updates → **Singleton CacheManager** invalidates outdated cached data

---

## Why These Patterns Are Non-Trivial

**Factory Method**: Manages two distinct content types with different properties and behaviors, extensible to more types. Alternative approaches (conditionals or single class) create maintenance nightmares.

**Strategy**: Implements multiple complex algorithms (trend analysis, history analysis, rating analysis) that must be swapped at runtime. Conditional approach would create 200+ line method that's impossible to test or extend.

**Observer**: Coordinates 4+ independent services that must react to events without tight coupling. Direct coupling would create rigid architecture where adding features requires modifying core services.

**Singleton**: Manages critical shared resources (20 connections, application-wide cache) where multiple instances would waste resources and cause inconsistency. Simple alternatives either waste resources or create maintenance burden.

---

## Conclusion

StreamFlix demonstrates four essential design patterns that solve real architectural challenges in a video streaming platform. Each pattern addresses specific problems that simpler approaches cannot handle effectively, providing extensibility, maintainability, and efficiency that are critical for a scalable application.
# SDT_Project

Project made by Bilciurescu Elena-Alina, Solomon Miruna-Maria and Toma Daria-Maria. 
Group 1241EA CTI-E.
