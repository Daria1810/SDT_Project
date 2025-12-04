# StreamFlix Postman Collection

## Essential API Collection (30 Requests)

This Postman collection provides comprehensive testing coverage for all StreamFlix microservices with essential endpoints only.

## ⚙️ Architecture Overview

- API Gateway: `http://localhost:8080` (Routes all API calls)
- User Service: Port 8081 (Singleton Pattern - ConfigurationManager)
- Content Service: Port 8082 (Factory Pattern - MovieFactory, TVSeriesFactory)
- Video Service: Port 8083 (Observer Pattern - Watch/Rating events trigger observers)
- Recommendation Service: Port 8084 (Strategy Pattern - TrendingStrategy, HistoryBasedStrategy, RatingBasedStrategy)

---

## 📋 Collection Contents

### Total Requests: 30

1. **Health Checks (5 requests)**
   - Gateway, User, Content, Video, Recommendation health endpoints

2. **User Service - Singleton Pattern (6 requests)**
   - User registration and login
   - User management
   - Singleton pattern demonstration endpoints

3. **Content Service - Factory Pattern (7 requests)**
   - Create movies and TV series (Factory pattern in action)
   - Search and filter content by genre
   - Get top-rated content
   - Factory pattern demonstration endpoints

4. **Video Service - Observer Pattern (6 requests)**
   - Record watch events (triggers AnalyticsObserver + RecommendationUpdateObserver)
   - Rate content (triggers observers)
   - Get watch history and ratings
   - Observer pattern demonstration endpoints

5. **Recommendation Service - Strategy Pattern (6 requests)**
   - Get personalized recommendations (Strategy pattern dynamically selects algorithm)
   - Update user preferences
   - Get trending and personalized feed
   - Strategy pattern demonstration endpoints

---

## 📥 How to Import

### Step 1: Import Collection

1. Open Postman
2. Click **Import** button (top-left corner)
3. Select **File** tab
4. Choose `StreamFlix_Essential_Collection.postman_collection.json`
5. Click **Import**

### Step 2: Using the Collection

The collection uses hardcoded URLs for immediate usability:
- All requests point to `http://localhost:8080/api`
- No environment variables needed
- Ready to use immediately after import

### Step 3: Run Requests in Order

Recommended flow:
1. Start with **Health Checks** to verify all services are running
2. Run **User Service** requests (6-11) to create users
3. Run **Content Service** requests (12-18) to create content
4. Run **Video Service** requests (19-24) to record watch events and ratings
5. Run **Recommendation Service** requests (25-30) to see personalized recommendations

---

## 🎯 Request Descriptions

### 01 Health Checks (5 requests)

1. **Gateway Health** - Checks API Gateway status and shows status of all backend services
2. **User Service Health** - Verifies User Service is running and accessible
3. **Content Service Health** - Verifies Content Service is running and accessible
4. **Video Service Health** - Verifies Video Service is running and accessible
5. **Recommendation Service Health** - Verifies Recommendation Service is running and accessible

### 02 User Service - Singleton Pattern (6 requests)

6. **Register User** - Creates a new user account with email, username, and password
7. **Login User** - Authenticates user with email and password, returns JWT token
8. **Get All Users** - Retrieves list of all registered users in the system
9. **Singleton Demo** - Demonstrates Singleton pattern by showing ConfigurationManager usage
10. **Get Config (Singleton)** - Returns current ConfigurationManager instance settings
11. **Verify Singleton Test** - Confirms that only one ConfigurationManager instance exists across multiple requests

### 03 Content Service - Factory Pattern (7 requests)

12. **Create Movie (Factory)** - Uses Factory pattern to create a Movie content item with duration
13. **Create TV Series (Factory)** - Uses Factory pattern to create a TV Series with seasons/episodes
14. **Get All Content** - Retrieves complete list of all content (movies and TV series)
15. **Search Content** - Searches content by title keyword
16. **Get by Genre** - Filters content by specific genre (e.g., Action, Drama, Sci-Fi)
17. **Get Top Rated** - Returns highest-rated content based on user ratings
18. **Factory Pattern Demo** - Demonstrates Factory pattern by showing how different content types are created

### 04 Video Service - Observer Pattern (6 requests)

19. **Record Watch Event (Observer)** - Records when user watches content, triggers observer notifications
20. **Rate Content (Observer)** - Allows user to rate content (1-5 stars), triggers observer notifications
21. **Get Watch History** - Retrieves user's complete watch history with timestamps
22. **Observer Pattern Demo** - Demonstrates Observer pattern showing event notifications
23. **List Observers** - Shows all registered observers listening to video events
24. **Observer Status** - Displays current state of observer pattern implementation

### 05 Recommendation Service - Strategy Pattern (6 requests)

25. **Get Recommendations (Strategy)** - Gets personalized content recommendations using selected strategy algorithm
26. **Update User Preferences** - Updates user's preferred genres to improve recommendations
27. **Strategy Pattern Demo** - Demonstrates Strategy pattern with different recommendation algorithms
28. **List All Strategies** - Shows all available recommendation strategy algorithms
29. **Get Trending Content** - Returns currently trending content across all users
30. **Get Personalized Feed** - Generates personalized content feed based on user preferences and watch history

---

## 🎨 Design Pattern Demonstrations

### Singleton Pattern (User Service)
- Requests 9-11: Demonstrate ConfigurationManager singleton
- The same instance is shared across all user service operations
- Configuration is centralized and thread-safe

### Factory Method Pattern (Content Service)
- Requests 12-13: Create different content types (Movie vs TV Series)
- MovieFactory and TVSeriesFactory create appropriate content objects
- Request 18 shows factory pattern in action

### Observer Pattern (Video Service)
- Requests 19-20: Watch events and ratings trigger multiple observers
- AnalyticsObserver tracks metrics
- RecommendationUpdateObserver updates user preferences
- Requests 22-24 demonstrate observer notifications

### Strategy Pattern (Recommendation Service)
- Request 25: Dynamically selects recommendation algorithm
  - TrendingStrategy for new users
  - HistoryBasedStrategy for users with watch history
  - RatingBasedStrategy for users with ratings
- Requests 27-28 show strategy selection logic

---

 ️ Prerequisites

Before running the collection, ensure:
. Docker Compose is running: `docker-compose up -d`
. All services are healthy: Run request  "All Services Status"
. PostgreSQL databases are initialized for each service

---

  Testing Tips

. Run in sequence: The collection is designed to be run in order
. Check test results: Many requests include automatic test scripts
. View console logs: Observer patterns log notifications to console
. Variable tracking: Watch the collection variables update automatically
. Error testing: Folder  tests error scenarios (expect xx responses)

---

  Example Usage Flow

```
. Health Checks (-) → Verify all services are up
. Register User () → Creates user, saves userId
. Login User () → Saves JWT token
. Create Movie () → Creates content, saves contentId & movieId
. Record Watch () → Observer pattern triggers
. Rate Content () → Observer pattern triggers
. Get Recommendations () → Strategy pattern selects algorithm
```

---

 ️ Troubleshooting

Services not responding?
- Check Docker: `docker-compose ps`
- Check logs: `docker-compose logs -f [service-name]`

Variables not saving?
- Ensure you run requests that create resources first (e.g., register user before using userId)

CORS errors?
- API Gateway has CORS configured for all origins
- Make sure requests go through `http://localhost:/api/`

---

  Additional Resources

- UI Interface: `http://localhost:/ui/index.html`
- API Gateway: `http://localhost:/actuator/health`
- Docker Compose File: See `docker-compose.yml` in project root

---

  You're All Set!

Import the collection and start testing your StreamFlix microservices. All  requests are ready to demonstrate the complete functionality and all  design patterns.

Happy Testing! 
