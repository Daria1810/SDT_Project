 StreamFlix Postman Collection

  Complete API Collection ( Requests)

This Postman collection provides comprehensive testing coverage for all StreamFlix microservices.

 ️ Architecture Overview

- API Gateway: `http://localhost:` (Routes all API calls)
- User Service: Port  (Singleton Pattern - ConfigurationManager)
- Content Service: Port  (Factory Pattern - MovieFactory, TVSeriesFactory)
- Video Service: Port  (Observer Pattern - Watch/Rating events trigger observers)
- Recommendation Service: Port  (Strategy Pattern - TrendingStrategy, HistoryBasedStrategy, RatingBasedStrategy)

---

  Collection Contents

 Total Requests: 

. Health Checks ( requests)
   - Gateway, User, Content, Video, Recommendation health endpoints

. User Service - Singleton Pattern ( requests)
   - User registration (BASIC, STANDARD, PREMIUM tiers)
   - Login with JWT token generation
   - User management (get, update, delete)
   - Singleton pattern demonstration endpoints

. Content Service - Factory Pattern ( requests)
   - Create movies and TV series (Factory pattern in action)
   - Search and filter content by genre
   - Get top-rated and most-viewed content
   - Factory pattern demonstration endpoints

. Video Service - Observer Pattern ( requests)
   - Record watch events (triggers AnalyticsObserver + RecommendationUpdateObserver)
   - Rate content (triggers observers)
   - Get watch history and ratings
   - Observer pattern demonstration endpoints

. Recommendation Service - Strategy Pattern ( requests)
   - Get personalized recommendations (Strategy pattern dynamically selects algorithm)
   - Update user preferences
   - Get trending and similar content
   - Strategy pattern demonstration endpoints

. Error Handling Tests ( requests)
   - Invalid registrations, non-existent resources, validation errors

---

  How to Import

 Step : Import Collection

. Open Postman
. Click Import button (top-left corner)
. Select File tab
. Choose `StreamFlix_Complete_Collection.postman_collection.json`
. Click Import

 Step : Using the Collection

The collection includes automatic variable management:
- Variables like `userId`, `contentId`, `movieId`, etc. are automatically captured from responses
- Test scripts automatically save IDs for subsequent requests
- All variables are scoped to the collection (no separate environment needed)

 Step : Run Requests in Order

Recommended flow:
. Start with Health Checks to verify all services are running
. Run User Service requests - to create users (captures `userId`)
. Run Content Service requests - to create content (captures `contentId`, `movieId`)
. Run Video Service requests - to record watch events and ratings
. Run Recommendation Service requests to see personalized recommendations

---

  Built-in Variables

The collection automatically manages these variables:

| Variable | Description | Set By |
|----------|-------------|---------|
| `baseUrl` | API Gateway base URL | Pre-configured: `http://localhost:/api` |
| `gatewayUrl` | Gateway root URL | Pre-configured: `http://localhost:` |
| `userId` | Last created/retrieved user ID | Auto-captured from User registration |
| `contentId` | Last created content ID | Auto-captured from Content creation |
| `movieId` | Last created movie ID | Auto-captured from Movie creation |
| `seriesId` | Last created TV series ID | Auto-captured from Series creation |
| `watchEventId` | Last created watch event ID | Auto-captured from Watch event |
| `ratingId` | Last created rating ID | Auto-captured from Rating creation |
| `jwtToken` | JWT authentication token | Auto-captured from Login |

---

  Design Pattern Demonstrations

 Singleton Pattern (User Service)
- Requests -: Demonstrate ConfigurationManager singleton
- The same instance is shared across all user service operations
- Configuration is centralized and thread-safe

 Factory Method Pattern (Content Service)
- Requests -: Create different content types (Movie vs TV Series)
- MovieFactory and TVSeriesFactory create appropriate content objects
- Request  shows factory pattern in action

 Observer Pattern (Video Service)
- Requests , : Watch events and ratings trigger multiple observers
- AnalyticsObserver tracks metrics
- RecommendationUpdateObserver updates user preferences
- Requests - demonstrate observer notifications

 Strategy Pattern (Recommendation Service)
- Request : Dynamically selects recommendation algorithm
  - TrendingStrategy for new users
  - HistoryBasedStrategy for users with watch history
  - RatingBasedStrategy for users with ratings
- Requests - show strategy selection logic

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
