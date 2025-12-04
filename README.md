# StreamFlix - Microservices Video Streaming Platform

A Netflix-like video streaming platform demonstrating microservices architecture and design patterns.

**Team:** Bilciurescu Elena-Alina, Solomon Miruna-Maria, Toma Daria-Maria  
**Group:** 1241EA CTI-E  
**Course:** Software Design Techniques  
**Target Grade:** 10/10

---

## Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Design Patterns](#design-patterns)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Team](#team)

---

## Project Overview

StreamFlix is a proof-of-concept microservices application that demonstrates four non-trivial design patterns (Factory Method, Strategy, Observer, Singleton) integrated into a video streaming platform architecture.

### Core Features

- **User Management**: Registration, authentication, subscription tiers (BASIC, STANDARD, PREMIUM)
- **Content Catalog**: Movies and TV Series with search and filtering
- **Video Streaming**: Watch events, progress tracking, watch history
- **Rating System**: 1-5 star ratings that influence recommendations
- **Recommendation Engine**: Personalized content suggestions using multiple algorithms
- **Real-time Notifications**: Event-driven architecture with observer pattern

### Technology Stack

- **Backend**: Java 17, Spring Boot 3.2.0, Spring Cloud Gateway
- **Database**: PostgreSQL 15 (4 instances - one per service)
- **Containerization**: Docker, Docker Compose
- **API Testing**: Postman (67 requests)
- **Build Tool**: Maven 3.8+

---

## Architecture

### Microservices Architecture

```
CLIENT
   |
   v
API GATEWAY (Port 8080)
   |
   +-- User Service (Port 8081) - Singleton Pattern
   |
   +-- Content Service (Port 8082) - Factory Pattern
   |
   +-- Video Service (Port 8083) - Observer Pattern
   |
   +-- Recommendation Service (Port 8084) - Strategy Pattern
```

### Database-per-Service

Each microservice has its own PostgreSQL database:
- `streamflix_user` (Port 5432)
- `streamflix_content` (Port 5433)
- `streamflix_video` (Port 5434)
- `streamflix_recommendation` (Port 5435)

### Services Overview

| Service | Port | Pattern | Purpose | Key Endpoints |
|---------|------|---------|---------|---------------|
| **API Gateway** | 8080 | Gateway | Request routing, CORS | `/api/*` |
| **User Service** | 8081 | Singleton | User management | `/api/users/*` |
| **Content Service** | 8082 | Factory | Content catalog | `/api/content/*` |
| **Video Service** | 8083 | Observer | Watch events, ratings | `/api/videos/*` |
| **Recommendation Service** | 8084 | Strategy | Personalized recommendations | `/api/recommendations/*` |

---

## Design Patterns

### 1. Factory Method Pattern (Content Service)

**Purpose**: Create different content types (Movie vs TV Series) without conditional logic.

**Implementation**:
- `ContentFactory` interface
- `MovieFactory` creates `Movie` objects with movie-specific fields (duration, director)
- `TVSeriesFactory` creates `TVSeries` objects with series-specific fields (seasons, episodes)

**Why?**: 
- Eliminates if/else statements for content type handling
- Easy to extend with new content types (Documentaries, Podcasts, etc.)
- Type-safe content creation with compile-time validation

**Example**:
```java
ContentFactory factory = contentType.equals("MOVIE") 
    ? new MovieFactory() 
    : new TVSeriesFactory();
Content content = factory.createContent(dto);
```

### 2. Strategy Pattern (Recommendation Service)

**Purpose**: Swap recommendation algorithms dynamically based on user behavior.

**Implementation**:
- `RecommendationStrategy` interface
- `TrendingStrategy` - For new users without history
- `HistoryBasedStrategy` - For users with watch history
- `RatingBasedStrategy` - For users who rate content

**Why?**:
- Avoids 200+ line methods with complex if/else logic
- Easy A/B testing by switching strategies
- Each algorithm is independently testable

**Example**:
```java
RecommendationStrategy strategy = selectStrategy(user);
List<Content> recommendations = strategy.recommend(user, limit);
```

### 3. Observer Pattern (Video Service)

**Purpose**: Multiple services react to watch events without tight coupling.

**Implementation**:
- `VideoEventPublisher` publishes watch and rating events
- `AnalyticsObserver` - Records metrics locally
- `RecommendationUpdateObserver` - Makes REST call to update recommendations
- `WatchHistoryObserver` - Updates user's watch history

**Why?**:
- Decouples event producers from consumers
- Easy to add new observers without modifying existing code
- Asynchronous event processing doesn't block main operations

**Example**:
```java
// Watch event triggers all registered observers
publisher.publishWatchEvent(watchEvent);
// -> AnalyticsObserver logs metrics
// -> RecommendationUpdateObserver updates recommendations
// -> WatchHistoryObserver updates history
```

### 4. Singleton Pattern (User Service)

**Purpose**: Single instance of shared resources (ConfigurationManager).

**Implementation**:
- `ConfigurationManager` - Manages service-wide configuration
- Thread-safe lazy initialization with double-checked locking
- Volatile keyword ensures visibility across threads

**Why?**:
- Prevents multiple instances wasting memory
- Ensures consistent configuration across all components
- Thread-safe access to shared resources

**Example**:
```java
ConfigurationManager config = ConfigurationManager.getInstance();
String jwtSecret = config.getJwtSecret();
```

### Pattern Interactions

The patterns work together cohesively:

1. **Factory → Observer**: ContentService creates content using factories, then publishes ContentAddedEvent through Observer pattern
2. **Observer → Strategy**: Watch events collected by observers feed data to recommendation strategies
3. **Singleton → All**: All services use ConfigurationManager singleton for consistent configuration
4. **Strategy → Observer**: Recommendation strategies analyze data collected by observers

---

## Getting Started

### Prerequisites

- Docker Desktop
- Java 17 JDK (for local development)
- Maven 3.8+ (for local development)
- Postman (for API testing)

### Quick Start with Docker

**1. Build all services:**
```bash
./build-all.sh  # Mac/Linux
# or
build-all.bat   # Windows
```

**2. Start all services:**
```bash
docker-compose up -d
```

**3. Verify services are running:**
```bash
docker-compose ps
```

All services should show "Up" and "healthy" status.

**4. Access the UI:**
```
http://localhost:8080/ui/index.html
```

**5. Check health endpoints:**
- API Gateway: http://localhost:8080/actuator/health
- User Service: http://localhost:8080/api/users/health
- Content Service: http://localhost:8080/api/content/health
- Video Service: http://localhost:8080/api/videos/health
- Recommendation Service: http://localhost:8080/api/recommendations/health

### Manual Build (without Docker)

```bash
# Build each service
cd user-service && mvn clean package -DskipTests
cd content-service && mvn clean package -DskipTests
cd video-service && mvn clean package -DskipTests
cd recommendation-service && mvn clean package -DskipTests
cd api-gateway && mvn clean package -DskipTests
```

### Stopping Services

```bash
docker-compose down
```

---

## API Documentation

### User Service (Port 8081)

**Base URL**: `/api/users`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/register` | Register new user | `{username, email, password, tier}` |
| POST | `/login` | User login | `{email, password}` |
| GET | `/{id}` | Get user by ID | - |
| GET | `/` | Get all users | - |
| PUT | `/{id}` | Update user profile | `{username, email, password}` |
| PUT | `/{id}/subscription` | Update subscription tier | `{tier}` |
| DELETE | `/{id}` | Delete user | - |
| GET | `/demo/singleton-test` | Test singleton pattern | - |

**Subscription Tiers**: `BASIC`, `STANDARD`, `PREMIUM`

### Content Service (Port 8082)

**Base URL**: `/api/content`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/` | Create content (Factory) | `{type, title, description, genre, releaseYear, ...}` |
| GET | `/` | Get all content | - |
| GET | `/{id}` | Get content by ID | - |
| GET | `/movies` | Get all movies | - |
| GET | `/series` | Get all TV series | - |
| GET | `/search?query={q}` | Search content | - |
| GET | `/genre/{genre}` | Get content by genre | - |
| GET | `/top-rated` | Get top rated content | - |

**Content Types**: `MOVIE`, `TV_SERIES`

**Movie Fields**: `duration` (minutes), `director`  
**TV Series Fields**: `seasons`, `episodesPerSeason`

### Video Service (Port 8083)

**Base URL**: `/api/videos`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| POST | `/watch` | Record watch event (Observer) | `{userId, contentId, progress, completed}` |
| POST | `/rate` | Rate content (Observer) | `{userId, contentId, score}` |
| GET | `/watch/user/{userId}` | Get watch history | - |
| GET | `/rate/user/{userId}` | Get user ratings | - |
| GET | `/watch/{id}` | Get watch event by ID | - |
| GET | `/rate/{id}` | Get rating by ID | - |
| GET | `/rate/content/{contentId}/average` | Get average rating | - |

**Progress**: Seconds watched  
**Score**: 0.0 to 5.0

### Recommendation Service (Port 8084)

**Base URL**: `/api/recommendations`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/{userId}?limit={n}` | Get recommendations (Strategy) | - |
| GET | `/trending?limit={n}` | Get trending content | - |
| GET | `/similar/{contentId}` | Get similar content | - |
| GET | `/preferences/{userId}` | Get user preferences | - |
| POST | `/preferences/update` | Update preferences | `{userId, contentId, interactionType}` |
| GET | `/strategy/demo` | Demo strategy pattern | - |
| GET | `/strategy/current/{userId}` | Get current strategy | - |

**Strategy Selection**:
- New user (no history) → `TrendingStrategy`
- User with watch history → `HistoryBasedStrategy`
- User with ratings → `RatingBasedStrategy`

---

## Testing

### Postman Collection

A comprehensive Postman collection with 67 requests is provided.

**Location**: `postman/StreamFlix_Complete_Collection.postman_collection.json`

**Collection Contents**:
1. Health Checks (6 requests)
2. User Service - Singleton Pattern (15 requests)
3. Content Service - Factory Pattern (15 requests)
4. Video Service - Observer Pattern (15 requests)
5. Recommendation Service - Strategy Pattern (10 requests)
6. Error Handling Tests (6 requests)

**How to Use**:
1. Open Postman
2. Import → File → Select `StreamFlix_Complete_Collection.postman_collection.json`
3. Run requests in order (health checks first)
4. Variables are auto-captured from responses

### Testing Flow

**Step 1: Health Checks**
```bash
curl http://localhost:8080/actuator/health
```

**Step 2: Register User**
```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john_doe","email":"john@example.com","password":"Password123!","tier":"PREMIUM"}'
```

**Step 3: Create Movie (Factory Pattern)**
```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{"type":"MOVIE","title":"Inception","description":"Dream thriller","genre":"Sci-Fi","releaseYear":2010,"duration":148,"director":"Christopher Nolan"}'
```

**Step 4: Record Watch Event (Observer Pattern)**
```bash
curl -X POST http://localhost:8080/api/videos/watch \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"contentId":1,"progress":3600,"completed":true}'
```

**Step 5: Rate Content**
```bash
curl -X POST http://localhost:8080/api/videos/rate \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"contentId":1,"score":5.0}'
```

**Step 6: Get Recommendations (Strategy Pattern)**
```bash
curl http://localhost:8080/api/recommendations/1?limit=5
```

**Step 7: Test Singleton Pattern**
```bash
curl http://localhost:8080/api/users/demo/singleton-test
```

### Viewing Logs

```bash
# View all logs
docker-compose logs -f

# View specific service
docker-compose logs -f user-service

# View pattern-specific logs
docker-compose logs user-service | grep "ConfigurationManager"  # Singleton
docker-compose logs content-service | grep -i "factory"         # Factory
docker-compose logs video-service | grep -i "observer"          # Observer
docker-compose logs recommendation-service | grep -i "strategy" # Strategy
```

---

## Project Structure

```
streamflix/
├── api-gateway/                 # Spring Cloud Gateway (Port 8080)
│   ├── src/main/
│   │   ├── java/                # Gateway configuration
│   │   └── resources/
│   │       ├── application.yml  # Routes configuration
│   │       └── static/ui/       # Web UI files
│   ├── Dockerfile
│   └── pom.xml
│
├── user-service/                # Singleton Pattern (Port 8081)
│   ├── src/main/java/
│   │   ├── config/
│   │   │   └── ConfigurationManager.java  # Singleton
│   │   ├── controller/          # REST endpoints
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access
│   │   ├── model/               # User entity
│   │   └── dto/                 # Data transfer objects
│   ├── Dockerfile
│   └── pom.xml
│
├── content-service/             # Factory Pattern (Port 8082)
│   ├── src/main/java/
│   │   ├── factory/
│   │   │   ├── ContentFactory.java      # Factory interface
│   │   │   ├── MovieFactory.java        # Movie creator
│   │   │   └── TVSeriesFactory.java     # TV Series creator
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/               # Content, Movie, TVSeries
│   │   └── dto/
│   ├── Dockerfile
│   └── pom.xml
│
├── video-service/               # Observer Pattern (Port 8083)
│   ├── src/main/java/
│   │   ├── observer/
│   │   │   ├── VideoEventPublisher.java      # Event publisher
│   │   │   ├── AnalyticsObserver.java        # Analytics tracking
│   │   │   └── RecommendationUpdateObserver.java  # Update recommendations
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/               # WatchEvent, Rating
│   │   └── dto/
│   ├── Dockerfile
│   └── pom.xml
│
├── recommendation-service/      # Strategy Pattern (Port 8084)
│   ├── src/main/java/
│   │   ├── strategy/
│   │   │   ├── RecommendationStrategy.java   # Strategy interface
│   │   │   ├── TrendingStrategy.java         # For new users
│   │   │   ├── HistoryBasedStrategy.java     # For returning users
│   │   │   └── RatingBasedStrategy.java      # For users with ratings
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/               # UserPreference
│   │   └── dto/
│   ├── Dockerfile
│   └── pom.xml
│
├── postman/                     # API Testing
│   ├── StreamFlix_Complete_Collection.postman_collection.json
│   └── README.md
│
├── docs/
│   └── diagrams/                # UML diagrams (Milestone 2)
│
├── docker-compose.yml           # Docker orchestration
├── build-all.sh                 # Build script (Mac/Linux)
├── build-all.bat                # Build script (Windows)
├── QUICKSTART.md                # Quick start guide
├── FIXES_APPLIED.md             # Recent fixes documentation
└── README.md                    # This file
```

---

## Troubleshooting

### Services Won't Start

**Check logs:**
```bash
docker-compose logs <service-name>
```

**Rebuild without cache:**
```bash
docker-compose build --no-cache
docker-compose up -d
```

### Port Already in Use

**Find process using port (Mac/Linux):**
```bash
lsof -i :8080
```

**Find process using port (Windows):**
```bash
netstat -ano | findstr :8080
```

**Kill process:**
```bash
kill -9 <PID>  # Mac/Linux
taskkill /PID <PID> /F  # Windows
```

### Database Connection Issues

**Check PostgreSQL containers:**
```bash
docker-compose ps | grep postgres
```

**Restart databases:**
```bash
docker-compose restart postgres-user postgres-content postgres-video postgres-recommendation
```

### Build Fails

**Clean Maven cache:**
```bash
mvn clean
```

**Verify Java version:**
```bash
java -version  # Must be Java 17
```

### UI Not Loading

**Check if API Gateway has UI files:**
```bash
docker exec streamflix-api-gateway ls -la /app/BOOT-INF/classes/static/ui/
```

**Rebuild API Gateway:**
```bash
cd api-gateway
mvn clean package -DskipTests
docker-compose build api-gateway
docker-compose up -d api-gateway
```

---

## References

- **Design Patterns**: [Refactoring Guru](https://refactoring.guru/design-patterns)
- **Spring Boot**: [Documentation](https://spring.io/projects/spring-boot)
- **Spring Cloud Gateway**: [Documentation](https://spring.io/projects/spring-cloud-gateway)
- **Docker**: [Documentation](https://docs.docker.com/)
- **PostgreSQL**: [Documentation](https://www.postgresql.org/docs/)
- **Microservices Architecture**: [Microservices.io](https://microservices.io/)

---

## Team

**Bilciurescu Elena-Alina** - 1241EA  
**Solomon Miruna-Maria** - 1241EA  
**Toma Daria-Maria** - 1241EA


